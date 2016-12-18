package com.icaihe.activity_fragment;

import java.util.List;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.admin.EspWifiAdmin;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.icaihe.R;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.icaihe.widget.ClearEditText;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;

public class ActivityReConfigWifi extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {
	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityReConfigWifi.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reconfig_wifi, this);
		initView();
		initData();
		initEvent();
	}
	@Override
	public void toWarnActivity(boolean isBattery) {
		Intent intent = ActivityWran.createIntent(context);
		intent.putExtra("isBattery", isBattery ? 1 : 0);
		toActivity(intent);
	}
	private EspWifiAdmin mWifiAdmin;

	private ImageView iv_back;
	private EditText et_wifi_ssid;
	private ClearEditText et_wifi_pwd;
	private Button bt_config;

	@Override
	public void initView() {
		super.initView();
		mWifiAdmin = new EspWifiAdmin(context);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_wifi_ssid = (EditText) findViewById(R.id.et_wifi_ssid);
		et_wifi_pwd = (ClearEditText) findViewById(R.id.et_wifi_pwd);
		bt_config = (Button) findViewById(R.id.bt_config);
	}

	@Override
	public void initData() {
		super.initData();
		String apSsid = mWifiAdmin.getWifiConnectedSsid();
		if (apSsid != null) {
			et_wifi_ssid.setText(apSsid);
		} else {
			et_wifi_ssid.setText("");
		}

		// check whether the wifi is connected
		boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
		bt_config.setEnabled(!isApSsidEmpty);

	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
		bt_config.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_config:
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

	private void boxWifiConfigSuccess() {
		showShortToast("恭喜您！财盒网络配置成功！");
		
		final User user = DataManager.getInstance().getCurrentUser();
		long boxId = user.getBoxId();
		final String wifiId = et_wifi_ssid.getText().toString();
		HttpRequest.updateBoxWifiId(boxId, wifiId, HttpRequest.RESULT_UPDATE_BOX_WIFI_ID_SUCCEED,
				new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						if (resultCode != 1) {
							showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
							return;
						}
						user.setWifiId(wifiId);
						DataManager.getInstance().saveCurrentUser(user);
						finish();
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
						finish();
					}
				});
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
						// sb.append("\nthere's " + (result.size() - count) + "
						// more result(s) without showing\n");
						sb.append("\n很抱歉！财盒网络配置失败。there's " + (result.size() - count)
								+ " more result(s) without showing\n");
					}
					mProgressDialog.setMessage(sb.toString());

					boxWifiConfigSuccess();
					
				} else {
					mProgressDialog.setMessage("很抱歉！财盒网络配置失败，请检查您输入的WIFI密码是否正确后重试。");
				}
			}
		}
	}

	private boolean checkFormValid() {
		if (et_wifi_pwd.getText().equals(null) || et_wifi_pwd.getText().toString().trim().equals("")) {
			et_wifi_pwd.setShakeAnimation();
			showShortToast("WIFI密码不能为空");
			return false;
		}
		return true;
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
