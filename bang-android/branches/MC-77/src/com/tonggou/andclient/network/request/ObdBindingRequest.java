package com.tonggou.andclient.network.request;

import android.text.TextUtils;

import com.tonggou.andclient.network.API;

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
	 */
	public void setRequestParams(String userNo, String obdSN, String vehicleId, String vehicleNo, String vehicleVin, String vehicleBrand, String vehicleModel) {
		HttpRequestParams params = new HttpRequestParams();
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
