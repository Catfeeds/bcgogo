package com.tonggou.andclient.network.request;

import android.text.TextUtils;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.network.API;
import com.tonggou.andclient.vo.VehicleInfo;

/**
 * �� OBD ����
 * @author lwz
 *
 */
public class ObdBindingRequest extends AbsTonggouHttpRequest {

	@Override
	public String getAPI() {
		return API.OBD_BINDING;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}

	/**
	 * 
	 * @param userNo		�û� id, not null
	 * @param obdSN			obdӲ��Ψһ��ʶ��, obd MAC ��ַ, not null
	 * @param vehicleId		���� id ��Ϊ null,  vehicleIdΪ null ��ʾ����
	 * @param vehicleNo		���ƺ�, not null
	 * @param vehicleVin	���ܺ�, not null
	 * @param vehicleBrand	Ʒ��, not null
	 * @param vehicleModel	����, not null
	 * @param vehicle		������Ϣ��ֱ�Ӵ� vechileInfo, ���Զ�ȡ��������Ϣ
	 * @param bindingShopId	���� id,����Ϊ null
	 * 
	 */
	public void setRequestParams(String userNo, String obdSN, String vehicleId, String vehicleNo, String vehicleVin, String vehicleBrand, String vehicleModel, VehicleInfo vehicle, String bindingShopId) {
		HttpRequestParams params = new HttpRequestParams();
		
		if( vehicle != null ) {
			String engineNo = vehicle.getEngineNo();
			String registNo = vehicle.getRegistNo();
			String nextMaintainMileage = vehicle.getNextMaintainMileage();
			String nextInsuranceTime = vehicle.getNextInsuranceTime();
			String nextExamineTime = vehicle.getNextExamineTime();
			String currentMileage = vehicle.getCurrentMileage();
			String vehicleBrandId = vehicle.getVehicleBrandId();
			String vehicleModelId = vehicle.getVehicleModelId();
			
			if( !TextUtils.isEmpty(bindingShopId) ) {
				params.put("sellShopId", bindingShopId);
			}
			if( !TextUtils.isEmpty(vehicleVin) ) {
				params.put("vehicleVin", vehicleVin);
			}
			if( !TextUtils.isEmpty(engineNo) ) {
				params.put("engineNo", engineNo);
			}
			if( !TextUtils.isEmpty(registNo) ) {
				params.put("registNo", registNo);
			}
			if( !TextUtils.isEmpty(vehicleBrandId) ) {
				params.put("vehicleBrandId", vehicleBrandId);
			}
			if( !TextUtils.isEmpty(vehicleModelId) ) {
				params.put("vehicleModelId", vehicleModelId);
			}
			if( !TextUtils.isEmpty(currentMileage) && !"null".equalsIgnoreCase(currentMileage)) {
				params.put("currentMileage",currentMileage);
			} 
			if( !TextUtils.isEmpty(nextMaintainMileage) && !"null".equalsIgnoreCase(nextMaintainMileage)) {
				params.put("nextMaintainMileage", nextMaintainMileage);
			} 
			if( !TextUtils.isEmpty(nextInsuranceTime)) {
				params.put("nextInsuranceTime", nextInsuranceTime);
			} 
			if( !TextUtils.isEmpty(nextExamineTime) ) {
				params.put("nextExamineTime", nextExamineTime);
			} 
			TongGouApplication.showLog(nextInsuranceTime + "  " + nextExamineTime);
		}
		
		params.put("userNo", userNo);
		params.put("obdSN", obdSN);
		params.put("vehicleNo", vehicleNo);
		params.put("vehicleVin", vehicleVin);
		params.put("vehicleBrand", vehicleBrand);
		params.put("vehicleModel", vehicleModel);
		
		if( !TextUtils.isEmpty(vehicleId) ) {
			params.put("vehicleId", vehicleId);
		} 
		super.setRequestParams(params);
	}

}
