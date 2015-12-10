package com.tonggou.andclient.network.request;

import android.text.TextUtils;

import com.tonggou.andclient.network.API;
import com.tonggou.andclient.vo.type.FaultCodeStatusType;

/**
 * �޸Ĺ���������
 * @author lwz
 *
 */
public class ModifyVehicleFaultStatusRequest extends AbsTonggouHttpRequest {

	@Override
	public String getAPI() {
		return API.MODIFY_VEHICLE_FAULT_STATUS;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}

	/**
	 * 
	 * @param faultCodeId		can be null, ��Ϊ null ��˵���Ǳ��ع��ϣ���Ϊ null ��˵���� ��ʷ����
	 * @param faultCode			������
	 * @param currentStatus		��ǰ״̬
	 * @param destStatus		Ŀ��״̬
	 * @param vehicleId			�������ϵĳ��� id
	 */
	public void setRequestParams(String faultCodeId, String faultCode, FaultCodeStatusType currentStatus, FaultCodeStatusType destStatus, String vehicleId) {
		HttpRequestParams params = new HttpRequestParams();
		if( !TextUtils.isEmpty(faultCodeId) ) {
			params.put("appVehicleFaultInfoDTOs[0].id", faultCodeId);
		}
		params.put("appVehicleFaultInfoDTOs[0].errorCode", faultCode);
		params.put("appVehicleFaultInfoDTOs[0].status", destStatus.getValue());
		params.put("appVehicleFaultInfoDTOs[0].lastStatus", currentStatus.getValue());
		params.put("appVehicleFaultInfoDTOs[0].appVehicleId", vehicleId);
		super.setRequestParams(params);
	}
	
	

}
