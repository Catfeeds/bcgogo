package com.tonggou.andclient.network.request;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * ���е�����Ӧ�ü̳д˷���
 * @author lwz
 *
 */
public abstract class AbsTonggouHttpRequest implements ITonggouHttpRequest {
	
	private boolean isGuestMode = false;
	private RequestParams requestParams = null;
	
	/**
	 * Ĭ��Ϊ ��¼��Աģʽ����Ҫ�����ο�ģʽ����ʹ�� {@link #AbsTonggouHttpRequest(boolean)}
	 */
	public AbsTonggouHttpRequest() {
		
	}
	
	/**
	 * @param isGuestMode 
	 * 				�Ƿ�Ϊ �ο�ģʽ  true �ο�ģʽ��false ��¼��Աģʽ
	 */
	public AbsTonggouHttpRequest(boolean isGuestMode) {
		this.isGuestMode  = isGuestMode;
	}
	
	@Override
	public abstract String getAPI();
	
	@Override
	public abstract HttpMethod getHttpMethod();
	
	public void setRequestParams(RequestParams params) {
		requestParams = params;
	}
	
	@Override
	public RequestParams getRequestParams() {
		return requestParams;
	}
	
	@Override
	public boolean isGuestMode() {
		return isGuestMode;
	}
	
	/**
	 * ��ʼ��������
	 * @param context			������
	 * @param responseHandler	��Ӧ���������
	 */
	public void doRequest(Context context, final AsyncHttpResponseHandler responseHandler) {
		String api = getAPI();
		RequestParams params = getRequestParams();
		HttpRequestClient client = new HttpRequestClient(context);
		switch (getHttpMethod()) {
			case GET: client.get(api, params, responseHandler); break;
			case POST: client.post(api, params, responseHandler); break;
			case DELETE: client.delete(api, responseHandler); break;
			case PUT: client.put(api, params, responseHandler); break;
			default: break;
		}
	}
	
}
