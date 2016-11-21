package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.SettingUtil;

/**
 * 选择财盒群界面
 * 
 * @author dyson
 *
 */
public class ChoseGroupActivity extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {
	// private static final String TAG = "ChoseGroupActivity";

	/**
	 * 启动这个Activity的Intent
	 * 
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, ChoseGroupActivity.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_group, this);

		initView();
		initData();
		initEvent();

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	private Button bt_create_group;
	private Button bt_add_group;

	@Override
	public void initView() {
		super.initView();
		bt_create_group = (Button) findViewById(R.id.bt_create_group);
		bt_add_group = (Button) findViewById(R.id.bt_add_group);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		super.initEvent();
		bt_create_group.setOnClickListener(this);
		bt_add_group.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_create_group:
			startActivity(CreateGroupActivity.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		case R.id.bt_add_group:
			startActivity(SerachGroupActivity.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
