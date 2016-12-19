package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.Calendar;

import com.icaihe.R;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class ActivityBoxOpenRemark extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityBoxOpenRemark.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_open_remark, this);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void toWarnActivity(boolean isBattery) {
		Intent intent = ActivityWran.createIntent(context);
		intent.putExtra("isBattery", isBattery ? 1 : 0);
		toActivity(intent);
	}

	private TextView tv_box_name;

	private RadioGroup rg_open_type;
	private RadioButton rb_fast;
	private RadioButton rb_delay;

	private EditText et_remark;

	private RelativeLayout rl_return_date;
	private EditText et_return_date;
	private ImageButton ib_chose_date;

	private Button bt_open_confirm;

	@Override
	public void initView() {
		super.initView();
		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		rg_open_type = (RadioGroup) findViewById(R.id.rg_open_type);
		rb_fast = (RadioButton) findViewById(R.id.rb_fast);
		rb_delay = (RadioButton) findViewById(R.id.rb_delay);

		et_remark = (EditText) findViewById(R.id.et_remark);

		et_return_date = (EditText) findViewById(R.id.et_return_date);
		rl_return_date = (RelativeLayout) findViewById(R.id.rl_return_date);
		ib_chose_date = (ImageButton) findViewById(R.id.ib_chose_date);

		bt_open_confirm = (Button) findViewById(R.id.bt_open_confirm);
	}

	private long boxId;
	private String boxName = "";

	@Override
	public void initData() {
		super.initData();

		String curr_boxId = DataKeeper.getRootSharedPreferences().getString("curr_boxId", "");
		boxId = Long.parseLong(curr_boxId);
		String curr_boxName = DataKeeper.getRootSharedPreferences().getString("curr_boxName", "");
		boxName = curr_boxName;

		tv_box_name.setText(boxName);
		et_return_date.setText("默认");
	}

	@Override
	public void initEvent() {
		super.initEvent();
		rb_fast.setOnClickListener(this);
		rb_delay.setOnClickListener(this);
		ib_chose_date.setOnClickListener(this);
		bt_open_confirm.setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	private int borrowType = 5;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_fast:
			rl_return_date.setVisibility(View.GONE);
			et_return_date.setText("默认");
			borrowType = 5;
			break;
		case R.id.rb_delay:
			rl_return_date.setVisibility(View.VISIBLE);
			et_return_date.setText("");
			borrowType = 4;
			break;
		case R.id.ib_chose_date:
			// toActivity(DatePickerWindow.createIntent(context, new int[] {
			// 1971, 0, 1 },
			// TimeUtil.getDateDetail(System.currentTimeMillis())),
			// REQUEST_TO_DATE_PICKER, false);
			showDatePickerDialog();
			break;
		case R.id.bt_open_confirm:
			openRemark();
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
			et_return_date.setText(year + "-" + (month+1) + "-" + day);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	/**
	 * TODO 开箱备注
	 */
	private void openRemark() {

		String remark = et_remark.getText().toString();
		String backTime = et_return_date.getText().toString();

		if (borrowType == 5) {
			HttpRequest.borrowReturnBox(borrowType, boxId, remark, HttpRequest.RESULT_BORROW_RETURN_BOX_SUCCEED,
					new OnHttpResponseListener() {
						@Override
						public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
								String resultData) {
							if (resultCode != 1) {
								showShortToast(resultMessage);
								return;
							}
							finish();
						}

						@Override
						public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
							showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
									+ resultMessage);
						}
					});
			return;
		}
		if (checkForm()) {
			if (borrowType == 4) {
				HttpRequest.borrowBox(borrowType, boxId, backTime, remark, HttpRequest.RESULT_BORROW_BOX_SUCCEED,
						new OnHttpResponseListener() {

							@Override
							public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
									String resultData) {
								if (resultCode != 1) {
									showShortToast(resultMessage);
									return;
								}
								finish();
							}

							@Override
							public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
								showShortToast("onHttpRequestError " + "requestCode->" + requestCode
										+ " resultMessage->" + resultMessage);
							}
						});
			}
		}
	}

	/**
	 * 表单验证
	 * 
	 * @return
	 */
	private boolean checkForm() {
		if (!StringUtil.isNotEmpty(et_remark.getText().toString().trim(), false)) {
			showShortToast("请填写备注信息");
			return false;
		}
		if (!StringUtil.isNotEmpty(et_return_date.getText().toString().trim(), false)) {
			showShortToast("请选择日期");
			return false;
		}
		return true;
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
					et_return_date.setText(selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
				}
			}
			break;
		default:
			break;
		}
	}
}
