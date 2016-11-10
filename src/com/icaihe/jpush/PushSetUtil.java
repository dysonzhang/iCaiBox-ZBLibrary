package com.icaihe.jpush;

import java.util.LinkedHashSet;
import java.util.Set;

import com.icaihe.R;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PushSetUtil {

	private Context context;
	private static String TAG = "PushSetUtil";

	public PushSetUtil(Context context) {
		this.context = context;
	}

	public void setTag(String tag) {

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(context, R.string.error_tag_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(context, R.string.error_tag_gs_empty,
						Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	public void setAlias(String alias) {

		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(context, R.string.error_alias_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(context, R.string.error_tag_gs_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	/**
	 * 设置通知提示方式 - 基础属性
	 */
	public void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
				context);
		builder.statusBarDrawable = R.drawable.icon;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND; // 设置为铃声（
																	// Notification.DEFAULT_SOUND）或者震动（
																	// Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
		Toast.makeText(context, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 设置通知栏样式 - 定义通知栏Layout
	 */
	public void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
				context, R.layout.customer_notitfication_layout, R.id.icon,
				R.id.title, R.id.text);
		builder.layoutIconDrawable = R.drawable.icon;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);
		Toast.makeText(context, "Custom Builder - 2", Toast.LENGTH_SHORT)
				.show();
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				// 这里往 SharePreference
				// 里写一个成功设置的alias状态。成功设置一次后，以后不必再次设置了。若alias不一样则重新设置
				// SPUtils.put(context,"setAlias", alias);

				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 10s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(context)) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 10);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
			// ExampleUtil.showToast(logs, context);
		}
	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 10s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(context)) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 10);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
			// ExampleUtil.showToast(logs, context);
		}
	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				JPushInterface.setAliasAndTags(context, (String) msg.obj, null,
						mAliasCallback);
				break;

			case MSG_SET_TAGS:
				Log.d(TAG, "Set tags in handler.");
				JPushInterface.setAliasAndTags(context, null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
}
