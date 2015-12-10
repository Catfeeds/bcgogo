package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.UserDataChangePasswordResponse;

public class UserDataChangePasswordParser extends TonggouBaseParser{
	UserDataChangePasswordResponse userDataChangePasswordResponse;
	 
	public UserDataChangePasswordResponse getUserDataChangePasswordResponse() {
		return userDataChangePasswordResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 userDataChangePasswordResponse = gson.fromJson(dataFormServer, UserDataChangePasswordResponse.class);
			 if(userDataChangePasswordResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(userDataChangePasswordResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = userDataChangePasswordResponse.getMessage();
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
