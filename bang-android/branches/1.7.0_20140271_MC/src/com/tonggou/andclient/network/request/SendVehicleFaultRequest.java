package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;

/**
 * ���͹���������
 * @author lwz
 *
 */
public class SendVehicleFaultRequest extends AbsTonggouHttpRequest {

	@Override
	public String getAPI() {
		return API.SEND_VEHICLE_FAULT;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}

	/**
	 * 
	 * @param userNo		�û��˺�
	 * @param obdSN			obdΨһ��ʶ�� MAC
	 * @param vehicleVin	����Ψһ��ʶ��
	 * @param vehicleId		��̨�������� , not null
	 * @param reportTime	����ʱ��
	 * @param faultCode		������ ����ж�����������Զ��� ,�ֿ���  һ����Խ��ն��������
	 */
	public void setRequestParams(String userNo, String obdSN, String vehicleVin, String vehicleId, long reportTime, String faultCode) {
		HttpRequestParams params = new HttpRequestParams();
		params.put("userNo", userNo);
		params.put("obdSN", obdSN);
		params.put("vehicleVin", vehicleVin);
		params.put("vehicleId", vehicleId);
		params.put("reportTime", reportTime);
		params.put("faultCode", faultCode);
		super.setRequestParams(params);
	}
	
	

}
