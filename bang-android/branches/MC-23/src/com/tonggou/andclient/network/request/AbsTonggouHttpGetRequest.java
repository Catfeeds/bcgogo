package com.tonggou.andclient.network.request;

/**
 * Get �������
 * @author lwz
 *
 */
public abstract class AbsTonggouHttpGetRequest extends AbsTonggouHttpRequest {

	private APIQueryParam mParams;
	private APIQueryParam mGuestParams;
	
	
	@Override
	public String getAPI() {
		return HttpRequestClient.getAPIWithQueryParams(getOriginApi(), getAPIQueryParams());
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.GET;
	}
	
	/**
	 * �õ� ���������� ԭʼ API ��ַ
	 * @return
	 */
	public abstract String getOriginApi(); 
	
	/**
	 * �õ����� API�ϵĲ���
	 * @return
	 */
	public APIQueryParam getAPIQueryParams() {
		return isGuestMode() ? mGuestParams : mParams;
	}
	
	/**
	 * ���õ�¼״̬�Ĳ���
	 * @param params
	 */
	protected void setApiParams(APIQueryParam params) {
		mParams = params;
	}
	
	/**
	 * �����ο�״̬�Ĳ���
	 * @param params
	 */
	protected void setGuestApiParams(APIQueryParam params) {
		mGuestParams = params;
	}
}
