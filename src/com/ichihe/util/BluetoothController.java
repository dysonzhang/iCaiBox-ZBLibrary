package com.ichihe.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Bluetooth Controller
 * 
 * @author dyson
 * 
 */
public class BluetoothController {

	public static String TAG = "BluetoothController";

	public static BluetoothController btController;

	private Context context;

	public static BluetoothAdapter mBtAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public BluetoothController(Context context) {
		this.context = context;
	}

	public static BluetoothController getInstance(Context context) {
		if (btController == null)
			btController = new BluetoothController(context);
		return btController;
	}

	/**
	 * turn on the local adapter device
	 * 
	 * @return
	 */
	public static void openBTAdapter() {
		if (!mBtAdapter.isEnabled()) {
			mBtAdapter.enable();
		}
	}

	/**
	 * 判断蓝牙是否打开
	 * 
	 * @return boolean
	 */
	public boolean isOpen() {
		return mBtAdapter.isEnabled();
	}

	/**
	 * turn off the local adapter device
	 * 
	 * @return
	 */
	public static void closeBTAdapter() {
		if (mBtAdapter.isEnabled()) {
			mBtAdapter.disable();
		}
	}

	/**
	 * Check if Bluetooth LE is supported by this Android device, and if so,
	 * make sure it is enabled.
	 * 
	 * @return false if it is supported and not enabled
	 */
	public static boolean checkSupportBluetoothLe(Context context) {
		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}
		return true;
	}
}
