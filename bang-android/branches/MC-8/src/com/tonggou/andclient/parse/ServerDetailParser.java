package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.ServerDetailResponse;


/**
 * �������������
 * @author think
 *
 */
public class ServerDetailParser extends TonggouBaseParser{
	ServerDetailResponse serverDetailReponse;
	 
	public ServerDetailResponse getServerDetailReponse() {
		return serverDetailReponse;
	}

	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		try{
			 Gson gson = new Gson();
			 serverDetailReponse = gson.fromJson(dataFormServer, ServerDetailResponse.class);
			 if(serverDetailReponse!=null){
				 if("SUCCESS".equalsIgnoreCase(serverDetailReponse.getStatus())){
					 //ע��ɹ�
					 parseSuccessfull = true;
				 }else{
					 errorMessage = serverDetailReponse.getMessage();
					 parseSuccessfull = false;
				 }
			 }else{
				 parseSuccessfull = false;
			 }
		}catch(Exception ex){
			 errorMessage = "��������";
			 parseSuccessfull = false;
		}
		
	}
}
