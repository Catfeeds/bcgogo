package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.LoginResponse;
import com.tonggou.andclient.jsonresponse.UserDateResponse;

public class UserDateParser extends TonggouBaseParser{
	UserDateResponse userDateResponse;
	 
	public UserDateResponse getUserDateResponse() {
		return userDateResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 userDateResponse = gson.fromJson(dataFormServer, UserDateResponse.class);
			 if(userDateResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(userDateResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = userDateResponse.getMessage();
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
