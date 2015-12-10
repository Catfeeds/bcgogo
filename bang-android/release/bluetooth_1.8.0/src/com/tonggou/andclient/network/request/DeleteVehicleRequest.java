package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * ɾ����������
 * @author lwz
 *
 */
public class DeleteVehicleRequest extends AbsTonggouHttpGetRequest {

	@Override
	public String getOriginApi() {
		return API.DELETE_VEHICLE_INFO;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}

	/**
	 * Ҫɾ���� ���� id
	 * @param vehicleId
	 */
	public void setApiParams(String vehicleId ) {
		APIQueryParam params = new APIQueryParam(true);
		params.put("vehicleId", vehicleId);
		super.setApiParams(params);
	}
	
}
