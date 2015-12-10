package com.tonggou.andclient.network.request;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;

import org.apache.http.client.CookieStore;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tonggou.andclient.network.SSLSocketFactoryEx;
import com.tonggou.andclient.util.PreferenceUtil;
import com.tonggou.andclient.vo.UserInfo;

public class HttpRequestClient {
//	private static final String PREF_NAME_COOKIE = "pref_cookie";
//	private static final String PREF_KEY_COOKIE = "JSESSIONID";
	
	public static final String PREF_NAME_USER_INFO = "pref_user_info";
	public static final String PREF_KEY_USER_INFO = "user_info";
	
	private static final int DEFAULT_TETRY_TIMES = 2;			// �������Դ���
	private static final int DEFAULT_TIMEOUT = 20000;			// ���ӳ�ʱʱ��
	private static final int DEFAULT_MAX_CONNECTIONS = 10;		// ���������
	
	private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
	
	private Context mContext;
	
	public HttpRequestClient(Context context) {
		mContext = context;
	}
	
	/**
	 * GET ����
	 * @param url				����� URL
	 * @param params			��������Ϊ null
	 * @param responseHandler	��Ӧ������
	 */
	public void get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		AsyncHttpClient client = getDefaultClient(mContext);
		client.addHeader(HEADER_KEY_CONTENT_TYPE, "application/json;charset=UTF-8");
		client.get(mContext, url, params, responseHandler);
	}
	
	/**
	 * POST ����
	 * @param url				����� URL
	 * @param params			��������Ϊ null
	 * @param responseHandler	��Ӧ������
	 */
	public void post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		AsyncHttpClient client = getDefaultClient(mContext);
		client.addHeader(HEADER_KEY_CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
		client.post(mContext, url, params, responseHandler);
	}
	
	/**
	 * PUT ����
	 * @param url				����� URL
	 * @param params			��������Ϊ null
	 * @param responseHandler	��Ӧ������
	 */
	public void put(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		AsyncHttpClient client = getDefaultClient(mContext);
		client.addHeader(HEADER_KEY_CONTENT_TYPE, "application/json;charset=UTF-8");
		client.put(mContext, url, params, responseHandler);
	}
	
	/**
	 * DELETE ����
	 * @param url				����� URL
	 * @param params			��������Ϊ null
	 * @param responseHandler	��Ӧ������
	 */
	public void delete(String url, ResponseHandlerInterface responseHandler) {
		AsyncHttpClient client = getDefaultClient(mContext);
		client.addHeader(HEADER_KEY_CONTENT_TYPE, "application/json;charset=UTF-8");
		client.delete(mContext, url, responseHandler);
	}
	
	private static AsyncHttpClient msAsyncHttpClient;
	
	public AsyncHttpClient getDefaultClient(Context context) {
		if( msAsyncHttpClient != null ) {
			return msAsyncHttpClient;
		}
		AsyncHttpClient client = new AsyncHttpClient(80, 443);
		Locale locale = Locale.getDefault();
		client.addHeader("Accept-Language", (locale.getLanguage()+"-"+locale.getCountry()).toLowerCase(locale));
		client.addHeader("Accept-Encoding", "gzip");
		
		// Basic Auth
		String userInfoStr = PreferenceUtil.getString(context, PREF_NAME_USER_INFO, PREF_KEY_USER_INFO);
		if( !TextUtils.isEmpty(userInfoStr) ) {
			UserInfo user = new Gson().fromJson(userInfoStr, UserInfo.class);
			if( user != null && !TextUtils.isEmpty(user.getUserNo()) && !TextUtils.isEmpty(user.getPassword())) {
				client.setBasicAuth(user.getUserNo(), user.getPassword());
			}
		}
		
		client.setTimeout(DEFAULT_TIMEOUT);
		client.setMaxRetriesAndTimeout(DEFAULT_TETRY_TIMES, DEFAULT_TIMEOUT);
		client.setMaxConnections(DEFAULT_MAX_CONNECTIONS);
		client.setCookieStore( getCookieStore(context) );
		client.setSSLSocketFactory( getSSLSocketFactory() );
		return client;
	}
	
	private CookieStore getCookieStore(Context context) {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
//		BasicClientCookie newCookie = new BasicClientCookie("JSESSIONID", restoreSessionId(context));
//		myCookieStore.addCookie( newCookie );
		return myCookieStore;
	}
	
	private SSLSocketFactory getSSLSocketFactory() {
		SSLSocketFactory sf = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);        
			sf = new SSLSocketFactoryEx(trustStore);  
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return sf;
	}
	
//	/**
//	 * �洢 sessionId
//	 * @param context
//	 * @param sessionId
//	 */
//	public static void storeSessionId(Context context, String sessionId) {
//		PreferenceUtil.putString(context, PREF_NAME_COOKIE, PREF_KEY_COOKIE, sessionId);
//	}
//	
//	/**
//	 * ��ȡ ���ش洢�� sessionId
//	 * @param context
//	 * @return
//	 */
//	private String restoreSessionId(Context context) {
//		return PreferenceUtil.getString(context, PREF_NAME_COOKIE, PREF_KEY_COOKIE);
//	}
	
	/**
	 * �� ����API �ϼ��ϲ�������� API
	 * @param api		���� API
	 * @param params	����
	 * @return
	 */
	public static String getAPIWithQueryParams(String api, APIQueryParam params) {
		api = api.trim();
		if( api.endsWith("/") ) {
			api = api.substring(0, api.length() - 1);
		}
		return api + params.toString();
	}
	
}
