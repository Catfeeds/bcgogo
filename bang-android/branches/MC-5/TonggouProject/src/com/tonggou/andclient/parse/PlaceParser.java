package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.PlaceResponse;

public class PlaceParser extends TonggouBaseParser{
	PlaceResponse placeResponse;
	 
	public PlaceResponse getPlaceResponse() {
		return placeResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 placeResponse = gson.fromJson(dataFormServer, PlaceResponse.class);
			 if(placeResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(placeResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = placeResponse.getMessage();
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

