package com.icaihe.activity_fragment;

import com.icaihe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 财盒fragment
 * 
 */
public class FragmentFristBox extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {

	public static FragmentFristBox createInstance() {
		return new FragmentFristBox();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.fragment_frist_box);

		initView();
		initData();
		initEvent();

		return view;
	}

	private Button bt_box_config;
	private Button bt_box_buy;

	@Override
	public void initView() {
		bt_box_config = (Button) findViewById(R.id.bt_box_config);
		bt_box_buy = (Button) findViewById(R.id.bt_box_buy);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
		bt_box_config.setOnClickListener(this);
		bt_box_buy.setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_box_config:
			replaceToAddBoxFragment();
			break;
		case R.id.bt_box_buy:
			showShortToast("bt_box_buy");
			break;
		default:
			break;
		}
	}

	/**
	 * 替换Fragment
	 */
	private void replaceToAddBoxFragment() {
		FragmentTransaction trasection = getFragmentManager().beginTransaction();
		Fragment fragment = new FragmentAddBox();
		trasection.replace(R.id.flBottomTabFragmentContainer, fragment);
		trasection.addToBackStack(null);
		trasection.commit();
	}
}