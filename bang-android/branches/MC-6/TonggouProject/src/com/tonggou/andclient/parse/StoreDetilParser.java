package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.BrandModelResponse;
import com.tonggou.andclient.jsonresponse.StoreDetilResponse;

public class StoreDetilParser extends TonggouBaseParser{
	StoreDetilResponse storeDetilResponse;
	 
	public StoreDetilResponse getStoreDetilResponse() {
		return storeDetilResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 dataFormServer = dataFormServer.replace("memberInfo\":\"\"", "memberInfo\":null");
			 storeDetilResponse = gson.fromJson(dataFormServer, StoreDetilResponse.class);
			 if(storeDetilResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(storeDetilResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = storeDetilResponse.getMessage();
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
