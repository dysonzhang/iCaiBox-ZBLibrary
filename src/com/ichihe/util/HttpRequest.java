package com.ichihe.util;

import java.util.ArrayList;
import java.util.List;

import com.icaihe.application.ICHApplication;
import com.icaihe.manager.DataManager;

import zuo.biao.library.bean.Parameter;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;

/**
 * HTTP请求工具类
 * 
 * @use 添加请求方法xxxMethod >> HttpRequest.xxxMethod(...)
 */
public class HttpRequest {

	/**
	 * 添加请求参数，value为空时不添加
	 * 
	 * @param list
	 * @param key
	 * @param value
	 */
	public static void addExistParameter(List<Parameter> list, String key, Object value) {
		if (list == null) {
			list = new ArrayList<Parameter>();
		}
		if (StringUtil.isNotEmpty(key, true) && StringUtil.isNotEmpty(value, true)) {
			list.add(new Parameter(key, value));
		}
	}

	/** 基础URL，这里服务器设置可切换 */
	public static final String URL_BASE = SettingUtil.getCurrentServerAddress(ICHApplication.getInstance());

	public static final String KEY_RANGE = "range";
	public static final String KEY_ID = "id";
	public static final String KEY_CURRENT_USER_ID = "currentUserId";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_AUTH_CODE = "authCode";

	public static final String KEY_SEX = "sex";
	public static final int SEX_MAIL = 1;
	public static final int SEX_FEMAIL = 2;
	public static final int SEX_ALL = 3;

	public static final String KEY_USER_ID = "userId";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_TYPE = "type";
	public static final String KEY_CODE = "code";

	public static final String KEY_GROUP_NAME = "groupName";
	public static final String KEY_CREATOR_NAME = "creatorName";
	public static final String KEY_GROUP_CREATE_TIME = "gCreateTime";
	public static final String KEY_GROUP_ADDRESS = "groupAddress";
	public static final String KEY_GROUP_ADDRESS_X = "addressX";
	public static final String KEY_GROUP_ADDRESS_Y = "addressY";

	public static final String KEY_GROUP_ID = "groupId";
	public static final String KEY_USER_NAME = "userName";
	public static final String KEY_GROUP_JOIN_DATE = "joinDate";

	public static final String KEY_ICH_ID = "ichId";
	public static final String KEY_BOX_NAME = "boxName";
	public static final String KEY_WIFI_ID = "wifiId";

	public static final String KEY_BOX_ID = "boxId";
	public static final String KEY_KEY = "key";

	public static final String KEY_BACK_TIME = "backTime";
	public static final String KEY_REMARK = "remark";

	public static final String KEY_PAGE_NO = "pageNo";
	public static final String KEY_MESSAGE = "message";

	public static final String KEY_TOKEN = "token";

	/**
	 * 获取短信验证码
	 * 
	 * @param phone
	 * @param type
	 *            登录的地方传1
	 * @param listener
	 */
	public static final int RESULT_GET_CODE_SUCCEED = 100;

	public static void getCode(final String phone, final int type, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();
		addExistParameter(paramList, KEY_PHONE, phone);
		addExistParameter(paramList, KEY_TYPE, type);

		HttpManager.getInstance().post(paramList, URL_BASE + "initApi/sendCode", requestCode, listener);
	}

	/**
	 * 验证码登录
	 * 
	 * @param phone
	 * @param code
	 * @param listener
	 */
	public static final int RESULT_LOGN_SUCCEED = 101;

	public static void login(final String phone, final String code, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();
		addExistParameter(paramList, KEY_PHONE, phone);
		addExistParameter(paramList, KEY_CODE, code);

		HttpManager.getInstance().post(paramList, URL_BASE + "initApi/login", requestCode, listener);
	}

