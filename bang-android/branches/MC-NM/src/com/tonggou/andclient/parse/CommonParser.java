package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.CommonResponse;


public class CommonParser extends TonggouBaseParser{
	CommonResponse commonResponse;
	 
	public CommonResponse getCommonResponse() {
		return commonResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		TongGouApplication.showLog(dataFormServer);
		try{
			 Gson gson = new Gson();
			 commonResponse = gson.fromJson(dataFormServer, CommonResponse.class);
			 if(commonResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(commonResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = commonResponse.getMessage();
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

