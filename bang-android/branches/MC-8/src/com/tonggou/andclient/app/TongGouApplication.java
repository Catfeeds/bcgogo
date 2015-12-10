package com.tonggou.andclient.app;


import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.tonggou.andclient.BaseActivity;
import com.tonggou.andclient.MainActivity;
import com.tonggou.andclient.parse.LoginParser;
import com.tonggou.andclient.vo.AppConfig;
import com.tonggou.andclient.vo.OBDBindInfo;
import com.tonggou.andclient.vo.OBDDevice;
import com.tonggou.andclient.vo.VehicleInfo;

public class TongGouApplication extends Application{
	public static List<OBDBindInfo> obdLists;
	public static boolean connetedOBD = false;       //��obd���ڱ�������״̬
	public static String  connetedVehicleName = "";  //���ϵĳ���Ʒ�ƺ��ͺ�
	public static String  connetedVIN = "";          //���ϵĳ���Ψһ��ʶ
	public static String  connetedObdSN = "";        //���ϵ�obd�豸 һ����ʽΪmac��ַ00:27:19:9d:bd:2e
	public static String  connetedVehicleID = "";    //���ϵĳ������ݿ��е�id

	public static String mainActivityDefaultCarInfo = "";
	public static VehicleInfo registerDefaultVehicle = null;

	private static TongGouApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;


	public static final String strKey = "QBL30Qeee7VVGKZNFs56LWlA";
	

	/*
	    	ע�⣺Ϊ�˸��û��ṩ����ȫ�ķ���Android SDK��v2.1.3�汾��ʼ������ȫ�µ�Key��֤��ϵ��
	    	��ˣ�����ѡ��ʹ��v2.1.3��֮��汾��SDKʱ����Ҫ���µ�Key����ҳ�����ȫ��Key�����룬
	    	���뼰����������ο�����ָ�ϵĶ�Ӧ�½�
	 */
	public LocationClient mLocationClient = null;
	public static BDLocation bdlocation;
	public BDLocationListener myListener = new MyLocationListener();

	public static long callbackTime;
	public static String platformVersion ;                //�û��ֻ�ϵͳƽ̨�汾 3.0 4.0
	public static String mobileModel = Build.MODEL;       //�û��ֻ��ͺ�
	public static String imageVersion ;                    //�ֻ�Ӳ���ֱ���  480 X 800
	
	private boolean  baiduIsStart = false;
	@Override
	public void onCreate() {
		platformVersion = Build.VERSION.RELEASE;   //����2.0   2.2 	
		
		super.onCreate();
		mInstance = this;
		initEngineManager(this);

		///////////////////////////////�ٶȶ�λ����///////////////////////////////////////////////////////////
		mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
		//mLocationClient.setAK(strKey);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all"); 
		option.setCoorType("bd09ll");
		option.setScanSpan(15000);                                 //���÷���λ����ļ��
		mLocationClient.registerLocationListener( myListener );    //ע���������
		mLocationClient.setLocOption(option);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	    MyCrashHandler crashHandler = MyCrashHandler.getInstance();  
	    crashHandler.init(getApplicationContext()); 
	}
	


	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey,new MyGeneralListener())) {
			//Toast.makeText(BaseMapApp.getInstance().getApplicationContext(), "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
		}
	}

	public static TongGouApplication getInstance() {
		return mInstance;
	}


	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				//Log.d("cccccc", "cccccccccccccccccccccccccccccccccc");
				TongGouApplication.getInstance().m_bKeyRight = false;
			}
		}
	}
	
	

	public void starBaiduLBS(){		
		if(mLocationClient!=null&&!baiduIsStart){
			//Log.d("DDDS", "SSSSSTART start():");
			mLocationClient.start();
			baiduIsStart = true;
		}
	}
	

	/**
	 * ͣ���ٶȶ�λ
	 */
	public void stopBaiduLBS(){	
		if(mLocationClient!=null&&baiduIsStart){
			Log.d("DDDS", "SSSSSTART stop():");
			mLocationClient.stop();
			baiduIsStart = false;
		}  	
	}
	

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			callbackTime = System.currentTimeMillis();
			if (location == null){
				//Log.d("Location","baidu location:null");
				getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
				.putString(BaseActivity.LOCATION_LAST_STATUES, "LAST")
				.commit();
				return ;
			}
