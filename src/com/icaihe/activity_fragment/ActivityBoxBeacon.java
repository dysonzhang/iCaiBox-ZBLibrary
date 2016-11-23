package com.icaihe.activity_fragment;

import java.util.ArrayList;

import com.bluetooth.RRBLE.SDK.RRBLEScan;
import com.icaihe.R;
import com.icaihe.adapter.BoxBeaconAdapter;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.SettingUtil;

/**
 * beacon搜索
 */
public class ActivityBoxBeacon extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityBoxBeacon.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_beacon, this);

		initView();
		initData();
		initEvent();

		if (SettingUtil.isOnTestMode) {
			showShortToast("测试服务器\n" + HttpRequest.URL_BASE);
		}
	}

	private ImageView iv_back;
	private TextView tv_bt_tips;
	private ImageView iv_bt_scan;
	private ListView lv_beacons;

	public static   BluetoothAdapter mBluetoothAdapter;
	public static  boolean mScanning;
	private Handler mHandler;

	private static final int REQUEST_ENABLE_BT = 1;
	// 扫描搜索时间 单位毫秒
	private static final long SCAN_PERIOD = 10 * 1000;

	private ArrayList<BluetoothDevice> mLeDevices;
	public static BoxBeaconAdapter boxBeaconAdapter;

	static class DeviceScan {
		/** check if BLE Supported device */
		public static boolean isBLESupported(Context context) {
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
		}

		/** get BluetoothManager */
		public static BluetoothManager getManager(Context context) {
			return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		}
	}

	@Override
	public void initView() {
		super.initView();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_bt_tips = (TextView) findViewById(R.id.tv_bt_tips);
		iv_bt_scan = (ImageView) findViewById(R.id.iv_bt_scan);
		lv_beacons = (ListView) findViewById(R.id.lv_beacons);
	}

	@Override
	public void initData() {
		super.initData();

		mHandler = new Handler();

		// 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		mBluetoothAdapter = RRBLEScan.EH_GetBLE_Manager(this);

		// 检查设备上是否支持蓝牙
		if (mBluetoothAdapter == null) {
			showShortToast("抱歉！您的手机暂时不支持蓝牙通讯，无法使用此功能！");
			finish();
			return;
		}

		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
		if (!DeviceScan.isBLESupported(this)) {
			showShortToast("抱歉！您的手机暂时不支持蓝牙4.0，无法使用此功能！");
			finish();
		}
	}

	@Override
	public void initEvent() {
		super.initEvent();
		iv_back.setOnClickListener(this);
		iv_bt_scan.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}

		// Initializes list view adapter.
		mLeDevices = new ArrayList<BluetoothDevice>();
		boxBeaconAdapter = new BoxBeaconAdapter(context, mLeDevices);
		lv_beacons.setAdapter(boxBeaconAdapter);

		scanLeDevice(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
		mLeDevices.clear();
		boxBeaconAdapter.notifyDataSetChanged();
	}

	/**
	 * BLE扫描方法
	 * 
	 * @param enable
	 */
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					showScanStatus();
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		showScanStatus();
	}

	// Device scan callback. api for bluetooth api
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String srvAdvData = RRBLEScan.byte2HexStr(scanRecord);
					if (RRBLEScan.EH_FilterUUID_128(srvAdvData)) {
						mLeDevices.add(device);
						refreshBeaconList();
					}
				}
			});
		}
	};

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
		case R.id.iv_bt_scan:
			if (mScanning) {
				this.showShortToast("财盒信号正在搜索中");
				return;
			}
			mLeDevices.clear();
			refreshBeaconList();
			scanLeDevice(true);
			break;
		default:
			break;
		}
	}

	public void showScanStatus() {
		if (mScanning) {
			tv_bt_tips.setText("正在搜索财盒信号...");
		} else {
			tv_bt_tips.setText("已停止搜索财盒信号，点击以上蓝牙图标可进行重新搜索。");
		}
	}

	private void refreshBeaconList() {
		if (mLeDevices.size() > 0) {
			// 通过ICHID过滤
			// List<BluetoothDevice> myDeviceList = new
			// ArrayList<BluetoothDevice>();
			// String ichid =
			// DataKeeper.getRootSharedPreferences().getString("curr_ichid",
			// "");
			// for (BluetoothDevice device : mLeDevices) {
			// if (device.getName().equals(ichid)) {
			// myDeviceList.add(device);
			// break;
			// }
			// }
		}

		boxBeaconAdapter = new BoxBeaconAdapter(context, mLeDevices);
		lv_beacons.setAdapter(boxBeaconAdapter);
		boxBeaconAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
