package com.icaihe.activity_fragment;

import com.icaihe.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * 闪屏activity
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(AboutActivity.createIntent(SplashActivity.this));
				finish();
			}
		}, 500);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

}