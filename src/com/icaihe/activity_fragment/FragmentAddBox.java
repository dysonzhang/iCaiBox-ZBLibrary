package com.icaihe.activity_fragment;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.admin.EspWifiAdmin;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.herily.dialog.HerilyAlertDialog;
import com.icaihe.R;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.icaihe.widget.ClearEditText;
import com.ichihe.util.BluetoothController;
import com.ichihe.util.HttpRequest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 添加财盒fragment
 */
public class FragmentAddBox extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {

	public static FragmentAddBox createInstance() {
		return new FragmentAddBox();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.fragment_add_box);

		initView();
		initData();
		initEvent();
		return view;
	}

	private EspWifiAdmin mWifiAdmin;

	private ClearEditText et_company_name;
	private ClearEditText et_ichid;
	private ImageButton ib_scan_box;

	private ClearEditText et_wifi_ssid;
	private ClearEditText et_wifi_pwd;
	private ClearEditText et_box_name;

	private Button bt_add_box;

	private String getICHIDString = "扫码获取ID\n自动搜索ID";
	private final String getICHID[] = getICHIDString.split("\n");
	private ButtonOnClick buttonOnClick = new ButtonOnClick(1);

	@Override
	public void initView() {
		mWifiAdmin = new EspWifiAdmin(context);

		et_company_name = (ClearEditText) findViewById(R.id.et_company_name);
		et_ichid = (ClearEditText) findViewById(R.id.et_ichid);
		ib_scan_box = (ImageButton) findViewById(R.id.ib_scan_box);

		et_wifi_ssid = (ClearEditText) findViewById(R.id.et_wifi_ssid);
		et_wifi_pwd = (ClearEditText) findViewById(R.id.et_wifi_pwd);
		et_box_name = (ClearEditText) findViewById(R.id.et_box_name);

		bt_add_box = (Button) findViewById(R.id.bt_add_box);

	}

	@Override
	public void initData() {
		String complanyName = DataManager.getInstance().getCurrentUser().getCompanyName();
		et_company_name.setText(complanyName);

		String apSsid = mWifiAdmin.getWifiConnectedSsid();
		if (apSsid != null) {
			et_wifi_ssid.setText(apSsid);
		} else {
			et_wifi_ssid.setText("");
		}

		// check whether the wifi is connected
		boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
		bt_add_box.setEnabled(!isApSsidEmpty);
	}

	@Override
	public void initEvent() {
		ib_scan_box.setOnClickListener(this);
		bt_add_box.setOnClickListener(this);
		onOpenBluetooth();
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}
	}

	public static final int REQUEST_TO_CAMERA_SCAN = 22;
	public static final int REQUEST_TO_BLE_SCAN = 23;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_scan_box:
			showSingleChoiceAlertDialog("请选择获取方式", getICHID, buttonOnClick, null, context);
			break;
		case R.id.bt_add_box:
			boxWifiConnectConfig();
			break;
		default:
			break;
		}
	}

	/**
	 * 财盒网络配置
	 */
	private void boxWifiConnectConfig() {
		if (checkFormValid()) {
			String apSsid = et_wifi_ssid.getText().toString();
			String apPassword = et_wifi_pwd.getText().toString();
			String apBssid = mWifiAdmin.getWifiConnectedBssid();
			String isSsidHiddenStr = "NO";
			String taskResultCountStr = Integer.toString(1);

			new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, isSsidHiddenStr, taskResultCountStr);
		}
	}

	private void onScanning() {
		BluetoothController.openBTAdapter();
		// .argument...argument.
	}

	private static final int REQUEST_FINE_LOCATION = 0;

	private void onOpenBluetooth() {

		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(context,
					Manifest.permission.ACCESS_COARSE_LOCATION);
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				// 判断是否需要 向用户解释，为什么要申请该权限
				if (ActivityCompat.shouldShowRequestPermissionRationale(context,
						Manifest.permission.ACCESS_COARSE_LOCATION))
					// showToast(context, "请允许开启扫描蓝牙打印机功能", Toast.LENGTH_LONG);
					ActivityCompat.requestPermissions(context,
							new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_FINE_LOCATION);
				return;
			} else {
				BluetoothController.openBTAdapter();
			}
		} else {
			BluetoothController.openBTAdapter();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		switch (requestCode) {
		case REQUEST_FINE_LOCATION:
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// The requested permission is granted.
				onScanning();
			} else {
				// The user disallowed the requested permission.
				onScanning();
			}
			break;
		}
	}

	/**
	 * TODO 配网失败添加财盒
	 */
	private void addConfigFailWifiBox() {

		showShortToast("很抱歉！财盒网络配置失败，请手动在财盒管理界面重新进行网络配置！");

		if (checkFormValid()) {

			String ichId = et_ichid.getText().toString();
			String boxName = et_box_name.getText().toString();
			long groupId = DataManager.getInstance().getCurrentUser().getGroupId();
			final String wifiId = "";

			HttpRequest.addBox(ichId, boxName, groupId, wifiId, HttpRequest.RESULT_ADD_BOX_SUCCEED,
					new OnHttpResponseListener() {

						@Override
						public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
								String resultData) {
							if (resultCode != 1) {
								showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
								return;
							}

							long boxId = JSON.parseObject(resultData).getLongValue("boxId");
							User user = DataManager.getInstance().getCurrentUser();
							user.setBoxId(boxId);
							DataManager.getInstance().saveCurrentUser(user);
							replaceToBoxFragment();
						}

						@Override
						public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
							showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
									+ resultMessage);
						}
					});
		}
	}

	/**
	 * TODO 配网成功添加财盒
	 */
	private void addConfigSuccessWifiBox() {

		showShortToast("恭喜您！财盒网络配置成功！");

		if (checkFormValid()) {

			String ichId = et_ichid.getText().toString();
			String boxName = et_box_name.getText().toString();
			long groupId = DataManager.getInstance().getCurrentUser().getGroupId();
			final String wifiId = et_wifi_ssid.getText().toString();

			HttpRequest.addBox(ichId, boxName, groupId, wifiId, HttpRequest.RESULT_ADD_BOX_SUCCEED,
					new OnHttpResponseListener() {

						@Override
						public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
								String resultData) {
							if (resultCode != 1) {
								showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
								return;
							}
							long boxId = JSON.parseObject(resultData).getLongValue("boxId");
							User user = DataManager.getInstance().getCurrentUser();
							user.setWifiId(wifiId);
							user.setBoxId(boxId);
							DataManager.getInstance().saveCurrentUser(user);
							replaceToBoxFragment();
						}

						@Override
						public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
							showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
									+ resultMessage);
						}
					});
		}
	}

	private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
		runUiThread(new Runnable() {
			@Override
			public void run() {
				// String text = result.getBssid() + "连接WIFI成功";
			}
		});
	}

	private IEsptouchListener myListener = new IEsptouchListener() {
		@Override
		public void onEsptouchResultAdded(final IEsptouchResult result) {
			onEsptoucResultAddedPerform(result);
		}
	};

	private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

		private ProgressDialog mProgressDialog;
		private IEsptouchTask mEsptouchTask;
		// without the lock, if the user tap confirm and cancel quickly enough,
		// the bug will arise. the reason is follows:
		// 0. task is starting created, but not finished
		// 1. the task is cancel for the task hasn't been created, it do nothing
		// 2. task is created
		// 3. Oops, the task should be cancelled, but it is running
		private final Object mLock = new Object();

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("正在配置财盒网络中...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						if (__IEsptouchTask.DEBUG) {
							Log.i("", "progress dialog is canceled");
						}
						if (mEsptouchTask != null) {
							mEsptouchTask.interrupt();
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "请稍等...", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
		}

		@Override
		protected List<IEsptouchResult> doInBackground(String... params) {
			int taskResultCount = -1;
			synchronized (mLock) {
				String apSsid = params[0];
				String apBssid = params[1];
				String apPassword = params[2];
				String isSsidHiddenStr = params[3];
				String taskResultCountStr = params[4];
				boolean isSsidHidden = false;
				if (isSsidHiddenStr.equals("YES")) {
					isSsidHidden = true;
				}
				taskResultCount = Integer.parseInt(taskResultCountStr);
				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, isSsidHidden, context);
				mEsptouchTask.setEsptouchListener(myListener);
			}
			List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
			return resultList;
		}

		@Override
		protected void onPostExecute(List<IEsptouchResult> result) {
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确认");
			IEsptouchResult firstResult = result.get(0);
			// check whether the task is cancelled and no results received
			if (!firstResult.isCancelled()) {
				int count = 0;
				// max results to be displayed, if it is more than
				// maxDisplayCount,
				// just show the count of redundant ones
				final int maxDisplayCount = 5;
				// the task received some results including cancelled while
				// executing before receiving enough results
				if (firstResult.isSuc()) {
					StringBuilder sb = new StringBuilder();
					for (IEsptouchResult resultInList : result) {
						// sb.append("配置成功, bssid = " + resultInList.getBssid()
						// + ",IP地址 = "
						// + resultInList.getInetAddress().getHostAddress() +
						// "\n");
						sb.append("恭喜您！财盒网络配置成功！\n");

						count++;
						if (count >= maxDisplayCount) {
							break;
						}
					}
					if (count < result.size()) {
						sb.append("\nthere's " + (result.size() - count) + "more result(s) without showing\n");
					}
					mProgressDialog.setMessage(sb.toString());

					addConfigSuccessWifiBox();
				} else {
					mProgressDialog.setMessage("很抱歉！财盒网络配置失败，请检查您输入的WIFI密码是否正确后重试。");

					addConfigFailWifiBox();
				}
			}
		}
	}

	public class ButtonOnClick implements DialogInterface.OnClickListener {
		private int index;

		public ButtonOnClick(int index) {
			this.index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which >= 0) {
				index = which;
			} else {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					if (getICHID[index].contains("扫码获取")) {
						toActivity(ActivityScan.createIntent(context), REQUEST_TO_CAMERA_SCAN);
					} else {
						toActivity(ActivityScanBox.createIntent(context), REQUEST_TO_BLE_SCAN);
					}
				} else if (which == DialogInterface.BUTTON_NEGATIVE) {
				}
			}
		}
	}

	/**
	 * TODO 表单验证
	 * 
	 * @return
	 */
	private boolean checkFormValid() {
		if (et_company_name.getText().equals(null) || et_company_name.getText().toString().trim().equals("")) {
			et_company_name.setShakeAnimation();
			showShortToast("企业组织名称不能为空");
			return false;
		}
		if (et_ichid.getText().equals(null) || et_ichid.getText().toString().trim().equals("")) {
			et_ichid.setShakeAnimation();
			showShortToast("财盒ID不能为空");
			return false;
		}
		if (et_wifi_ssid.getText().equals(null) || et_wifi_ssid.getText().toString().trim().equals("")) {
			et_wifi_ssid.setShakeAnimation();
			showShortToast("WIFI名称不能为空");
			return false;
		}
		if (et_wifi_pwd.getText().equals(null) || et_wifi_pwd.getText().toString().trim().equals("")) {
			et_wifi_pwd.setShakeAnimation();
			showShortToast("WIFI密码不能为空");
			return false;
		}
		if (et_box_name.getText().equals(null) || et_box_name.getText().toString().trim().equals("")) {
			et_box_name.setShakeAnimation();
			showShortToast("财盒名称不能为空");
			return false;
		}

		return true;
	}

	/**
	 * 替换为BoxFragment
	 */
	private void replaceToBoxFragment() {
		FragmentTransaction trasection = getFragmentManager().beginTransaction();
		Fragment boxFragment = new FragmentBox();
		trasection.replace(R.id.flBottomTabFragmentContainer, boxFragment);
		trasection.addToBackStack(null);
		trasection.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_CAMERA_SCAN:
			if (data != null) {
				String result = data.getStringExtra(ActivityScan.RESULT_QRCODE_STRING);
				et_ichid.setText(result);
			}
			break;
		case REQUEST_TO_BLE_SCAN:
			if (data != null) {
				String result = data.getStringExtra(ActivityScanBox.RESULT_BOX_BLE_STRING);
				et_ichid.setText(result);
			}
			break;
		default:
			break;
		}
	}

	public static AlertDialog showSingleChoiceAlertDialog(String title, CharSequence[] items,
			ButtonOnClick buttonOnClick, DialogInterface.OnDismissListener pDismissListener, Context context) {
		AlertDialog mAlertDialog = new HerilyAlertDialog.Builder(context).setTitle(title)
				.setSingleChoiceItems(items, 2, buttonOnClick).setPositiveButton(android.R.string.ok, buttonOnClick)
				.setNegativeButton(android.R.string.cancel, buttonOnClick).show();
		mAlertDialog.setCanceledOnTouchOutside(false);
		mAlertDialog.setCancelable(false);
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}
}