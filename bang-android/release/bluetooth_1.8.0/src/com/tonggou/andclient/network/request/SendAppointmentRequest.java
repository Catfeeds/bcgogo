package com.tonggou.andclient.network.request;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tonggou.andclient.network.API;

/**
 * ��������ԤԼ����
 * @author lwz
 *
 */
public class SendAppointmentRequest extends AbsTonggouHttpRequest {

	@Override
	public String getAPI() {
		return API.SEND_APPOINTMENT;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PUT;
	}
	
	/**
	 * 
	 * @param shopId			ԤԼ�ĵ��� ID
	 * @param serviceCategoryId	ԤԼ�ķ��� ID
	 * @param mobile	ԤԼ�˵绰
	 * @param contact	ԤԼ������
	 * @param userNo	�û���
	 * @param appointTime ԤԼʱ��
	 * @param vehicleNo	���ƺ�
	 * @param vehicleVin ���ܺ�
	 * @param vehicleBrand ��Ʒ��
	 * @param vehicleBrandId Ʒ�� ID
	 * @param vehicleModel	����
	 * @param vehicleModelId ���� ID
	 * @param remark ��ע
	 * @param faultInfoItems	������Ϣ����
	 */
	public void setRequestParams(String shopId, String serviceCategoryId, 
			String mobile, String contact, String userNo, long appointTime,
			String vehicleNo, String vehicleVin, String vehicleBrand, 
			String vehicleBrandId, String vehicleModel,  String vehicleModelId, 
			String remark, List<Map<String, String>> faultInfoItems) {
		HttpRequestParams params = new HttpRequestParams();
		params.put("shopId", shopId);
		params.put("serviceCategoryId", serviceCategoryId);
		params.put("appointTime", appointTime);
		params.put("mobile", mobile);
		params.put("contact", contact);
		params.put("userNo", userNo);
		params.put("vehicleNo", vehicleNo);
		params.put("vehicleVin", vehicleVin);
		params.put("vehicleBrand", vehicleBrand);
		params.put("vehicleBrandId", vehicleBrandId);
		params.put("vehicleModel", vehicleModel);
		params.put("vehicleModelId", vehicleModelId);
		params.put("remark", remark);
		params.put("faultInfoItems", faultInfoItems == null 
				|| faultInfoItems.isEmpty() ? null : new Gson().toJsonTree(faultInfoItems));
		super.setRequestParams(params);
	}

}
