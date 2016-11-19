package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.Notice;
import com.icaihe.view.NoticeView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

/**
 * 最新动态adapter
 * 
 * @author dyson NoticeView
 */
public class NoticeAdapter extends BaseAdapter<Notice> {

	public NoticeAdapter(Activity context, List<Notice> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		NoticeView noticeView = convertView == null ? null : (NoticeView) convertView.getTag();
		if (convertView == null) {
			noticeView = new NoticeView(context, resources);
			convertView = noticeView.createView(inflater, getItemViewType(position));
			convertView.setTag(noticeView);
		}

		noticeView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}