/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

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
import zuo.biao.library.util.CommonUtil;

/**
 * 扫描二维码Activity
 * 
 * @use toActivity(ScanActivity.createIntent(...));
 */
public class ScanActivity extends CaptureActivity implements Callback, ActivityPresenter, OnClickListener {
	public static final String TAG = "ScanActivity";

	/**
	 * 启动这个Activity的Intent
	 * 
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, ScanActivity.class);
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