package com.tonggou.andclient.network.request;


/**
 * �����̨���û����еĳ���
 * <p> ����ע�������޸ģ������Ѿ���ʹ����
 * @author lwz
 *
 */
@Deprecated
public class QueryVehicleInfoSuggestionRequest extends AbsTonggouHttpGetRequest {

	@Override
	protected String getOriginApi() {
//		return API.QUERY_VEHICLE_INFO_SUGGESTION;
		return "";
	}
	
	public void setApiParams(String mobile, String vehicleNo) {
		APIQueryParam params = new APIQueryParam(false);
		params.put("mobile", mobile);
		params.put("vehicleNo", vehicleNo);
		super.setApiParams(params);
	}

}
