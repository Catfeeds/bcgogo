package com.tonggou.andclient.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * ���¹�����
 * <p>
 * 	ͨ�����࣬�������ӷ��������жϵ�ǰ�Ƿ����µ�Ӧ�ð汾
 * </p>
 * 
 * @author lwz
 */
public class UpdateUtil {
	
	private OnUpdateListener mUpdateListener;
	
	/**
	 * ���½ӿ�
	 * @author lwz
	 */
	public interface OnUpdateListener{
		/**
		 * ���¶���
		 */
		public void onUpdate();
		
		/**
		 * ��ǰ�汾��������
		 */
		public void onNoNeedUpdate();
	}
	
	public void setOnUpdateListener( OnUpdateListener listener ){
		mUpdateListener = listener;
	}
	
	/**
	 * ��ñ��ذ�װ������Ϣ
	 * 
	 * @param context �����Ķ���
	 * @throws NameNotFoundException 
	 */
	public static PackageInfo getLocalPackageInfo(Context context) throws NameNotFoundException{
		// ��ȡpackagemanager��ʵ��
        PackageManager packageManager = context.getPackageManager();
        
        // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
        return packageManager.getPackageInfo( context.getPackageName(),0);
	}
	
	/**
	 * ��֤�Ƿ�Ҫ����
	 * 
	 * @param context �����Ķ���
	 * @param versionCode �������д��صİ汾��
	 * @return �п��ø��·��� true, ���򷵻� false
	 * @throws NameNotFoundException
	 */
	private boolean validateUpdate( Context context, String versionCode , String versionName) throws NameNotFoundException{
		return getLocalPackageInfo(context).versionName.compareTo( versionName ) < 0 ? true : false;
	}
	
	/**
	 * ����
	 * 
	 * @param context �����Ķ���
	 * @param versionCode �������д��صİ汾��
	 * @throws NameNotFoundException 
	 */
	public void update(Context context, String versionCode, String versionName) throws NameNotFoundException{
		if( validateUpdate(context, versionCode, versionName) ){
			if( mUpdateListener != null ){
				mUpdateListener.onUpdate();
			}
		} else {
			if( mUpdateListener != null ){
				mUpdateListener.onNoNeedUpdate();
			}
		}
	}
}
