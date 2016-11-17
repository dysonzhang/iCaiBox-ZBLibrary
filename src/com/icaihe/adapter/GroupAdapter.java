package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.Group;
import com.icaihe.view.GroupView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

/**
 * 搜索财盒adapter
 * 
 * @author dyson
 *
 */
public class GroupAdapter extends BaseAdapter<Group> {

	public GroupAdapter(Activity context, List<Group> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		GroupView groupView = convertView == null ? null : (GroupView) convertView.getTag();
		if (convertView == null) {
			groupView = new GroupView(context, resources);
			convertView = groupView.createView(inflater, getItemViewType(position));
			convertView.setTag(groupView);
		}

		groupView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}