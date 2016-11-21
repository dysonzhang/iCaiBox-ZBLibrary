package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.AuthMember;
import com.icaihe.view.AuthMemberView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

public class AuthMemberAdapter extends BaseAdapter<AuthMember> {

	public AuthMemberAdapter(Activity context, List<AuthMember> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AuthMemberView authMemberView = convertView == null ? null : (AuthMemberView) convertView.getTag();
		if (authMemberView == null) {
			authMemberView = new AuthMemberView(context, resources);
			convertView = authMemberView.createView(inflater, getItemViewType(position));
			convertView.setTag(authMemberView);
		}

		authMemberView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}