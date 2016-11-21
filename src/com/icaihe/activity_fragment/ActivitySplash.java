package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.icaihe.application.ICHApplication;
import com.icaihe.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

/**
 * 
 * 闪屏activity
 * 
 * @author dyson
 *
 */
public class ActivitySplash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				User user = ICHApplication.getInstance().getCurrentUser();
				if (user == null) {
					// 未登录
					startActivity(ActivityLogin.createIntent(ActivitySplash.this));
				} else {
					// 已登录
					if (user.isNewUser()) {
						startActivity(ActivityChoseGroup.createIntent(ActivitySplash.this));
					} else {
						startActivity(ActivityMainTab.createIntent(ActivitySplash.this));
					}
				}
				finish();
			}
		}, 1000);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

}