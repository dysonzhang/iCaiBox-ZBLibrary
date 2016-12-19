package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.ichihe.util.APPManagement;
import com.ichihe.util.Constant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.CommonUtil;

/**
 * 我的设置fragment
 */
public class FragmentMySetting extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {

	public static String TAG = "FragmentMySetting";

	public static FragmentMySetting createInstance() {
		return new FragmentMySetting();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.fragment_my_setting);

		initView();
		initData();
		initEvent();

		return view;
	}

	private ImageView iv_setting_head;
	private TextView tv_user_name;
	private TextView tv_user_phone;
	private TextView tv_version;
	private Button bt_logout;

	@Override
	public void initView() {
		iv_setting_head = (ImageView) findViewById(R.id.iv_setting_head);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
		tv_version = (TextView) findViewById(R.id.tv_version);
		bt_logout = (Button) findViewById(R.id.bt_logout);

		ActivityMainTab.setTabMenu(0);
	}

	@Override
	public void initData() {
		String name = DataManager.getInstance().getCurrentUser().getName();
		String phone = DataManager.getInstance().getCurrentUser().getPhone();
		tv_user_name.setText(name + "");
		tv_user_phone.setText(phone + "");
		tv_version.setText("v" + APPManagement.getVersionName(context));
	}

	private void logout() {
		PushSetUtil pushSetUtil = new PushSetUtil(context);
		pushSetUtil.setAlias("null");

		User user = DataManager.getInstance().getCurrentUser();

		DataManager.getInstance().removeUser(user);
		startActivity(ActivityLogin.createIntent(context));

		context.finish();
	}

	@Override
	public void initEvent() {

		iv_setting_head.setOnClickListener(this);
		bt_logout.setOnClickListener(this);

		findViewById(R.id.ll_my_group).setOnClickListener(this);
		findViewById(R.id.ll_help_center).setOnClickListener(this);
		findViewById(R.id.ll_feedback).setOnClickListener(this);
		findViewById(R.id.ll_about).setOnClickListener(this);
		// findViewById(R.id.ll_version).setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			logout();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_setting_head:
			// showShortToast("onClick iv_setting_head");
			break;
		case R.id.bt_logout:
			new AlertDialog(context, "提示", "请确定是否退出登录？", true, 0, this).show();
			break;
		case R.id.ll_my_group:
			toActivity(ActivityGroupMember.createIntent(context));
			break;
		case R.id.ll_help_center:
			CommonUtil.openWebSite(context, Constant.APP_WEBSITE_HELP_CENTER);
			break;
		case R.id.ll_feedback:
			toActivity(ActivityFeedback.createIntent(context));
			break;
		case R.id.ll_about:
			CommonUtil.openWebSite(context, Constant.APP_WEBSITE_ABOUT);
			break;
		case R.id.ll_version:
			break;
		default:
			break;
		}
	}
}