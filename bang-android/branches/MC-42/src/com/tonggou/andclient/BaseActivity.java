package com.tonggou.andclient;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tonggou.andclient.app.BaseConnectOBDService;
import com.tonggou.andclient.app.MyCrashHandler;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.app.TongGouService;
import com.tonggou.andclient.util.BitmapCache;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.vo.Shop;
import com.tonggou.andclient.vo.ShopIntent;

public abstract class BaseActivity extends AbsUMengActivity{
	public static LinkedList<Activity> sAllActivities = new LinkedList<Activity>();
	public static final String SETTING_INFOS = "SETTING_Infos";

	public static final String LOGINED = "login";                              // ��¼��ȥ��

	public static final String SCREEN = "screen_size";                         // �ֱ���
	public static final String PASSWORD = "PASSWORD";                          // �洢������
	public static final String NAME = "NAME";                                  // �洢��  �û��˺�
	public static final String SERVICE="SERVICE";                              // �洢��  �û�ѡ��ķ���
	public static final String PHONENAME = "PHONENAME";                        // �洢��  �û���ϵ��
	public static final String PHONE="PHONE";                                  // �洢��  �û���ϵ��ʽ
	public static final String BRAND = "BRAND";                                // �洢��  �û�����
	public static final String MODEL="MODEL";                                  // �洢��  �û�Ʒ��
	public static final String VEHICLENUM="VEHICLENUM";                        // �洢��  �û�����
	public static final String CHECKVOICE="YES";                               // �û���Ϣ��ʾ��
	public static final String COOKIES_STR = "cookies_str";                    // cookies
	//public static final String FAULT_DIC_VERSON = "fault_dic_version";         // ���峵�͹�����汾��
	//public static final String COMMON_FAULT_DIC_VERSON = "common_fault_dic_version";         // ͨ�ù�����汾��
	public static final String VEHICLE_MODE_ID = "vehicle_mode_id";            // ����ID

	public static final String APPCONFIG_OBD_READ_INTERVAL = "obd_Read_Interval";                    // ��obd��ȡ���ݵ����ڼ������λΪ���� 
	public static final String APPCONFIG_SERVER_READ_INTERVAL = "server_Read_Interval";              // �ӷ���˶�ȡ���ݵ����ڼ������λΪ����
	public static final String APPCONFIG_MILEAGE_INFORM_INTERVAL = "mileage_Inform_Interval";        // �����˷��ͳ���������Ĺ������������λΪ����
	public static final String APPCONFIG_ERROR_ALERT_INTERVAL = "error_alert_Interval";              // �ظ���������ʾ���  ��λΪСʱ
	public static final String APPCONFIG_OIL_ALERT_INTERVAL = "oil_alert_Interval";                  // ������������   15_25
	public static final String APPCONFIG_OIL_LAST_STATUS = "oil_alert_status";                       // ������һ�ξ�������  0~15--0 /  15~25--1/  25����--2

	public static final String LOCATION_LAST_POSITION_LAT = "location_last_position_lac";                       // ��һ��γ��
	public static final String LOCATION_LAST_POSITION_LON = "location_last_position_lon";                       // ��һ�ξ���
	public static final String LOCATION_LAST_POSITION = "location_last_position";                       // ��һ�ξ�γ��
	public static final String LOCATION_LAST_CITYCODE = "location_last_citycode";                       // ��һ��citycode
	public static final String LOCATION_LAST_CITYNAME = "location_last_cityname";                       // ��һ�γ�����
	public static final String LOCATION_LAST_PROVINCENAME = "location_last_provincename";               // ��һ��ʡ��	
	public static final String LOCATION_LAST_STATUES = "location_last_statues";                         // ��һ�ζ�λ״̬


	public static final String NEW_MESSAGE_COUNT = "new_message_count";        //����Ϣ����


	/** ������ʾ��ʾ��Ϣ */
	public static final int SEND_MESSAGE = -1;












	protected SharedPreferences sharedPreferences ;
	protected String currentUserId;   //��ǰ�û���
	protected String currentPassWd;   //��ǰ����
	private BitmapCache myBitmapCache;

	//public static MediaPlayer mediaPlayer2;
	//public static SoundPool soundPool;
	//public static  HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();     

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		sAllActivities.add(this);
	
