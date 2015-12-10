package com.tonggou.andclient.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.jsonresponse.UpgradeCheckResponse;
import com.tonggou.andclient.network.parser.AsyncJSONResponseParseHandler;
import com.tonggou.andclient.network.request.UpdateCheekRequest;

/**
 * ������
 * @author lwz
 *
 */
public class DefaultUpdateCheck implements DialogInterface.OnClickListener {
	
	/**
	 * ����������
	 * @author lwz
	 *
	 */
	public class OnUpdateCheckListener {
		/**
		 * ���°汾
		 * @param isForceUpdate true��ǿ��  ; false������
		 * @param updateMesssage ������ʾ��Ϣ
		 */
		public void onShouldUpdate(boolean isForceUpdate, String updateMesssage) {
			showUpdateAlertDialog(isForceUpdate, updateMesssage);
		}
		/**
		 * û���°汾
		 */
		public void onNothingToUpdate() {
			
		}
	}
	
	private Activity mContext;
	private AlertDialog mUpdateDialog;
	private UpgradeCheckResponse mResponse;
	
	public DefaultUpdateCheck(Activity context) {
		mContext = context;
	}
	
	/**
	 * ���¶���
	 * @param listener ����Ϊ null
	 */
	public void doCheckUpdate(final OnUpdateCheckListener listener) {
		UpdateCheekRequest request = new UpdateCheekRequest();
		request.doRequest(mContext, new AsyncJSONResponseParseHandler<UpgradeCheckResponse>() {

			@Override
			public void onParseSuccess(UpgradeCheckResponse result, byte[] originResult) {
				super.onParseSuccess(result, originResult);
				mResponse = result;
				String action = result.getAction();
				if( ! "normal".equals( action ) ) {
					listener.onNothingToUpdate();
				} else {
					listener.onShouldUpdate( "force".equals(action), result.getMessage() );
				}
			}
			
			@Override
			public void onParseFailure(String errorCode, String errorMsg) {
				listener.onNothingToUpdate();
			}
			
			@Override
			public Class<UpgradeCheckResponse> getTypeClass() {
				return UpgradeCheckResponse.class;
			}
			
		});
	}
	
	private void showUpdateAlertDialog(boolean isForceUpdate, String msg) {
		dismissDoalog();
		mUpdateDialog = new AlertDialog.Builder(mContext).create();
		mUpdateDialog.setTitle(isForceUpdate ? "ǿ��������ʾ" : "������ʾ");
		mUpdateDialog.setIcon(android.R.drawable.ic_dialog_alert);
		mUpdateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "����", this);
		mUpdateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "�´���˵", (Message)null);
		mUpdateDialog.setMessage( msg );
		if( mContext != null && !mContext.isFinishing() ) {
			mUpdateDialog.show();
		} else {
			mUpdateDialog = null;
		}
	}
	
	public void dismissDoalog() {
		if( mUpdateDialog != null && mUpdateDialog.isShowing() ) {
			mUpdateDialog.dismiss();
		}
		mUpdateDialog = null;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if( which == DialogInterface.BUTTON_POSITIVE ) {
			String downUrl = mResponse.getUrl();
			if(downUrl==null||"".equals(downUrl)){
				//url������ʾ
				TongGouApplication.showToast("����������!");
			}else{
				Uri uri = Uri.parse(downUrl);  
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
				if( !mContext.isFinishing() ) {
					mContext.startActivity(intent);
				}
			}
		}
	}
}
