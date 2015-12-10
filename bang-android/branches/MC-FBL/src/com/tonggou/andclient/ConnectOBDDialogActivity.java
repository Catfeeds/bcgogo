package com.tonggou.andclient;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.OBDDevice;
import com.tonggou.andclient.vo.VehicleInfo;

/**
 * ���� OBD ��Activity
 * <p>
 * ʹ�ø� Activity Ӧ��ʹ�� startActivityForResult() ��������
 * </p>
 * <p>
 * ���� OBD �ɹ���onActivityResult() ������ intent�����л���� ���� VIN����Ϊ null��, ��Ϊ
 * EXTRA_VEHICLE_VIN
 * </p>
 * 
 * @author lwz
 * 
 */
public class ConnectOBDDialogActivity extends AbsScanObdDeviceDialogActivity {

	/**
	 * ������ VIN,���ɹ��� OBD �󣬻᷵�س��� VIN
	 */
	public static final String EXTRA_VEHICLE_VIN = "extra_vehicle_vin";
	public static final String EXTRA_OBD_DEVICE = "extra_obd_device";
	private static final String TAG = "ConnectOBDDialogActivity";

	private OBDDevice mDevice;
	private String mVechicleVin;
	private ArrayList<VehicleInfo> mVehicleInfos;
	private boolean isFromSettings;
	private boolean isOBDBindOK;
	private boolean receivedVehicleInfoBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			String strExtra = intent.getStringExtra("tonggou.connectobd.from");
			if (strExtra != null && "fromSettings".equals(strExtra)) {
				isFromSettings = true;
				registerReceiver();
				startService(new Intent(CommonRequestService.ACTION_GET_VEHICLE_LIST));
			}
		}
	}

	@Override
	public void onStateChange(int statusCode) {
		super.onStateChange(statusCode);
		TongGouApplication.showLog("onStateChange  " + statusCode);
	}

	@Override
	public void onSendOrderSuccess() {
		super.onSendOrderSuccess();
		TongGouApplication.showLog("onSendOrderSuccess");
	}

	@Override
	public void onReceiveResultSuccess(String result) {
		synchronized (this) {
			isOBDBindOK = true;
			super.onReceiveResultSuccess(result);
			setScanDialogTitle("�Ѿ���  OBD");

			TongGouApplication.showLog("ODB������Ϣ��|" + result);
			// Log.i("Bluetooth thinks", "receive:  " + readMessage);
			// readMessage = "##VIN:";

			if (result != null && result.contains("VIN:")) {
				String vehicleVin = result.substring(result.indexOf("VIN:") + 4);
				if (vehicleVin.indexOf("\r\n") != -1) {
					vehicleVin = vehicleVin.replaceAll("\r\n", "");
				}
				TongGouApplication.showLog("fvehicleVin = " + vehicleVin.trim());
				mVechicleVin = vehicleVin.trim();
			}

			TongGouApplication.getInstance().notifyBindOBDSuccess(mDevice, mVechicleVin);
			TongGouApplication.showLog(mVechicleVin);
			Intent data = new Intent();
			data.putExtra(EXTRA_VEHICLE_VIN, mVechicleVin);
			data.putExtra(EXTRA_OBD_DEVICE, mDevice);
			setResult(RESULT_OK, data);
			Log.d(TAG, "isFromSettings:" + isFromSettings + " // "
					+ "receivedVehicleInfoBroadcast:" + receivedVehicleInfoBroadcast);
			if (isFromSettings) {
				if (receivedVehicleInfoBroadcast) {
					if (mVehicleInfos != null) {
						startCertainActivity();
					} else {
						errorWarning();
					}
				} else {
					setScanDialogTitle("���ڻ�ȡ�����б�...");
					showScanDevicesIndiactor();
				}
			} else {
				finish();
			}
		}
	}

	@Override
	public void onConnectSuccess(OBDDevice device) {
		super.onConnectSuccess(device);
		mDevice = device;
		TongGouApplication.showLog("onConnectSuccess  device = " + device.toString());
	}

	@Override
	public void onConnectFailure(String msg) {
		super.onConnectFailure(msg);
		TongGouApplication.showLog("onConnectFailure  " + msg);
	}

	@Override
	public void onConnectLost() {
		super.onConnectLost();
		TongGouApplication.showLog("onConnectLost");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		TongGouApplication.getInstance().notifyBindOBDCancle();
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(CommonRequestService.ACTION_GET_VEHICLE_LIST_RESULT);
		registerReceiver(mBroadcastReceiver, filter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (CommonRequestService.ACTION_GET_VEHICLE_LIST_RESULT.equals(intent.getAction())) {
				receivedVehicleInfoBroadcast = true;
				int resultState = intent.getIntExtra(CommonRequestService.EXTRA_RESULT_STATE, 0);
				Log.d(TAG, "onReceive resultState:" + resultState);
				if (CommonRequestService.RESULT_SUCCESS == resultState) {
					mVehicleInfos = intent
							.getParcelableArrayListExtra(CommonRequestService.EXTRA_VEHICLE_LIST);
					if (isOBDBindOK) {
						startCertainActivity();
					}
				} else {
					if (isOBDBindOK) {
						errorWarning();
					}
				}
			}
		}
	};

	private void startCertainActivity() {
		if (mVehicleInfos.size() > 0) {
			startBindOBDActivity();
		} else {
			startAddBindCarActivity();
		}
		finish();
	}

	private void errorWarning() {
		TongGouApplication.showLongToast("���������쳣����ȡ�����б���Ϣʧ��");
		finish();
	}

	private void startBindOBDActivity() {
		Intent intent = new Intent(this, BindOBDActivity.class);
		intent.putExtra(EXTRA_VEHICLE_VIN, mVechicleVin);
		intent.putExtra(EXTRA_OBD_DEVICE, mDevice);
		intent.putExtra(CommonRequestService.EXTRA_VEHICLE_LIST, mVehicleInfos);
		startActivity(intent);
	}

	private void startAddBindCarActivity() {
		Intent intent = new Intent(this, AddBindCarActivity.class);
		intent.putExtra(EXTRA_VEHICLE_VIN, mVechicleVin);
		intent.putExtra(EXTRA_OBD_DEVICE, mDevice);
		startActivity(intent);
	}

	protected void onDestroy() {
		super.onDestroy();
		if (isFromSettings) {
			unregisterReceiver(mBroadcastReceiver);
		}
	};

}
