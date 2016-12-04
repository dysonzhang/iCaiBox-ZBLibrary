package com.bluetooth.RRBLE.SDK;

import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.IBinder;
import android.util.Log;

public class RRBLEGatt {
	private final static String TAG = "BLEGatt";
	public static String DataServiceUUID = "FFEA2103-0000-3000-1001-FE019966FFFF";
	public static String Shake_UUID = "FFEA2103-0001-3000-1001-FE019966FFFF";
	public static String Control_UUID = "FFEA2103-0002-3000-1001-FE019966FFFF";
	public static String Data_UUID = "FFEA2103-0003-3000-1001-FE019966FFFF";
	public static String Mac_Address_UUID = "FFEA2103-0004-3000-1001-FE019966FFFF";

	public static String InfoServiceUUID = "0000180a-0000-1000-8000-00805f9b34fb";
	public static String Version_UUID = "00002a26-0000-1000-8000-00805f9b34fb";

	public static BluetoothLeService mBluetoothLeService;

	private static BluetoothGattService RRBLE_DataService;
	private static BluetoothGattCharacteristic RRBLE_DataCharacteristic;
	private static BluetoothGattCharacteristic RRBLE_ConfigCharacteristic;
	private static BluetoothGattCharacteristic RRBLE_ShakeCharacteristic;
	private static BluetoothGattCharacteristic RRBLE_MacCharacteristic;

	private static BluetoothGattService RRBLE_InfoService;
	private static BluetoothGattCharacteristic RRBLE_InfoCharacteristic;

	public static BluetoothGattCharacteristic RR_GATT_GetCharacteristic(BluetoothGattService svc, UUID uuid) {
		List<BluetoothGattCharacteristic> characteristics = svc.getCharacteristics();

		for (BluetoothGattCharacteristic chr : characteristics) {
			Log.i(TAG, "characteristic uuid =" + chr.getUuid().toString());

		}

		for (BluetoothGattCharacteristic gattCharacteristic : characteristics) {
			if (uuid.equals(gattCharacteristic.getUuid())) {
				return gattCharacteristic;
			}
		}

		return null;
	}

	public boolean RR_GATT_GetServiceAndCharacteristics() {

		RRBLE_DataService = RR_GATT_GetSerivce(UUID.fromString(DataServiceUUID));
		RRBLE_InfoService = RR_GATT_GetSerivce(UUID.fromString(InfoServiceUUID));
		if (RRBLE_DataService == null) {
			// Toast.makeText(this, "Can't get service",
			// Toast.LENGTH_SHORT).show();
			return false;
		}

		RRBLE_DataCharacteristic = RR_GATT_GetCharacteristic(RRBLE_DataService, UUID.fromString(Data_UUID));
		RRBLE_ShakeCharacteristic = RR_GATT_GetCharacteristic(RRBLE_DataService, UUID.fromString(Shake_UUID));
		RRBLE_ConfigCharacteristic = RR_GATT_GetCharacteristic(RRBLE_DataService, UUID.fromString(Control_UUID));
		RRBLE_MacCharacteristic = RR_GATT_GetCharacteristic(RRBLE_DataService, UUID.fromString(Mac_Address_UUID));
		RRBLE_InfoCharacteristic = RR_GATT_GetCharacteristic(RRBLE_InfoService, UUID.fromString(Version_UUID));

		if (RRBLE_DataCharacteristic == null) {
			// Toast.makeText(this, "Can't get characteristics",
			// Toast.LENGTH_SHORT).show();
			return false;
		}

		Log.i("lock", "get service over");
		RR_GATT_StartNotify(true);

		return true;
	}

	public void RR_GATT_StartNotify(boolean on) {
		mBluetoothLeService.setCharacteristicNotification(RRBLE_DataCharacteristic, on);
	}

	public static BluetoothGattService RR_GATT_GetSerivce(UUID uuid) {

		List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();

		for (BluetoothGattService svc : services) {
			Log.i(TAG, "service uuid =" + svc.getUuid().toString());

		}

		for (BluetoothGattService gattService : services) {
			if (uuid.equals(gattService.getUuid())) {
				return gattService;
			}
		}

		return null;
	}

	public boolean RR_GATT_Init(IBinder service, String mDeviceAddress) {

		mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

		if (!mBluetoothLeService.initialize()) {
			// Log.e(TAG, "Unable to initialize Bluetooth");
			return false;
		}
		// Automatically connects to the device upon successful start-up
		// initialization.
		mBluetoothLeService.connect(mDeviceAddress);

		return true;
	}

	public void RR_GATT_Reinit() {
		mBluetoothLeService = null;
	}

	public boolean RR_GATT_IsConnectAction(String action) {

		return BluetoothLeService.ACTION_GATT_CONNECTED.equals(action);
	}

	public boolean RR_GATT_IsDisconnectAction(String action) {

		return BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action);
	}

	public boolean RR_GATT_IsDiscoverService(String action) {

		return BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action);
	}

	public boolean RR_GATT_IsDataComeIn(String action) {

		return BluetoothLeService.ACTION_DATA_NOTIFY.equals(action);
	}

	public boolean RR_GATT_IsDataReadBack(String action) {
		Log.i("RRBLEGatt", "ACTION_DATA_READBACK----" + action);
		return BluetoothLeService.ACTION_DATA_READBACK.equals(action);
	}

	public boolean RR_GATT_IsDataWriteOver(String action) {
		Log.i("RRBLEGatt", "ACTION_DATA_WRITEOVER----" + action);
		return BluetoothLeService.ACTION_DATA_WRITEOVER.equals(action);
	}

	public boolean RR_GATT_Connect(String address) {
		return mBluetoothLeService.connect(address);
	}

	public void RR_GATT_Disconnect() {
		mBluetoothLeService.disconnect();
	}

	public BluetoothLeService RR_GATT_ReturnLeService() {
		return mBluetoothLeService;
	}

	/////// **RRBLE api*///////////////////////
	public void RR_GATT_SendData(byte[] d) {

		RRBLE_DataCharacteristic.setValue(d);
		RRBLE_DataCharacteristic.setWriteType(RRBLE_DataCharacteristic.getWriteType());
		mBluetoothLeService.writeCharacteristic(RRBLE_DataCharacteristic);
	}

	public void RR_GATT_Locker_on(boolean on) {
		byte[] lockON = new byte[2];// {0, 0};
		if (on) {
			lockON[0] = 1;
			lockON[1] = 1;
		}

		RRBLE_ConfigCharacteristic.setValue(lockON);
		RRBLE_ConfigCharacteristic.setWriteType(RRBLE_ConfigCharacteristic.getWriteType());
		mBluetoothLeService.writeCharacteristic(RRBLE_ConfigCharacteristic);
	}

	public void RR_GATT_ReadShakeRandomData() {

		mBluetoothLeService.readCharacteristic(RRBLE_ShakeCharacteristic);
	}

	public void RR_GATT_SendShakeData(byte[] d) {

		RRBLE_ShakeCharacteristic.setValue(d);
		RRBLE_ShakeCharacteristic.setWriteType(RRBLE_ShakeCharacteristic.getWriteType());
		mBluetoothLeService.writeCharacteristic(RRBLE_ShakeCharacteristic);
	}

	public void RR_GATT_ReadVersion() {

		mBluetoothLeService.readCharacteristic(RRBLE_InfoCharacteristic);
	}

}
