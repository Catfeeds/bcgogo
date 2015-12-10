package com.tonggou.andclient.parse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tonggou.andclient.vo.TonggouMessage;

public class GetMessageParser extends TonggouBaseParser{

	private ArrayList<TonggouMessage> messages ;
	
	
	
	public GetMessageParser(){
		
	}
	
	@Override
	public void parsing(String dataFormServer) {
		Type listType = new TypeToken<ArrayList<TonggouMessage>>(){}.getType();
        Gson gson = new Gson();
        messages = gson.fromJson(dataFormServer, listType);
        //�õ���������users
        for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
        	TonggouMessage user = (TonggouMessage) iterator.next();
          }
		
		//�����ɹ�
        parseSuccessfull = true;
	}


	public ArrayList<TonggouMessage> getMessages() {
		return messages;
	}

}
