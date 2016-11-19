package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.icaihe.application.ICHApplication;

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

/**
 * 设置fragment
 * 
 * @use new MySettingFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class MySettingFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {
	// private static final String TAG = "MySettingFragment";

	/**
	 * 创建一个Fragment实例
	 * 
	 * @return
	 */
	public static MySettingFragment createInstance() {
		return new MySettingFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.my_setting_fragment);

		initView();
		initData();
		initEvent();

		return view;
	}

	private ImageView iv_setting_head;
	private TextView tv_user_name;
	private TextView tv_user_phone;
	private Button bt_logout;

	@Override
	public void initView() {
		iv_setting_head = (ImageView) findViewById(R.id.iv_setting_head);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
		bt_logout = (Button) findViewById(R.id.bt_logout);
	}

	@Override
	public void initData() {
		String name = ICHApplication.getInstance().getCurrentUser().getName();
		String phone = ICHApplication.getInstance().getCurrentUser().getPhone();
		tv_user_name.setText(name + "");
		tv_user_phone.setText(phone + "");
	}

	private void logout() {
		context.finish();
	}

	@Override
	public void initEvent() {

		iv_setting_head.setOnClickListener(this);
		bt_logout.setOnClickListener(this);

		findViewById(R.id.ll_my_icaihe).setOnClickListener(this);
		findViewById(R.id.ll_help_center).setOnClickListener(this);
		findViewById(R.id.ll_feedback).setOnClickListener(this);
		findViewById(R.id.ll_about).setOnClickListener(this);
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

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onClick(View v) {// 直接调用不会显示v被点击效果
		switch (v.getId()) {
		case R.id.iv_setting_head:
			showShortToast("onClick  iv_setting_head");
			break;
		case R.id.bt_logout:
			new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
			break;
		case R.id.ll_my_icaihe:
			break;
		case R.id.ll_help_center:
			break;
		case R.id.ll_feedback:
			break;
		case R.id.ll_about:
			break;
		default:
			break;
		}
	}
}