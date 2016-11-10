package com.icaihe.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("icaihe.alarm.action")) {
			Intent i = new Intent();
			i.setClass(context, RestartService.class);
			// 启动service
			// 多次调用startService并不会启动多个service 而是会多次调用onStart
			context.startService(i);
		}

	}

}
