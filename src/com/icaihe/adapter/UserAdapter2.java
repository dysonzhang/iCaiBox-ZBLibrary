package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.User;
import com.icaihe.view.UserView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseAdapter;

/**用户adapter
 * @author Lemon
 */
public class UserAdapter2 extends BaseAdapter<User> {
	//	private static final String TAG = "UserAdapter";

	public UserAdapter2(Activity context, List<User> list) {
		super(context, list);
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		UserView userView = convertView == null ? null : (UserView) convertView.getTag();
		if (convertView == null) {
			userView = new UserView(context, resources);
			convertView = userView.createView(inflater, getItemViewType(position));

			convertView.setTag(userView);
		}

		userView.setView(getItem(position), position, getItemViewType(position));

		return super.getView(position, convertView, parent);
	}

}