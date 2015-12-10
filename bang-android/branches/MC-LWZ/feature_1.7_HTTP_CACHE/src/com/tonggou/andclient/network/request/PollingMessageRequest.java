package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;
import com.tonggou.andclient.vo.type.MessageType;

/**
 * ��Ϣ��ѯ ����
 * @author lwz
 *
 */
public class PollingMessageRequest extends AbsTonggouHttpGetRequest {

	private final String KEY_USER_NO = "userNo";
	private final String KEY_TYPES = "types";
	
	@Override
	public String getOriginApi() {
		return API.POLLING_MESSAGE;
	}

	/**
	 * 
	 * @param userNo	�û���
	 * @param types		��Ϣ���� {@link MessageType}
	 */
	public void setApiParams(String userNo, MessageType...types ) {
		APIQueryParam params = new APIQueryParam();
		StringBuffer typesStr = new StringBuffer();
		for( MessageType type : types ) {
			typesStr.append("," + type.getValue());
		}
		typesStr.deleteCharAt(0);
		params.put(KEY_TYPES, typesStr.toString());
		params.put(KEY_USER_NO, userNo);
		super.setApiParams(params);
	}

}
