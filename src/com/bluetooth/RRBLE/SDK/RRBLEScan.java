package com.bluetooth.RRBLE.SDK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

public class RRBLEScan {

	// private BluetoothAdapter mBluetoothAdapter;

	private Handler mHandler;
	private int mMC10State;
	// 10秒后停止查找搜索.
	/// FFEA2103-0000-3000-1001-FE019966FFFF FFEA2103000030001001FE019966FFFF
	// F85CF3F7-AE11-4366-841C-6719EE09197C
	private static final String EH_MC10_DEFAULT_UUID128 = "F85CF3F7AE114366841C6719EE09197C";
	private static final long SCAN_PERIOD = 100000;

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

	private static final String[] hexArr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
			"F" };

	public static String byte2HexStr(byte[] byt) {
		StringBuffer strRet = new StringBuffer();
		for (int i = 0; i < byt.length; i++) {
			strRet.append(hexArr[(byt[i] & 0xf0) / 16]);
			strRet.append(hexArr[byt[i] & 0x0f]);
		}
		return strRet.toString();
	}

	//// used for fileter the device by uuid /* 2015-03-29*/
	/// uuid ===
	//// 0201061107FFEA2103000030001001FE019966FFFF031900000A0952522D4C4F434B45520000000000000000000000000000000000000000000000000000

	public static boolean EH_FilterUUID_128(String srcAdvData) {

		Log.i("lock", "uuid === " + srcAdvData);
		if (srcAdvData.substring(10, 42).equalsIgnoreCase(EH_MC10_DEFAULT_UUID128)) {
			return true;
		}
		return false;
	}

	public static boolean EH_BLE_Support(Context context) {
		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
		if (!DeviceScan.isBLESupported(context)) {
			// Toast.makeText(this, R.string.ble_not_supported,
			// Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;

	}

	public static BluetoothAdapter EH_GetBLE_Manager(Context context) {
		// 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		final BluetoothManager bluetoothManager = DeviceScan.getManager(context);
		final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		return mBluetoothAdapter;
	}
	/*
	 * private void scanLeDevice(final boolean enable) { if (enable) { // Stops
	 * scanning after a pre-defined scan period. mHandler.postDelayed(new
	 * Runnable() {
	 * 
	 * @Override public void run() { mMC10State = 1;
	 * mBluetoothAdapter.stopLeScan(mLeScanCallback); } }, SCAN_PERIOD);
	 * 
	 * mMC10State = 1; mBluetoothAdapter.startLeScan(mLeScanCallback); } else {
	 * mMC10State = 0; mBluetoothAdapter.stopLeScan(mLeScanCallback); } }
	 */

}
