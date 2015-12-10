package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * ��ѯ һ����������
 * @author lwz
 *
 */
public class QuerySingleVehicleRequest extends AbsTonggouHttpGetRequest {

	@Override
	public String getOriginApi() {
		return API.QUERY_SINGLE_VEHICLE_INFO;
	}

	/**
	 * 
	 * @param vehicleId ���� id
	 */
	public void setApiParams(String vehicleId) {
		APIQueryParam params = new APIQueryParam(true);
		params.put("vehicleId", vehicleId);
		super.setApiParams(params);
	}
	
	

}
