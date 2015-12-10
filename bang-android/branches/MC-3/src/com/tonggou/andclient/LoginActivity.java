package com.tonggou.andclient;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.BaseResponse;
import com.tonggou.andclient.jsonresponse.LoginResponse;
import com.tonggou.andclient.network.API;
import com.tonggou.andclient.network.parser.AsyncJSONResponseParseHandler;
import com.tonggou.andclient.network.request.APIQueryParam;
import com.tonggou.andclient.network.request.HttpRequestClient;
import com.tonggou.andclient.network.request.LoginRequest;

public class LoginActivity extends BaseActivity {
	
	public static final String ARG_KEY_LOGIN_EXPIRE = "tonggou.loginExpire";
	public static final String ARG_KEY_IS_LOGOUT = "tonggou.is_logout";

	private EditText mUserNameEdit;
	private EditText mUserPwdEdit;
	private AlertDialog mSearchPwdDoalog;
	private boolean isLoginExpress = false; 
	private boolean isLogout = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mUserNameEdit=(EditText) findViewById(R.id.loginname);
		mUserPwdEdit=(EditText) findViewById(R.id.loginpassword);
		
		afterViews();
	}
	
	private void afterViews() {
		final String userSave = sharedPreferences.getString(BaseActivity.NAME, "");
		final String passSave = sharedPreferences.getString(BaseActivity.PASSWORD, "");
		
		mUserNameEdit.setText(TextUtils.isEmpty(userSave) ? "" : userSave);
		mUserPwdEdit.setText( TextUtils.isEmpty(passSave) ? "" : passSave );
		
		Intent intent = getIntent();
		if( intent != null ) {
			if( intent.hasExtra(ARG_KEY_LOGIN_EXPIRE) ) {
				isLoginExpress = true;
		        	//��¼������ʾ
	        	showErrorMessageDialog("��¼���ڣ������µ�¼");
	        	TongGouApplication.getInstance().setLogin(false);
			} else {
				isLoginExpress = false;
			}
			isLogout = intent.getBooleanExtra(ARG_KEY_IS_LOGOUT, false);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if( isLoginExpress ) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onLoginButtonClick(View view) {
		String userName = mUserNameEdit.getText().toString().trim();
		String userPwd = mUserPwdEdit.getText().toString().trim();
		if( TextUtils.isEmpty(userName) ){ 
			TongGouApplication.showToast( getString(R.string.login_name_null) );
			return;
		}
		
		if( TextUtils.isEmpty(userPwd) ) {
			TongGouApplication.showToast( getString(R.string.login_password_null) );
			return;
		}
		
		doLogin(userName, userPwd);
	}
	
	public void onRegisterButtonClick(View view) {
		Intent toRegister = new Intent(LoginActivity.this,RegisterActivity.class);
		// Ϊ�˵�ע��ɹ���Ҳfinish �� activity,���� startActivityForResult()
		startActivityForResult(toRegister, 0x111);
	}
	
	public void onForgetPwdButtonClick(View view) {
		mSearchPwdDoalog= new AlertDialog.Builder(LoginActivity.this).create();
		mSearchPwdDoalog.setView((LoginActivity.this).getLayoutInflater().inflate(R.layout.searchpasswordback, null));
		mSearchPwdDoalog.show();			
		LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
		View textEntryView = inflater.inflate(R.layout.searchpasswordback, null);
		Window window = mSearchPwdDoalog.getWindow();
		window.setContentView(textEntryView);

		final EditText searchpasswordEdit=(EditText) window.findViewById(R.id.searchpasswordname);
		TextView searchpassword=(TextView) window.findViewById(R.id.searchpasswordok);
		searchpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String searchpassword_name=searchpasswordEdit.getText().toString();
				if(searchpassword_name!=null&&!"".equals(searchpassword_name)){
					doSearchPwd(searchpassword_name);
					mSearchPwdDoalog.dismiss();
				}else{
					Toast.makeText(LoginActivity.this,getString(R.string.searchpassword_name), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void doLogin(final String username, final String password){
		showLoadingDialog("��¼��...");
		LoginRequest request = new LoginRequest();
		request.setRequestParams(username, password);
		TongGouApplication.showLog(username + "  " + password);
		request.doRequest(this, new AsyncJSONResponseParseHandler<LoginResponse>() {
			
			@Override
			public void onParseSuccess(LoginResponse result, byte[] originResult) {
				super.onParseSuccess(result, originResult);
				TongGouApplication.getInstance()
					.saveSomeInformation(result,sharedPreferences, username, password);
				goToHomePage();
			}
			
			@Override
			public void onParseFailure(String errorCode, String errorMsg) {
				super.onParseFailure(errorCode, errorMsg);
				showErrorMessageDialog(errorMsg);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				dismissLoadingDialog();
			}

			@Override
			public Class<LoginResponse> getTypeClass() {
				return LoginResponse.class;
			}
			
		});
	}

	private void goToHomePage() {	
		//��ת��ҳ
		MainActivity.ifAutoLogin = false;
		Intent toHome = new Intent(LoginActivity.this,HomePageActivity.class);
		toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(toHome);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		goToHomePage();
	}

	private void doSearchPwd(final String userName) {
		showLoadingDialog("���Ժ�...");
		HttpRequestClient client = new HttpRequestClient(this);
		APIQueryParam params = new APIQueryParam(true);
		params.put("userNo", userName);
		final String findPwdUrl = HttpRequestClient.getAPIWithQueryParams(API.FIND_PASSWORD, params);
		client.get( findPwdUrl , null, new AsyncJSONResponseParseHandler<BaseResponse>() {

			@Override
			public void onParseFailure(String errorCode, String errorMsg) {
				showErrorMessageDialog(errorMsg);
			}
			
			@Override
			public void onParseSuccess(BaseResponse result, byte[] originResult) {
				showSuccessMessageDialog( result.getMessage() );
			}
			
			@Override
			public Class<BaseResponse> getTypeClass() {
				return BaseResponse.class;
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				dismissLoadingDialog();
			}
		});

	}

	public void onGuestModeButtonClick(View view) {
		TongGouApplication.getInstance().setLogin(false);
		if( isLoginExpress || isLogout) {
			goToHomePage();
		} else {
			// �ص�ԭ������
			finish();
		} 
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if( mSearchPwdDoalog != null && mSearchPwdDoalog.isShowing() ) {
			mSearchPwdDoalog.dismiss();
		}
		mSearchPwdDoalog = null;
	}
	
}

