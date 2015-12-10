package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * �޸���������
 * @author lwz
 *
 */
public class ModifyPasswordRequest extends AbsTonggouHttpRequest {

	@Override
	public String getAPI() {
		return API.MODIFY_PASSWORD;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PUT;
	}

	/**
	 * 
	 * @param userNo	�û���
	 * @param oldPwd	������
	 * @param newPwd	������
	 */
	public void setRequestParams(String userNo, String oldPwd, String newPwd) {
		HttpRequestParams params = new HttpRequestParams();
		params.put("userNo", userNo);
		params.put("oldPassword", oldPwd);
		params.put("newPassword", newPwd);
		super.setRequestParams(params);
	}
	
}
