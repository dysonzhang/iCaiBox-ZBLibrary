package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.zxing.activity.CaptureActivity;
import com.zxing.camera.CameraManager;
import com.zxing.view.ViewfinderView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import zuo.biao.library.interfaces.ActivityPresenter;

/**
 * 扫描二维码Activity
 */
public class ActivityScan extends CaptureActivity implements Callback, ActivityPresenter, OnClickListener {
	public static final String TAG = "ScanActivity";

	/**
	 * 启动这个Activity的Intent
	 * 
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityScan.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		init(this, (SurfaceView) findViewById(R.id.svCameraScan), (ViewfinderView) findViewById(R.id.vfvCameraScan));

		initView();
		initData();
		initEvent();

	}

	@Override
	public void initView() {
	}

	private boolean isOpen = false;

	/**
	 * 打开或关闭闪关灯
	 * 
	 * @param open
	 */
	private void switchLight(boolean open) {
		if (open == isOpen) {
			return;
		}
		isOpen = CameraManager.get().switchLight(open);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
		findViewById(R.id.ivCameraScanLight).setOnClickListener(this);
	}

	@Override
	public void onReturnClick(View v) {
		finish();
	}

	@Override
	public void onForwardClick(View v) {
		// CommonUtil.toActivity(context, QRCodeActivity.createIntent(context,
		// 1));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivCameraScanLight:
			switchLight(!isOpen);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public boolean isRunning() {
		return false;
	}
}