package com.icaihe.manager;

import com.icaihe.application.ICHApplication;
import com.icaihe.model.User;

import android.content.Context;
import android.content.SharedPreferences;
import zuo.biao.library.util.Json;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 数据工具类
 */
public class DataManager {
	private final String TAG = "DataManager";

	private Context context;

	private DataManager(Context context) {
		this.context = context;
	}

	private static DataManager instance;

	public static synchronized DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager(ICHApplication.getInstance());
		}
		return instance;
	}

	// 用户
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private String PATH_USER = "PATH_ICAIBOX_USER";

	public final String KEY_USER = "KEY_USER";
	public final String KEY_USER_ID = "KEY_USER_ID";
	public final String KEY_USER_NAME = "KEY_USER_NAME";
	public final String KEY_USER_PHONE = "KEY_USER_PHONE";

	public final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";
	public final String KEY_LAST_USER_ID = "KEY_LAST_USER_ID";

	/**
	 * 判断是否为当前用户
	 * 
	 * @param context
	 * @param userId
	 * @return
	 */
	public boolean isCurrentUser(long userId) {
		return userId > 0 && userId == getCurrentUserId();
	}

	/**
	 * 获取当前用户id
	 * 
	 * @param context
	 * @return
	 */
	public long getCurrentUserId() {
		User user = getCurrentUser();
		return user == null ? 0 : user.getId();
	}

	/**
	 * 获取当前用户的手机号
	 * 
	 * @param context
	 * @return
	 */
	public String getCurrentUserPhone() {
		User user = getCurrentUser();
		return user == null ? "" : user.getPhone();
	}

	/**
	 * 获取当前用户
	 * 
	 * @param context
	 * @return
	 */
	public User getCurrentUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		return sdf == null ? null : getUser(sdf.getLong(KEY_CURRENT_USER_ID, 0));
	}

	/**
	 * 获取最后一次登录的用户的手机号
	 * 
	 * @param context
	 * @return
	 */
	public String getLastUserPhone() {
		User user = getLastUser();
		return user == null ? "" : user.getPhone();
	}

	/**
	 * 获取最后一次登录的用户
	 * 
	 * @param context
	 * @return
	 */
	public User getLastUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		return sdf == null ? null : getUser(sdf.getLong(KEY_LAST_USER_ID, 0));
	}

	/**
	 * 获取某一个userId的用户
	 * 
	 * @param context
	 * @param userId
	 * @return
	 */
	public User getUser(long userId) {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "get sdf == null >>  return;");
			return null;
		}
		Log.i(TAG, "getUser  userId = " + userId);
		return Json.parseObject(sdf.getString(StringUtil.getTrimedString(userId), null), User.class);
	}

	/**
	 * 保存当前用户,只在登录或注销时调用
	 * 
	 * @param context
	 * @param user
	 *            user == null >> user = new User();
	 */
	public void saveCurrentUser(User user) {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "saveUser sdf == null  >> return;");
			return;
		}
		if (user == null) {
			Log.w(TAG, "saveUser  user == null >>  user = new User();");
			user = new User();
		}
		SharedPreferences.Editor editor = sdf.edit();
		editor.remove(KEY_LAST_USER_ID).putLong(KEY_LAST_USER_ID, getCurrentUserId());
		editor.remove(KEY_CURRENT_USER_ID).putLong(KEY_CURRENT_USER_ID, user.getId());
		editor.commit();

		saveUser(sdf, user);
	}

	/**
	 * 保存用户 保存任意一个用户的信息
	 * 
	 * @param context
	 * @param user
	 */
	public void saveUser(User user) {
		saveUser(context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE), user);
	}

	/**
	 * 保存用户
	 * 
	 * @param context
	 * @param sdf
	 * @param user
	 */
	private void saveUser(SharedPreferences sdf, User user) {
		if (sdf == null || user == null) {
			Log.e(TAG, "saveUser sdf == null || user == null >> return;");
			return;
		}
		String key = StringUtil.getTrimedString(user.getId());
		Log.i(TAG, "saveUser  key = user.getId() = " + user.getId());
		sdf.edit().remove(key).putString(key, Json.toJSONString(user)).commit();
	}

	/**
	 * 删除用户
	 * 
	 * @param context
	 * @param sdf
	 */
	public void removeUser(SharedPreferences sdf, long userId) {
		if (sdf == null) {
			Log.e(TAG, "removeUser sdf == null  >> return;");
			return;
		}
		sdf.edit().remove(StringUtil.getTrimedString(userId)).commit();
	}

	/**
	 * 设置当前用户手机号
	 * 
	 * @param context
	 * @param phone
	 */
	public void setCurrentUserPhone(String phone) {
		User user = getCurrentUser();
		if (user == null) {
			user = new User();
		}
		user.setPhone(phone);
		saveUser(user);
	}

	/**
	 * 设置当前用户姓名
	 * 
	 * @param context
	 * @param name
	 */
	public void setCurrentUserName(String name) {
		User user = getCurrentUser();
		if (user == null) {
			user = new User();
		}
		user.setName(name);
		saveUser(user);
	}

	// 用户
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
