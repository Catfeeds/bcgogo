package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.LoginResponse;

public class LoginParser extends TonggouBaseParser{
	LoginResponse loginResponse;
	 
	public LoginResponse getLoginResponse() {
		return loginResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 loginResponse = gson.fromJson(dataFormServer, LoginResponse.class);
			 if(loginResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(loginResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = loginResponse.getMessage();
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
