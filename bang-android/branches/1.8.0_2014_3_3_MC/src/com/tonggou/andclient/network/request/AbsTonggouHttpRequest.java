package com.tonggou.andclient.network.request;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.db.LocalCache;
import com.tonggou.andclient.db.LocalCacheDao;
import com.tonggou.andclient.network.parser.AsyncLoadCacheJsonResponseParseHandler;
import com.tonggou.andclient.util.NetworkUtil;

/**
 * ���е�����Ӧ�ü̳д˷���
 * @author lwz
 *
 */
public abstract class AbsTonggouHttpRequest implements ITonggouHttpRequest {
	
	private boolean isGuestMode = false;
	private HttpRequestParams requestParams = null;
	private HttpRequestParams guestRequestParams = null;
	
	/**
	 * Ĭ��Ϊ ��¼��Աģʽ����Ҫ�����ο�ģʽ����ʹ�� {@link #AbsTonggouHttpRequest(boolean)}
	 */
	public AbsTonggouHttpRequest() {
	}
	
	/**
	 * @param isGuestMode 
	 * 				�Ƿ�Ϊ �ο�ģʽ  true �ο�ģʽ��false ��¼��Աģʽ
	 */
	public AbsTonggouHttpRequest(boolean isGuestMode) {
		this.isGuestMode  = isGuestMode;
	}
	
	public void setGuestMode(boolean isGuestMode) {
		this.isGuestMode = isGuestMode;
	}
	
	@Override
	public abstract String getAPI();
	
	@Override
	public abstract HttpMethod getHttpMethod();
	
	public void setRequestParams(HttpRequestParams params) {
		requestParams = params;
	}
	
	public void setGuestRequestParams(HttpRequestParams params) {
		guestRequestParams = params;
	}
	
	@Override
	public HttpRequestParams getRequestParams() {
		return requestParams;
	}
	
	@Override
	public HttpRequestParams getGuestRequestParams() {
		return guestRequestParams;
	}
	
	@Override
	public boolean isGuestMode() {
		return isGuestMode;
	}
	
	/**
	 * ��ʼ��������
	 * @param context			������
	 * @param responseHandler	��Ӧ���������
	 */
	public void doRequest(Context context, final AsyncHttpResponseHandler responseHandler) {
		String api = getAPI();
		HttpRequestParams params = isGuestMode() ? getGuestRequestParams() : getRequestParams();
		loadCahceCallback(getCacheKey(api, getHttpMethod(), params), responseHandler);
		
		if( !NetworkUtil.isNetworkConnected(context) ) {
			TongGouApplication.showLog("network disable");
			responseHandler.onFinish();
			return;
		}
		HttpRequestClient client = new HttpRequestClient(context);
		switch (getHttpMethod()) {
			case GET: client.get(api, params, responseHandler); break;
			case POST: client.post(api, params, responseHandler); break;
			case DELETE: client.delete(api, responseHandler); break;
			case PUT: client.put(api, params, responseHandler); break;
			default: break;
		}
		
		TongGouApplication.showLog("API ----- @ " + api);
		TongGouApplication.showLog("param ----- @ " + params);
	}
	
	/**
	 * ���ػ������ݻص�
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadCahceCallback(String cacheKey, ResponseHandlerInterface responseHandler) {
		if( responseHandler instanceof AsyncLoadCacheJsonResponseParseHandler ) {
			AsyncLoadCacheJsonResponseParseHandler handler = (AsyncLoadCacheJsonResponseParseHandler) responseHandler;
			if( !handler.isCache() ) {
				return;
			}
			String customCacheKey = handler.getCacheKey();
			// �����Զ����˴洢�������ݵļ�ֵ����ô��ʹ����
			if( !TextUtils.isEmpty(customCacheKey) ) {
				cacheKey = customCacheKey;
			}
			// �������ã���Ϊ��������ɹ���ʹ����ȷ�� cacheKey ���洢��������
			handler.setCacheKey(cacheKey);
			String cacheStr = restoreCacheData(cacheKey, handler.getUserNo() );
			handler.onLoadCache( 
					handler.getJsonResponseParser().parse( cacheStr ),
					cacheStr,
					NetworkUtil.isNetworkConnected(TongGouApplication.getInstance()));
		}
	}
	
	/**
	 *	ȡ���洢���� 
	 * @param cacheKey
	 * @param userNo
	 * @return
	 */
	private String restoreCacheData( String cacheKey, String userNo ) {
		LocalCache cache = LocalCacheDao.getCache(userNo, cacheKey);
		return LocalCacheDao.isEmpty(cache) ? "": cache.content;
	}
	
	/**
	 * ����ĸ�ʽΪ  httpUrl@httpMethod@paramsStr
	 * @param url
	 * @param requestMethod
	 * @param params
	 * @return
	 */
	private String getCacheKey(String url, HttpMethod requestMethod, HttpRequestParams params) {
        StringBuffer sb = new StringBuffer();
		if( ! TextUtils.isEmpty(url) ) {
			sb.append( url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+") );
			sb.append("@");
        }
		sb.append( String.valueOf( requestMethod ));
		if( params != null ) {
			sb.append("@").append( params.toString() );
		}
		return sb.toString();
    }
	
}
