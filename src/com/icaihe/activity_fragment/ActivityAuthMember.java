package com.icaihe.activity_fragment;

import java.util.List;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.adapter.AuthMemberAdapter;
import com.icaihe.model.AuthMember;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Json;

/**
 * 授权财盒管理
 */
public class ActivityAuthMember extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityAuthMember.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_member, this);

		initView();
		initData();
		initEvent();

	}
	@Override
	public void toWarnActivity(boolean isBattery) {
		Intent intent = ActivityWran.createIntent(context);
		intent.putExtra("isBattery", isBattery ? 1 : 0);
		toActivity(intent);
	}
	private ImageView iv_back;
	private ListView lv_authors;
	private AuthMemberAdapter authMemberAdapter;

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_authors = (ListView) findViewById(R.id.lv_authors);
	}

	@Override
	public void initData() {
		super.initData();

		getAuthList();
	}

	@Override
	public void initEvent() {
		super.initEvent();

		iv_back.setOnClickListener(this);
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
		default:
			break;
		}
	}

	private void getAuthList() {

		String groupId = DataKeeper.getRootSharedPreferences().getString("curr_groupId", "");
		String boxId = DataKeeper.getRootSharedPreferences().getString("curr_boxId", "");
		if (groupId.equals("") || boxId.equals("")) {
			showShortToast("当前财盒的群ID或财盒ID数据错误");
			return;
		}
		SVProgressHUD.showWithStatus(context, "请稍候...");
		HttpRequest.getBoxAuthorityList(Long.parseLong(groupId), Long.parseLong(boxId),
				HttpRequest.RESULT_GET_BOX_AUTH_LIST_SUCCEED, new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						SVProgressHUD.dismiss(context);
						
						if (resultCode != 1) {
							showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
							return;
						}
						
						if (resultData.equals("null")) {
							showShortToast("暂无授权记录");
							return;
						}

						List<AuthMember> authMemberList = Json.parseArray(resultData, AuthMember.class);
						if (authMemberList != null && authMemberList.size() > 0) {
							authMemberAdapter = new AuthMemberAdapter(context, authMemberList);
							lv_authors.setAdapter(authMemberAdapter);
							authMemberAdapter.notifyDataSetChanged();
						} else {
							showShortToast("暂无授权记录");
							return;
						}
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						SVProgressHUD.dismiss(context);
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
