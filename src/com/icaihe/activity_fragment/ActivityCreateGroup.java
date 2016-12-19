package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.icaihe.jpush.PushSetUtil;
import com.icaihe.manager.DataManager;
import com.icaihe.model.User;
import com.icaihe.widget.ClearEditText;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.Log;

/**
 * 财盒群创建界面
 */
public class ActivityCreateGroup extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityCreateGroup.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group, this);
		initView();
		initData();
		initEvent();
	}

	private ImageView iv_back;

	private ClearEditText et_company_name;
	private ClearEditText et_setup_man;

	private ClearEditText et_company_date;
	private ImageButton ib_chose_date;

	private ClearEditText et_company_location;
	private ImageButton ib_chose_location;

	private Button bt_create;

	public static String addressX = "";
	public static String addressY = "";
	public static String location = "";

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

	int isPrivate;

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		isPrivate = intent.getIntExtra("isPrivate", 0);
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
		ib_chose_date.setOnClickListener(this);
		ib_chose_location.setOnClickListener(this);
		bt_create.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (location.length() > 13) {
			et_company_location.setText(location.substring(0, 12) + "...");
		} else {
			et_company_location.setText(location);
		}
	}

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
			// toActivity(DatePickerWindow.createIntent(context, new int[] {
			// 1971, 0, 1 },
			// TimeUtil.getDateDetail(System.currentTimeMillis())),
			// REQUEST_TO_DATE_PICKER, false);
			showDatePickerDialog();
			break;
		case R.id.ib_chose_location:
			startActivity(ActivityMap.createIntent(context));
			overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
			break;
		case R.id.bt_create:
			create();
			break;
		default:
			break;
		}
	}

	public void showDatePickerDialog() {
		DatePickerFragment datePicker = new DatePickerFragment();
		datePicker.show(getFragmentManager(), "datePicker");
	}

	class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Log.d("OnDateSet", "select year:" + year + ";month:" + month + ";day:" + day);
			et_company_date.setText(year + "-" + (month+1) + "-" + day);
		}
	}

	/**
	 * TODO 创建财盒群
	 */
	private void create() {
		if (checkFormValid()) {

			String groupName = et_company_name.getText().toString();
			String creatorName = et_setup_man.getText().toString();
			String gCreateTime = et_company_date.getText().toString();
			String groupAddress = et_company_location.getText().toString();

			if (isPrivate == 1) {
				groupName += "_个人";
			}

			SVProgressHUD.showWithStatus(this, "请稍候...");
			HttpRequest.createGroup(groupName, creatorName, gCreateTime, groupAddress, addressX, addressY,
					HttpRequest.RESULT_CREATE_GROUP_SUCCEED, new OnHttpResponseListener() {

						@Override
						public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
								String resultData) {
							SVProgressHUD.dismiss(context);
							if (resultCode != 1) {
								PushSetUtil pushSetUtil = new PushSetUtil(context);
								pushSetUtil.setAlias("null");
								User user = DataManager.getInstance().getCurrentUser();
								DataManager.getInstance().removeUser(user);
								startActivity(ActivityLogin.createIntent(context));
								showShortToast(resultMessage);
								return;
							}
							showShortToast("创建成功！");

							JSONObject jsonObject;
							long groupId = 0L;
							String groupName = "";
							try {
								jsonObject = new JSONObject(resultData);
								groupId = jsonObject.getLong("id");
								groupName = jsonObject.getString("groupName");
							} catch (JSONException e) {
								e.printStackTrace();
							}

							// 更新用户信息
							User user = DataManager.getInstance().getCurrentUser();
							String creatorName = et_setup_man.getText().toString();
							user.setName(creatorName);
							user.setCompanyName(groupName);
							user.setGroupId(groupId);
							user.setNewUser(false);
							user.setGroupCreator(true);
							DataManager.getInstance().saveCurrentUser(user);

							// 跳转至主页
							startActivity(
									ActivityMainTab.createIntent(context).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
							enterAnim = exitAnim = R.anim.null_anim;
							finish();
						}

						@Override
						public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
							SVProgressHUD.dismiss(context);
							showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
									+ resultMessage);
							finish();
						}
					});
		}
	}

	/**
	 * TODO 创建财盒群表单验证
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
	private static final int REQUEST_TO_DATE_PICKER = 1;

	public static final int REQUEST_TO_MAP_LOCATION = 2;

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
					// showShortToast("选择的日期为" + selectedDate[0] + "-" +
					// (selectedDate[1] + 1) + "-" + selectedDate[2]);
					et_company_date.setText(selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
				}
			}
			break;
		case REQUEST_TO_MAP_LOCATION:
			if (data != null) {
				String result = data.getStringExtra(ActivityMap.RESULT_LOCATION_STRING);
				et_company_location.setText(result);
			}
		default:
			break;
		}
	}
}
