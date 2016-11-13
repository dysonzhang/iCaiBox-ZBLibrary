package com.icaihe.activity_fragment;

import com.icaihe.R;

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
	private static final String TAG = "MySettingFragment";

	// 与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 创建一个Fragment实例
	 * 
	 * @return
	 */
	public static MySettingFragment createInstance() {
		return new MySettingFragment();
	}

	// 与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 类相关初始化，必须使用<<<<<<<<<<<<<<<<
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.my_setting_fragment);
		// 类相关初始化，必须使用>>>>>>>>>>>>>>>>

		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private ImageView iv_setting_head;
	private TextView tv_user_name;
	private TextView tv_user_phone;
	private Button bt_logout;

	@Override
	public void initView() {// 必须调用
		iv_setting_head = (ImageView) findViewById(R.id.iv_setting_head);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
		bt_logout = (Button) findViewById(R.id.bt_logout);
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {// 必须调用
		tv_user_name.setText("dysonzhang");
		tv_user_phone.setText("18666668888");
	}

	private void logout() {
		context.finish();
	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {// 必须调用

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

	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}