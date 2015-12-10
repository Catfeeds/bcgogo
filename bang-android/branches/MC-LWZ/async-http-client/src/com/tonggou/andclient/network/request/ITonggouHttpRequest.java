package com.tonggou.andclient.network.request;

import com.loopj.android.http.RequestParams;

/**
 * ����ӿ�
 * @author lwz
 *
 */
public interface ITonggouHttpRequest {
	
	/**
	 * �õ� API
	 * @return
	 */
	public String getAPI();
	
	/**
	 * �õ� ��������
	 * @return
	 */
	public HttpMethod getHttpMethod();
	
	/**
	 * �õ��������������Ϊ null
	 * @return
	 */
	public RequestParams getRequestParams();
	
	/**
	 * �Ƿ�Ϊ �ο�ģʽ
	 * @return true �ο�ģʽ��false ��¼��Աģʽ
	 */
	public boolean isGuestMode();
	
}
