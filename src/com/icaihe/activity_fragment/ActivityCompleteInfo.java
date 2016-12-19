package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 完善个人信息界面
 */
public class ActivityCompleteInfo extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityCompleteInfo.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_info, this);
		initView();
		initData();
		initEvent();
	}

	private ImageView iv_back;
	private TextView tv_add_group_tips;
	private ClearEditText et_user_name;
	private ClearEditText et_add_date;
	private ImageButton ib_chose_date;
	private Button bt_add_group;

	private long groupId = 0L;
	private String groupName = "";

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_add_group_tips = (TextView) findViewById(R.id.tv_add_group_tips);
		et_user_name = (ClearEditText) findViewById(R.id.et_user_name);
		et_add_date = (ClearEditText) findViewById(R.id.et_add_date);
		ib_chose_date = (ImageButton) findViewById(R.id.ib_chose_date);
		bt_add_group = (Button) findViewById(R.id.bt_add_group);
	}

	@Override
	public void initData() {
		super.initData();

		Intent intent = getIntent();
		groupId = intent.getLongExtra("groupId", 0L);
		groupName = intent.getStringExtra("groupName");

		tv_add_group_tips.setText(" 您已选择加入" + groupName + "财盒群，请完善个人信息。");
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
		ib_chose_date.setOnClickListener(this);
		bt_add_group.setOnClickListener(this);
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
		case R.id.bt_add_group:
			addGroup();
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
			et_add_date.setText(year + "-" + (month+1) + "-" + day);
		}
	}

	/**
	 * 表单验证
	 * 
	 * @return
	 */
	private boolean checkForm() {
		if (!StringUtil.isNotEmpty(et_user_name.getText().toString().trim(), false)) {
			showShortToast("请填写您的昵称");
			return false;
		}
		if (!StringUtil.isNotEmpty(et_add_date.getText().toString().trim(), false)) {
			showShortToast("请选择你加入的日期");
			return false;
		}
		return true;
	}

	/**
	 * TODO 加入财盒群
	 */
	private void addGroup() {

		if (groupId == 0L) {
			showShortToast("加入失败，财盒群ID为0！");
			return;
		}
		if (checkForm()) {
			final String userName = et_user_name.getText().toString();
			String joinDate = et_add_date.getText().toString();
			SVProgressHUD.showWithStatus(this, "请稍候..");
			HttpRequest.joinGroup(groupId, userName, joinDate, HttpRequest.RESULT_JOIN_GROUP_SUCCEED,
					new OnHttpResponseListener() {

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
							showShortToast("加入成功！");

							// 更新用户信息
							User user = DataManager.getInstance().getCurrentUser();
							user.setName(userName);
							user.setCompanyName(groupName);
							user.setGroupId(groupId);
							user.setNewUser(false);
							user.setGroupCreator(false);
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

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	private int[] selectedDate = new int[] { 1971, 0, 1 };
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
					// showShortToast("选择的日期为" + selectedDate[0] + "-" +
					// (selectedDate[1] + 1) + "-" + selectedDate[2]);
					et_add_date.setText(selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
				}
			}
			break;
		default:
			break;
		}
	}
}
