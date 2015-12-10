package com.tonggou.andclient.network.parser;

import java.net.SocketTimeoutException;

import org.apache.http.Header;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.BaseResponse;
import com.tonggou.andclient.network.NetworkState;

/**
 * �첽�����������������
 * <p> �������첽�ģ�JSON������ͬ����
 * <p> �����Ƕ� AsyncHttpResponseHandler �����չ������� OnSuccess �����н��� JSON �������Լ� Session/Cookie �Ĵ洢
 * @author lwz
 *
 * @param <T>
 */
public abstract class AsyncJSONResponseParseHandler<T extends BaseResponse> extends AsyncHttpResponseHandler implements IJSONParseHandler<T> {
	
	private static String TAG = "AsyncJSONResponseParseHandler";
	
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		super.onFailure(arg0, arg1, arg2, arg3);
		if( arg3 != null ) {
			TongGouApplication.showLog("request onFailure @ " + arg3.getMessage());
		} else {
			TongGouApplication.showLog("request onFailure @ " + new String(arg2));
		}
		if( arg3 instanceof SocketTimeoutException ) {
			TongGouApplication.showToast(NetworkState.ERROR_CLIENT_ERROR_SOCKETTIMEOUT_MESSAGE);
		} else {
			TongGouApplication.showToast(NetworkState.ERROR_CLIENT_ERROR_TIMEOUT_MESSAGE);
		}
 	}

	@Override
	public void onFinish() {
		super.onFinish();
		TongGouApplication.showLog("request finish");
	}

	@Override
	public void onRetry() {
		super.onRetry();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] result) {
		super.onSuccess(statusCode, headers, result);
		Log.i(TAG, new String(result));
		BaseResponseParser<T> parser = new BaseResponseParser<T>(getTypeClass());
		try {
			T response = parser.parse(new String(result));
			if( response == null ) {
				onFailure(statusCode, headers, ("���ص�����" + new String(result)  + "����Ϊ null").getBytes(), null);
				return;
			}
			if( response.isSuccess() ) {
				onParseSuccess(response, result);
			} else {
				
				onParseFailure(response.getMsgCode() + "", response.getMessage());
				if( response.getMsgCode() == -202 ) {
					 // ��¼����
					TongGouApplication.getInstance().doExpireLogin();
				}
			}
			
		} catch (Exception e) {
			onParseException(e);
		}
	}

	@Override
	public void onParseSuccess(T result, byte[] originResult) {
		
	}

	@Override
	public void onParseFailure(String errorCode, String errorMsg) {
		if( !"-202".equals( errorCode ) ) {
			TongGouApplication.showToast(errorMsg);
		}
	}

	@Override
	public void onParseException(Exception e) {
		e.printStackTrace();
	}
	
	public abstract Class<T> getTypeClass();
	
}
