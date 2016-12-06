package com.icaihe.activity_fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.espressif.iot.esptouch.admin.EspWifiAdmin;
import com.icaihe.R;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.icaihe.widget.BadgeView;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.SettingUtil;

/**
 * 应用主页
 */
public class ActivityMainTab extends ActivityBaseBottomTab
		implements OnClickListener, OnBottomDragListener, OnDialogButtonClickListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityMainTab.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab, this);

		initView();
		initData();
		initEvent();

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	private static LinearLayout ll_bell;
	private static ImageView iv_bell;
	private static TextView tv_open_log;
	private static BadgeView badge;

	@Override
	public void initView() {// 必须调用
		super.initView();
		exitAnim = R.anim.bottom_push_out;

		ll_bell = (LinearLayout) findViewById(R.id.ll_bell);
		iv_bell = (ImageView) findViewById(R.id.iv_bell);
		tv_open_log = (TextView) findViewById(R.id.tv_open_log);
		badge = new BadgeView(context, ll_bell);
		badge.setTextSize(8);
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge.hide();
	}

	@Override
	protected int[] getTabClickIds() {
		return new int[] { R.id.ll_tab_notice, R.id.ll_tab_box, R.id.ll_tab_contract, R.id.ll_tab_my };
	}

	@Override
	protected int[][] getTabSelectIds() {
		return new int[][] { new int[] { R.id.iv_tab_notice, R.id.iv_tab_box, R.id.iv_tab_contract, R.id.iv_tab_my }, // 顶部图标
				new int[] { R.id.tv_tab_notice, R.id.tv_tab_box, R.id.tv_tab_contract, R.id.tv_tab_my }// 底部文字
		};
	}

	public static void setTabMenu(int type) {
		if (type == 0) {
			iv_bell.setVisibility(View.GONE);
			badge.hide();
			tv_open_log.setVisibility(View.GONE);
		} else if (type == 1) {
			iv_bell.setVisibility(View.VISIBLE);
			setAlarmNumber();
			tv_open_log.setVisibility(View.GONE);
		} else if (type == 2) {
			iv_bell.setVisibility(View.GONE);
			badge.hide();
			tv_open_log.setVisibility(View.VISIBLE);
		} else {
			iv_bell.setVisibility(View.GONE);
			badge.hide();
			tv_open_log.setVisibility(View.GONE);
		}
	}

	@Override
	protected Fragment getFragment(int position) {
		switch (position) {
		case 1:
			User user = DataManager.getInstance().getCurrentUser();
			long boxId = user.getBoxId();
			if (boxId == 0) {
				// 该用户无已授权财盒或没有财盒
				return FragmentFristBox.createInstance();
			} else {
				wifiConfigCheck();
				// 该用户有已授权财盒或拥有财盒
				return FragmentBox.createInstance();
			}
		case 2:
			return FragmentContractList.createInstance();
		case 3:
			return FragmentMySetting.createInstance();
		default:
			return FragmentNoticeList.createInstance();
		}
	}

	private static final String[] TAB_NAMES = { "动态", "财盒", "通讯录", "我的" };

	@Override
	protected void selectTab(int position) {
		// 导致切换时闪屏，建议去掉BottomTabActivity中的topbar，在fragment中显示topbar
		// rlBottomTabTopbar.setVisibility(position == 2 ? View.GONE :
		// View.VISIBLE);
		tvBaseTitle.setText(TAB_NAMES[position]);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();
		iv_bell.setOnClickListener(this);
		tv_open_log.setOnClickListener(this);
		wifiConfigCheck();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUserInfo();
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
	}

	private long firstTime = 0;
	private static int count = 0;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {
				showShortToast("再按一次退出");
				firstTime = secondTime;
			} else {// 完全退出
				moveTaskToBack(false);// 应用退到后台
				System.exit(0);
			}
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_bell:
			startActivity(ActivityAlarmNotice.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			clearAlarm();
			break;
		case R.id.tv_open_log:
			startActivity(ActivityOpenRecord.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		default:
			break;
		}
	}

	private static void setAlarmNumber() {
		if (iv_bell.getVisibility() == View.VISIBLE && count > 0) {
			badge.setText("" + count);
			badge.show();
		} else {
			badge.hide();
		}
	}

	private void updateUserInfo() {
		HttpRequest.getUserDetail(HttpRequest.RESULT_GET_USER_DETAIL_SUCCEED, new OnHttpResponseListener() {
			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {

				if (resultCode != 1) {
					showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
					if (resultCode < 0) {
						PushSetUtil pushSetUtil = new PushSetUtil(context);
						pushSetUtil.setAlias("null");
						User user = DataManager.getInstance().getCurrentUser();
						DataManager.getInstance().removeUser(user);
						startActivity(ActivityLogin.createIntent(context));
					}
					return;
				}
				JSONObject jsonObject;
				User user = DataManager.getInstance().getCurrentUser();
				try {
					jsonObject = new JSONObject(resultData);

					Long userId = Long.parseLong(
							jsonObject.getString("userId").equals("null") ? "0" : jsonObject.getString("userId"));
					String name = jsonObject.getString("name").equals("null") ? "" : jsonObject.getString("name");
					String phone = jsonObject.getString("phone").equals("null") ? "" : jsonObject.getString("phone");
					String alarmNum = jsonObject.getString("alarmNum").equals("null") ? ""
							: jsonObject.getString("alarmNum");
					Boolean isNewUser = jsonObject.getBoolean("isNewUser");
					Long groupId = Long.parseLong(
							jsonObject.getString("groupId").equals("null") ? "0" : jsonObject.getString("groupId"));
					String companyName = jsonObject.getString("companyName").equals("null") ? ""
							: jsonObject.getString("companyName");
					Long boxId = Long.parseLong(
							jsonObject.getString("boxId").equals("null") ? "0" : jsonObject.getString("boxId"));
					String wifiId = jsonObject.getString("wifiId").equals("null") ? "" : jsonObject.getString("wifiId");

					user.setUserId(userId);
					user.setBoxId(boxId);
					user.setCompanyName(companyName);
					user.setGroupId(groupId);
					user.setName(name);
					user.setNewUser(isNewUser);
					user.setPhone(phone);
					user.setAlarmNum(alarmNum);
					user.setWifiId(wifiId);
					DataManager.getInstance().saveCurrentUser(user);

					count = Integer.parseInt(alarmNum);
					setAlarmNumber();

				} catch (JSONException e) {
					e.printStackTrace();
					showShortToast("数据转换异常！");
				}
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});
	}

	private void clearAlarm() {
		HttpRequest.resetAlarmNum(HttpRequest.RESULT_RESET_ALARM_NUM_SUCCEED, new OnHttpResponseListener() {
			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
				if (resultCode != 1) {
					showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
					if (resultCode < 0) {
						PushSetUtil pushSetUtil = new PushSetUtil(context);
						pushSetUtil.setAlias("null");
						User user = DataManager.getInstance().getCurrentUser();
						DataManager.getInstance().removeUser(user);
						startActivity(ActivityLogin.createIntent(context));
					}
					return;
				}
				count = 0;
				badge.setText("");
				badge.hide();
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});
	}

	public void wifiConfigCheck() {
		User user = DataManager.getInstance().getCurrentUser();
		if (user.getBoxId() != 0 && user.isGroupCreator()) {
			if (user.getWifiId().equals("")) {
				new AlertDialog(context, "提示", "您的财盒目前尚未完成连接网络，只有连接网络后，APP才能查看到相关操作记录哦。请确认是否配置财盒WIFI？", true, 1, this)
						.show();
				return;
			}

			EspWifiAdmin mWifiAdmin = new EspWifiAdmin(context);
			String apSsid = mWifiAdmin.getWifiConnectedSsid();
			if (!user.getWifiId().equals(apSsid)) {
				new AlertDialog(context, "提示", "您的手机网络目前和您的财盒网络连接了不同的WIFI，请确认是否需要重新配置财盒WIFI？", true, 2, this).show();
			}
		}
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 1:
			this.toActivity(ActivityReConfigWifi.createIntent(context));
			break;
		case 2:
			this.toActivity(ActivityReConfigWifi.createIntent(context));
			break;
		default:
			break;
		}
	}
}