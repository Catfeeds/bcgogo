package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * ��ѯ�ۺ�֧��Υ�³����б�
 * @author lwz
 *
 */
public class QueryJuheCityListRequest extends AbsTonggouHttpGetRequest {

	@Override
	protected String getOriginApi() {
		return API.QUERY_JUHE_CITY_LIST;
	}
	
}
