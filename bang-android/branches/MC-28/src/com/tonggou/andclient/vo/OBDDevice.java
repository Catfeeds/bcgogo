package com.tonggou.andclient.vo;

import java.io.Serializable;

public class OBDDevice implements Serializable {

	private static final long serialVersionUID = 6394260140066966485L;
	
	private String deviceName = "";
	private String deviceAddress = "";
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
	@Override
	public String toString() {
		return deviceName + "\n" + deviceAddress;
	}
	
	/**
	 * ������ OBD �豸�� MAC ��ַ��һ���ģ���ô����Ϊ��һ�� OBD �豸
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if( ! (o instanceof OBDDevice) ) {
			return false;
		} 
		OBDDevice otherDevice = (OBDDevice)o;
		return deviceAddress.equals(otherDevice.getDeviceAddress());
	}
	
	@Override
	public int hashCode() {
		return deviceAddress.hashCode();
	}
	
	
}
