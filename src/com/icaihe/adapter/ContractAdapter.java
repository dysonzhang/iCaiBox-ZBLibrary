package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.Contract;
import com.icaihe.view.ContractView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

/**
 * 通讯录adapter
 * 
 * @author dyson
 *
 */
public class ContractAdapter extends BaseAdapter<Contract> {

	public ContractAdapter(Activity context, List<Contract> list) {
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ContractView contractView = convertView == null ? null : (ContractView) convertView.getTag();
		if (convertView == null) {
			contractView = new ContractView(context, resources);
			convertView = contractView.createView(inflater, getItemViewType(position));
			convertView.setTag(contractView);
		}

		contractView.setView(getItem(position), position, getItemViewType(position));
		return super.getView(position, convertView, parent);
	}
}