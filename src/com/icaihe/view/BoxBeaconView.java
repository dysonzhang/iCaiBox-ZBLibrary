package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.activity_fragment.BoxBeaconActivity;
import com.icaihe.model.BoxBeacon;
import com.ichihe.util.HttpRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;

public class BoxBeaconView extends BaseView<BoxBeacon> implements OnClickListener {
	private static final String TAG = "BoxBeaconView";

	public BoxBeaconView(Activity context, Resources resources) {
		super(context, resources);
	}

	public TextView tv_beacon_name;
	public Button bt_open_box;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_box_beacon, null);

		tv_beacon_name = findViewById(R.id.tv_beacon_name, this);
		bt_open_box = findViewById(R.id.bt_open_box, this);

		return convertView;
	}

	@Override
	public void setView(BoxBeacon data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		tv_beacon_name.setText(data.getBeaconName());
		bt_open_box.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.bt_open_box:
			openBoxAgreement(data.getBeaconName(), "");
			break;
		default:
			break;
		}
	}

	/**
	 * 开箱握手协议
	 * 
	 * @param beaconName
	 * @param key
	 */
	private void openBoxAgreement(String beaconName, String key) {

		HttpRequest.openBoxAgreement(beaconName, key, HttpRequest.RESULT_OPEN_BOX_AGREEMENT_SUCCEED,
				new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						showShortToast("开箱秘钥->" + resultData);

						showShortToast("发送开箱秘钥...");

						String curr_boxId = DataKeeper.getRootSharedPreferences().getString("curr_boxId", "");
						String curr_boxName = DataKeeper.getRootSharedPreferences().getString("curr_boxName", "");

						if (curr_boxId.equals("") || curr_boxName.equals("")) {
							showShortToast("获取当前开箱boxId和boxName为空");
							return;
						}

						showShortToast("开箱成功后跳转...");
						Intent intent = BoxBeaconActivity.createIntent(context);
						intent.putExtra("boxId", Long.parseLong(curr_boxId));
						intent.putExtra("boxName", curr_boxName);
						toActivity(intent);
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}
}