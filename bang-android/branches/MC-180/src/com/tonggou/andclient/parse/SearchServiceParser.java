package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.SearchServiceResponse;

/**
 * ��ѯ���������
 * @author think
 *
 */
public class SearchServiceParser extends TonggouBaseParser{
	SearchServiceResponse searchServiceResponse;
	 
	public SearchServiceResponse getStoreQueryResponse() {
		return searchServiceResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 searchServiceResponse = gson.fromJson(dataFormServer, SearchServiceResponse.class);
			 if(searchServiceResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(searchServiceResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = searchServiceResponse.getMessage();
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