	/**
	 * 创建财盒群
	 * 
	 * @param groupName
	 * @param creatorName
	 * @param gCreateTime
	 * @param groupAddress
	 * @param addressX
	 * @param addressY
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_CREATE_GROUP_SUCCEED = 102;

	public static void createGroup(final String groupName, final String creatorName, final String gCreateTime,
			final String groupAddress, final String addressX, final String addressY, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_GROUP_NAME, groupName);
		addExistParameter(paramList, KEY_CREATOR_NAME, creatorName);
		addExistParameter(paramList, KEY_GROUP_CREATE_TIME, gCreateTime);
		addExistParameter(paramList, KEY_GROUP_ADDRESS, groupAddress);
		addExistParameter(paramList, KEY_GROUP_ADDRESS_X, addressX);
		addExistParameter(paramList, KEY_GROUP_ADDRESS_Y, addressY);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/group/add", requestCode, listener);
	}

	/**
	 * 搜索财盒群
	 * 
	 * @param groupName
	 * @param listener
	 */
	public static final int RESULT_SEARCH_GROUP_SUCCEED = 103;

	public static void searchGroup(final String groupName, final int requestCode,
			final OnHttpResponseListener listener) {

		List<Parameter> paramList = new ArrayList<Parameter>();
		addExistParameter(paramList, KEY_GROUP_NAME, groupName);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/group/list", requestCode, listener);
	}

