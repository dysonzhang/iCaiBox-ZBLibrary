package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.OpenRecord;
import com.icaihe.view.OpenRecordView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

public class OpenRecordAdapter extends BaseAdapter<OpenRecord> {

	public OpenRecordAdapter(Activity context, List<OpenRecord> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		OpenRecordView openRecordView = convertView == null ? null : (OpenRecordView) convertView.getTag();
		if (openRecordView == null) {
			openRecordView = new OpenRecordView(context, resources);
			convertView = openRecordView.createView(inflater, getItemViewType(position));
			convertView.setTag(openRecordView);
		}

		openRecordView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}