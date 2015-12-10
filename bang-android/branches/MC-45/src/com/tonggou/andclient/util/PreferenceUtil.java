package com.tonggou.andclient.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �û�����\���ݹ�����
 * <p>����ShardPreferences��ȡ�û�������
 * </p>
 * 
 * @author lwz
 */
public class PreferenceUtil {
	
	/** 
	 * �����ݵ�key�õ������û��ַ�������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @return һ���ַ�������
	 */
	public static String getString(Context context, String prefName, String key){
		return key == null ? null : getPreference(context, prefName).getString(key, null);
		
	}
	
	/** 
	 * ���û��ַ�������д�뵽sharePreferences��
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @param value ���ݵ�ֵ
	 */
	public static void putString(Context context, String prefName, String key, String value){
		if( key != null){
			getPreference(context, prefName).edit().putString(key, value).commit();
		}
	}
	
	/** 
	 * �����ݵ�key�õ������û���������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @return һ���ַ�������
	 */
	public static int getInt(Context context, String prefName, String key){
		return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
	}

	
	/** 
	 * ���û���������д�뵽sharePreferences��
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @param value ���ݵ�ֵ
	 */
	public static void putInt(Context context, String prefName, String key, int value){
		if( key != null){
			getPreference(context, prefName).edit().putInt(key, value).commit();
		}
	}
	
	/** 
	 * �����ݵ�key�õ������û�����������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @return һ������������
	 */
	public static float getFloat(Context context, String prefName, String key){
		return key == null ? 0 : getPreference(context, prefName).getFloat(key, 0F);
	}
	
	/** 
	 * ���û�����������д�뵽sharePreferences��
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @param value ���ݵ�ֵ
	 */
	public static void putFloat(Context context, String prefName, String key, float value){
		if( key != null){
			getPreference(context, prefName).edit().putFloat(key, value).commit();
		}
	}
	
	/** 
	 * �����ݵ�key�õ������û�����������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @return һ������������
	 */
	public static boolean getBoolean(Context context, String prefName, String key){
		return key == null ? false : getPreference(context, prefName).getBoolean(key, false);
	}

	
	/** 
	 * ���û�����������д�뵽sharePreferences��
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 * @param value ���ݵ�ֵ
	 */
	public static void putBoolean(Context context, String prefName, String key, boolean value){
		if( key != null){
			getPreference(context, prefName).edit().putBoolean(key, value).commit();
		}
	}
	
	/**
	 * ɾ������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 * @param key ���ݵļ�
	 */
	public static void remove(Context context, String prefName, String key){
		if( key != null){
			getPreference(context, prefName).edit().remove(key).commit();
		}
	}
	
	/**
	 * ����ļ��е���������
	 * 
	 * @param context �����Ķ���,����ȡ��shardPreferences
	 * @param prefName shardPreferences�ļ���
	 */
	public static void clearAll(Context context, String prefName){
		getPreference(context, prefName).edit().clear().commit();
	}
	
	/**
	 * ͨ�������Ķ���õ�sharePreferences����
	 * 
	 * @param context �����Ķ���
	 * @param prefName	sharePreferences�ļ���
	 * @return	sharePreferences����
	 */
	private static SharedPreferences getPreference(Context context, String prefName ){
		return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
	}
}
