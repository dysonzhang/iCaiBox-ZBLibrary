package com.icaihe.jpush;

import org.json.JSONException;
import org.json.JSONObject;

import com.icaihe.R;
import com.icaihe.activity_fragment.ActivitySplash;
import com.icaihe.application.ICHApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import zuo.biao.library.base.BaseActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	private static final int PUSH_NOTIFICATION_ID = 8888;

	public static final String ICAIHE_NOTIFICATION_CLICK = "icaihe_notification_click";

	// 保存服务器推送下来的消息的标题。对应 API 消息内容的 title 字段。对应
	// Portal 推送消息界面上的“标题”字段（可选
	private String strTitle = "";

	// 保存服务器推送下来的消息内容。对应 API 消息内容的 message 字段。对应
	// Portal 推送消息界面上的"消息内容”字段。
	private String strMessage = "";

	// 保存服务器推送下来的附加字段。这是个 JSON 字符串。对应 API 消息内容的
	// extras 字段。对应 Portal 推送消息界面上的“自定义内容”。
	private String strExtra = "";

	// 保存服务器推送下来的内容类型。对应 API 消息内容的
	// content_type 字段。Portal 上暂时未提供输入字段。
	private String strContentType = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

			// 接收数据
			strTitle = bundle.getString(JPushInterface.EXTRA_TITLE);
			if ("null".equals(strTitle) || "".equals(strTitle)) {
				strTitle = "i财盒";
			}

			strMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			if ("null".equals(strMessage)) {
				strMessage = "";
			}

			strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			if ("null".equals(strExtra)) {
				strExtra = "";
			}

			strContentType = bundle
					.getString(JPushInterface.EXTRA_CONTENT_TYPE);
			if ("null".equals(strContentType)) {
				strContentType = "";
			}

			// 自定义通知
			NotificationManager manger = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// 建立Builder
			Notification.Builder builder = new Notification.Builder(context)
					.setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_VIBRATE)
					.setSmallIcon(R.drawable.icon)
					.setWhen(System.currentTimeMillis()).setOngoing(true);

			Intent intentMsg = intent;
			intentMsg.putExtra("Message", strMessage);
			intentMsg.setAction(MyReceiver.ICAIHE_NOTIFICATION_CLICK);
			PendingIntent pdmsg = PendingIntent.getBroadcast(context, 0,
					intentMsg, PendingIntent.FLAG_UPDATE_CURRENT);

			builder.setContentTitle(strTitle).setContentText(strMessage)
					.setContentIntent(pdmsg);

			Notification notification = builder.getNotification();

			notification.flags = Notification.FLAG_AUTO_CANCEL;

			// 发出状态栏通知
			manger.notify(PUSH_NOTIFICATION_ID, notification);

			processCustomMessage(context, bundle);

		} else if (intent
				.getAction()
				.equals(com.icaihe.jpush.MyReceiver.ICAIHE_NOTIFICATION_CLICK)) {

			// 打开Activity
			if (ICHApplication.isAppOnForeground(context)) {

			} else {
				// 打开Activity
				Intent i = new Intent(context, ActivitySplash.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			// 打开Activity
			if (ICHApplication.isAppOnForeground(context)) {

			} else {
				// 打开Activity
				Intent i = new Intent(context, ActivitySplash.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to Activity
	private void processCustomMessage(Context context, Bundle bundle) {
		// BaseActivity推送通知
		if (BaseActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(BaseActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(BaseActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(BaseActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {
				}
			}
			context.sendBroadcast(msgIntent);
		} 
	}
}
