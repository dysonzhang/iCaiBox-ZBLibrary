package com.icaihe.jpush;

import cn.jpush.android.service.PushService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RestartService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		if (intent != null && this != null) {
			// 这里可以做Service该做的事
			intent.setClass(this, PushService.class);
			// 启动service
			// 多次调用startService并不会启动多个service 而是会多次调用onStart
			this.startService(intent);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Intent localIntent = new Intent();
		localIntent.setClass(this, RestartService.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}
}