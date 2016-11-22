package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import com.icaihe.R;
import com.icaihe.adapter.BoxBeaconAdapter;
import com.icaihe.model.BoxBeacon;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.SettingUtil;

/**
 * beacon搜索
 */
public class ActivityBoxBeacon extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityBoxBeacon.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_beacon, this);

		initView();
		initData();
		initEvent();

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	private ImageView iv_back;
	private TextView tv_bt_tips;
	private ListView lv_beacons;
	private BoxBeaconAdapter boxBeaconAdapter;

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_bt_tips = (TextView) findViewById(R.id.tv_bt_tips);
		lv_beacons = (ListView) findViewById(R.id.lv_beacons);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void addBoxBeaconData(List<BoxBeacon> boxBeaconList) {
		if (boxBeaconList.size() <= 0) {
			return;
		}
		
		List<BoxBeacon> myBoxList = new ArrayList<BoxBeacon>();
		
		String ichid = DataKeeper.getRootSharedPreferences().getString("curr_ichid", "");
		for (BoxBeacon boxBeacon : boxBeaconList) {
			if (boxBeacon.getBeaconName().equals(ichid)) {
				myBoxList.add(boxBeacon);
				break;
			}
		}

		if (myBoxList.size() <= 0) {
			return;
		}
		
		boxBeaconAdapter = new BoxBeaconAdapter(context, myBoxList);
		lv_beacons.setAdapter(boxBeaconAdapter);
		boxBeaconAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
