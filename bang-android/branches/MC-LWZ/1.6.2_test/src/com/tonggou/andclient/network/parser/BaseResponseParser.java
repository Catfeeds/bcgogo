package com.tonggou.andclient.network.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tonggou.andclient.jsonresponse.BaseResponse;

/**
 * ����������
 * @author lwz
 *
 * @param &lt;T extends BaseResponse&gt; ������Ŀ����
 */
public class BaseResponseParser<T extends BaseResponse> implements IResponseParser<T>{

	private Class<T> classOfT;
	
	/**
	 * 
	 * @param classOfT ���͵���
	 */
	public BaseResponseParser(Class<T> classOfT) {
		this.classOfT = classOfT;
	}
	
	/**
	 * �����ڲ��� Gson ʵ��
	 */
	@Override
	public T parse(String jsonData) {
		if( TextUtils.isEmpty(jsonData) ) {
			return null;
		}
		return new Gson().fromJson(jsonData, classOfT);
	}

	
}
