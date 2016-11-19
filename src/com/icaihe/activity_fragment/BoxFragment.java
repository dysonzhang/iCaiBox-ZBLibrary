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
//	private static final String TAG = "BoxFragment";
	/**
	 * 创建一个Fragment实例
	 * 
	 * @return
	 */
	public static BoxFragment createInstance() {
		return new BoxFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.box_fragment);
		
		initView();
		initData();
		initEvent();
		
		return view;
	}

	private TextView tv_box_name;
	private TextView tv_add_time;
	private Button bt_open;
	private Button bt_auth;

	@Override
	public void initView() {
		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		tv_add_time = (TextView) findViewById(R.id.tv_add_time);

		bt_open = (Button) findViewById(R.id.bt_open);
		bt_auth = (Button) findViewById(R.id.bt_auth);
	}

	@Override
	public void initData() {
		tv_box_name.setText("dyson的财盒");
		tv_add_time.setText("添加时间：2016-12-12 12:12");
	}

	@Override
	public void initEvent() {
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

	@Override
	public void onClick(View v) {
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
}