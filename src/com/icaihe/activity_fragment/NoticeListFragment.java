package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.icaihe.R;
import com.icaihe.adapter.NoticeAdapter;
import com.icaihe.application.ICHApplication;
import com.icaihe.model.Notice;
import com.ichihe.util.HttpRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Json;

/**
 * 用户列表界面fragment
 * 
 * @use new NoticeListFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 * @must 查看 .HttpManager 中的@must和@warn 查看 .SettingUtil 中的@must和@warn
 */
public class NoticeListFragment extends BaseHttpListFragment<Notice, NoticeAdapter>
		implements OnItemClickListener, OnClickListener, CacheCallBack<Notice> {
	// private static final String TAG = "NoticeListFragment";

	public static NoticeListFragment createInstance() {
		NoticeListFragment fragment = new NoticeListFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState, R.layout.notice_list_fragment);
		argument = getArguments();
		if (argument != null) {
		}
		initCache(this);

		initView();
		initData();
		initEvent();

		lvBaseList.onRefresh();
		return view;
	}

	private ImageView iv_head_user;
	private TextView tv_name;
	private Button bt_my_box;
	private Button bt_check_in;

	@Override
	public void initView() {
		super.initView();

		iv_head_user = (ImageView) findViewById(R.id.iv_head_user);
		tv_name = (TextView) findViewById(R.id.tv_name);
		bt_my_box = (Button) findViewById(R.id.bt_my_box);
		bt_check_in = (Button) findViewById(R.id.bt_check_in);
	}

	@Override
	public void setList(final List<Notice> list) {
		setList(list, new AdapterCallBack<NoticeAdapter>() {
			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}

			@Override
			public NoticeAdapter createAdapter() {
				return new NoticeAdapter(context, list);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
		String name = ICHApplication.getInstance().getCurrentUser().getName();
		tv_name.setText(name);
	}

	@Override
	public void getListAsync(final int pageNum) {
		HttpRequest.getUserNotice(pageNum, HttpRequest.RESULT_GET_USER_NOTICE_SUCCEED, new OnHttpResponseListener() {
			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
				setOnHttpRequestSuccess(requestCode, resultCode, resultMessage, resultData);
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});
	}

	private void setOnHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
		// List<Notice> noticeList = processNoticeData(resultData);
		JSONObject jsonObject;
		String results = "";
		try {
			jsonObject = new JSONObject(resultData);
			results = jsonObject.getString("results");
			onHttpRequestSuccess(requestCode, resultCode, resultMessage, results);
		} catch (JSONException e) {
			showShortToast("最新动态数据转换异常！");
			e.printStackTrace();
		}

	}

	private List<Notice> processNoticeData(String resultData) {
		List<Notice> noticeList = new ArrayList<Notice>();
		JSONObject jsonObject;
		JSONArray jsonArray;
		try {
			jsonObject = new JSONObject(resultData);
			String results = jsonObject.getString("results");
			jsonArray = new JSONArray(results);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = jsonArray.getJSONObject(i);

				String createTime = jObject.getString("createTime");
				String remark = jObject.getString("remark");
				String userName = jObject.getString("userName");
				String userHead = jObject.getString("userHead");
				int type = jObject.getInt("type");

				Notice notice = new Notice();
				notice.setCreateTime(createTime);
				notice.setRemark(remark);
				notice.setType(type);
				notice.setUserHead(userHead);
				notice.setUserName(userName);

				noticeList.add(notice);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showShortToast("最新动态数据转换异常！");
		}
		return noticeList;
	}

	@Override
	public List<Notice> parseArray(String json) {
		return Json.parseArray(json, Notice.class);
	}

	@Override
	public Class<Notice> getCacheClass() {
		return Notice.class;
	}

	@Override
	public String getCacheGroup() {
		// return "range=" + range;
		return "";
	}

	@Override
	public String getCacheId(Notice data) {
		return data == null ? null : "" + data.getId();
	}

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();

		iv_head_user.setOnClickListener(this);
		tv_name.setOnClickListener(this);
		bt_my_box.setOnClickListener(this);
		bt_check_in.setOnClickListener(this);
		lvBaseList.setOnItemClickListener(this);
	}

	// 系统自带监听方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= lvBaseList.getHeaderViewsCount();
		if (position < 0 || adapter == null || position >= adapter.getCount()) {
			return;
		}

		Notice notice = adapter.getItem(position);
		if (BaseModel.isCorrect(notice)) {// 相当于 user != null && user.getId() >
											// 0
			// toActivity(UserActivity.createIntent(context, user.getId()));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_user:
			showShortToast("onClick  iv_head_user");
			break;
		case R.id.tv_name:
			showShortToast("onClick  tv_name");
			break;
		case R.id.bt_my_box:
			showShortToast("onClick  bt_my_box");
			break;
		case R.id.bt_check_in:
			showShortToast("onClick  bt_check_in");
			break;
		default:
			break;
		}
	}
}