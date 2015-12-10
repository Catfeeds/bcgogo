package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.BrandModelResponse;
import com.tonggou.andclient.jsonresponse.VehicleListResponse;

public class VehicleListParser extends TonggouBaseParser{
	VehicleListResponse vehicleListResponse;
	 
	public VehicleListResponse getVehicleListResponse() {
		return vehicleListResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 vehicleListResponse = gson.fromJson(dataFormServer, VehicleListResponse.class);
			 if(vehicleListResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(vehicleListResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = vehicleListResponse.getMessage();
					 parseSuccessfull = false;
				 }
			 }else{
				 parseSuccessfull = false;
			 }
		}catch(Exception ex){
			 parseSuccessfull = false;
		}
	}
}
