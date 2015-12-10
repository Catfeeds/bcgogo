package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * ���¹����ֵ�
 * @author lwz
 *
 */
public class UpdateVehicleFaultDicRequest extends AbsTonggouHttpGetRequest {

	@Override
	public String getOriginApi() {
		return API.UPDATE_VEHICLE_FAULT_DIC;
	}

	/**
	 * 
	 * @param dicVersion	�ֵ�汾 (����汾��Ϊ����ȡ���°棩
	 * @param vehicleModelId	����ID ( �������IdΪ����ȡͨ���ֵ�)
	 */
	public void setApiParams(String dicVersion, String vehicleModelId ) {
		APIQueryParam params = new APIQueryParam();
		params.put("dicVersion", dicVersion);
		params.put("vehicleModelId", vehicleModelId);
		super.setApiParams(params);
	}
	
	

}
