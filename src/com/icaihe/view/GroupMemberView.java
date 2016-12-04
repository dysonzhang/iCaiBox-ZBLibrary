package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.GroupMember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

public class GroupMemberView extends BaseView<GroupMember> implements OnClickListener {
	private static final String TAG = "GroupMemberView";

	public GroupMemberView(Activity context, Resources resources) {
		super(context, resources);
	}

	public ImageView iv_user_head;
	public TextView tv_user_name;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_group_member, null);

		iv_user_head = findViewById(R.id.iv_user_head, this);
		tv_user_name = findViewById(R.id.tv_user_name, this);

		return convertView;
	}

	@Override
	public void setView(GroupMember data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_user_name.setText(data.getUserName());
		// iv_user_head.setText();

	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
	}
}