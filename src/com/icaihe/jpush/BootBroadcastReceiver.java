package com.icaihe.jpush;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			// 启动完成
			Intent myintent = new Intent(context, MyAlarmReceiver.class);
			myintent.setAction("icaihe.alarm.action");
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					myintent, 0);
			long firstime = SystemClock.elapsedRealtime();
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			// 10秒一个周期，不停的发送广播
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
					10 * 1000, sender);
		}
	}
}