//			Log.d("Location","baidu location type:" + location.getLocType());
//			
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("Poi time : ");
//			sb.append(location.getTime());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			Log.d("Location","baidu:"+sb.toString()+":"+location.getCity()+":"+location.getCityCode());
			
			if(location.getLocType()!=61 && location.getLocType()!=65 && location.getLocType()!=66 && location.getLocType()!=161){
				getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
				.putString(BaseActivity.LOCATION_LAST_STATUES, "LAST")
				.commit();
				return ;
			}
						
			String baidubackStr = location.getLongitude()+","+location.getLatitude();
			if(baidubackStr!=null&&baidubackStr.indexOf("E")==-1){				
				getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
				.putString(BaseActivity.LOCATION_LAST_POSITION_LAT, location.getLatitude()+"")
				.putString(BaseActivity.LOCATION_LAST_POSITION_LON, location.getLongitude()+"")
				.putString(BaseActivity.LOCATION_LAST_POSITION, baidubackStr)	
				.putString(BaseActivity.LOCATION_LAST_CITYNAME, location.getCity())
				.putString(BaseActivity.LOCATION_LAST_PROVINCENAME, location.getProvince())
				.putString(BaseActivity.LOCATION_LAST_STATUES, "CURRENT")
				.commit();
				
				if(location.getCityCode()==null||"".equals(location.getCityCode())||"null".equals(location.getCityCode())){
					getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
					.putString(BaseActivity.LOCATION_LAST_CITYCODE, "")
					.commit();
				}else{
					getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
					.putString(BaseActivity.LOCATION_LAST_CITYCODE, location.getCityCode())
					.commit();
				}
				
			}else{
				getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
				.putString(BaseActivity.LOCATION_LAST_STATUES, "LAST")
				.commit();
			}
			bdlocation = location;
		}

		public void onReceivePoi(BDLocation poiLocation) {	       
		}
	}


	public void saveSomeInformation(LoginParser loginParser,SharedPreferences sharedPreferences,String userID,String userPassword){
		if(userID!=null&&!"".equals(userID)){
			sharedPreferences.edit()
			.putString(BaseActivity.NAME, userID).commit();
		}
		if(userPassword!=null&&!"".equals(userPassword)){
			sharedPreferences.edit()
			.putString(BaseActivity.PASSWORD, userPassword).commit();
		}
		if(imageVersion!=null&&!"".equals(imageVersion)){
			sharedPreferences.edit()
			.putString(BaseActivity.SCREEN, imageVersion).commit();
		}
		
		sharedPreferences.edit().putBoolean(BaseActivity.LOGINED, true).commit();

		obdLists = loginParser.getLoginResponse().getObdList();
		storeDefaultCar();
		AppConfig appConfig = loginParser.getLoginResponse().getAppConfig();
		if(appConfig!=null){
			String obdReadInterval = appConfig.getObdReadInterval();
			if(obdReadInterval!=null&&!"".equals(obdReadInterval)){
				sharedPreferences.edit().putString(BaseActivity.APPCONFIG_OBD_READ_INTERVAL, obdReadInterval).commit();
			}
			String serverReadInterval = appConfig.getServerReadInterval();
			if(serverReadInterval!=null&&!"".equals(serverReadInterval)){
				sharedPreferences.edit().putString(BaseActivity.APPCONFIG_SERVER_READ_INTERVAL, serverReadInterval).commit();
			}
			String mileageInformInterval = appConfig.getMileageInformInterval();
			if(mileageInformInterval!=null&&!"".equals(mileageInformInterval)){
				sharedPreferences.edit().putString(BaseActivity.APPCONFIG_MILEAGE_INFORM_INTERVAL, mileageInformInterval).commit();
			}

			String appVehicleErrorCodeWarnIntervals = appConfig.getAppVehicleErrorCodeWarnIntervals();
			if(appVehicleErrorCodeWarnIntervals!=null&&!"".equals(appVehicleErrorCodeWarnIntervals)){
				sharedPreferences.edit().putString(BaseActivity.APPCONFIG_ERROR_ALERT_INTERVAL, appVehicleErrorCodeWarnIntervals).commit();
			}

			String remainOilMassWarn = appConfig.getRemainOilMassWarn();
			if(remainOilMassWarn!=null&&!"".equals(remainOilMassWarn)){
				//sharedPreferences.edit().putString(BaseActivity.APPCONFIG_OIL_ALERT_INTERVAL, remainOilMassWarn).commit();
				if(remainOilMassWarn.indexOf("_")!=-1){
					try{
						TongGouService.interOne = Integer.parseInt(remainOilMassWarn.substring(0, remainOilMassWarn.indexOf("_")));
						TongGouService.interTwo = Integer.parseInt(remainOilMassWarn.substring(remainOilMassWarn.indexOf("_")+1));
					}catch(NumberFormatException er){						
					}
				}
			}
		}

		new Thread(){
			public void run(){

				//����obd
				Intent intent = new Intent();//����Intent����
				intent.setAction(TongGouService.TONGGOU_ACTION_START);
				intent.putExtra("com.tonggou.server","SCAN_OBD");
				sendBroadcast(intent);//���͹㲥

				TongGouService.allowPollingMessage = true;
				//������ѯ
				Intent intent2 = new Intent();//����Intent����
				intent2.setAction(TongGouService.TONGGOU_ACTION_START);
				intent2.putExtra("com.tonggou.server","PULLING");
				sendBroadcast(intent2);//���͹㲥
			}
		}.start();

	}
	
	
	
	public  void  storeDefaultCar(){
		for(int i=0;i<obdLists.size();i++){
			if(obdLists.get(i).getVehicleInfo()!=null){
				if("YES".equals(obdLists.get(i).getVehicleInfo().getIsDefault())){
					String vehiBrand = obdLists.get(i).getVehicleInfo().getVehicleBrand();
					String vehiModel = obdLists.get(i).getVehicleInfo().getVehicleModel();
					if(vehiBrand==null){
						vehiBrand = "";
					}
					if(vehiModel==null){
						vehiModel = "";
					}
					
					MainActivity.defaultBrandAndModle = vehiBrand + " " +vehiModel;
					if(obdLists.get(i).getVehicleInfo().getVehicleBrand()!=null){
						getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
						.putString(BaseActivity.BRAND, obdLists.get(i).getVehicleInfo().getVehicleBrand()).commit();
					}
					if(obdLists.get(i).getVehicleInfo().getVehicleModel()!=null){
						getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
						.putString(BaseActivity.MODEL, obdLists.get(i).getVehicleInfo().getVehicleModel()).commit();
					}
					if(obdLists.get(i).getVehicleInfo().getVehicleNo()!=null){
						getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
						.putString(BaseActivity.VEHICLENUM,obdLists.get(i).getVehicleInfo().getVehicleNo()).commit();
					}
					if(obdLists.get(i).getVehicleInfo().getVehicleModelId()!=null){
						getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
						.putString(BaseActivity.VEHICLE_MODE_ID, obdLists.get(i).getVehicleInfo().getVehicleModelId()).commit();
					}
				}
			}
		}
	}
	
	/**
	 * ��ʾ Toast
	 * @param msg
	 */
	public static void showToast(Object msg) {
		Toast.makeText(mInstance, msg + "", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * ��ӡ log
	 * @param msg
	 */
	public static void showLog(Object msg) {
		Log.i("Tonggou Log", msg + "");
	}
	
	public void sendMainActivityChangeTitleBroadcast(String title) {
		Intent intent = new Intent(MainActivity.ACTION_CHANGE_TITLE);
		intent.putExtra(MainActivity.KEY_ARG_TITLE, title);
		sendBroadcast(intent);
	}
	
	////////////////           �� OBD ����
	public static interface OnBindOBDListener {
		public void onBindOBDSuccess(OBDDevice device, String vin);
		public void onBindOBDCancle();
	}
	
	private List<OnBindOBDListener> mBindOBDListeners = new ArrayList<OnBindOBDListener>();
	
	public void registerBindOBDListener(OnBindOBDListener listener) {
		mBindOBDListeners.add(listener);
	}
	
	public void unregisterBindOBDListener(OnBindOBDListener listener) {
		mBindOBDListeners.remove(listener);
	}
	
	public void notifyBindOBDSuccess(OBDDevice device, String vin) {
		for( OnBindOBDListener listener : mBindOBDListeners ) {
			listener.onBindOBDSuccess(device, vin);
		}
	}
	
	public void notifyBindOBDCancle() {
		for( OnBindOBDListener listener : mBindOBDListeners ) {
			listener.onBindOBDCancle();
		}
	}
	
	/// �� ���� ����
	public static interface OnBindShopListener {
		public void onBindShopSuccess(String shopName, String shopId);
		public void onBindShopCancle();
	}
	
	private List<OnBindShopListener> mBindShopListeners = new ArrayList<OnBindShopListener>();
	
	public void registerBindShopListener(OnBindShopListener listener) {
		mBindShopListeners.add(listener);
	}
	
	public void unregisterBindShopListener(OnBindShopListener listener) {
		mBindShopListeners.remove(listener);
	}
	
	public void notifyBindShopSuccess(String shopName, String shopId) {
		for( OnBindShopListener listener : mBindShopListeners ) {
			listener.onBindShopSuccess(shopName, shopId);
		}
	}
	
	public void notifyBindShopCancle() {
		for( OnBindShopListener listener : mBindShopListeners ) {
			listener.onBindShopCancle();
		}
	}
	
	/// ѡ�� ���Ƴ���
	public static interface OnSelectVehicleBrandTypeListener {
		public void onBrandSelected(boolean isCancle, String brandName, String brandId);
		public void onTypeSelected(boolean isCancle, String typeName, String typeId);
	}
	
	private List<OnSelectVehicleBrandTypeListener> mOnSelectVehicleBrandTypeListeners = new ArrayList<OnSelectVehicleBrandTypeListener>();
	
	public void registerSelectVehicleBrandTypeListener( OnSelectVehicleBrandTypeListener listener ) {
		mOnSelectVehicleBrandTypeListeners.add(listener);
	}
	
	public void unregisterSelectVehicleBrandTypeListener( OnSelectVehicleBrandTypeListener listener ) {
		mOnSelectVehicleBrandTypeListeners.remove(listener);
	}
	
	public void notifyBrandSelected(boolean isCancle, String brandName, String brandId) {
		for( OnSelectVehicleBrandTypeListener l : mOnSelectVehicleBrandTypeListeners ) {
			l.onBrandSelected(isCancle, brandName, brandId);
		}
	}
	
	public void notifyTypeSelected(boolean isCancle, String typeName, String typeId) {
		for( OnSelectVehicleBrandTypeListener l : mOnSelectVehicleBrandTypeListeners ) {
			l.onTypeSelected(isCancle, typeName, typeId);
		}
	}
	
}
