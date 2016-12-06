package com.icaihe.activity_fragment;

import com.alibaba.fastjson.JSON;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
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
import zuo.biao.library.util.DataKeeper;

/**
 * 用户有财盒fragment
 */
public class FragmentBox extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {

	public static FragmentBox createInstance() {
		return new FragmentBox();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.fragment_box);

		initView();
		initData();
		initEvent();

		return view;
	}

	private TextView tv_box_name;
	private TextView tv_add_time;
	private Button bt_open;
	private Button bt_auth;
	private Button bt_config;
	private long boxId;
	private boolean isCanAddBox;

	@Override
	public void initView() {
		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		tv_add_time = (TextView) findViewById(R.id.tv_add_time);

		bt_open = (Button) findViewById(R.id.bt_open);
		bt_auth = (Button) findViewById(R.id.bt_auth);
		bt_config = (Button) findViewById(R.id.bt_config);
		
		ActivityMainTab.setTabMenu(2);
	}

	@Override
	public void initData() {

		User user = DataManager.getInstance().getCurrentUser();
		isCanAddBox = user.isGroupCreator();
		boxId = user.getBoxId();

		if (isCanAddBox) {
			bt_open.setVisibility(View.VISIBLE);
			bt_auth.setVisibility(View.VISIBLE);
			bt_config.setVisibility(View.VISIBLE);
		} else {
			bt_open.setVisibility(View.VISIBLE);
			bt_auth.setVisibility(View.GONE);
			bt_config.setVisibility(View.GONE);
		}
		if (boxId == 0L) {
			showShortToast("财盒ID为0");
			return;
		}
		SVProgressHUD.showWithStatus(context, "请稍候...");
		HttpRequest.queryBoxDetail(boxId, HttpRequest.RESULT_QUERY_BOX_DETAIL_SUCCEED, new OnHttpResponseListener() {
			@Override
			public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage, String resultData) {
				SVProgressHUD.dismiss(context);
				if (resultCode != 1) {
					showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
					if(resultCode<0){
						PushSetUtil pushSetUtil = new PushSetUtil(context);
						pushSetUtil.setAlias("null");
						User user = DataManager.getInstance().getCurrentUser();
						DataManager.getInstance().removeUser(user);
						startActivity(ActivityLogin.createIntent(context));
					}
					return;
				}
				
				long id = JSON.parseObject(resultData).getLongValue("id");
				String ichid = JSON.parseObject(resultData).getString("ichId");
				String ibeaconId = JSON.parseObject(resultData).getString("ibeaconId");
				String wifiId = JSON.parseObject(resultData).getString("wifiId");
				String wifiPassword = JSON.parseObject(resultData).getString("wifiPassword");
				String boxName = JSON.parseObject(resultData).getString("boxName");
				long groupId = JSON.parseObject(resultData).getLongValue("groupId");
				String createTime = JSON.parseObject(resultData).getString("createTime");
				String updateTime = JSON.parseObject(resultData).getString("updateTime");

				DataKeeper.save(DataKeeper.getRootSharedPreferences(), "curr_groupId", groupId + "");
				DataKeeper.save(DataKeeper.getRootSharedPreferences(), "curr_boxId", boxId + "");
				DataKeeper.save(DataKeeper.getRootSharedPreferences(), "curr_ichid", ichid);
				DataKeeper.save(DataKeeper.getRootSharedPreferences(), "curr_boxName", boxName);

				tv_box_name.setText(boxName + "");
				tv_add_time.setText("添加时间：" + createTime);
			}

			@Override
			public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
				SVProgressHUD.dismiss(context);
				showShortToast(
						"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
			}
		});
	}

	@Override
	public void initEvent() {
		bt_open.setOnClickListener(this);
		bt_auth.setOnClickListener(this);
		bt_config.setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (!isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			this.toActivity(ActivityReConfigWifi.createIntent(context));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_open:
			toActivity(ActivityBoxBeacon.createIntent(context));
			break;
		case R.id.bt_auth:
			toActivity(ActivityAuthMember.createIntent(context));
			break;
		case R.id.bt_config:
			new AlertDialog(context, "提示", "请确定是否重新配置财盒的WIFI信息？", true, 0, this).show();
			break;
		default:
			break;
		}
	}


}