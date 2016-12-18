package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 用户无财盒fragment
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

	private TextView tv_box_config_tips;
	private Button bt_box_config;
	private Button bt_box_buy;
	private boolean isCanAddBox;

	@Override
	public void initView() {
		tv_box_config_tips = (TextView) findViewById(R.id.tv_box_config_tips);
		bt_box_config = (Button) findViewById(R.id.bt_box_config);
		bt_box_buy = (Button) findViewById(R.id.bt_box_buy);

		ActivityMainTab.setTabMenu(0);
	}

	@Override
	public void initData() {
		isCanAddBox = DataManager.getInstance().getCurrentUser().isGroupCreator();
		if (isCanAddBox) {
			tv_box_config_tips.setText(" 您还没有添加财盒哦！马上点击按钮添加吧！");
			bt_box_config.setVisibility(View.VISIBLE);
		} else {
			tv_box_config_tips.setText(" 您还没有已授权的财盒哦！");
			bt_box_config.setVisibility(View.GONE);
		}
	}

	@Override
	public void initEvent() {
		bt_box_config.setOnClickListener(this);
		bt_box_buy.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		User user = DataManager.getInstance().getCurrentUser();
		long boxId = user.getBoxId();
		if (boxId != 0) {
			// 该用户有已授权财盒或拥有财盒
			replaceToBoxFragment();
		}
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