	/**
	 * 加入财盒群
	 * 
	 * @param groupId
	 * @param userName
	 * @param joinDate
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_JOIN_GROUP_SUCCEED = 104;

	public static void joinGroup(long groupId, final String userName, final String joinDate, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_GROUP_ID, groupId);
		addExistParameter(paramList, KEY_USER_NAME, userName);
		addExistParameter(paramList, KEY_GROUP_JOIN_DATE, joinDate);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/group/join", requestCode, listener);
	}

	/**
	 * 添加财盒
	 * 
	 * @param ichId
	 * @param boxName
	 * @param groupId
	 * @param wifiId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_ADD_BOX_SUCCEED = 105;

	public static void addBox(final String ichId, final String boxName, final long groupId, final String wifiId,
			final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_ICH_ID, ichId);
		addExistParameter(paramList, KEY_BOX_NAME, boxName);
		addExistParameter(paramList, KEY_GROUP_ID, groupId);
		addExistParameter(paramList, KEY_WIFI_ID, wifiId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/box/add", requestCode, listener);
	}

	/**
	 * 修改财盒wifiid
	 * 
	 * @param boxId
	 * @param wifiId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_UPDATE_BOX_WIFI_ID_SUCCEED = 106;

	public static void updateBoxWifiId(final long boxId, final String wifiId, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_WIFI_ID, wifiId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/box/wifiId/update", requestCode, listener);
	}

	/**
	 * 查看财盒
	 * 
	 * @param boxId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_QUERY_BOX_DETAIL_SUCCEED = 107;

	public static void queryBoxDetail(final long boxId, final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/box/detail", requestCode, listener);
	}

	/**
	 * 开箱
	 * 
	 * @param boxId
	 * @param type
	 *            此处type=1
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_OPEN_BOX_SUCCEED = 108;

	public static void openBox(final long boxId, final int type, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_TYPE, type);
		addExistParameter(paramList, KEY_BOX_ID, boxId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxRecord/add", requestCode, listener);
	}

	/**
	 * 开箱握手协议
	 * 
	 * @param ichId
	 * @param key
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_OPEN_BOX_AGREEMENT_SUCCEED = 109;

	public static void openBoxAgreement(final String ichId, final String key, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_KEY, key);
		addExistParameter(paramList, KEY_ICH_ID, ichId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/agreement/openBox/detail", requestCode, listener);
	}

	/**
	 * 外借
	 * 
	 * @param type
	 *            此处type=4
	 * @param boxId
	 * @param backTime
	 * @param remark
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_BORROW_BOX_SUCCEED = 110;

	public static void borrowBox(final String type, final long boxId, final String backTime, final String remark,
			final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_TYPE, type);
		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_BACK_TIME, backTime);
		addExistParameter(paramList, KEY_REMARK, remark);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxRecord/add", requestCode, listener);
	}

	/**
	 * 即借即还
	 * 
	 * @param type
	 *            此处type=5
	 * @param boxId
	 * @param remark
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_BORROW_RETURN_BOX_SUCCEED = 111;

	public static void borrowReturnBox(final String type, final long boxId, final String remark, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_TYPE, type);
		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_REMARK, remark);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxRecord/add", requestCode, listener);
	}

	/**
	 * 授权管理列表
	 * 
	 * @param groupId
	 * @param boxId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_GET_BOX_AUTH_LIST_SUCCEED = 112;

	public static void getBoxAuthorityList(final long groupId, final long boxId, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_GROUP_ID, groupId);
		addExistParameter(paramList, KEY_BOX_ID, boxId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);
		HttpManager.getInstance().get(paramList, URL_BASE + "api/v1/groupMember/authority/list", requestCode, listener);
	}

	/**
	 * 通讯录列表
	 * 
	 * @param groupId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_GET_GROUP_MENBER_SUCCEED = 113;

	public static void getGroupMemberList(final long groupId, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_GROUP_ID, groupId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().get(paramList, URL_BASE + "api/v1/groupMember/list", requestCode, listener);
	}

	/**
	 * 授权开箱
	 * 
	 * @param boxId
	 * @param userId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_AUTH_USER_SUCCEED = 114;

	public static void authUser(final long boxId, final long userId, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_USER_ID, userId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);
		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxUser/add", requestCode, listener);
	}

	/**
	 * 取消授权开箱
	 * 
	 * @param boxId
	 * @param userId
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_CANCEL_AUTH_USER_SUCCEED = 115;

	public static void cancelAuthUser(final long boxId, final long userId, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_USER_ID, userId);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);
		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxUser/delete", requestCode, listener);
	}

	/**
	 * 获取用户个人信息
	 * 
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_GET_USER_DETAIL_SUCCEED = 116;

	public static void getUserDetail(final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().get(paramList, URL_BASE + "api/v1/user/detail", requestCode, listener);
	}

	/**
	 * 获取用户动态
	 * 
	 * @param pageNo
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_GET_USER_NOTICE_SUCCEED = 117;

	public static void getUserNotice(final int pageNo, final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_PAGE_NO, pageNo);
		
		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().get(paramList, URL_BASE + "api/v1/boxRecord/userRecord/list", requestCode, listener);
	}

	/**
	 * 查看开箱记录
	 * 
	 * @param boxId
	 * @param userName
	 * @param pageNo
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_QUERY_OPEN_RECORDS_SUCCEED = 118;

	public static void queryOpenRecords(final int boxId, final String userName, final int pageNo, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_USER_NAME, userName);
		addExistParameter(paramList, KEY_PAGE_NO, pageNo);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxRecord/openRecord/list", requestCode, listener);
	}

	/**
	 * 查看报警记录（包含保险箱报警和保险箱电量不足）
	 * 
	 * @param boxId
	 * @param pageNo
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_QUERY_ALARM_RECORDS_SUCCEED = 119;

	public static void queryAlarmRecords(final int boxId, final int pageNo, final int requestCode,
			final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_BOX_ID, boxId);
		addExistParameter(paramList, KEY_PAGE_NO, pageNo);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/boxRecord/alarmRecord/list", requestCode,
				listener);
	}

	/**
	 * 查看群成员（我的财盒）
	 * 
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_GROUP_USER_SUCCEED = 120;

	public static void getGroupUsers(final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);
		HttpManager.getInstance().get(paramList, URL_BASE + "api/v1/groupMember/group/list", requestCode, listener);
	}

	/**
	 * 意见反馈
	 * 
	 * @param message
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_FEEDBACK_SUCCEED = 121;

	public static void feedback(final String message, final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		addExistParameter(paramList, KEY_MESSAGE, message);

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/message/add", requestCode, listener);
	}

	/**
	 * 未读报警记录条数清零
	 * 
	 * @param requestCode
	 * @param listener
	 */
	public static final int RESULT_RESET_ALARM_NUM_SUCCEED = 122;

	public static void resetAlarmNum(final int requestCode, final OnHttpResponseListener listener) {
		List<Parameter> paramList = new ArrayList<Parameter>();

		String token = DataManager.getInstance().getCurrentUser().getToken();
		addExistParameter(paramList, KEY_TOKEN, token);

		HttpManager.getInstance().post(paramList, URL_BASE + "api/v1/alarmNum/delete", requestCode, listener);
	}
}