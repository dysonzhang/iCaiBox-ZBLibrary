package com.icaihe.activity_fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.icaihe.widget.ClearEditText;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;

/**
 * 手机号登录界面
 */
public class ActivityLogin extends BaseActivity implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static String TAG = "ActivityLogin";

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityLogin.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login, this);

		initView();
		initData();
		initEvent();
	}

	private TimeCount time = new TimeCount(60000, 1000);

	private ClearEditText et_phone;
	private ClearEditText et_code;
	private Button bt_get_code;
	private Button bt_login;

	@Override
	public void initView() {
		super.initView();
		et_phone = (ClearEditText) findViewById(R.id.et_phone);
		et_code = (ClearEditText) findViewById(R.id.et_code);
		bt_get_code = (Button) findViewById(R.id.bt_get_code);
		bt_login = (Button) findViewById(R.id.bt_login);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		super.initEvent();
		et_phone.setOnClickListener(this);
		et_code.setOnClickListener(this);
		bt_get_code.setOnClickListener(this);
		bt_login.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_get_code:
			getCode();
			break;
		case R.id.bt_login:
			login();
			break;
		default:
			break;
		}
	}

	/**
	 * TODO 获取手机验证码
	 */
	private void getCode() {
		if (checkPhone()) {
			// 开始计时
			time.start();

			final String phone = et_phone.getText().toString();
			SVProgressHUD.showWithStatus(this, "请稍候...");
			HttpRequest.getCode(phone, 1, HttpRequest.RESULT_GET_CODE_SUCCEED, new OnHttpResponseListener() {

				@Override
				public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
						String resultData) {
					SVProgressHUD.dismiss(context);
					if (resultCode != 1) {
						showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
						return;
					}
					// 收到验证码后存储手机号码
					DataKeeper.save(DataKeeper.getRootSharedPreferences(), "verifyPhone", phone);
					showShortToast("验证码已发送，请稍候...");
				}

				@Override
				public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
					SVProgressHUD.dismiss(context);
					showShortToast(
							"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
				}
			});
		}
	}

	/**
	 * TODO 手机号和验证码登录
	 */
	private void login() {
		if (checkFormValid()) {

			// 验证存储的手机号是否为空
			if (DataKeeper.getRootSharedPreferences().getString("verifyPhone", "").toString().trim().equals("")) {
				showShortToast("请先验证您的手机号码");
				return;
			}

			final String phone = et_phone.getText().toString();
			final String code = et_code.getText().toString();

			SVProgressHUD.showWithStatus(this, "请稍候...");
			HttpRequest.login(phone, code, HttpRequest.RESULT_LOGN_SUCCEED, new OnHttpResponseListener() {

				@Override
				public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
						String resultData) {
					SVProgressHUD.dismiss(context);
					if (resultCode != 1) {
						showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
						return;
					}
					showShortToast("登录成功！");

					Log.i(TAG, "resultCode-->" + resultCode + " resultMessage-->" + resultMessage);
					Log.i(TAG, "resultData-->" + resultData);

					// 保存用户信息
					User user = saveUserInfo(resultData);

					// JPush推送设置标签和别名
					PushSetUtil pushSetUtil = new PushSetUtil(context);
					pushSetUtil.setAlias(user.getUserId() + "");

					// 跳转到选择财盒群组界面
					if (user.isNewUser()) {
						startActivity(ActivityChoseGroup.createIntent(context));
						overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
					} else {
						startActivity(ActivityMainTab.createIntent(context));
						overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
					}
					finish();
				}

				@Override
				public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
					SVProgressHUD.dismiss(context);
					showShortToast(
							"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
				}
			});
		}
	}

	/**
	 * TODO 保存用户登录信息
	 * 
	 * @param resultData
	 * @return
	 */
	private User saveUserInfo(String resultData) {

		JSONObject jsonObject;
		User user = new User();
		try {
			jsonObject = new JSONObject(resultData);

			String token = jsonObject.getString("token").equals("null") ? "" : jsonObject.getString("token");
			Long userId = Long
					.parseLong(jsonObject.getString("userId").equals("null") ? "0" : jsonObject.getString("userId"));
			String name = jsonObject.getString("name").equals("null") ? "" : jsonObject.getString("name");
			String phone = jsonObject.getString("phone").equals("null") ? "" : jsonObject.getString("phone");
			String alarmNum = jsonObject.getString("alarmNum").equals("null") ? "" : jsonObject.getString("alarmNum");

			Boolean isNewUser = jsonObject.getBoolean("isNewUser");
			Long groupId = Long
					.parseLong(jsonObject.getString("groupId").equals("null") ? "0" : jsonObject.getString("groupId"));

			String companyName = jsonObject.getString("companyName").equals("null") ? ""
					: jsonObject.getString("companyName");

			Long boxId = Long
					.parseLong(jsonObject.getString("boxId").equals("null") ? "0" : jsonObject.getString("boxId"));

			String wifiId = jsonObject.getString("wifiId").equals("null") ? "" : jsonObject.getString("wifiId");

			Boolean isGroupCreator = jsonObject.getBoolean("isGroupCreator");

			// user =JSON.parseObject(resultData, User.class);
			user.setUserId(userId);
			user.setToken(token);
			user.setBoxId(boxId);
			user.setCompanyName(companyName);
			user.setGroupId(groupId);
			user.setName(name);
			user.setNewUser(isNewUser);
			user.setPhone(phone);
			user.setAlarmNum(alarmNum);
			user.setWifiId(wifiId);
			user.setGroupCreator(isGroupCreator);
			DataManager.getInstance().saveCurrentUser(user);

		} catch (JSONException e) {
			e.printStackTrace();
			showShortToast("搜索财盒群数据转换异常！");
		}

		return user;
	}

	/**
	 * TODO 检测手机号
	 * 
	 * @return
	 */
	private boolean checkPhone() {
		if (et_phone.getText().equals(null) || et_phone.getText().toString().trim().equals("")) {
			// 设置晃动
			et_phone.setShakeAnimation();
			// 设置提示
			showShortToast("请填写您的手机号");
			return false;
		}
		if (et_phone.getText().toString().length() != 11) {
			et_phone.setShakeAnimation();
			showShortToast("手机号长度必须是11位");
			return false;
		}
		return true;
	}

	/**
	 * TODO 检测手机号和验证码
	 * 
	 * @return
	 */
	private boolean checkFormValid() {
		if (et_code.getText().equals(null) || et_code.getText().toString().trim().equals("")) {
			et_code.setShakeAnimation();
			showShortToast("验证码不能为空");
			return false;
		}
		if (et_code.getText().toString().length() > 6) {
			et_code.setShakeAnimation();
			showShortToast("验证码长度不能大于6位");
			return false;
		}
		if (et_phone.getText().equals(null) || et_phone.getText().toString().trim().equals("")) {
			et_phone.setShakeAnimation();
			showShortToast("手机号不能为空");
			return false;
		}
		if (et_phone.getText().toString().length() != 11) {
			et_phone.setShakeAnimation();
			showShortToast("手机号长度必须是11位");
			return false;
		}
		String verifyPhone = DataKeeper.getRootSharedPreferences().getString("verifyPhone", "");
		if (!verifyPhone.toString().equals(et_phone.getText().toString().trim())) {
			showShortToast("手机号发送变化，请重新输入获取验证码验证该手机号");
			return false;
		}
		return true;
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	/**
	 * TODO 获取验证码倒计时
	 * 
	 * @author dyson
	 *
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			bt_get_code.setText("重新验证");
			bt_get_code.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			bt_get_code.setClickable(false);
			bt_get_code.setText(millisUntilFinished / 1000 + "秒后可重发");
		}
	}
}
