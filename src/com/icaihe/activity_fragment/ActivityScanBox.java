package com.icaihe.activity_fragment;

import java.util.ArrayList;

import com.bluetooth.RRBLE.SDK.BluetoothLeService;
import com.bluetooth.RRBLE.SDK.RRBLEGatt;
import com.bluetooth.RRBLE.SDK.RRBLEScan;
import com.icaihe.R;
import com.skyfishjy.library.RippleBackground;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.Log;

public class ActivityScanBox extends BaseActivity
		implements OnClickListener, OnLongClickListener, OnBottomDragListener {

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityScanBox.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	public static RRBLEGatt LOCKER = new RRBLEGatt();
	public static BluetoothAdapter mBluetoothAdapter;
	public static boolean mScanning;
	private Handler mHandler;
	private static final int REQUEST_ENABLE_BT = 1;
	// 扫描搜索时间 单位毫秒
	private static final long SCAN_PERIOD = 10 * 1000;
	private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();
	// public static BoxBeaconAdapter boxBeaconAdapter;

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

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.i("LOCKER_TAG", "onReceive action ----- >" + action);
			if (LOCKER.RR_GATT_IsConnectAction(action)) {
				Log.i("LOCKER_TAG", "The module  connected !");
			} else if (LOCKER.RR_GATT_IsDisconnectAction(action)) {
				Log.i("LOCKER_TAG", "The module  disconnected!");
			} else if (LOCKER.RR_GATT_IsDiscoverService(action)) {
			} else if (LOCKER.RR_GATT_IsDataComeIn(action)) {
			} else if (LOCKER.RR_GATT_IsDataReadBack(action)) {
			} else if (LOCKER.RR_GATT_IsDataWriteOver(action)) {
			}
		}
	};

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

	private ImageView iv_back;
	private ImageView foundDevice;
	private TextView tv_add_group_tips;
	private Button bt_scan_qr_code;
	private ImageView centerImage;
	private RippleBackground rippleBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_box, this);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {
		super.initView();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		foundDevice = (ImageView) findViewById(R.id.foundDevice);
		rippleBackground = (RippleBackground) findViewById(R.id.content);
		tv_add_group_tips = (TextView) findViewById(R.id.tv_add_group_tips);
		bt_scan_qr_code = (Button) findViewById(R.id.bt_scan_qr_code);
		centerImage = (ImageView) findViewById(R.id.centerImage);
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
		centerImage.setOnClickListener(this);
		bt_scan_qr_code.setOnClickListener(this);
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
		scanLeDevice(true);
		startWranAnimation();
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

	private void startWranAnimation() {
		final Handler handler = new Handler();
		rippleBackground.startRippleAnimation();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				foundDevice();
			}
		}, 3000);
	}

	private void foundDevice() {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(400);
		animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		ArrayList<Animator> animatorList = new ArrayList<Animator>();
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
		animatorList.add(scaleXAnimator);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
		animatorList.add(scaleYAnimator);
		animatorSet.playTogether(animatorList);
		// foundDevice.setVisibility(View.VISIBLE);
		animatorSet.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 开锁服务取消注册
		unregisterReceiver(mGattUpdateReceiver);
		// 搜索服务
		scanLeDevice(false);
		mLeDevices.clear();
	}

	/** BLE扫描方法 */
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
							checkBeaconList();
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

	public static final String RESULT_BOX_BLE_STRING = "RESULT_BOX_BLE_STRING";

	private void checkBeaconList() {
		if (mLeDevices.size() > 0) {
			// 通过ICHID过滤
			for (BluetoothDevice device : mLeDevices) {
				if (device.getName().contains("ICH")) {
					setResult(RESULT_OK, new Intent().putExtra(RESULT_BOX_BLE_STRING, device.getName()));
					finish();
					break;
				}
			}
		}
	}

	public void showScanStatus() {
		if (mScanning) {
			tv_add_group_tips.setText("搜索财盒需要开启WIFI网络和蓝牙权限哦！财盒信号检测中...");
		} else {
			if (mLeDevices.size() <= 0) {
				tv_add_group_tips
						.setText("未检测到财盒信号，请确认APP是否开启WIFI和蓝牙权限。点击手机图标可重新检测信号。" + "您也可以点击以下‘扫一扫’按钮扫描二维码获取财盒ID。");
			} else {
				// tv_add_group_tips.setText("已停止，可点击以上蓝牙图标可进行重新检测。");
				checkBeaconList();
			}
		}
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.centerImage:
			if (mScanning) {
				this.showShortToast("财盒信号正在检测中...");
				return;
			}
			mLeDevices.clear();
			scanLeDevice(true);
			break;
		case R.id.bt_scan_qr_code:
			// toActivity(ActivityScan.createIntent(context),
			// REQUEST_TO_CAMERA_SCAN);
			finish();
			break;
		default:
			break;
		}
	}
}
