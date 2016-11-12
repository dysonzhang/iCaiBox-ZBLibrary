package com.icaihe.activity_fragment;

import java.util.ArrayList;

import com.icaihe.R;
import com.icaihe.widget.ClearEditText;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.TimeUtil;

/**
 * 财盒群创建界面
 * 
 * @author dyson
 *
 */
public class CreateGroupActivity extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {
	private static final String TAG = "CreateGroupActivity";

	// 启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 启动这个Activity的Intent
	 * 
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, CreateGroupActivity.class);
	}

	// 启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group_activity, this);

		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private ImageView iv_back;

	private ClearEditText et_company_name;
	private ClearEditText et_setup_man;

	private ClearEditText et_company_date;
	private ImageButton ib_chose_date;

	private ClearEditText et_company_location;
	private ImageButton ib_chose_location;

	private Button bt_create;

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);

		et_company_name = (ClearEditText) findViewById(R.id.et_company_name);
		et_setup_man = (ClearEditText) findViewById(R.id.et_setup_man);

		et_company_date = (ClearEditText) findViewById(R.id.et_company_date);
		ib_chose_date = (ImageButton) findViewById(R.id.ib_chose_date);

		et_company_location = (ClearEditText) findViewById(R.id.et_company_location);
		ib_chose_location = (ImageButton) findViewById(R.id.ib_chose_location);

		bt_create = (Button) findViewById(R.id.bt_create);
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {
		super.initData();

	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {
		super.initEvent();

		iv_back.setOnClickListener(this);

		ib_chose_date.setOnClickListener(this);
		ib_chose_location.setOnClickListener(this);
		bt_create.setOnClickListener(this);
	}

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;
		case R.id.ib_chose_date:
			toActivity(DatePickerWindow.createIntent(context, new int[] { 1971, 0, 1 },
					TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);
			break;
		case R.id.ib_chose_location:
			// startActivity(BottomTabActivity.createIntent(context).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			// overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			// enterAnim = exitAnim = R.anim.null_anim;
			// finish();
			et_company_location.setText("打开地图选择地址");
			break;
		case R.id.bt_create:
			create();
			break;
		default:
			break;
		}
	}

	private void create() {
		if (checkFormValid()) {
			startActivity(CompleteInfoActivity.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
		}
	}

	/**
	 * 验证表单
	 * 
	 * @return
	 */
	private boolean checkFormValid() {
		if (et_company_name.getText().equals(null) || et_company_name.getText().toString().trim().equals("")) {
			et_company_name.setShakeAnimation();
			showShortToast("企业组织名称不能为空");
			return false;
		}
		if (et_setup_man.getText().equals(null) || et_setup_man.getText().toString().trim().equals("")) {
			et_setup_man.setShakeAnimation();
			showShortToast("创始人不能为空");
			return false;
		}
		if (et_company_date.getText().equals(null) || et_company_date.getText().toString().trim().equals("")) {
			et_company_date.setShakeAnimation();
			showShortToast("企业组织创立日期不能为空");
			return false;
		}
		if (et_company_location.getText().equals(null) || et_company_location.getText().toString().trim().equals("")) {
			et_company_location.setShakeAnimation();
			showShortToast("企业组织地址");
			return false;
		}
		return true;
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	private int[] selectedDate = new int[] { 1971, 0, 1 };
	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	private static final int REQUEST_TO_DATE_PICKER = 1;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_DATE_PICKER:
			if (data != null) {
				ArrayList<Integer> list = data.getIntegerArrayListExtra(DatePickerWindow.RESULT_DATE_DETAIL_LIST);
				if (list != null && list.size() >= 3) {

					selectedDate = new int[list.size()];
					for (int i = 0; i < list.size(); i++) {
						selectedDate[i] = list.get(i);
					}
					showShortToast("选择的日期为" + selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);

					et_company_date.setText(selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
				}
			}
			break;
		default:
			break;
		}
	}
	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
