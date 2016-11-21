package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.AuthMember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

/**
 * 
 * @author dyson
 *
 */
public class AuthMemberView extends BaseView<AuthMember> implements OnClickListener {
	private static final String TAG = "AuthMemberView";

	public AuthMemberView(Activity context, Resources resources) {
		super(context, resources);
	}

	public ImageView iv_user_head;
	public TextView tv_user_name;
	public TextView tv_user_number;
	public Button bt_auth;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_auth_member, null);

		iv_user_head = findViewById(R.id.iv_user_head, this);
		tv_user_name = findViewById(R.id.tv_user_name, this);
		tv_user_number = findViewById(R.id.tv_user_number, this);
		bt_auth = findViewById(R.id.bt_auth, this);

		return convertView;
	}

	@Override
	public void setView(AuthMember data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_user_name.setText(data.getUserName());
		tv_user_number.setText(data.getUserPhone());
		bt_auth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.bt_auth:
			
			break;
		default:
			break;
		}
	}
}