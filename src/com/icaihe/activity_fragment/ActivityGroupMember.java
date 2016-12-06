package com.icaihe.activity_fragment;

import java.util.List;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.adapter.GroupMemberAdapter;
import com.icaihe.model.GroupMember;
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
import zuo.biao.library.util.Json;

/**
 * 财盒群管理
 */
public class ActivityGroupMember extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityGroupMember.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_member, this);
		initView();
		initData();
		initEvent();
	}

	private ImageView iv_back;
	private ListView lv_members;
	private GroupMemberAdapter groupMemberAdapter;

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_members = (ListView) findViewById(R.id.lv_members);
	}

	@Override
	public void initData() {
		super.initData();

		getMemberList();
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

	private void getMemberList() {
		SVProgressHUD.showWithStatus(context, "请稍候...");
		HttpRequest.getGroupUsers(HttpRequest.RESULT_GROUP_USER_SUCCEED, new OnHttpResponseListener() {

			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
	 
				SVProgressHUD.dismiss(context);
				if (resultCode != 1) {
					showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
					return;
				}
				if (resultData.equals("null")) {
					showShortToast("会员记录为空");
					return;
				}

				String memberListStr = Json.parseObject(resultData).getString("memberList");
				List<GroupMember> memberList = Json.parseArray(memberListStr, GroupMember.class);

				if (memberList != null && memberList.size() > 0) {
					groupMemberAdapter = new GroupMemberAdapter(context, memberList);
					lv_members.setAdapter(groupMemberAdapter);
					groupMemberAdapter.notifyDataSetChanged();
				} else {
					showShortToast("会员记录为空");
					return;
				}
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				SVProgressHUD.dismiss(context);
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
