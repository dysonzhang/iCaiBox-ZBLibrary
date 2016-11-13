package com.icaihe.activity_fragment;

import com.icaihe.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 财盒fragment
 * 
 * @use new BoxFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class BoxFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {
	private static final String TAG = "BoxFragment";

	// 与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 创建一个Fragment实例
	 * 
	 * @return
	 */
	public static BoxFragment createInstance() {
		return new BoxFragment();
	}

	// 与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 类相关初始化，必须使用<<<<<<<<<<<<<<<<
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.box_fragment);
		// 类相关初始化，必须使用>>>>>>>>>>>>>>>>

		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private TextView tv_box_name;
	private TextView tv_add_time;
	private Button bt_open;
	private Button bt_auth;

	@Override
	public void initView() {// 必须调用

		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		tv_add_time = (TextView) findViewById(R.id.tv_add_time);

		bt_open = (Button) findViewById(R.id.bt_open);
		bt_auth = (Button) findViewById(R.id.bt_auth);
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {// 必须调用
		tv_box_name.setText("dyson的财盒");
		tv_add_time.setText("添加时间：2016-12-12 12:12");
	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {// 必须调用

		bt_open.setOnClickListener(this);
		bt_auth.setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			this.showShortToast("确定打开");
			break;
		case 1:
			this.showShortToast("确定取消");
			break;
		default:
			break;
		}
	}

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onClick(View v) {// 直接调用不会显示v被点击效果
		switch (v.getId()) {
		case R.id.bt_open:
			new AlertDialog(context, "打开", "确定打开？", true, 0, this).show();
			break;
		case R.id.bt_auth:
			new AlertDialog(context, "授权", "确定授权？", true, 1, this).show();
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