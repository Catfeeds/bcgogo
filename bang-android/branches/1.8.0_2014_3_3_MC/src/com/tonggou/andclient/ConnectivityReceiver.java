package com.tonggou.andclient;

import com.tonggou.andclient.app.UploadLocalCarCondition;
import com.tonggou.andclient.util.SomeUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
			NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			// �������ʱ����ϴ�������
			uploadLocalCarCondition(context);
			
			if (mobileInfo != null && mobileInfo.isConnected()) {
				// ���ӵ��ƶ�����
			} else if (wifiInfo != null && wifiInfo.isConnected()) {
				// ���ӵ�wifi
				uploadDJs(context);
			}
		} else {
			// ����Ͽ�
		}
	}

	private void uploadDJs(Context context) {
		// ͬһ�¼����ù㲥�п��ܽ��ն�Σ�������жϷ�ֹ�ظ�����
		if (!SomeUtil.isServiceRunning(context, DJUploadService.class)) {
			context.startService(new Intent(DJUploadService.ACTION_UPLOAD_DRIVING_JOURNALS));
		}
	}
	
	public void uploadLocalCarCondition(Context context) {
		context.startService(new Intent(context, UploadLocalCarCondition.class));
	}

}
