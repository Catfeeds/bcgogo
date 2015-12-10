package com.tonggou.andclient;



import java.lang.ref.WeakReference;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.LoginResponse;
import com.tonggou.andclient.network.parser.AsyncLoadCacheJsonBaseResponseParseHandler;
import com.tonggou.andclient.network.request.LoginRequest;


public class PreLoginActivity extends BaseActivity {

	private final int SKIP_TIME = 3000;		// ��ת
	
	private TimeHandler mTimeHandler;
	private boolean mIsLoginRequestFinished = false;
	private boolean mIsSkipTimeout = false;
	private Class<?> mTargetActivityClass = LoginActivity.class;

	static class TimeHandler extends Handler {
		private WeakReference<PreLoginActivity> reference;
		
		public TimeHandler(PreLoginActivity context) {
			reference = new WeakReference<PreLoginActivity>(context);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if( null != reference && reference.get() != null) {
				reference.get().onHandleMessage(msg);
			}
		}
	}
	
	private void onHandleMessage(Message msg) {
		mIsSkipTimeout = true;
		toNextActivity();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prelogin);		
		
		//��������
		Intent intentSer = new Intent(this, com.tonggou.andclient.app.TongGouService.class);
        startService(intentSer);
		
        mTimeHandler = new TimeHandler(this);
        mTimeHandler.sendEmptyMessageDelayed(0, SKIP_TIME);
        
        autoLogin();
	}
	
	private void autoLogin() {
		if( "NULL".equals( currentUsername ) || "NULL".equals(currentPwd) 
				|| TextUtils.isEmpty(currentUsername) || TextUtils.isEmpty(currentPwd)) {
			mIsLoginRequestFinished = true;
			MainActivity.ifAutoLogin = false;
			// �ο�ģʽ
			mTargetActivityClass = HomePageActivity.class;
//			TongGouApplication.showToast("�ο����");
		} else {
			LoginRequest request = new LoginRequest();
			request.setRequestParams(currentUsername, currentPwd);
			request.doRequest(this, new AsyncLoadCacheJsonBaseResponseParseHandler<LoginResponse>() {

				@Override
				public void onParseSuccess(LoginResponse result, String originResult) {
					super.onParseSuccess(result, originResult);
					TongGouApplication.getInstance().saveSomeInformation(
							PreLoginActivity.this, result, sharedPreferences, currentUsername, currentPwd);
					MainActivity.ifAutoLogin = true;
					// �û��Զ���¼
					mTargetActivityClass = HomePageActivity.class;
//					TongGouApplication.showToast(currentUsername + " ���");
				}
				
				@Override
				public void onParseFailure(String errorCode, String errorMsg) {
					MainActivity.ifAutoLogin = false;
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					MainActivity.ifAutoLogin = false;
				}
				
				@Override
				public void onFinish() {
					super.onFinish();
					mIsLoginRequestFinished = true;
					toNextActivity();
				}
				
				@Override
				public Class<LoginResponse> getTypeClass() {
					return LoginResponse.class;
				}

				@Override
				public void onLoadCache(LoginResponse result, String originResult, boolean isNetworkConnected) {
					if( !isNetworkConnected && result != null) {
						onParseSuccess(result, originResult);
					}
				}

			});
		}
		
		
	}
	
//	@delete by lwz
//	@reason ֱ���� MainActivity �м�⼴��
//	private void updateCheck() {
//		final UpdateCheck chk = new DefaultUpdateCheck(this, false);      //�������ͨѶ
//		new Thread(){
//			public void run(){
//			    ////�������ͨѶ				
//				chk.checkUpgradeAction();
//			}
//		}.start();
//	}
	
	private synchronized void toNextActivity() {
		if( mIsLoginRequestFinished && mIsSkipTimeout) {
			Intent intent = new Intent();
			intent.setClass(this, mTargetActivityClass);
			startActivity(intent);
			finish();
		}
	}

}
