package com.tonggou.andclient.network.parser;

/**
 * ���ر������ݴ������ӿ�
 * @author lwz
 *
 * @param <T>
 */
public interface ILoadCacheHandler<T> {

	/**
	 * ���ر�������
	 * <p>������û�л������ݣ��򲻻���ô˷���</p>
	 * 
	 * @param result
	 */
	public void onLoadCache(T result, String originResult, boolean isNetworkConnected);
	
}
