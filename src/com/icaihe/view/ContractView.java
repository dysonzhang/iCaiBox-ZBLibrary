package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.Contract;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.Log;

/**
 * 联系人View
 * 
 * @author dyson
 *
 */
public class ContractView extends BaseView<Contract> implements OnClickListener, OnDialogButtonClickListener {
	private static final String TAG = "ContractView";

	public ContractView(Activity context, Resources resources) {
		super(context, resources);
	}

	public ImageView iv_user_head;
	public TextView tv_user_name;
	public TextView tv_user_number;
	public ImageView iv_call_user;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_contract, null);

		iv_user_head = findViewById(R.id.iv_user_head, this);
		tv_user_name = findViewById(R.id.tv_user_name, this);
		tv_user_number = findViewById(R.id.tv_user_number, this);
		iv_call_user = findViewById(R.id.iv_call_user, this);

		return convertView;
	}

	@Override
	public void setView(Contract data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_user_name.setText(data.getUserName());
		tv_user_number.setText(data.getUserPhone());
		iv_call_user.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.iv_call_user:
			new AlertDialog(context, "提示", "您确定要给他打电话？", true, 0, this).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			onCall(data.getUserPhone() + "");
			break;
		default:
			break;
		}
	}

	final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

	private void onCall(String mobile) {
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.CALL_PHONE },
						REQUEST_CODE_ASK_CALL_PHONE);
				return;
			} else {
				callDirectly(mobile);
			}
		} else {
			callDirectly(mobile);
		}
	}

	private void callDirectly(String mobile) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + mobile));
		context.startActivity(intent);
	}
}