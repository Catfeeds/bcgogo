package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.SetCheckResponse;

public class SetCheckParser  extends TonggouBaseParser{
	SetCheckResponse setCheckResponse;
	 
	public SetCheckResponse getSetCheckResponse() {
		return setCheckResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 setCheckResponse = gson.fromJson(dataFormServer, SetCheckResponse.class);
			 if(setCheckResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(setCheckResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = setCheckResponse.getMessage();
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
