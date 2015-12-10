package com.tonggou.andclient.parse;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.LoginResponse;
import com.tonggou.andclient.jsonresponse.PollingMessagesResponse;
/**
 * ������ѯ��Ϣ
 * @author think
 *
 */
public class PollingMessagesParser extends TonggouBaseParser{
	PollingMessagesResponse pollingMessagesResponse;
	 
	public PollingMessagesResponse getPollingMessagesResponse() {
		return pollingMessagesResponse;
	}
	/**
	 * ����Ľ�������
	 */
	public void parsing(String dataFormServer) {
		
		try{
			 Gson gson = new Gson();
			 pollingMessagesResponse = gson.fromJson(dataFormServer, PollingMessagesResponse.class);
			 if(pollingMessagesResponse!=null){
				 if("SUCCESS".equalsIgnoreCase(pollingMessagesResponse.getStatus())){
					//ע��ɹ�
					parseSuccessfull = true;
				 }else{
					 errorMessage = pollingMessagesResponse.getMessage();
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
