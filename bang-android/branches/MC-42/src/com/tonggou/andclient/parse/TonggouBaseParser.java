package com.tonggou.andclient.parse;

/**
 * �����Ļ���,���Խ����඼Ӧ�̳���
 * @author think
 *
 */
public class TonggouBaseParser implements JSONParseInterface{
	 public boolean parseSuccessfull = false;    //�����Ƿ�ɹ�
	  public String errorMessage = "";           //������ʾ
	
	@Override
	public void parsing(String dataFormServer) {
		// TODO Auto-generated method stub
	}

	public boolean isSuccessfull() {
		return parseSuccessfull;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
