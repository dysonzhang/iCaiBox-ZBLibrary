package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.AuthMember;
import com.ichihe.util.HttpRequest;

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
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;

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
		// ‘0’表示未授权开箱，‘1’表示已经授权开箱
		if (data.getAuthority() == 0) {
			bt_auth.setText("马上授权");
		} else {
			bt_auth.setText("取消授权");
		}
		bt_auth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.bt_auth:
			// ‘0’表示未授权开箱，‘1’表示已经授权开箱
			String boxId = DataKeeper.getRootSharedPreferences().getString("curr_boxId", "");
			if (boxId.equals("")) {
				showShortToast("当前财盒ID数据错误");
				return;
			}

			if (data.getAuthority() == 0) {
				HttpRequest.authUser(Long.parseLong(boxId), data.getUserId(), HttpRequest.RESULT_AUTH_USER_SUCCEED,
						new OnHttpResponseListener() {

							@Override
							public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
									String resultData) {
								showShortToast("授权成功");
							}
							@Override
							public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
								showShortToast("onHttpRequestError " + "requestCode->" + requestCode
										+ " resultMessage->" + resultMessage);
							}
						});

			} else {
				HttpRequest.cancelAuthUser(Long.parseLong(boxId), data.getUserId(), HttpRequest.RESULT_CANCEL_AUTH_USER_SUCCEED,
						new OnHttpResponseListener() {

							@Override
							public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
									String resultData) {
								showShortToast("取消授权成功");
							}
							@Override
							public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
								showShortToast("onHttpRequestError " + "requestCode->" + requestCode
										+ " resultMessage->" + resultMessage);
							}
						});
			}
			break;
		default:
			break;
		}
	}
}