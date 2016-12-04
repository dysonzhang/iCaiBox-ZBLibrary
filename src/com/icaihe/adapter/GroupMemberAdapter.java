package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.GroupMember;
import com.icaihe.view.GroupMemberView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

public class GroupMemberAdapter extends BaseAdapter<GroupMember> {

	public GroupMemberAdapter(Activity context, List<GroupMember> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		GroupMemberView groupMemberView = convertView == null ? null : (GroupMemberView) convertView.getTag();
		if (groupMemberView == null) {
			groupMemberView = new GroupMemberView(context, resources);
			convertView = groupMemberView.createView(inflater, getItemViewType(position));
			convertView.setTag(groupMemberView);
		}

		groupMemberView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}