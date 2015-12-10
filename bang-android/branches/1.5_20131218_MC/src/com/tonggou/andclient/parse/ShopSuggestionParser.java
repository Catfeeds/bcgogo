package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.ShopSuggestionResponse;

public class ShopSuggestionParser extends TonggouBaseParser{
	ShopSuggestionResponse shopSuggestionResponse;
	 
	public ShopSuggestionResponse getShopSuggestionResponse() {
		return shopSuggestionResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 shopSuggestionResponse = gson.fromJson(dataFormServer, ShopSuggestionResponse.class);
			 if(shopSuggestionResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(shopSuggestionResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = shopSuggestionResponse.getMessage();
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
