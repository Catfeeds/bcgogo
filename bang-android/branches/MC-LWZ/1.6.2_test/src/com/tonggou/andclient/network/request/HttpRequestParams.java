package com.tonggou.andclient.network.request;

import java.util.HashMap;
import java.util.Set;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

/**
 * HTTP ����Ĳ���
 * 
 * <p>�̳���  HashMap&lt;String, Object&gt;</p>
 * <p>��д�� toString() ������ ����JSON</p>
 * @author lwz
 *
 */
public class HttpRequestParams extends HashMap<String, Object> {

	private static final long serialVersionUID = -3736319677853216129L;

	public static RequestParams convert2RequestParams(HttpRequestParams params) {
		if( params == null ) {
			return null;
		}
		RequestParams requestParams = new RequestParams();
		Set<Entry<String, Object>> entries = params.entrySet();
		for( Entry<String, Object> entry : entries) {
			requestParams.put(entry.getKey(), entry.getValue());
		}
		return requestParams;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
