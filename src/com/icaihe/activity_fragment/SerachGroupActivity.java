package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.icaihe.R;
import com.icaihe.adapter.GroupAdapter;
import com.icaihe.manager.DataManager;
import com.icaihe.model.Group;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.SettingUtil;

/**
 * 搜索加入财盒群界面
 * 
 * @author dyson
 *
 */
public class SerachGroupActivity extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, SerachGroupActivity.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serach_group_activity, this);

		initView();
		initData();
		initEvent();

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	private ImageView iv_back;
	private EditText et_search;
	private Button bt_clear_search;
	private ListView lv_groups;
	private GroupAdapter groupAdapter;

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_search = (EditText) findViewById(R.id.et_search);
		bt_clear_search = (Button) findViewById(R.id.bt_clear_search);
		lv_groups = (ListView) findViewById(R.id.lv_groups);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		super.initEvent();

		iv_back.setOnClickListener(this);
		bt_clear_search.setOnClickListener(this);

		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = et_search.getText().length();
				if (textLength > 0) {
					bt_clear_search.setVisibility(View.VISIBLE);
				} else {
					bt_clear_search.setVisibility(View.GONE);
				}
			}
		});

		et_search.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					Toast.makeText(SerachGroupActivity.this, et_search.getText().toString().trim(), Toast.LENGTH_LONG)
							.show();
					String groupName = et_search.getText().toString().trim();
					search(groupName);
				}
				return false;
			}
		});
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.et_search:
			break;
		case R.id.bt_clear_search:
			et_search.setText("");
			bt_clear_search.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 将返回的json财盒群数据转成List
	 * 
	 * @param resultData
	 * @return
	 */
	private List<Group> getGroupListByJsonData(String resultData) {
		List<Group> groupList = new ArrayList<Group>();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(resultData);

			if (jsonArray.length() <= 0) {
				showShortToast("尚未搜索到此财盒群名称");
				return groupList;
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = jsonArray.getJSONObject(i);
				String groupName = jObject.getString("groupName");
				long groupId = jObject.getLong("groupId");
				Group group = new Group();
				group.setId(groupId);
				group.setGroupName(groupName);
				groupList.add(group);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			showShortToast("搜索财盒群数据转换异常！");
		}
		
		return groupList;
	}

	/**
	 * TODO 搜索财盒群
	 * 
	 * @param groupName
	 */
	private void search(String groupName) {
		HttpRequest.searchGroup(groupName, HttpRequest.RESULT_SEARCH_GROUP_SUCCEED, new OnHttpResponseListener() {

			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
				if (resultData.equals("null")) {
					showShortToast("未搜索到相关财盒群");
					return;
				}
				List<Group> groupList = getGroupListByJsonData(resultData);
				if (groupList != null) {
					groupAdapter = new GroupAdapter(context, groupList);
					lv_groups.setAdapter(groupAdapter);
					groupAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
				DataManager.getInstance().saveCurrentUser(null);
				startActivity(LoginActivity.createIntent(context));
				finish();
			}
		});
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
