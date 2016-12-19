package com.icaihe.activity_fragment;

import java.util.List;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.adapter.ContractAdapter;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.Contract;
import com.icaihe.model.User;
import com.ichihe.util.HttpRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Json;

/**
 * 通讯录界面fragment
 * 
 */
public class FragmentContractList extends BaseHttpListFragment<Contract, ContractAdapter>
		implements OnItemClickListener, OnClickListener {

	public static String TAG = "FragmentContractList";

	public static FragmentContractList createInstance() {
		FragmentContractList fragment = new FragmentContractList();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_contract);
		argument = getArguments();
		if (argument != null) {
		}

		initView();
		initData();
		initEvent();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// SVProgressHUD.showWithStatus(context, "请稍候...");
		lvBaseList.onRefresh();
		// SVProgressHUD.dismiss(context);
	}

	@Override
	public void initView() {
		super.initView();

		ActivityMainTab.setTabMenu(0);
	}

	@Override
	public void setList(final List<Contract> list) {
		setList(list, new AdapterCallBack<ContractAdapter>() {
			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}

			@Override
			public ContractAdapter createAdapter() {
				return new ContractAdapter(context, list);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void getListAsync(int pageNum) {

		if (pageNum != 1) {
			onStopLoadMore(false);
			stopLoadData();
			return;
		}
		long groupId = DataManager.getInstance().getCurrentUser().getGroupId();
		HttpRequest.getGroupMemberList(groupId, HttpRequest.RESULT_GET_GROUP_MENBER_SUCCEED,
				new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						if (resultCode != 1) {
							showShortToast(resultMessage);
							if (resultCode < 0) {
								PushSetUtil pushSetUtil = new PushSetUtil(context);
								pushSetUtil.setAlias("null");
								User user = DataManager.getInstance().getCurrentUser();
								DataManager.getInstance().removeUser(user);
								startActivity(ActivityLogin.createIntent(context));
							}
							return;
						}
						setOnHttpRequestSuccess(requestCode, resultCode, resultMessage, resultData);
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}

	private void setOnHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {

		List<Contract> contractList = Json.parseArray(resultData, Contract.class);
		if (contractList.size() <= 0) {
			stopLoadData();
		} else {
			onHttpRequestSuccess(requestCode, resultCode, resultMessage, resultData);
			onStopLoadMore(false);
			stopLoadData();
		}
	}

	@Override
	public List<Contract> parseArray(String json) {
		return Json.parseArray(json, Contract.class);
	}

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

		Contract contract = adapter.getItem(position);
		if (BaseModel.isCorrect(contract)) {// 相当于 user != null && user.getId()
											// >
											// 0
			// toActivity(UserActivity.createIntent(context, user.getId()));
		}
	}

	@Override
	public void onClick(View v) {

	}
}