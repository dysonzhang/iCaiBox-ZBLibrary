package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.AlarmNotice;

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

public class AlarmNoticeView extends BaseView<AlarmNotice> implements OnClickListener {
	private static final String TAG = "AlarmNoriceView";

	public AlarmNoticeView(Activity context, Resources resources) {
		super(context, resources);
	}

	public TextView tv_remark;
	public TextView tv_time;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_alarm_notice, null);

		tv_remark = findViewById(R.id.tv_remark, this);
		tv_time = findViewById(R.id.tv_time, this);

		return convertView;
	}

	@Override
	public void setView(AlarmNotice data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_remark.setText(data.getRemark());
		tv_time.setText(data.getCreateTime());

	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
	}
}