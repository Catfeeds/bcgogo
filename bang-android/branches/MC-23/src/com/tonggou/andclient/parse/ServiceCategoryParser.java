package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.PlaceResponse;
import com.tonggou.andclient.jsonresponse.ServiceCategoryResponse;
/**
 * ȡ���������б������
 * @author think
 *
 */
public class ServiceCategoryParser extends TonggouBaseParser{
	ServiceCategoryResponse serviceCategoryResponse;
	 
	public ServiceCategoryResponse getPlaceResponse() {
		return serviceCategoryResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 serviceCategoryResponse = gson.fromJson(dataFormServer, ServiceCategoryResponse.class);
			 if(serviceCategoryResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(serviceCategoryResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = serviceCategoryResponse.getMessage();
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

