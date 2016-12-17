package com.icaihe.activity_fragment;

import java.util.ArrayList;

import com.icaihe.R;
import com.skyfishjy.library.RippleBackground;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class ActivityWran extends BaseActivity implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityWran.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	private ImageView iv_back;
	private ImageView foundDevice;
	private TextView tv_add_group_tips;
	private ImageView button;
	private RippleBackground rippleBackground;

	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wran, this);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {
		super.initView();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		foundDevice = (ImageView) findViewById(R.id.foundDevice);
		rippleBackground = (RippleBackground) findViewById(R.id.content);
		tv_add_group_tips = (TextView) findViewById(R.id.tv_add_group_tips);
		button = (ImageView) findViewById(R.id.centerImage);
	}

	@Override
	public void initData() {
		super.initData();

		Intent intent = getIntent();
		int isBattery = intent.getIntExtra("isBattery", 0);
		if (isBattery == 1) {
			button.setImageResource(R.drawable.icon_wran_battery_low);
			tv_add_group_tips.setText(" 注意！当前电量不足，请及时更换电池以免影响使用！");
		} else {
			button.setImageResource(R.drawable.icon_warn_ring);
			tv_add_group_tips.setText(" 注意！财盒有异常信息，请立即检查。");
		}
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);

		startWranAnimation();

		startVibrater();
	}

	private void startWranAnimation() {
		final Handler handler = new Handler();
		rippleBackground.startRippleAnimation();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				foundDevice();
			}
		}, 3000);
	}

	private void startVibrater() {
		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 300, 400, 300, 400 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, 2); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}

	private void stopVibrater() {
		vibrator.cancel();
	}

	private void foundDevice() {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(400);
		animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		ArrayList<Animator> animatorList = new ArrayList<Animator>();
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
		animatorList.add(scaleXAnimator);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
		animatorList.add(scaleYAnimator);
		animatorSet.playTogether(animatorList);
		// foundDevice.setVisibility(View.VISIBLE);
		animatorSet.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (vibrator != null) {
			stopVibrater();
		}
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
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
}
