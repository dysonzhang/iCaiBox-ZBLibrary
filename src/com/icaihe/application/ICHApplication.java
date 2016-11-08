package com.icaihe.application;

import com.icaihe.manager.DataManager;
import com.icaihe.model.User;

import android.util.Log;
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
}
