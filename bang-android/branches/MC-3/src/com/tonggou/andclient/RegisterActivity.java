package com.tonggou.andclient;

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.BaseResponse;
import com.tonggou.andclient.jsonresponse.VehicleInfoSuggestionResponse;
import com.tonggou.andclient.network.parser.AsyncJSONResponseParseHandler;
import com.tonggou.andclient.network.request.QueryVehicleInfoSuggestionRequest;
import com.tonggou.andclient.network.request.RegisterRequest;
import com.tonggou.andclient.util.SoftKeyboardUtil;
import com.tonggou.andclient.util.SomeUtil;

public class RegisterActivity extends AbsBackableActivity {
	
	
	private EditText mAccountNameEdit;
	private EditText mPasswordEdit;
	private EditText mPasswordAgainEdit;
	private EditText mPhoneEdit;

	private String mAccountName;
	private String mPassword;
	private String mPasswordAgain;
	private String mPhone;

	@Override
	protected int getContentLayout() {
		return R.layout.activity_register;
	}
	
	@Override
	public void onBackPressed() {
//		Intent toHome = new Intent(this, LoginActivity.class);
//		toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		startActivity(toHome);
		finish();
	}

	@Override
	protected void findViews(Bundle saveInstanceState) {
		super.findViews(saveInstanceState);
		getSimpleTitle().setTitle(R.string.register_title);
		mAccountNameEdit=(EditText) findViewById(R.id.account_name);
		mPasswordEdit=(EditText) findViewById(R.id.password);
		mPasswordAgainEdit=(EditText) findViewById(R.id.password_again);
		mPhoneEdit=(EditText) findViewById(R.id.phone_num);
		
		afterViews();
	}

	private void afterViews() {
		clearEditorTextFocus();
		setListener();
	}
	
	private void clearEditorTextFocus() {
		mPasswordEdit.clearFocus();
		mPasswordAgainEdit.clearFocus();
		mPhoneEdit.clearFocus();
	}

	private void setListener() {
		mAccountNameEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable et) {
				String s=et.toString();
			    if( SomeUtil.isMobileNo(s) ) {
			    	mPhoneEdit.setText(s);
			    }
			    mPhoneEdit.setError(null);
			}
		});
		
	}
	
