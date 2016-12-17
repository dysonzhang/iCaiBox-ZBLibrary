package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bluetooth.RRBLE.SDK.BluetoothLeService;
import com.bluetooth.RRBLE.SDK.RRBLEGatt;
import com.bluetooth.RRBLE.SDK.RRBLEScan;
import com.icaihe.R;
import com.icaihe.adapter.BoxBeaconAdapter;
import com.ichihe.util.Constant;
import com.ichihe.util.HexUtil;
import com.ichihe.util.HttpRequest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.Log;

/**
 * beacon搜索
 */
public class ActivityBoxBeacon extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	private static String TAG = "ActivityBoxBeacon";

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
	}
	@Override
	public void toWarnActivity(boolean isBattery) {
		Intent intent = ActivityWran.createIntent(context);
		intent.putExtra("isBattery", isBattery ? 1 : 0);
		toActivity(intent);
	}
	/**
	 * 连接开锁
	 * START--------------------------------------------------------------------------------------------------------------------------------------
	 */
	public static RRBLEGatt LOCKER = new RRBLEGatt();
	private boolean mConnected = false;
	public static boolean isBindService = false;
	public static boolean isOpen = false;
	public static final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			if (!LOCKER.RR_GATT_Init(service, Constant.mDeviceAddress)) {
				Log.e("LOCKER_TAG", "BluetoothLeService initialize failed!");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			LOCKER.RR_GATT_Reinit();
		}
	};

	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.i("LOCKER_TAG", "onReceive action ----- >" + action);
			if (LOCKER.RR_GATT_IsConnectAction(action)) {
				mConnected = true;
				Log.i("LOCKER_TAG", "The module  connected !");
			} else if (LOCKER.RR_GATT_IsDisconnectAction(action)) {
				mConnected = false;
				Log.i("LOCKER_TAG", "The module  disconnected!");
			} else if (LOCKER.RR_GATT_IsDiscoverService(action)) {
				if (!LOCKER.RR_GATT_GetServiceAndCharacteristics()) {
					showShortToast("Can't get service characteristics");
				} else {
					// 每隔0.5s执行
					shankHandle.postDelayed(runFunShake, 500);
				}
			} else if (LOCKER.RR_GATT_IsDataComeIn(action)) {
				/// display the data from ble device
				String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
				int rxCounter = 0;
				String revBuff = "";
				if (data != null) {
					revBuff += data;
					rxCounter += data.length();
					Log.i(TAG, "receive ble date count：+" + rxCounter + "-->data-->" + revBuff);
				}
			} else if (LOCKER.RR_GATT_IsDataReadBack(action)) {
				sendOpenBoxKey(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
			} else if (LOCKER.RR_GATT_IsDataWriteOver(action)) {
				if (!isOpen) {
					LOCKER.RR_GATT_Locker_on(true);
					openBox();
					isOpen = true;
				}
			}
		}
	};

	Handler shankHandle = new Handler();
	Runnable runFunShake = new Runnable() {
		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				Log.i(TAG, "read random");
				LOCKER.RR_GATT_ReadShakeRandomData();
			} catch (Exception e) {
				Log.e(TAG, "Exception " + e.getMessage());
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBindService) {
			unbindService(mServiceConnection);
			LOCKER.RR_GATT_Reinit();
		}
	}

	private void sendOpenBoxKey(final byte[] data) {

		if (Constant.mDeviceAddress.equals("")) {
			showShortToast("财盒ID为空");
			return;
		}

		String curr_ichid = DataKeeper.getRootSharedPreferences().getString("curr_ichid", "");
		String key = HexUtil.bytesToHexString(data);

		HttpRequest.openBoxAgreement(curr_ichid, key, HttpRequest.RESULT_OPEN_BOX_AGREEMENT_SUCCEED,
				new OnHttpResponseListener() {
					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						if (resultCode != 1) {
							shakeUseData(data);
							showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
							return;
						}

						byte[] shakeData = HexUtil.hexStringToBytes(resultData);
						LOCKER.RR_GATT_SendShakeData(shakeData);
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						shakeUseData(data);
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}

	private void shakeUseData(byte[] data) {
		byte[] randCmd = new byte[4];
		byte t = 0;

		randCmd[0] = (byte) (data[0] >= 0 ? data[0] : data[0] + 256);
		randCmd[1] = (byte) (data[1] >= 0 ? data[1] : data[1] + 256);
		t = (byte) (data[0] ^ data[1]);
		randCmd[2] = (byte) (t >= 0 ? t : t + 256);
		t = (byte) (data[0] & data[1]);
		randCmd[3] = (byte) (t >= 0 ? t : t + 256);

		LOCKER.RR_GATT_SendShakeData(randCmd);
		Log.i(TAG, "ranmd== " + randCmd[0] + ", " + randCmd[1] + ", " + randCmd[2] + ", " + randCmd[3]);
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_READBACK);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITEOVER);
		return intentFilter;
	}

	/**
	 * TODO 开箱记录添加 打开成功时添加
	 * 
	 * @param boxId
	 */
	private void openBox() {

		final String curr_boxId = DataKeeper.getRootSharedPreferences().getString("curr_boxId", "");
		final String curr_boxName = DataKeeper.getRootSharedPreferences().getString("curr_boxName", "");

		HttpRequest.openBox(Long.parseLong(curr_boxId), 1, HttpRequest.RESULT_OPEN_BOX_SUCCEED,
				new OnHttpResponseListener() {

					@Override
					public void onHttpRequestSuccess(int requestCode, int resultCode, String resultMessage,
							String resultData) {
						SVProgressHUD.dismiss(context);
						if (resultCode != 1) {
							isOpen = false;
							finish();
							showShortToast("requestCode->" + requestCode + " resultMessage->" + resultMessage);
							return;
						}

						toActivity(ActivityBoxOpenRemark.createIntent(context));
						isOpen = false;
						finish();
					}

					@Override
					public void onHttpRequestError(int requestCode, String resultMessage, Exception exception) {
						SVProgressHUD.dismiss(context);
						toActivity(ActivityBoxOpenRemark.createIntent(context));
						isOpen = false;
						finish();
						showShortToast("onHttpRequestError " + "requestCode->" + requestCode + " resultMessage->"
								+ resultMessage);
					}
				});
	}

	/**
	 * END--------------------------------------------------------------------------------------------------------------------------------------
	 */

	private ImageView iv_back;
	private TextView tv_bt_tips;
	private ImageView iv_bt_scan;
	private ListView lv_beacons;

	public static BluetoothAdapter mBluetoothAdapter;
	public static boolean mScanning;
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

		// 开锁服务注册
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
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
		// 开锁服务取消注册
		unregisterReceiver(mGattUpdateReceiver);
		// 搜索服务
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
						if (!deviceIsHave(mLeDevices, device)) {
							mLeDevices.add(device);
							refreshBeaconList();
						}
					}
				}
			});
		}
	};

	private boolean deviceIsHave(ArrayList<BluetoothDevice> btDevices, BluetoothDevice device) {
		if (btDevices == null || btDevices.size() <= 0) {
			return false;
		}
		for (BluetoothDevice bd : btDevices) {
			if (device.getAddress().equals(bd.getAddress())) {
				return true;
			}
		}
		return false;
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
		case R.id.iv_bt_scan:
			if (mScanning) {
				this.showShortToast("财盒信号检测中...");
				return;
			}
			mLeDevices.clear();
			refreshBeaconList();
			scanLeDevice(true);
			Constant.mDeviceAddress = "";
			Constant.mDeviceName = "";
			break;
		default:
			break;
		}
	}

	public void showScanStatus() {
		if (mScanning) {
			tv_bt_tips.setText("财盒信号检测中...");
		} else {
			if (mLeDevices.size() <= 0) {
				tv_bt_tips.setText("未检测到财盒信号，请确认APP是否开启WIFI和蓝牙权限。点击以上蓝牙图标可进行重新检测。");
			} else {
				tv_bt_tips.setText("已停止，可点击以上蓝牙图标可进行重新检测。");
			}

		}
	}

	private void refreshBeaconList() {
		String ichid = DataKeeper.getRootSharedPreferences().getString("curr_ichid", "");
		List<BluetoothDevice> myDeviceList = new ArrayList<BluetoothDevice>();
		if (mLeDevices.size() > 0 && !ichid.equals("")) {
			// 通过ICHID过滤
			for (BluetoothDevice device : mLeDevices) {
				if (device.getName().equals(ichid)) {
					myDeviceList.add(device);
					break;
				}
			}
		}

		boxBeaconAdapter = new BoxBeaconAdapter(context, myDeviceList);
		lv_beacons.setAdapter(boxBeaconAdapter);
		boxBeaconAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
