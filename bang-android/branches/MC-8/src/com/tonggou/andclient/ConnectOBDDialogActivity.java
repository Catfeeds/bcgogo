package com.tonggou.andclient;

import android.content.Intent;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.vo.OBDDevice;

/**
 * ���� OBD ��Activity
 * <p>ʹ�ø� Activity Ӧ��ʹ�� startActivityForResult() ��������</p>
 * <p>���� OBD �ɹ���onActivityResult() ������ intent�����л���� ���� VIN����Ϊ null��, ��Ϊ EXTRA_VEHICLE_VIN</p>
 * @author lwz
 *
 */
public class ConnectOBDDialogActivity extends AbsScanObdDeviceDialogActivity {

	/**
	 * ������ VIN,���ɹ��� OBD �󣬻᷵�س��� VIN
	 */
	public static final String EXTRA_VEHICLE_VIN = "extra_vehicle_vin";
	
	private OBDDevice mDevice;
	
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
		super.onReceiveResultSuccess(result);
		setScanDialogTitle("�Ѿ���  OBD");
		
		String vechicleVin = null;
		TongGouApplication.showLog("ODB������Ϣ��|" + result);
		// Log.i("Bluetooth thinks", "receive:  " + readMessage);
		// readMessage = "##VIN:";
		
		if ( result != null && result.contains("VIN:")) {
			String vehicleVin = result.substring(result.indexOf("VIN:") + 4);
			if (vehicleVin.indexOf("\r\n") != -1) {
				vehicleVin = vehicleVin.replaceAll("\r\n", "");
			}
			TongGouApplication.showLog("fvehicleVin = " + vehicleVin.trim());
			vechicleVin = vehicleVin.trim();
		}
		
		TongGouApplication.getInstance().notifyBindOBDSuccess(mDevice, vechicleVin);
		TongGouApplication.showLog(vechicleVin);
		Intent data = new Intent();
		data.putExtra(EXTRA_VEHICLE_VIN, vechicleVin);
		setResult(RESULT_OK, data);
		finish();
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
	
}