//	@Override 
//	public void onFocusChange(View v, boolean hasFocus) {
//		switch (v.getId()) {
//			case R.id.account_name: isAccountNameValidate(); break;
//			case R.id.password: isPasswordValidate(); break;
//			case R.id.password_again: isPasswordAgainValidate(); break;
//			case R.id.phone_num: isPhoneValidate(); break;
//			default: break;
//		}
//	}
	
	public void onRegisterBtnClick(View view) {
		clearEditorTextFocus();
		SoftKeyboardUtil.hide(this, view.getWindowToken());
		if( validateData() ) {
			register(mAccountName, mPassword, mPhone);
		}
	}
	
	private boolean validateData() {
		return isAccountNameValidate() && isPasswordValidate() 
				&& isPasswordAgainValidate() && isPhoneValidate();
	}
	
	private boolean isAccountNameValidate() {
		boolean isValidate = true;
		mAccountName = mAccountNameEdit.getText().toString().trim();
		if( TextUtils.isEmpty(mAccountName) ){	 
			isValidate = false;
		}
		
		if(SomeUtil.isMobileNo(mAccountName)){  //isMobileNO  isNumeric
			mPhone = mAccountName;
			isValidate = true;
		} else if( SomeUtil.isNumeric(mAccountName) ) {
			mAccountNameEdit.setError("��������ȷ���ֻ��� �� ֱ���ó��ƺ�ע��");
			isValidate = false;
		} else if( !SomeUtil.isVehicleNo(mAccountName.toUpperCase(Locale.getDefault())) ) {
			mAccountNameEdit.setError("��������ȷ�ĳ��ƺ� �� ֱ�����ֻ���ע��");
			isValidate = false;
		}
		
		return isValidate;
	}
	
	private boolean isPasswordValidate() {
		mPassword = mPasswordEdit.getText().toString().trim();
		if( TextUtils.isEmpty(mPassword) ) {
			mPasswordEdit.setError("����������");
			return false;
		}
		return true;
	}
	
	private boolean isPasswordAgainValidate() {
		mPasswordAgain = mPasswordAgainEdit.getText().toString().trim();
		if( TextUtils.isEmpty(mPasswordAgain) || !mPasswordAgain.equals(mPassword) ) {
			mPasswordAgainEdit.setError("������������Ĳ�һ��");
			return false;
		}
		return true;
	}
	
	private boolean isPhoneValidate() {
		mPhone = mPhoneEdit.getText().toString().trim();
		if( TextUtils.isEmpty(mPhone) ) {
			mPhoneEdit.setError("�������ֻ���");
			return false;
		}
		
		if( !SomeUtil.isMobileNo(mPhone)) {
			mPhoneEdit.setError("��������ȷ���ֻ���");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param accountName      //�û���  ����
	 * @param password         //����       ����
	 * @param phonenumber      //�ֻ���  ����
	 */
	private void register(String accountName,String password,String phonenumber){
		showLoadingDialog("ע����...");
		RegisterRequest request = new RegisterRequest();
		request.setRequestParams(accountName, password, phonenumber);
		request.doRequest(this, new AsyncJSONResponseParseHandler<BaseResponse>() {

			@Override
			public void onParseSuccess(BaseResponse result, byte[] originResult) {
				super.onParseSuccess(result, originResult);
				onRegisterSuccess();
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
		
//		RegistrationParser registrationParser = new RegistrationParser();
//		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/user/registration";	
//
//		StringBuffer sb1 = new StringBuffer();
//		//email={email}&name={name}&shopEmployee={shopEmployee}
//		//����� �û��� �����룬�ֻ���
//		String value1 = "{\"userNo\":\""+accountName+"\", " +" \"password\":\""+password+"\", " +
//				" \"mobile\":\""+phonenumber+"\"" ;
//		//" \"mobile\":\""+phonenumber+"\",\"name\":\""+nickname+"\"" ;
//
//		sb1.append(value1);
//// Ĭ�ϲ�������
////		if(SomeUtil.isVehicleNo(accountName)){//���ƺ�
////			String valueVB = ",\"vehicleNo\":\""+accountName.toUpperCase(Locale.getDefault())+"\"" ;
////			sb1.append(valueVB);
////		}
//		sb1.append(",\"loginInfo\":{");
//
//		String platformStr = "\"platform\":\""+INFO.MOBILE_PLATFORM+"\""  ;
//		sb1.append(platformStr);
//
//		if(INFO.VERSION!=null){
//			String appVersionStr = ",\"appVersion\":\""+INFO.VERSION+"\""  ;
//			sb1.append(appVersionStr);
//		}
//		String platformVersionStr = ",\"platformVersion\":\""+INFO.MOBILE_PLATFORM_VERSION+"\""  ;
//		sb1.append(platformVersionStr);
//		String mobileModelStr = ",\"mobileModel\":\""+INFO.MOBILE_MODEL+"\""  ;
//		sb1.append(mobileModelStr);
//		String imageVersionStr = ",\"imageVersion\":\""+INFO.IMAGE_VERSION+"\""  ;
//		sb1.append(imageVersionStr);
//
//		String valueMK = "}}";
//		sb1.append(valueMK);
//
//		String resultStr1 = sb1.toString();
//
//		NetworkState ns = Network.getNetwork(RegisterActivity.this).httpPutUpdateString(url,resultStr1.getBytes(),registrationParser);
//		if(ns.isNetworkSuccess()){
//			if(registrationParser.isSuccessfull()){
//				//��ȷ�Ĵ����߼� 
//				String data = registrationParser.getRegistrationReponse().getData();
//				sendMessage(LOGIN_SUCCEED, data);
//				
////				Intent intent = new Intent(RegisterActivity.this,RegisterActivityNext.class);
////				intent.putExtra("tonggou.carNum",carNum);
//				/*intent.putExtra("tonggou.name",nameStr);
//				intent.putExtra("tonggou.password",passwordStr);
//				
//				intent.putExtra("tonggou.phonenumber",phonenumberStr);*/
////				startActivity(intent);
//			}else{
//				//��ʾ�û�����
//				String errorMsg = registrationParser.getErrorMessage();
//				sendMessage(LOGIN_FAILD, errorMsg);
//			}
//		}else{
//			//�������
//			sendMessage(LOGIN_FAILD, ns.getErrorMessage());
//		}
	}
	
	private void onRegisterSuccess() {
		sharedPreferences.edit()
			.putString(NAME, mAccountName)
			.putString(PASSWORD, mPassword)
			.putString(PHONE, mPhone)
			.commit();
		TongGouApplication.getInstance().setLogin(true);
		MainActivity.ifAutoLogin = false;
		TongGouApplication.showToast("ע�����");
		goHome();
//		requestVehicleSuggestionData();
		
//		if( TextUtils.isEmpty( data ) || "null".equalsIgnoreCase(data.trim()) ) {
//			goHome();
//		} else {
//			
//		}
	}
	
	private void requestVehicleSuggestionData() {
		showLoadingDialog("���ڼ����û�����");
		QueryVehicleInfoSuggestionRequest request = new QueryVehicleInfoSuggestionRequest();
		if( SomeUtil.isMobileNo(mAccountName) ) {
			request.setApiParams(mAccountName, null);
		} else {
			request.setApiParams(null, mAccountName);
		}
		request.doRequest(this, new AsyncJSONResponseParseHandler<VehicleInfoSuggestionResponse>() {

			@Override
			public void onParseSuccess(VehicleInfoSuggestionResponse result, byte[] originResult) {
				super.onParseSuccess(result, originResult);
				
				if( result.getResult() == null ) {
					goHome();
				} else {
				
					String vehicleNo = "";
					String brandName = "";
					String brandId = "";
					String typeName = "";
					String typeId = "";
					try {
						JSONObject jsonData = result.getResult();
						vehicleNo = jsonData.getString("vehicleNo");
						JSONObject jsonBrandTypeData = jsonData.getJSONObject("brandModel");
						brandName = jsonBrandTypeData.getString("brandName");
						brandId = jsonBrandTypeData.getString("brandId");
						typeName = jsonBrandTypeData.getString("modelName");
						typeId = jsonBrandTypeData.getString("modelId");
						
					} catch (JSONException e) {
						TongGouApplication.showToast("���ݽ���ʧ��");
						e.printStackTrace();
					}
					turnToChangeVehicleInfoActivity(vehicleNo, brandName, brandId, typeName, typeId);
				}
			}
			
			@Override
			public void onParseFailure(String errorCode, String errorMsg) {
//				super.onParseFailure(errorCode, errorMsg);
				goHome();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
				goHome();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				dismissLoadingDialog();
			}
			
			@Override
			public Class<VehicleInfoSuggestionResponse> getTypeClass() {
				return VehicleInfoSuggestionResponse.class;
			}
			
		});
		
		
//		String url = INFO.HTTP_HEAD+INFO.HOST_IP + "/vehicle/info/suggestion"; 
////		url += "/18001211000/NULL";
//		if( SomeUtil.isMobileNo(mAccountName) ) {
//			url += "/" + mAccountName +"/NULL";
//		} else {
//			url += "/NULL/" + mAccountName;
//		}
//		
//		final NetworkState ns = Network.getNetwork(this).httpGetUpdateString(url, new JSONParseInterface() {
//			
//			@Override
//			public void parsing(String dataFormServer) {
//				try {
//					TongGouApplication.showLog("ע����ѯ������Ϣ �� ---   " + dataFormServer);
//					JSONObject result = new JSONObject(dataFormServer);
//					if( "SUCCESS".equalsIgnoreCase(result.getString("status"))) {
//						if( result.has("result") && !result.isNull("result")) {
//							sendMessage(REQUEST_SUGGESSION_DATA_SUCCESS, result.getJSONObject("result").toString());
//						} else {
//							sendMessage(REQUEST_SUGGESSION_DATA_SUCCESS,"");
//						}
//						
//					} else {
//						//��ʾ�û�����
//						sendMessage(REQUEST_SUGGESSION_DATA_FAILD, result.getString("message"));
//					}
//				} catch (JSONException e) {
//					sendMessage(REQUEST_SUGGESSION_DATA_FAILD, "���ݽ���ʧ��");
//					e.printStackTrace();
//				}
//			}
//		});	
//		if( !ns.isNetworkSuccess()){
//			//�������
//			sendMessage(REQUEST_SUGGESSION_DATA_FAILD, ns.getErrorMessage());
//		}
	}
	
//	private void onSuggestionRequestSuccess(String data) {
//		TongGouApplication.showLog("���ص�����   " + data);
//		if( TextUtils.isEmpty(data) || "null".equalsIgnoreCase(data)) {
//			goHome();
//		} else {
//			String vehicleNo = "";
//			String brandName = "";
//			String brandId = "";
//			String typeName = "";
//			String typeId = "";
//			try {
//				JSONObject jsonData = new JSONObject(data);
//				vehicleNo = jsonData.getString("vehicleNo");
//				JSONObject jsonBrandTypeData = jsonData.getJSONObject("brandModel");
//				brandName = jsonBrandTypeData.getString("brandName");
//				brandId = jsonBrandTypeData.getString("brandId");
//				typeName = jsonBrandTypeData.getString("modelName");
//				typeId = jsonBrandTypeData.getString("modelId");
//				
//			} catch (JSONException e) {
//				TongGouApplication.showToast("���ݽ���ʧ��");
//				e.printStackTrace();
//			}
//			turnToChangeVehicleInfoActivity(vehicleNo, brandName, brandId, typeName, typeId);
//		}
//	}
	
	private void goHome() {
		//��ת��ҳ
		MainActivity.ifAutoLogin = true;
		if(sAllActivities!=null){
			while( !sAllActivities.isEmpty()) {
				WeakReference<Activity> reference = sAllActivities.getFirst();
				Activity activity = reference.get();
				if( activity != null && !activity.isFinishing() ) {
					activity.finish();
				}
				sAllActivities.removeFirst();
			}
		}
		
		Activity parentActivity = getParent();
		// ��ת�� HomeActivity ֮ǰ�Ƚ��� ��¼ Activity
		if( parentActivity != null ) {
			parentActivity.finish();
		}
		Intent intent = new Intent();
		intent.putExtra(HomePageActivity.EXTRA_FLAG_PREVIOUS_ACTIVITY_NAME, RegisterActivity.class.getName());
		intent.setClass(this, HomePageActivity.class);
		startActivity(intent);	
		finish();
		
	}
	
	private void turnToChangeVehicleInfoActivity(String vehicleNo, String brandName, String brandId, String typeName, String typeId) {
		Intent intent = new Intent();
		intent.setClass(this, RegisterChangeVehicleInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(RegisterChangeVehicleInfoActivity.KEY_ARG_VEHICLE_NO, vehicleNo);
		args.putString(RegisterChangeVehicleInfoActivity.KEY_ARG_BRAND_NAME, brandName);
		args.putString(RegisterChangeVehicleInfoActivity.KEY_ARG_BRAND_ID, brandId);
		args.putString(RegisterChangeVehicleInfoActivity.KEY_ARG_TYPE_NAME, typeName);
		args.putString(RegisterChangeVehicleInfoActivity.KEY_ARG_TYPE_ID, typeId);
		intent.putExtras(args);
		startActivity(intent);
		finish();
	}
}
