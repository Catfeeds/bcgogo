package com.tonggou.andclient.network.parser;


/**
 * JSON ��������ӿ�
 * @author lwz
 *
 * @param <T>
 */
public interface IJSONParseHandler<T> {
	
	/**
	 * �����ɹ�
	 * <p> �� status = SUCCESS ʱ�����ôη���
	 * @param result
	 * @param responseBody ���������ص�ԭʼ���
	 */
	public void onParseSuccess(T result, String responseBody);
	
	/**
	 * ����ʧ��
	 * <p> �� status = FAIL ʱ�����ô˷���
	 * @param errorCode	����ԭ��
	 * @param errorMsg	��������
	 */
	public void onParseFailure(String errorCode, String errorMsg);
	
	/**
	 * �����쳣
	 * @param e
	 */
	public void onParseException(Exception e);
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public IResponseParser<T> getJsonResponseParser();
	
}