		//mediaPlayer2 = new MediaPlayer();
		//mediaPlayer2 = MediaPlayer.create(this,R.raw.tink);
		//mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC); 

//		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);  
//		soundPoolMap = new HashMap<Integer, Integer>();  
//		soundPoolMap.put(1,soundPool.load(this, R.raw.tink, 1));

		myBitmapCache = BitmapCache.getInstance();
		sharedPreferences = getSharedPreferences(BaseActivity.SETTING_INFOS, 0);
		currentUserId = sharedPreferences.getString(BaseActivity.NAME, "NULL");
		currentPassWd = sharedPreferences.getString(BaseActivity.PASSWORD, "NULL");

		
	
		

	}	

	protected void sendMessage(Handler handler,int what, String content) {
		if (what < 0) {
			what = BaseActivity.SEND_MESSAGE;
		}
		Message msg = Message.obtain(handler, what, content);
		if(msg!=null){
			msg.sendToTarget();
		}
	}
	public void setLikes(String like,ImageView  like1,ImageView  like2,ImageView  like3,ImageView  like4,ImageView  like5) {
		if(Double.parseDouble(like)<=0.0){	
			like1.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
			like2.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
			like3.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
			like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
			like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
		}else{
			if(Double.parseDouble(like)<=0.5){
				like1.setImageDrawable(getResources().getDrawable(R.drawable.halfstar));
				like2.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
				like3.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
				like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
				like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));									
			}else{						
				if(Double.parseDouble(like)<=1.0){
					like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
					like2.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
					like3.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
					like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
					like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));						
				}else{
					if(Double.parseDouble(like)<=1.5){
						like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
						like2.setImageDrawable(getResources().getDrawable(R.drawable.halfstar));
						like3.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
						like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
						like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));							
					}else{
						if(Double.parseDouble(like)<=2.0){
							like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
							like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
							like3.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
							like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
							like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
						}else{
							if(Double.parseDouble(like)<=2.5){
								like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
								like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
								like3.setImageDrawable(getResources().getDrawable(R.drawable.halfstar));
								like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
								like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
							}else{
								if(Double.parseDouble(like)<=3.0){
									like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
									like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
									like3.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
									like4.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
									like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
								}else{
									if(Double.parseDouble(like)<=3.5){
										like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
										like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
										like3.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
										like4.setImageDrawable(getResources().getDrawable(R.drawable.halfstar));
										like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
									}else{
										if(Double.parseDouble(like)<=4.0){
											like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
											like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
											like3.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
											like4.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
											like5.setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
										}else{
											if(Double.parseDouble(like)<=4.5){
												like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
												like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
												like3.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
												like4.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
												like5.setImageDrawable(getResources().getDrawable(R.drawable.halfstar));
											}else{
												if(Double.parseDouble(like)<=5.0){
													like1.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
													like2.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
													like3.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
													like4.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
													like5.setImageDrawable(getResources().getDrawable(R.drawable.yellowstar));
												}
											}

										}
									}
								}
							}
						}
					}
				}
			}
		}
	}


	/***
	 * �˳�����
	 */
	public void exit() {		
		if(sAllActivities!=null){
			try{
			for (Activity activity : sAllActivities) {
				activity.finish();
			}
			sAllActivities.clear();
			}catch(ConcurrentModificationException ex){	
				ex.printStackTrace();
			}
		}
		//�˳��ٶȶ�λ
		TongGouApplication app = (TongGouApplication)getApplication();
		app.stopBaiduLBS();
	}


	public void deInit(){
		TongGouApplication.showLog("deInit()   @@@@@@@@@@@@@@@@@@@");
		TongGouService.allowPollingMessage = false;
		if(TongGouApplication.obdLists!=null){
			TongGouApplication.obdLists.clear();
		}
		MainActivity.haveFaultCode = false;
		sharedPreferences.edit().putBoolean(BaseActivity.LOGINED, false).commit();
		getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
		.putString(BaseActivity.PHONENAME, null)
		.putString(BaseActivity.SERVICE, null)
		.putString(BaseActivity.PHONE, null)
		.putString(BaseActivity.BRAND, null)
		.putString(BaseActivity.MODEL, null)
		.putString(BaseActivity.VEHICLENUM, null)
		.putString(BaseActivity.CHECKVOICE, null)
		.putString(BaseActivity.VEHICLE_MODE_ID, "")
		.putString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "2")
		.putInt(BaseActivity.NEW_MESSAGE_COUNT,0).commit();
		
		CarConditionQueryActivity.ssyhStr = "- -";
        CarConditionQueryActivity.pjyhStr = "- - l/h";
        CarConditionQueryActivity.syylStr = "- -";
        CarConditionQueryActivity.sxwdStr = "- -";
        TongGouApplication.connetedVehicleName = "";  
        TongGouApplication.connetedVIN = "";          
        TongGouApplication.connetedObdSN = "";   
        TongGouApplication.connetedVehicleID = "";   
        MainActivity.defaultBrandAndModle = "";
        BaseConnectOBDService.cmile = null;
	}


	protected Bitmap getPicture(String portraitUrl) {
		return myBitmapCache.getPicture(portraitUrl,this);       

	}

	protected Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	protected ShopIntent setShopIntent(Shop allShops){
		ShopIntent intentShop=new ShopIntent();

		intentShop.setId(allShops.getId());
		intentShop.setName(allShops.getName());
		intentShop.setCoordinate(allShops.getCoordinate());
		intentShop.setAddress(allShops.getAddress());

		intentShop.setServiceScope(allShops.getServiceScope());
		intentShop.setDistance(allShops.getDistance());
		intentShop.setTotalScore(allShops.getTotalScore());
		intentShop.setBigImageUrl(allShops.getBigImageUrl());	

		intentShop.setSmallImageUrl(allShops.getSmallImageUrl());
		intentShop.setMobile(allShops.getMobile());
		intentShop.setShopScore(allShops.getShopScore());
		intentShop.setCityCode(allShops.getCityCode());
		intentShop.setMemberInfo(allShops.getMemberInfo());
		return intentShop;
	}
	protected Shop setShop(ShopIntent allShops){
		Shop intentShop=new Shop();

		intentShop.setId(allShops.getId());
		intentShop.setName(allShops.getName());
		intentShop.setCoordinate(allShops.getCoordinate());
		intentShop.setAddress(allShops.getAddress());

		intentShop.setServiceScope(allShops.getServiceScope());
		intentShop.setDistance(allShops.getDistance());
		intentShop.setTotalScore(allShops.getTotalScore());
		intentShop.setBigImageUrl(allShops.getBigImageUrl());	

		intentShop.setSmallImageUrl(allShops.getSmallImageUrl());
		intentShop.setMobile(allShops.getMobile());
		intentShop.setShopScore(allShops.getShopScore());
		intentShop.setCityCode(allShops.getCityCode());
		intentShop.setMemberInfo(allShops.getMemberInfo());
		return intentShop;
	}
	
	
	private ProgressDialog mProgressDialog;
	private AlertDialog mErrorDialog;
	
	/**
	 * ��ʾ���ڼ��صĽ��ȿ�
	 */
	protected void showProgressDialog(String msg) {
		dismissProgressDialog();
		dismissErrorDialog();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage(msg);
		mProgressDialog.setCancelable(false);
		if( !isFinishing() ){
			mProgressDialog.show();
		}
	}
	
	/**
	 * ж�ؽ��ȿ�
	 */
	protected void dismissProgressDialog() {
		if( mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	/**
	 * ��ʾ����Ի���
	 * @param msg
	 */
	protected void showErrorDialog(String msg) {
		dismissProgressDialog();
		dismissErrorDialog();
		mErrorDialog = new AlertDialog.Builder(this)
			.setTitle("ע��ʧ��")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(msg)
			.setNegativeButton(R.string.button_ok, null)
			.create();
		mErrorDialog.setCanceledOnTouchOutside(false);
		if( !isFinishing() ){
			mErrorDialog.show();
		}
	}
	
	/**
	 * ж�ش���Ի���
	 */
	protected void dismissErrorDialog() {
		if( mErrorDialog != null && mErrorDialog.isShowing()) {
			mErrorDialog.dismiss();
			mErrorDialog = null;
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		dismissErrorDialog();
		dismissProgressDialog();
		sAllActivities.remove(this);
	}
	
//
//	public static  void playVoice0(){
//		//mediaPlayer2.start();
//
//		soundPool.play(soundPoolMap.get(1), 1, 1, 0, 0, 1);  
//	}

}
