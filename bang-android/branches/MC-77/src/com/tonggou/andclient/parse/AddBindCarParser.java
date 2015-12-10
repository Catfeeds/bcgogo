package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.AddBindCarResponse;

public class AddBindCarParser extends TonggouBaseParser{
	AddBindCarResponse addBindCarReponse;
	 
	public AddBindCarResponse getAddBindCarResponse() {
		return addBindCarReponse;
	}

	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		try{
			 Gson gson = new Gson();
			 addBindCarReponse = gson.fromJson(dataFormServer, AddBindCarResponse.class);
			 if(addBindCarReponse!=null){
				 if("SUCCESS".equalsIgnoreCase(addBindCarReponse.getStatus())){
					 //ע��ɹ�
					 parseSuccessfull = true;
				 }else{
					 errorMessage = addBindCarReponse.getMessage();
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
