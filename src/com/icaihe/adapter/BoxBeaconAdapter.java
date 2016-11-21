package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.BoxBeacon;
import com.icaihe.view.BoxBeaconView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

public class BoxBeaconAdapter extends BaseAdapter<BoxBeacon> {

	public BoxBeaconAdapter(Activity context, List<BoxBeacon> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BoxBeaconView boxBeaconView = convertView == null ? null : (BoxBeaconView) convertView.getTag();
		if (convertView == null) {
			boxBeaconView = new BoxBeaconView(context, resources);
			convertView = boxBeaconView.createView(inflater, getItemViewType(position));
			convertView.setTag(boxBeaconView);
		}

		boxBeaconView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}