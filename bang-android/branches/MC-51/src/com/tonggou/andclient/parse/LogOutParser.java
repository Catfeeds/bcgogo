package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.LogOutResponse;

public class LogOutParser extends TonggouBaseParser{
	LogOutResponse logOutResponse;
	 
	public LogOutResponse getLogOutResponse() {
		return logOutResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 logOutResponse = gson.fromJson(dataFormServer, LogOutResponse.class);
			 if(logOutResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(logOutResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = logOutResponse.getMessage();
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
