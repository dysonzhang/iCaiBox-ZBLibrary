package com.icaihe.activity_fragment;

import java.util.List;

import com.icaihe.adapter.UserAdapter;
import com.icaihe.model.User;
import com.ichihe.util.HttpRequest;
import com.ichihe.util.TestUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.util.Json;

/**
 * 通讯录列表界面fragment
 * 
 * @use new UserListFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 * @must 查看 .HttpManager 中的@must和@warn 查看 .SettingUtil 中的@must和@warn
 */
public class ContractFragment extends BaseHttpListFragment<User, UserAdapter>
		implements OnItemClickListener, CacheCallBack<User> {
	 private static final String TAG = "ContractFragment";

	// 与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String ARGUMENT_RANGE = "ARGUMENT_RANGE";

	public static UserListFragment createInstance(int range) {
		UserListFragment fragment = new UserListFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(ARGUMENT_RANGE, range);

		fragment.setArguments(bundle);
		return fragment;
	}

	// 与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	private int range = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		argument = getArguments();
		if (argument != null) {
			range = argument.getInt(ARGUMENT_RANGE, range);
		}

		Toast.makeText(context, "服务器配置有误，请查看这个类的@must", Toast.LENGTH_LONG).show();

		initCache(this);

		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>

		lvBaseList.onRefresh();

		return view;
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {// 必须调用
		super.initView();

	}

	@Override
	public void setList(final List<User> list) {
		setList(list, new AdapterCallBack<UserAdapter>() {

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}

			@Override
			public UserAdapter createAdapter() {
				return new UserAdapter(context, list);
			}
		});
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {// 必须调用
		super.initData();

	}

	@Override
	public void getListAsync(final int pageNum) {
		// 实际使用时用这个，需要配置服务器地址 HttpRequest.getUserList(range, pageNum, 0, this);

		// 仅测试用<<<<<<<<<<<
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				onHttpRequestSuccess(0, 100,"",
						Json.toJSONString(TestUtil.getUserList(pageNum, getCachePageSize())));
			}
		}, 1000);
		// 仅测试用>>>>>>>>>>>>
	}

	@Override
	public List<User> parseArray(String json) {
		return Json.parseArray(json, User.class);
	}

	@Override
	public Class<User> getCacheClass() {
		return User.class;
	}

	@Override
	public String getCacheGroup() {
		return "range=" + range;
	}

	@Override
	public String getCacheId(User data) {
		return data == null ? null : "" + data.getId();
	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();

		lvBaseList.setOnItemClickListener(this);
	}

	// 系统自带监听方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= lvBaseList.getHeaderViewsCount();
		if (position < 0 || adapter == null || position >= adapter.getCount()) {
			return;
		}

		User user = adapter.getItem(position);
		if (BaseModel.isCorrect(user)) {// 相当于 user != null && user.getId() > 0
			toActivity(UserActivity.createIntent(context, user.getId()));
		}
	}

	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}