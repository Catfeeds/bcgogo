package com.tonggou.andclient.network;

import java.net.URLEncoder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tonggou.andclient.PreLoginActivity;
import com.tonggou.andclient.R;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.parse.UpgradeCheckParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.vo.Version;

public class DefaultUpdateCheck extends UpdateCheck {
	private boolean needToAlertMessage = true;      //�Ƿ���Ҫ��ʾ�û����汾�Ľ��
	public static Version versionAction ;
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch( msg.what ) {
//				default:
//					Toast.makeText(context, (CharSequence)msg.obj, Toast.LENGTH_LONG).show();
//					break;
//			}
//		}
//	};
	
	public DefaultUpdateCheck(Context context,boolean needToAlertMessage) {
		super(context);
		this.needToAlertMessage = needToAlertMessage;
	}
	
	/***
	 * �ֶ������£��и���ʱֻ�ǽ�����ʾ��������Ӱ����������
	 */
	/**
	 * false ʧ��
	 * true  �ɹ�
	 */
	public boolean checkUpgradeAction() {
		UpgradeCheckParser upgradeCheck = sendUpdateCheckRequest();
		if(!upgradeCheck.isSuccessfull()){
			return false;
		}
	
		
		Version version = upgradeCheck.getVersion();	
		versionAction = version;

//		switch(version.getAction()){
//			case Version.UPDATE_ACTION_NORMAL:
//				if(needToAlertMessage){
//					sendMessage(-1, context.getString(R.string.no_update));
//				}
//				
//				break;
//			case Version.UPDAATE_ACTION_ALERT:
//			case Version.UPDATE_ACTION_MINOR:
//				if(needToAlertMessage){
//					sendMessage(-1, version.getMessage());
//				}
//				
//				break;
//			case Version.UPDATE_ACTION_FORCE:
//
//				if(version.getMessage() != null){
//					if(needToAlertMessage){
//						sendMessage(-1, version.getMessage());
//					}
//				}else{
//					if(needToAlertMessage){
//						sendMessage(-1, context.getString(R.string.notice_update));
//					}
//				}
//				
//				break;
//			default:
//				break;
//		}
		return true;
	}
	
//	protected void sendMessage(int what, CharSequence obj) {
//		Message msg = handler.obtainMessage(what, obj);
//		handler.sendMessage(msg);
//	}
	
	protected UpgradeCheckParser sendUpdateCheckRequest() {
		UpgradeCheckParser upgradeCheck = new UpgradeCheckParser();
		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/newVersion/platform/"+INFO.MOBILE_PLATFORM+"/appVersion/"+INFO.VERSION+"/platformVersion/"+URLEncoder.encode(INFO.MOBILE_PLATFORM_VERSION)+"/mobileModel/"+URLEncoder.encode(INFO.MOBILE_MODEL);
		Network.getNetwork(context).httpGetUpdateString(url,upgradeCheck);
		return upgradeCheck;
	}
}
