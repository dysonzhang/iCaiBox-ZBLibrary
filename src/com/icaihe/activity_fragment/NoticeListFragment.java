/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import com.icaihe.R;
import com.ichihe.util.HttpRequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import zblibrary.demo.DEMO.DemoAdapter;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.bean.Entry;

/**
 * 最新动态列表界面fragment
 * 
 * @use new UserListFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 * @must 查看 .HttpManager 中的@must和@warn 查看 .SettingUtil 中的@must和@warn
 */
public class NoticeListFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = "NoticeListFragment";

	// 与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String ARGUMENT_RANGE = "ARGUMENT_RANGE";

	public static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
	public static final String ARGUMENT_USER_NAME = "ARGUMENT_USER_NAME";

	public static NoticeListFragment createInstance(long userId, String userName) {
		NoticeListFragment fragment = new NoticeListFragment();

		Bundle bundle = new Bundle();
		bundle.putLong(ARGUMENT_USER_ID, userId);
		bundle.putString(ARGUMENT_USER_NAME, userName);

		bundle.putInt(ARGUMENT_RANGE, 0);

		fragment.setArguments(bundle);
		return fragment;
	}

	// 与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 

	private long userId = 0;
	private String userName = null;
	private int range = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.notice_list_fragment);

		argument = getArguments();
		if (argument != null) {
			userId = argument.getLong(ARGUMENT_USER_ID, userId);
			userName = argument.getString(ARGUMENT_USER_NAME, userName);
			range = argument.getInt(ARGUMENT_RANGE, range);
		}

		Toast.makeText(context, "服务器配置有误，请查看这个类的@must", Toast.LENGTH_LONG).show();

		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private ListView lv_notice;
	private ImageView iv_head_user;
	private TextView tv_name;
	private Button bt_my_box;
	private Button bt_check_in;

	@Override
	public void initView() {
		// 必须调用
		lv_notice = (ListView) findViewById(R.id.lv_notice);

		iv_head_user = (ImageView) findViewById(R.id.iv_head_user);
		tv_name = (TextView) findViewById(R.id.tv_name);
		bt_my_box = (Button) findViewById(R.id.bt_my_box);
		bt_check_in = (Button) findViewById(R.id.bt_check_in);
	}

	private DemoAdapter adapter;

	/**
	 * 显示列表内容
	 * 
	 * @author author
	 * @param list
	 */
	private void setList(List<Entry<String, String>> list) {
		if (list == null || list.isEmpty()) {
			Log.i(TAG, "setList list == null || list.isEmpty() >> lvDemoFragment.setAdapter(null); return;");
			adapter = null;
			lv_notice.setAdapter(null);
			return;
		}

		if (adapter == null) {
			adapter = new DemoAdapter(context, list);
			lv_notice.setAdapter(adapter);
		} else {
			adapter.refresh(list);
		}
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 
	private List<Entry<String, String>> list;

	@Override
	public void initData() {

		tv_name.setText(""+userName);
		
		showShortToast(TAG + ": userId = " + userId + "; userName = " + userName);

		showProgressDialog(R.string.loading);

		runThread(TAG + "initData", new Runnable() {
			@Override
			public void run() {
				list = getList(userId);
				runUiThread(new Runnable() {
					@Override
					public void run() {
						dismissProgressDialog();
						setList(list);
					}
				});
			}
		});
	}

	/**
	 * 示例方法：获取号码列表
	 * 
	 * @author lemon
	 * @param userId
	 * @return
	 */
	protected List<Entry<String, String>> getList(long userId) {
		list = new ArrayList<Entry<String, String>>();
		for (int i = 0; i < 64; i++) {
			list.add(new Entry<String, String>("联系人" + i, String.valueOf(1311736568 + i * i)));
		}
		return list;
	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {
		// 必须在onCreateView方法内调用

		lv_notice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toActivity(UserActivity.createIntent(context, id));
			}
		});

		iv_head_user.setOnClickListener(this);
		bt_my_box.setOnClickListener(this);
		bt_check_in.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {// 直接调用不会显示v被点击效果
		switch (v.getId()) {
		case R.id.iv_head_user:
			showShortToast("onClick  iv_head_user");
			break;
		case R.id.bt_my_box:
			// toActivity(SettingActivity.createIntent(context));
			showShortToast("onClick  bt_my_box");
			break;
		case R.id.bt_check_in:
			// toActivity(AboutActivity.createIntent(context));
			showShortToast("onClick  bt_check_in");
			break;
		default:
			break;
		}
	}

	// 系统自带监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}