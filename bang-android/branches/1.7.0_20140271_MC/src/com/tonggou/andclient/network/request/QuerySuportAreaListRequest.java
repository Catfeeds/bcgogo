package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;
import com.tonggou.andclient.vo.type.AreaType;

/**
 * ��ѯ֧�ֵĵ����б�
 * @author lwz
 *
 */
public class QuerySuportAreaListRequest extends AbsTonggouHttpGetRequest {

	@Override
	public String getOriginApi() {
		return API.QUERY_SUPPORT_AREA_LIST;
	}
	
	/**
	 * 
	 * @param type			��ѯ���ͣ� {@link AreaType}
	 * @param provinceId	�� type Ϊ  {@link AreaType #PROVINCE} ʱ�� ��Ϊ null
	 */
	public void setApiParams(AreaType type, String provinceId ) {
		APIQueryParam params = new APIQueryParam(false);
		params.put("type", String.valueOf(type));
		params.put("provinceId", provinceId == null ? "NULL": provinceId);
		super.setApiParams(params);
	}

}
