package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.activity_fragment.CompleteInfoActivity;
import com.icaihe.model.Group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

/**
 * 搜索财盒View
 * 
 * @author dyson
 *
 */
public class GroupView extends BaseView<Group> implements OnClickListener {
	private static final String TAG = "GroupView";

	public GroupView(Activity context, Resources resources) {
		super(context, resources);
	}

	public TextView tv_group_name;
	public Button bt_add_group;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_serach_group, null);

		tv_group_name = findViewById(R.id.tv_group_name, this);
		bt_add_group = findViewById(R.id.bt_add_group, this);

		return convertView;
	}

	@Override
	public void setView(Group data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_group_name.setText(data.getGroupName());
		bt_add_group.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.bt_add_group:
			Intent intent =CompleteInfoActivity.createIntent(context);
			intent.putExtra("groupId", data.getId());
			intent.putExtra("groupName", data.getGroupName());
			toActivity(intent);
			break;
		default:
			break;
		}
	}
}