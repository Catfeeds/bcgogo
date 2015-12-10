package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.DeleteVehicleResponse;
import com.tonggou.andclient.jsonresponse.VehicleListResponse;

public class DeleteVehicleParser extends TonggouBaseParser{
	DeleteVehicleResponse deleteVehicleResponse;
	 
	public DeleteVehicleResponse getDeleteVehicleResponse() {
		return deleteVehicleResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 deleteVehicleResponse = gson.fromJson(dataFormServer, DeleteVehicleResponse.class);
			 if(deleteVehicleResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(deleteVehicleResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = deleteVehicleResponse.getMessage();
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
