package com.icaihe.activity_fragment;

import java.util.List;

import com.icaihe.R;
import com.icaihe.adapter.ContractAdapter;
import com.icaihe.application.ICHApplication;
import com.icaihe.model.Contract;
import com.ichihe.util.HttpRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import zuo.biao.library.base.BaseListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Json;

/**
 * 通讯录Fragment
 */
public class ContractFragment extends BaseListFragment<Contract, ListView, ContractAdapter> {

	public static ContractFragment createInstance() {
		return new ContractFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState, R.layout.contract_fragment);

		initView();
		initData();
		initEvent();
		onRefresh();
		return view;
	}

	@Override
	public void initView() {
		super.initView();
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

		showProgressDialog(R.string.loading);

		long groupId = ICHApplication.getInstance().getCurrentUser().getGroupId();
		HttpRequest.getGroupMemberList(groupId, HttpRequest.RESULT_GET_GROUP_MENBER_SUCCEED,
				new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						showShortToast("加载成功");
						list = Json.parseArray(resultData, Contract.class);
						onLoadSucceed(list);
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}

	@Override
	public void initEvent() {
		super.initEvent();
		lvBaseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
	}
}