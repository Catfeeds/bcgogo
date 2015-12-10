package com.tonggou.andclient.network.request;


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
	public HttpRequestParams getRequestParams();
	
	/**
	 * �õ��ο��������������Ϊ null
	 * @return
	 */
	public HttpRequestParams getGuestRequestParams();
	
	/**
	 * �Ƿ�Ϊ �ο�ģʽ
	 * @return true �ο�ģʽ��false ��¼��Աģʽ
	 */
	public boolean isGuestMode();
	
}
