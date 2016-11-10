package com.icaihe.application;

import java.util.List;

import com.icaihe.jpush.RestartService;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.util.StringUtil;

/**
 * Application
 */
public class ICHApplication extends BaseApplication {
	private static final String TAG = "ICHApplication";

	private static ICHApplication context;

	public static ICHApplication getInstance() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        
		Intent localIntent = new Intent();
		localIntent.setClass(this, RestartService.class);
		this.startService(localIntent);
	}
	
	private static User currentUser = null;
	
	/**
	 * 获取当前用户id
	 * 
	 * @return
	 */
	public long getCurrentUserId() {
		currentUser = getCurrentUser();
		Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.getId()));
		return currentUser == null ? 0 : currentUser.getId();
	}

	public User getCurrentUser() {
		if (currentUser == null) {
			currentUser = DataManager.getInstance().getCurrentUser();
		}
		return currentUser;
	}

	public void saveCurrentUser(User user) {
		if (user == null) {
			Log.e(TAG, "saveCurrentUser  currentUser == null >> return;");
			return;
		}
		if (user.getId() <= 0 && StringUtil.isNotEmpty(user.getName(), true) == false) {
			Log.e(TAG, "saveCurrentUser  user.getId() <= 0"
					+ " && StringUtil.isNotEmpty(user.getName(), true) == false >> return;");
			return;
		}

		currentUser = user;
		DataManager.getInstance().saveCurrentUser(currentUser);
	}

	/**
	 * 判断是否为当前用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isCurrentUser(long userId) {
		return DataManager.getInstance().isCurrentUser(userId);
	}
	
	/**
	 * Check application isBackground
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i(TAG, "app is background");
					return true;
				} else {
					Log.i(TAG, "app is foreground");
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * isAppOnForeground
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				Log.i(TAG, "app is foreground");
				return true;
			}
		}
		Log.i(TAG, "app is background");
		return false;
	}
}
