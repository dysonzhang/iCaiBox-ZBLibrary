package com.icaihe.view;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bluetooth.RRBLE.SDK.BluetoothLeService;
import com.icaihe.R;
import com.icaihe.activity_fragment.ActivityBoxBeacon;
import com.ichihe.util.Constant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;

public class BoxBeaconView extends BaseView<BluetoothDevice> implements OnClickListener {
	private static final String TAG = "BoxBeaconView";

	public BoxBeaconView(Activity context, Resources resources) {
		super(context, resources);
	}

	public TextView tv_box_name;
	public TextView tv_beacon_name;
	public Button bt_open_box;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.item_box_beacon, null);
		tv_box_name = findViewById(R.id.tv_box_name, this);
		tv_beacon_name = findViewById(R.id.tv_beacon_name, this);
		bt_open_box = findViewById(R.id.bt_open_box, this);

		return convertView;
	}

	@Override
	public void setView(BluetoothDevice data) {
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;
		final String curr_boxName = DataKeeper.getRootSharedPreferences().getString("curr_boxName", "");
		tv_box_name.setText(curr_boxName);
		tv_beacon_name.setText(data.getName());
		bt_open_box.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_open_box:
			Constant.mDeviceAddress = data.getAddress();
			Constant.mDeviceName = data.getName();

			SVProgressHUD.showWithStatus(context, "开锁中...");
			// 开锁服务绑定
			Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
			ActivityBoxBeacon.isBindService = context.bindService(gattServiceIntent,
					ActivityBoxBeacon.mServiceConnection, Context.BIND_AUTO_CREATE);
			if (ActivityBoxBeacon.LOCKER.RR_GATT_ReturnLeService() != null) {
				ActivityBoxBeacon.LOCKER.RR_GATT_Connect(Constant.mDeviceAddress);
			}

			break;
		default:
			break;
		}
	}

}