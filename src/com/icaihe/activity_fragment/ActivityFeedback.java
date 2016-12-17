package com.icaihe.activity_fragment;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.icaihe.R;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.StringUtil;

/**
 * 意见反馈
 */
public class ActivityFeedback extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {
	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityFeedback.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback, this);
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
	private ImageView iv_back;
	private EditText et_content;
	private Button bt_submit;

	@Override
	public void initView() {
		super.initView();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_content = (EditText) findViewById(R.id.et_content);
		bt_submit = (Button) findViewById(R.id.bt_submit);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
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
		case R.id.bt_submit:
			submitFeedback();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	private void submitFeedback() {

		if (checkForm()) {
			String content = et_content.getText().toString();
			SVProgressHUD.showWithStatus(context, "请稍候...");
			HttpRequest.feedback(content, HttpRequest.RESULT_FEEDBACK_SUCCEED, new OnHttpResponseListener() {
				@Override
				public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
						String resultData) {
					SVProgressHUD.dismiss(context);
					if (resultCode != 1) {
						showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
						return;
					}
					showShortToast("非常感谢您的反馈！");
					finish();
				}

				@Override
				public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
					SVProgressHUD.dismiss(context);
					showShortToast(
							"onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->" + resultMessage);
				}
			});
		}
	}

	/**
	 * 表单验证
	 * 
	 * @return
	 */
	private boolean checkForm() {
		if (!StringUtil.isNotEmpty(et_content.getText().toString().trim(), false)) {
			showShortToast("请先填写您的反馈信息");
			return false;
		}
		return true;
	}
}
