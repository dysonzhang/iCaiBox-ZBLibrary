package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.AlarmNotice;
import com.icaihe.view.AlarmNoticeView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

public class AlarmNoticeAdapter extends BaseAdapter<AlarmNotice> {

	public AlarmNoticeAdapter(Activity context, List<AlarmNotice> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AlarmNoticeView alarmNoticeView = convertView == null ? null : (AlarmNoticeView) convertView.getTag();
		if (alarmNoticeView == null) {
			alarmNoticeView = new AlarmNoticeView(context, resources);
			convertView = alarmNoticeView.createView(inflater, getItemViewType(position));
			convertView.setTag(alarmNoticeView);
		}

		alarmNoticeView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}