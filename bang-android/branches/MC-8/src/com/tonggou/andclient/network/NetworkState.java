package com.tonggou.andclient.network;

public class NetworkState {
	
	public static final String ERROR_SERVER_ERROR_MESSAGE ="����������";
	public static final String ERROR_SERVER_ERROR_MESSAGE_EN ="server error";

	public static final String ERROR_CLIENT_ERROR_TIMEOUT_MESSAGE = "���粻ͨ�������ú����ֶ�ˢ��";	
	public static final String ERROR_CLIENT_ERROR_TIMEOUT_MESSAGE_EN ="connectionless network service";
	
	public static final String ERROR_CLIENT_ERROR_SOCKETTIMEOUT_MESSAGE ="������������ʱ";	
	public static final String ERROR_CLIENT_ERROR_SOCKETTIMEOUT_MESSAGE_EN ="socket_timeout";
	
	public static final String ERROR_RESPONSE_ERROR_MESSAGE ="���������쳣��";
	public static final String ERROR_RESPONSE_ERROR_MESSAGE_EN ="http response error";
		
	private boolean networkState = false;       
	
	private String errorMessage = "";
	
	public boolean isNetworkSuccess() {
		return networkState;
	}
	public void setNetworkState(boolean networkState) {
		this.networkState = networkState;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
