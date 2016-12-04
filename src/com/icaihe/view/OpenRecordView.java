package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.OpenRecord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

public class OpenRecordView extends BaseView<OpenRecord> implements OnClickListener {
	private static final String TAG = "OpenRecordView";

	public OpenRecordView(Activity context, Resources resources) {
		super(context, resources);
	}

	public TextView tv_user_name;
	public TextView tv_time;

	public TextView tv_notice_type;
	public TextView tv_notice_remark;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_open_record, null);

		tv_user_name = findViewById(R.id.tv_user_name, this);
		tv_time = findViewById(R.id.tv_time, this);
		tv_notice_type = findViewById(R.id.tv_notice_type, this);
		tv_notice_remark = findViewById(R.id.tv_notice_remark, this);

		return convertView;
	}

	@Override
	public void setView(OpenRecord data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_user_name.setText(data.getUserName());
		tv_time.setText(data.getCreateTime());
		tv_notice_type.setText(getTextByType(data.getType()) + "");
		tv_notice_remark.setText(data.getRemark());
	}

	// 1：开箱
	// 2：授权开箱
	// 3：取消授权
	// 4：外借
	// 5：即借即还
	// 6：外借到期归还提醒
	// 7：保险箱报警；
	// 8：保险箱电量不足
	private String getTextByType(int type) {
		switch (type) {
		case 1:
			return "开";
		case 2:
			return "授";
		case 3:
			return "授";
		case 4:
			return "借";
		case 5:
			return "借";
		case 6:
			return "借";
		case 7:
			return "警";
		case 8:
			return "电";
		default:
			return "E";
		}
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
	}
}