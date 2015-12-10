package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.UserDateSaveResponse;

public class UserDateSaveParser  extends TonggouBaseParser{
	UserDateSaveResponse userDateSaveResponse;
	 
	public UserDateSaveResponse getUserDateSaveResponse() {
		return userDateSaveResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 userDateSaveResponse = gson.fromJson(dataFormServer,UserDateSaveResponse.class);
			 if(userDateSaveResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(userDateSaveResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = userDateSaveResponse.getMessage();
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
