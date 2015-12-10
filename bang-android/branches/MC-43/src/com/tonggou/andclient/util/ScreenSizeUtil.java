package com.tonggou.andclient.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * �ֻ���Ļ������
 * <p>����ɻ���ֻ���Ļ�ĸ߶ȺͿ��</p>
 * 
 * @author lwz
 *
 */
public class ScreenSizeUtil {

	private final String PREF_NAME = "screen_pref";  				// preferences ������
	private final String KEY_WIDTH = "screen_width";				// preferences ����Ļ��ļ�
	private final String KEY_HEIGHT = "screen_height"; 				// preferences ����Ļ�ߵļ�
	private final String KEY_DENSITY_DPI = "screen_density_dpi"; 	// preferences ����Ļ�ܶ�Dpi�ļ�
	private final String KEY_DENSITY = "screen_density"; 			// preferences ����Ļ�ܶȵļ�
	
	private Context mContext; 		// �����Ķ���
	
	private int mScreenWidth;	// ��Ļ�Ŀ�
	private int mScreenHeight; 	// ��Ļ�ĸ�
	private int mDensityDpi;		// ��Ļ�ܶ�Dpi
	private float mDensity; 		// ��Ļ�ܶ�
	
	private ScreenSizeUtil() {
	}
	
	public ScreenSizeUtil( Context context) {
		this();
		mContext = context;
		getScreenDefaultSize();
	}
	
	/**
	 *  �õ���ĻĬ�ϵĳߴ�
	 */
	private void getScreenDefaultSize(){
		mScreenWidth = PreferenceUtil.getInt(mContext, PREF_NAME, KEY_WIDTH);
		mScreenHeight = PreferenceUtil.getInt(mContext, PREF_NAME, KEY_HEIGHT);
		mDensityDpi = PreferenceUtil.getInt(mContext, PREF_NAME, KEY_DENSITY_DPI);
		mDensity = PreferenceUtil.getFloat(mContext, PREF_NAME, KEY_DENSITY);
		
		// �� preferences �в����ڼ�ֵ����ô�ͻ�ȡ
		// Ȼ��浽 preferences ��
		if( mScreenWidth == 0 || mScreenHeight == 0 || mDensityDpi == 0 || mDensity == 0F){
			DisplayMetrics metrics = new DisplayMetrics(); // ��ʾ�����غ�������
			WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(metrics);
			
			mScreenWidth = metrics.widthPixels;
			mScreenHeight = metrics.heightPixels;
			mDensityDpi = metrics.densityDpi;
			mDensity = metrics.density;
			
			PreferenceUtil.putInt(mContext, PREF_NAME, KEY_WIDTH, mScreenWidth);
			PreferenceUtil.putInt(mContext, PREF_NAME, KEY_HEIGHT, mScreenHeight);
			PreferenceUtil.putInt(mContext, PREF_NAME, KEY_DENSITY_DPI, mDensityDpi);
			PreferenceUtil.putFloat(mContext, PREF_NAME, KEY_DENSITY, mDensity);
		}
	}
	
	/**
	 * �õ���Ļ�Ŀ�
	 * @return int ��Ļ�Ŀ�
	 */
	public int getScreenWidth(){
		return mScreenWidth;
	}
	/**
	 * �õ���Ļ�ĸ�
	 * @return int ��Ļ�ĸ�
	 */
	public int getScreenHeight(){
		return Integer.valueOf(mScreenHeight);
	}
	
	/**
	 * �õ���Ļ���ܶ�Dpi
	 * @return int ��Ļ���ܶ�Dpi
	 */
	public int getScreenDensityDpi(){
		return  mDensityDpi;
	}
	
	/**
	 * �õ���Ļ���ܶ�
	 * @return float ��Ļ���ܶ�
	 */
	public float getScreenDensity(){
		return mDensity;
	}
	
	/**
	 * dip תΪ px
	 * @param dipValue
	 * @return
	 */
	public int dip2px(float dipValue) {
		return (int) (dipValue * getScreenDensity() + 0.5f);
	}
	
	/**
	 * dip תΪ px
	 * @param res Resources ����
	 * @param dipRes dip �� xml �������ļ��е� id
	 * @return
	 */
	public int dip2px(Resources res, int dipRes) {
		return dip2px(res.getDimension(dipRes));
	}

	/**
	 * px תΪ dip
	 * @param pxValue
	 * @return
	 */
	public int px2dip( float pxValue) {
		return (int) (pxValue / getScreenDensity() + 0.5f);
	}
}