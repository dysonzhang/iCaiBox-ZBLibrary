package com.icaihe.activity_fragment;

import com.alibaba.fastjson.JSON;
import com.icaihe.R;
import com.icaihe.application.ICHApplication;
import com.ichihe.util.HttpRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 财盒fragment
 * 
 */
public class BoxFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {

	public static BoxFragment createInstance() {
		return new BoxFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.box_fragment);

		initView();
		initData();
		initEvent();

		return view;
	}

	private TextView tv_box_name;
	private TextView tv_add_time;
	private Button bt_open;
	private Button bt_auth;

	@Override
	public void initView() {
		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		tv_add_time = (TextView) findViewById(R.id.tv_add_time);

		bt_open = (Button) findViewById(R.id.bt_open);
		bt_auth = (Button) findViewById(R.id.bt_auth);
	}

	@Override
	public void initData() {

		long boxId = ICHApplication.getInstance().getCurrentUser().getBoxId();

		HttpRequest.queryBoxDetail(boxId, HttpRequest.RESULT_QUERY_BOX_DETAIL_SUCCEED, new OnHttpResponseListener() {
			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {

				long id = JSON.parseObject(resultData).getLongValue("id");
				String ichid = JSON.parseObject(resultData).getString("ichId");
				String ibeaconId = JSON.parseObject(resultData).getString("ibeaconId");
				String wifiId = JSON.parseObject(resultData).getString("wifiId");
				String wifiPassword = JSON.parseObject(resultData).getString("wifiPassword");
				String boxName = JSON.parseObject(resultData).getString("boxName");
				long groupId = JSON.parseObject(resultData).getLongValue("groupId");
				String createTime = JSON.parseObject(resultData).getString("createTime");
				String updateTime = JSON.parseObject(resultData).getString("updateTime");

				String name = ICHApplication.getInstance().getCurrentUser().getName();

				tv_box_name.setText(name + "的财盒");
				tv_add_time.setText("添加时间：" + createTime);
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});

	}

	@Override
	public void initEvent() {
		bt_open.setOnClickListener(this);
		bt_auth.setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			this.showShortToast("确定打开");
			break;
		case 1:
			this.showShortToast("确定取消");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_open:
			new AlertDialog(context, "打开", "确定打开？", true, 0, this).show();
			break;
		case R.id.bt_auth:
			new AlertDialog(context, "授权", "确定授权？", true, 1, this).show();
			break;
		default:
			break;
		}
	}
}