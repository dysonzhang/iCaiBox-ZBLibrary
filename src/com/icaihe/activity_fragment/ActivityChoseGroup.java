package com.icaihe.activity_fragment;

import com.icaihe.R;

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

/**
 * 选择财盒群界面
 */
public class ActivityChoseGroup extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {
	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityChoseGroup.class);
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
	}

	private Button bt_create_group;
	private Button bt_create_private_group;
	private Button bt_add_group;

	@Override
	public void initView() {
		super.initView();
		bt_create_group = (Button) findViewById(R.id.bt_create_group);
		bt_create_private_group = (Button) findViewById(R.id.bt_create_private_group);
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
		bt_create_private_group.setOnClickListener(this);
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
			Intent intent0 =ActivityCreateGroup.createIntent(context);
			intent0.putExtra("isPrivate", 0); 
			toActivity(intent0);
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		case R.id.bt_create_private_group:
			Intent intent1 =ActivityCreateGroup.createIntent(context);
			intent1.putExtra("isPrivate", 1); 
			toActivity(intent1);
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		case R.id.bt_add_group:
			startActivity(ActivitySerachGroup.createIntent(context));
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
