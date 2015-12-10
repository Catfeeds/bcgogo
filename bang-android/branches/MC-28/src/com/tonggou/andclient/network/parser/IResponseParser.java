package com.tonggou.andclient.network.parser;

/**
 * ��������Ӧ����������ӿ�
 * @author lwz
 *
 * @param &lt;T&gt; ������Ŀ����
 */
public interface IResponseParser<T> {
	
	/**
	 * ��������
	 * @param jsonData		 Ҫ����������
	 * @return T ������Ŀ����
	 */
	public T parse(String jsonData);
	
}
