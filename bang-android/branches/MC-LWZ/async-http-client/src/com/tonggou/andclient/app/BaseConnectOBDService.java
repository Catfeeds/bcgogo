package com.tonggou.andclient.app;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;
import com.tonggou.andclient.BaseActivity;
import com.tonggou.andclient.CarConditionQueryActivity;
import com.tonggou.andclient.CarErrorActivity;
import com.tonggou.andclient.OilErrorActivity;
import com.tonggou.andclient.PreLoginActivity;
import com.tonggou.andclient.network.MyBluetoothService;
import com.tonggou.andclient.network.Network;
import com.tonggou.andclient.network.NetworkState;
import com.tonggou.andclient.parse.CommonParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.util.SaveDB;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.CarCondition;
import com.tonggou.andclient.vo.FaultCodeInfo;
import com.tonggou.andclient.vo.OBDBindInfo;

/**
 * ��̨����obd
 * @author think
 *
 */
public class BaseConnectOBDService extends Service{
	
	private static final boolean DEBUG = true;
	
	public static String TONGGOU_ACTION_START = "com.tonggou.action.startobd";
	public static String TONGGOU_ACTION_READ_CURRENT_RTD_CONDITION = "com.tonggou.ACTION.READ_RTD_CONDITION";
	public static String TONGGOU_ACTION_READ_CURRENT_DTC_CONDITION = "com.tonggou.ACTION.READ_DTC_CONDITION";
	public static String TONGGOU_ACTION_UPDATEUI = "com.tonggou.action.updateui";
	public static String TONGGOU_ACTION_NEW_MESSAGE = "com.tonggou.action.newmessage";
	public static String TONGGOU_ACTION_DISPLAY_MESSAGE = "com.tonggou.action.displaymessage";
	
	private ArrayList<BluetoothDevice>  devices;

	private BluetoothAdapter mBtAdapter;
	String defaultObdSN = null;
	
    // Key names received from the MyBluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    ///////////////////////////////////////////////////
    
    private MyBluetoothService myBTService = null;
    

    private boolean connectOBDSuccess = false;  //���ɹ� ��������ɹ�  ��Ҫ��鳵vin
    //private String defaultObdSN = null;
    private final Object allow = new Object();
    private final Object allowA = new Object();
    private final Object lockA = new Object();

    private String readFromOBDvehicleVin;
    private ReadCurrentCarConditionReceiver readCurrentCarConditionReceiver;

    public static int connetState = -1;  //-1��ʼ״̬   0--������  1--����  3--���ڶ�ȡobd����  
    public static String cmile;
    public static boolean addingCar = false; //����������豸����
	public void onCreate() {		
		TongGouApplication.showLog("BaseConnectOBDService start");
		myBTService = new MyBluetoothService(this, mHandler);
		
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);                                // Register for broadcasts when a device is discovered

       
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);                                // Register for broadcasts when discovery has finished
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();                       // Get the local Bluetooth adapter		
        
        readCurrentCarConditionReceiver = new ReadCurrentCarConditionReceiver();
        IntentFilter readfilter = new IntentFilter();
        readfilter.addAction(TONGGOU_ACTION_READ_CURRENT_RTD_CONDITION);
        readfilter.addAction(TONGGOU_ACTION_READ_CURRENT_DTC_CONDITION);
        registerReceiver(readCurrentCarConditionReceiver, readfilter);
        
       
	  }
	
	  /**
	   * ��ʼ����ɨ������
	   * @param alertUser    �Ƿ�Ҫ��ʾ�û������Ǻ�̨�Զ����Ӳ���
	   */
	 protected void startConnect(){
		  if(mBtAdapter!=null){
	        	// If BT is not on, request that it be enabled.
		        if (!mBtAdapter.isEnabled()) {
		        	connetState = 1; 
		        }else{
		        	try{
		        		//Log.d("testthread","conneting obd.....");
						doSomeFindingJob();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
		        } 	        
	        }
	  }
	

	 
	  
	  
	  public void onDestroy() {
	        super.onDestroy();
	        stopConnect();
	        this.unregisterReceiver(mReceiver);//ȡ��ע��Receiver
	        this.unregisterReceiver(readCurrentCarConditionReceiver);//ȡ��ע��Receiver
	        TongGouApplication.showLog("BaseConnectOBDService stop");  
	  }
	  
	  public void stopConnect(){
		  if (myBTService != null){
	        	myBTService.stop();
      	}
	  }
	
	/**
	 * ��ʼ���ҹ���
	 */
	  private void doSomeFindingJob()throws InterruptedException{	
		readFromOBDvehicleVin = null;

		boolean intoScan = false; // �Ƿ�����ɨ��

		if (TongGouApplication.obdLists==null || TongGouApplication.obdLists.size()== 0) {
			connetState = 1;
			return;
		}
		connetState = 0;

		devices = new ArrayList<BluetoothDevice>();
		devices.clear();
		
		for (int i = 0; i < TongGouApplication.obdLists.size(); i++) {
			if ("YES".equals(TongGouApplication.obdLists.get(i).getIsDefault())) {
				defaultObdSN = TongGouApplication.obdLists.get(i).getObdSN(); // �ҵ���������������Ĭ��obd
				break;
			}
		}
    	///////////////////////////////
		//�õ��Ѿ���Թ����豸
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();      // Get a set of currently paired devices
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices!=null && !pairedDevices.isEmpty()) {
//        	//ת��ArrayList
        	ArrayList<BluetoothDevice> needToChecks = new ArrayList<BluetoothDevice>();
//        	for(BluetoothDevice device : pairedDevices){
//				needToChecks.add(device);
//            }
			// ////////////////////////////////////////////////////Ĭ�ϵ���������
			if ( !TextUtils.isEmpty( defaultObdSN)) { // defaultObdSN such as 00:0D:18:28:4B:6A
				BluetoothDevice device2 = null;
				for (BluetoothDevice device : pairedDevices) {
					if (defaultObdSN.equals(device.getAddress())) { // �ҵ�ɨ�赽���Ѿ���Ե�Ĭ��obd
						device2 = device;
						break;
					}
				}
				if (device2 != null) {
//					needToChecks.remove(device2);
//					needToChecks.add(0, device2);
					needToChecks.clear();
					needToChecks.add(device2);
				}
			}

			cricleConnetOBD(needToChecks); // �����ȼ�����OBD
        }
//        
//		// û��������Ե���ʼɨ��
		if (!connectOBDSuccess) {
			intoScan = true;
			doDiscovery();
		}
//		
//        //////////////////////////////////
        if(!intoScan){
        	connetState = 1;
        }
	}
	  
	  private void printUUids(ParcelUuid[] uuids) {
		  int i=0;
		  for(ParcelUuid uuid : uuids) 
			  TongGouApplication.showLog( "@---- " + (++i) + ". UUID = " + uuid.getUuid() );
	  }
	
	
	/**
	 * 
	 * @param devices
	 * @param defaultObdSN
	 * @return �ɹ� ��ʧ��
	 * @throws InterruptedException
	 */
	@SuppressLint("NewApi")
	private void cricleConnetOBD(ArrayList<BluetoothDevice> devices)throws InterruptedException {
		if( devices==null || devices.isEmpty()){
			//Log.d("testthread", "devices devices.size()==0");	
			return;
		}

		for (final BluetoothDevice device : devices) {
			if ( TextUtils.isEmpty( device.getAddress() )) {
				continue;
			}
TongGouApplication.showLog("device.getName  " + device.getName() + " @ " + device.getAddress() );
printUUids(device.getUuids());
			boolean inMyList = false;
			OBDBindInfo obdInfo = null;
			TongGouApplication.showLog("TongGouApplication.obdLists.size()  = " + TongGouApplication.obdLists.size());
			for (int i=0; i<TongGouApplication.obdLists.size(); i++) {
				obdInfo = TongGouApplication.obdLists.get(i);
				if (device.getAddress().equals(obdInfo.getObdSN())) { 
					inMyList = true;
					break;
				}
			}
			if (!inMyList) {
				continue;
			}
				
			TongGouApplication.showLog("circle connecting 111111111111");

			myBTService.connecting(device);                  //����obd
			
			synchronized(allowA){
				allowA.wait();
			}
			
			//Log.d("testthread", "circle connecting 222222222222");
			
			if (!connectOBDSuccess) {
				continue;  
			}
				
			//���ӳɹ�
			//������vin �����ж����obd�ͳ����İ󶨹�ϵ
			//Log.d("testthread", "circle readVin 111111111111");
			String currentConnectVin = readVinFromOBD();
			//Log.d("testthread", "circle readVin 222222222222:"+currentConnectVin);
			
			boolean bInMyVehicleList = false;   // �Ƿ����Լ��ĳ��б���
			boolean bBindNoChange = false;      // ����OBD��ϵ�Ƿ񲻱�
				if (currentConnectVin!=null && obdInfo.getVehicleInfo()!=null) {
					//�Ƿ����Լ��ĳ��б���
					if (matchObdVin(currentConnectVin) || "".equals(currentConnectVin)) {
						bInMyVehicleList = true;
					}
					else {
						bInMyVehicleList = false;
					}
					if (!bInMyVehicleList) {
						//�Ͽ�
						stopConnect();						
						continue;
					}
					
					String thisObdVin = obdInfo.getVehicleInfo().getVehicleVin();  //���obd�϶�Ӧ�ĳ�
					if (currentConnectVin.equals(thisObdVin)){ // ����OBD��ϵ�Ƿ񲻱�
						bBindNoChange = true;
					}
					finishConnectOBD(obdInfo.getVehicleInfo().getVehicleBrand()
				            +" "+obdInfo.getVehicleInfo().getVehicleModel(),
				            currentConnectVin,obdInfo.getVehicleInfo().getVehicleModelId(),device.getAddress(),obdInfo.getVehicleInfo().getVehicleId());
					if (!bBindNoChange) {
						//֪ͨ�����������������������µ�obd��vin�İ󶨹�ϵ����������������������
						//TODO
					}
					return;
			} else {
				//�Ͽ�
				stopConnect();
			}
        }
	}
	
	/**
	 * ��obd�ж�ȡ����vin
	 * @return vin ����������    null ��ʱ
	 * @throws InterruptedException 
	 */
	private String readVinFromOBD(){
		new Thread(){
			public void run(){
				connetState = 3;
				myBTService.write("##VIN\r\n".getBytes());
				myBTService.write("##DTC\r\n".getBytes());	
			}
		}.start();
		startReadOBDVinTimer();
		synchronized(lockA){
			try {
				lockA.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stopReadOBDVinTimer();
		return readFromOBDvehicleVin;
	}
	
	private void writeCurrentMile(final String miles){
		new Thread(){
			public void run(){
                String writeMiles = "##MIL:"+miles+"\r\n";
				myBTService.write(writeMiles.getBytes());
			}
		}.start();
	}
	
	
	//////////////////
    /**
	 * ɨ�賬ʱscan timeout
	 */
	private ScanTimeOutTask scanTimeoutTask;   					 
	private Timer scanTimeoutTimer;
	private void startTimeoutTimer(){
		stopTimeoutTimer();
		try{
		scanTimeoutTask = new ScanTimeOutTask();
		scanTimeoutTimer = new Timer();		
		scanTimeoutTimer.schedule(scanTimeoutTask,60000);
		}catch(Exception ex){}
	}

	private void stopTimeoutTimer(){
		if(scanTimeoutTask != null){
			scanTimeoutTask.cancel();
		}
		scanTimeoutTask = null;
		if(scanTimeoutTimer != null){
			scanTimeoutTimer.cancel();
		}
		scanTimeoutTimer = null;
	}

	 private class ScanTimeOutTask extends TimerTask{
   	 	public void run(){  
   	 		//Log.d("testthread", "scan ��ʱ............................");
   	 		connetState = 1; //���ҽ���
   	 	}
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * ��ȡobdVIN ��ʱ��
	 */
	private ReadOBDVinTask readOBDVinTask;   					 
	private Timer readOBDVinTimer;
	private void startReadOBDVinTimer(){
		stopReadOBDVinTimer();
		try{
		readOBDVinTask = new ReadOBDVinTask();
		readOBDVinTimer = new Timer();
		readOBDVinTimer.schedule(readOBDVinTask,10000); //10��
		}catch(Exception ex){}
	}

	private void stopReadOBDVinTimer(){
		if(readOBDVinTask != null){
			readOBDVinTask.cancel();
		}
		readOBDVinTask = null;
		if(readOBDVinTimer != null){
			readOBDVinTimer.cancel();
		}
		readOBDVinTimer = null;
	}

	 private class ReadOBDVinTask extends TimerTask{
   	 	public void run(){  	 	
   	 		//Log.e("testthread", "stop read obd vin ,stop connect obd......��vin��ʱ");
	   	 	synchronized(lockA){ //���Ѷ�vin�߳�
				lockA.notify();
			}
   	 	}
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * �ж����������б����Ƿ������vin
	 * @param obdVin
	 * @return true  ����
	 */
	private boolean matchObdVin(String obdVin){
		if(obdVin==null||"".equals(obdVin)){
			return false;
		}else{
			for(int j=0;j<TongGouApplication.obdLists.size();j++){ 
				if(TongGouApplication.obdLists.get(j).getVehicleInfo()!=null
						&&obdVin.equals(TongGouApplication.obdLists.get(j).getVehicleInfo().getVehicleVin())){
					return true;
				}
			}
			return false;
		}
	}
	

	
	  /**
	   * ���ӳɹ���һЩ����
	   * @param vehicleName
	   * @param vehVin
	   * @param vehModle
	   */
     private void finishConnectOBD(String vehicleName,String vehVin,final String vehModle,String obdSn,String vehicleId){
    	TongGouApplication.connetedVehicleName = vehicleName;
		TongGouApplication.connetedVIN = vehVin;
		TongGouApplication.connetedObdSN = obdSn;
		TongGouApplication.connetedVehicleID = vehicleId;	
		TongGouApplication.connetedOBD = true;		
		if(cmile!=null&&!"".equals(cmile)){
			//Log.i("Bluetooth thinks", "д����" + cmile);
			writeCurrentMile(cmile);
		}
     }
	
	

	
   
	
	
	//�����MyBluetoothService���ص���Ϣ 
	protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MyBluetoothService.MESSAGE_STATE_CHANGE:
                 //Log.i("Bluetooth thinks", "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case MyBluetoothService.STATE_CONNECTED:
                    //�����Ѿ����ӵ�״̬
                    break;
                case MyBluetoothService.STATE_CONNECTING:
                	//�����������ӵ�״̬
                    break;
                case MyBluetoothService.STATE_LISTEN:
                case MyBluetoothService.STATE_NONE:
                    //��ʼ״̬
                    break;
                }
                break;
            case MyBluetoothService.MESSAGE_WRITE:
            	//���͡�������
                //byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                //String writeMessage = new String(writeBuf);
                //Log.i("Bluetooth thinks", "send:  " + writeMessage);
         
                break;
            case MyBluetoothService.MESSAGE_READ:
            	//�յ�����������
                String readMessage = (String) msg.obj;
                Log.i("Bluetooth thinks", "receiveAAAAA:  " + readMessage);
                if(readMessage!=null){
                	if(readMessage.indexOf("DTC:")!=-1&&readMessage.indexOf("RTD:")!=-1){
                		String[] dtcAndRtd = readMessage.split("##");
                		if(dtcAndRtd.length>0){
                			for(int i=0;i<dtcAndRtd.length;i++){
                				String temStr = dtcAndRtd[i];
                				if(temStr.indexOf("DTC:")!=-1&&temStr.length()>4){  //������
                            		String obdErrorCode = temStr.substring(temStr.indexOf("DTC:")+4);
                            		prosseingOBDError(obdErrorCode.trim());
                            	}else if(temStr.indexOf("RTD:")!=-1&&temStr.length()>4){
                            		String currentConditon = temStr.substring(temStr.indexOf("RTD:")+4);
                            		prosseingCurrentConditon(currentConditon.trim());
                            	}
                			}
                		}
                		return;
                	}
                	if(readMessage.indexOf("VIN:")!=-1&&readMessage.length()>4){
                		String vehicleVin = readMessage.substring(readMessage.indexOf("VIN:")+4);
                		
                		if(vehicleVin==null){
                			vehicleVin = "";
    	        		}
    	        		final String fvehicleVin = vehicleVin.trim();  
    	        		//readFromOBDvehicleVin = "";  //����
        	        	readFromOBDvehicleVin = fvehicleVin;       	        		
                		synchronized(lockA){ //���Ѷ�vin�߳�
                			lockA.notify();
            			}
                	}else if(readMessage.indexOf("DTC:")!=-1&&readMessage.length()>4){  //������
                		String obdErrorCode = readMessage.substring(readMessage.indexOf("DTC:")+4).trim();
                		
                		obdErrorCode = SomeUtil.transformDTCResult(obdErrorCode);
                		
                		if( !TextUtils.isEmpty( obdErrorCode ) ) {
                			prosseingOBDError(obdErrorCode);
                		}
                	}else if(readMessage.indexOf("RTD:")!=-1&&readMessage.length()>4){
                		String currentConditon = readMessage.substring(readMessage.indexOf("RTD:")+4);
                		prosseingCurrentConditon(currentConditon.trim());
                	}else if(readMessage.indexOf("MIL")!=-1){
                		if(readMessage.indexOf("OK")!=-1){              			
                			cmile=null;
                			//Log.i("Bluetooth thinks", "�幫��" + cmile);
                		}
                	}
                }
                break;

            case MyBluetoothService.MESSAGE_DEVICE_NAME:
                // save the connected device's name
            	//�Ѿ����豸�������
            	//Log.d("testthread", "connect obd ok���ӳɹ�");
            	
            	
        		connectOBDSuccess = true;
        		synchronized(allow){
        			allow.notify();
    			}
            		
        		synchronized(allowA){
        			allowA.notify();
    			}
            
                break;
            case MyBluetoothService.MESSAGE_CONNECT_FAILT:  //����ʧ��
            	//Log.d("testthread", "connect obd failt����ʧ��");
            	connectOBDSuccess = false;
            	//Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_LONG).show();
        		synchronized(allow){
        			allow.notify();
    			}
            	
        		synchronized(allowA){
        			allowA.notify();
    			}
          
                break;
            case MyBluetoothService.MESSAGE_CONNECT_LOST:  //���Ӷϵ�
            	//Log.d("testthread", "connect obd lost���ӶϿ�");
            	//Toast.makeText(getApplicationContext(), "�������ӶϿ�",Toast.LENGTH_LONG).show();
            	connectOBDSuccess = false;
            	TongGouApplication.connetedOBD = false;
            	synchronized(allow){
        			allow.notify();
    			}
            	
            	synchronized(allowA){
        			allowA.notify();
    			}
                break;
            case MyBluetoothService.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_LONG).show();
                break;
            }
        }
    };
	
	
	
	
	
	
	 
	 /*
	  * ���������豸����
	  */
	 private void doDiscovery(){
		 startTimeoutTimer(); //��ʱ
		 //Log.d("testthread", "scanning bluetooth devices ....");
         // If we're already discovering, stop it
         if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
         }
         TongGouApplication.showLog("@@@@@@@ doDiscovery @@@@@@@@");
         //Request discover from BluetoothAdapter
         mBtAdapter.startDiscovery();
	 }
	 
	    // The BroadcastReceiver that listens for discovered devices 
	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	
	        	if( addingCar ) {
	        		return;
	        	}
	        	
	            String action = intent.getAction();

	            // When discovery finds a device
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                // Get the BluetoothDevice object from the Intent
	                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                //Log.d("bbtt", "device:"+device.getName()+":"+device.getAddress());
	                
	                // If it's already paired, skip it, because it's been listed already
//	                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//	                	if(devices!=null){
//	                		//devices.add(device);
//	                		addDevice(device);
//	                	}
	                	TongGouApplication.showLog("ACTION_FOUND  " + defaultObdSN);
	                	if( TextUtils.isEmpty(defaultObdSN) ) {
							connetState = 1; //���ҽ���
							return;
						}
	                	
						final ArrayList<BluetoothDevice> needConnectDevices = new ArrayList<BluetoothDevice>();
						if( defaultObdSN.equals( device.getAddress()) ) {
							needConnectDevices.add(device);
							new Thread(){
								public void run(){
									TongGouApplication.showLog("ACTION_FOUND  Thread device.getAddress()" + device.getAddress());
				                	try {
										if(!BaseConnectOBDService.addingCar){
											cricleConnetOBD(needConnectDevices);
										}
										connetState = 1; //���ҽ���
									} catch (InterruptedException e) {
										connetState = 1; //���ҽ���
										e.printStackTrace();
									}  
								}
		                	}.start();
							mBtAdapter.cancelDiscovery();
						}
						
	                	if(DEBUG) Log.d("bbtt", "device2:"+device.getName()+":"+device.getAddress());
//					}
	                
	            // When discovery is finished, change the Activity title
	            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {		            	
	            	//if(devices!=null){
	            		 //Log.d("testthread", "scanning devices finish ...."+devices.size());	
                	//}
	            	stopTimeoutTimer();   //ͣ����ʱ
	            }
	        }
	    };
	    
	    
	    
	    private void addDevice(BluetoothDevice devs){
	    	boolean finded = false;
	    	for(int i=0;i<devices.size();i++){
	    		if(devices.get(i).getAddress().equals(devs.getAddress())){
	    			finded = true;
	    		}
	    	}
	    	if(!finded&&(!"".equals(devs.getAddress())&&(!"".equals(devs.getName())))&&devs.getAddress()!=null&&devs.getName()!=null){
	    		devices.add(devs);
	    		if(DEBUG) TongGouApplication.showLog("devices added name:" + devs.getName() + " + addr: " + devs.getAddress() );
	    	}
	    }
	    
	    /**
	     * �����յ��Ĺ����룬���ɳ�����ʾ��Ϣ
	     * @param codeStrs  ����:pu003,pu9999
	     */
	    private void prosseingOBDError(final String codeStrs){	  
	    	
	    	if(!TongGouApplication.connetedOBD||codeStrs==null||"".equals(codeStrs)){
				return;
			}
	    	String userID = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME, "");
	    	String modleId = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.VEHICLE_MODE_ID, "NULL");
	    	String[] errors = codeStrs.split(",");
	        //errors[0] = "P0403";
	    	for(int i=0;i<errors.length;i++){
	    		if(errors[i].indexOf("#")!=-1){
	    			continue;
	    		}
	    		ArrayList<FaultCodeInfo> modleFaults = SaveDB.getSaveDB(this).getSomeFaultCodesById(modleId,errors[i]);
	    		if(modleFaults==null||modleFaults.size()==0){
	    			modleFaults = SaveDB.getSaveDB(this).getSomeFaultCodesById("common",errors[i]);
	    		}
	    		if(modleFaults!=null&&modleFaults.size()>0){
		    		FaultCodeInfo oneFault = modleFaults.get(0);
		    		CarCondition oneConditionMessage = new CarCondition();
		    		oneConditionMessage.setAlarmId(System.currentTimeMillis()+"");
		    		oneConditionMessage.setUserID(userID);
		    		oneConditionMessage.setFaultCode(oneFault.getFaultCode());
		    		
		    		if(modleFaults.size()>=2){ //��������
		    			StringBuffer sber = new StringBuffer();
			    		for(int j=0;j<modleFaults.size();j++){
			    			sber.append(modleFaults.get(j).getDescription());
			    			sber.append("\n");
			    		}
			    		oneConditionMessage.setContent(sber.toString());
		    		}else{		    		
		    			oneConditionMessage.setContent(oneFault.getDescription());
		    		}
		    		oneConditionMessage.setReportTime(System.currentTimeMillis()+"");
		    		oneConditionMessage.setVehicleVin(TongGouApplication.connetedVIN);
		    		SaveDB.getSaveDB(this).saveAlarm(oneConditionMessage);   //���浽���ݿ�		    		
	    		}else{	    		
		    		CarCondition oneConditionMessage = new CarCondition();
		    		oneConditionMessage.setAlarmId(System.currentTimeMillis()+"");
		    		oneConditionMessage.setUserID(getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME, ""));
		    		oneConditionMessage.setFaultCode(errors[i]);
		    		oneConditionMessage.setContent("����ʶ��Ĺ�����");
		    		oneConditionMessage.setReportTime(System.currentTimeMillis()+"");
		    		oneConditionMessage.setVehicleVin(TongGouApplication.connetedVIN);
		    		SaveDB.getSaveDB(this).saveAlarm(oneConditionMessage);   //���浽���ݿ�
	    		}
	    	}
	    	
	    	sendReceivedOBDDTCBroadcast();
	    	
	    	if(errors.length>0){//��ʾ�û�
	    		SharedPreferences settingPres = getSharedPreferences(BaseActivity.SETTING_INFOS, 0);
				if (settingPres!=null&&settingPres.getBoolean(BaseActivity.LOGINED, false)) {  	    		//����ǵ�¼״̬						
					Intent intent =new Intent(this,CarErrorActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);				
				}else{
					Intent toLogin = new Intent(this, PreLoginActivity.class);
					toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(toLogin);			
				}
				new Thread(){
					public void run(){
						sendVehicleProblem(codeStrs);
					}
				}.start();
	    	}
	    }
	    
	    /**
	     * ���ͽ��յ� OBD ������ ��������Ϣ�㲥
	     */
	    private void sendReceivedOBDDTCBroadcast() {
	    	Intent intent = new Intent();
	    	intent.setAction(CarConditionQueryActivity.ACTION_RECEIVED_OBD_DTC);
	    	sendBroadcast(intent);
	    }
	    
		/**
		 * ���ͳ���������Ϣ
		 */
		private void sendVehicleProblem(String faultCodes){
			String userNo = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME, "");
		
			String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/vehicle/fault";
			CommonParser commonParser = new CommonParser();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("faultCode",faultCodes));	
			nameValuePairs.add(new BasicNameValuePair("userNo",userNo));
			nameValuePairs.add(new BasicNameValuePair("vehicleId",TongGouApplication.connetedVehicleID));      //����Ψһ��ʶ��
			nameValuePairs.add(new BasicNameValuePair("vehicleVin",TongGouApplication.connetedVIN));      //����Ψһ��ʶ��
			nameValuePairs.add(new BasicNameValuePair("obdSN",TongGouApplication.connetedObdSN));             //obdΨһ��ʶ��
			nameValuePairs.add(new BasicNameValuePair("reportTime",System.currentTimeMillis()+""));



			NetworkState ns = Network.getNetwork(this).httpPostUpdateString(url,nameValuePairs,commonParser);	
			TongGouApplication.showLog("���͹�����  ��" + faultCodes);
		}

	    
	    /**
	     * �����յ�ʵʱ��ʾ����
	     * @param codeStrs  
	     */
	    private void prosseingCurrentConditon(String codeStrs){	    	
	    	if(!TongGouApplication.connetedOBD||codeStrs==null||"".equals(codeStrs)){
				return;
			}
//	    	OBD�˻ظ���##˲ʱ�ͺģ�ml/s�����ٹ����ͺģ�l/100km��,�ͺ�(l/h), ������%��,��̣�km����������ˮ�£��棩����ƿ��ѹ��V��\r\n
	    	
//	    	,�ͺ�(l/h)��������%����Щ����Ҳ��֧�֣���ظ�N/A;

	    	String ssyh = "";  //˲ʱ�ͺ�
        	String pjyh = "";  //�ٹ����ͺ�
        	String youhao = "";  
        	String syyl = "";  //����
        	String licheng = "";
        	String sxwd = "";  //������ˮ��
        	String dianya = "";
        	String shudu = "";  //�ٶ�
        	
        	String ssyhToSever = "";  //˲ʱ�ͺ�
        	String pjyhToSever = "";  //�ٹ����ͺ�
        	String youhaoToSever = "";  
        	String syylToSever = "";  //����
        	String lichengToSever = "";
        	String sxwdToSever = "";  //������ˮ��
        	String dianyaToSever = "";
        	
	    	String[] errors = codeStrs.split(",");
	    	if(errors.length>=1){
	    		ssyh = errors[0];
	    		ssyhToSever = errors[0];
	    	}
	    	if(errors.length>=2){
	    		pjyh = errors[1];
	    		pjyhToSever = errors[1];
	    	}
	    	if(errors.length>=3){
	    		youhao = errors[2];
	    		youhaoToSever = errors[2];
	    	}
	    	if(errors.length>=4){
	    		syyl = errors[3];
	    		syylToSever = errors[3];
	    	}
	    	if(errors.length>=5){
	    		licheng = errors[4];
	    		lichengToSever = errors[4];
	    	}
	    	if(errors.length>=6){
	    		sxwd = errors[5];
	    		sxwdToSever = errors[5];
	    	}
	    	if(errors.length>=7){
	    		dianya = errors[6];
	    		dianyaToSever = errors[6];
	    	}
	    	if(errors.length>=8){
	    		shudu = errors[7];
	    	}
	    	
	    	    if("N/A".equals(ssyh)){
	        		ssyh = "- -";
	        	}else{
	        		try{
		        		double ssyhDouble = Double.parseDouble(ssyh);
		        		double ssyhHour = (ssyhDouble*3600)/1000;        //˲ʱ�ͺ� ����ml/s -> l/h ������ʾ
		        		BigDecimal bg = new BigDecimal(ssyhHour);
		                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		        		ssyh = f1+"";
	        		}catch(NumberFormatException er){
	        			ssyh = "- -";
	        		}
	        	}
	        	if("N/A".equals(pjyh)){
	        		pjyh = "- - l/h";
	        	}else{
	        		if(shudu!=null&&!"".equals(shudu)&&!"N/A".equals(shudu)){
	        			   try{
		        				double speed = Double.parseDouble(shudu);
		        				if(speed<10){   // ���ٶȵ���10km/hʱ���ٹ����ͺ���l/h��
		        					pjyh = pjyh+" l/h";
		        				}else{           //���ڵ���ʱ�ٹ����ͺ���l/100Km
		        					pjyh = pjyh+" l/100km";
		        				}
	        			   }catch(NumberFormatException ex){
	        				   pjyh = "- - l/h";
	        			   }
 	        			
	        		}else{         // �ٹ����ͺ���l/h��
	        			pjyh = pjyh+" l/h";
	        		}
	        	}
	        	
	        	if("N/A".equals(syyl)){
	        		syyl = "- -";
	        	}
	        	
	        	if("N/A".equals(sxwd)){
	        		sxwd = "- -";
	        	}
	
	    	Intent intent = new Intent();
	        intent.setAction(TONGGOU_ACTION_UPDATEUI);
	        intent.putExtra(CarConditionQueryActivity.CONDITION_VALUE_SSYH, ssyh);
	        intent.putExtra(CarConditionQueryActivity.CONDITION_VALUE_PJYH, pjyh);
	        intent.putExtra(CarConditionQueryActivity.CONDITION_VALUE_SYYL, syyl);
	        intent.putExtra(CarConditionQueryActivity.CONDITION_VALUE_SXWD, sxwd);

	        CarConditionQueryActivity.ssyhStr = ssyh;
	        CarConditionQueryActivity.pjyhStr = pjyh;
	        CarConditionQueryActivity.syylStr = syyl;
	        CarConditionQueryActivity.sxwdStr = sxwd;	        
	        sendBroadcast(intent);       //���͹㲥
	        
	        
	        final String ssyhF =  ssyhToSever;             //˲ʱ�ͺ�
	        final String pjyhF = pjyhToSever;              //�ٹ����ͺ�
	        final String youhaoF = youhaoToSever;  
	        final String syylF = syylToSever+"%";          //����
	        final String lichengF = lichengToSever;
	        //Log.d("CONTEETTT","read current condition.......���:"+lichengF);
	        final String sxwdF = sxwdToSever;              //������ˮ��
	        final String dianyaF = dianyaToSever;
	        new Thread(){
				public void run(){
					sendVehicleCondition(youhaoF,lichengF,ssyhF,pjyhF,syylF,sxwdF,dianyaF);
				}
			}.start();
			
			judgeTheOil(syyl);
	    }
	    
	    
	    private void judgeTheOil(String syylStr){
	    	if(syylStr==null||"".equals(syylStr)||"- -".equals(syylStr)){
	    		return;
	    	}
	    	try{
	    		float syylFloat = Float.parseFloat(syylStr);
	    		String lastArea = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "2");  // ������һ�ξ�������  0~15--0 /  15~25--1/  25����--2
	  
	    		
	    		if(syylFloat>=TongGouService.interTwo){     //25��������
	    			getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
					.putString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "2").commit();
	    			//������䲻��ʾ
	    		}else if(syylFloat>=TongGouService.interOne&&syylFloat<TongGouService.interTwo){   //15--25����
	    			getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
					.putString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "1").commit();
	    			
	    			if("0".equals(lastArea)||"2".equals(lastArea)){
	    				//��ʾ
	    				Intent intent =new Intent(this,OilErrorActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("tonggou.oil.alert", "��������"+TongGouService.interTwo+"%");
						startActivity(intent);		
	    			}
	    		}else if(syylFloat>=0&&syylFloat<TongGouService.interOne){            //0--15����
	    			getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
					.putString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "0").commit();
	    			if("1".equals(lastArea)||"2".equals(lastArea)){
	    				//��ʾ
	    				Intent intent =new Intent(this,OilErrorActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("tonggou.oil.alert", "��������"+TongGouService.interOne+"%");
						startActivity(intent);		    					    				
	    			}
	    		}
	    	}catch(NumberFormatException ex){    		
	    	}
	    }
	    
	    
		@Override
		public IBinder onBind(Intent arg0) {
			return null;
		}

		
		/**
		 * ������ʵʱ������Ϣ�㲥
		 * @author think
		 *
		 */
		 private class ReadCurrentCarConditionReceiver extends BroadcastReceiver{
		        public void onReceive(Context context, Intent intent) {
		        	//Log.d("CONTEETTT","RRRRRRRRRRR.....");
		        	if( !TongGouApplication.connetedOBD || myBTService == null){ 
		        		return;
		        	}
		        	String action = intent.getAction();
		        	if( TONGGOU_ACTION_READ_CURRENT_RTD_CONDITION.equals(action) ) {
		        		new Thread(){
	        				public void run(){
	        					//Log.d("CONTEETTT","read current condition.......");
	        					myBTService.write("##RTD\r\n".getBytes());		
	        				}
	        			}.start();
		        	} else if( TONGGOU_ACTION_READ_CURRENT_DTC_CONDITION.equals(action) ) {
		        		TongGouApplication.showLog("�� OBD ����  DTC ָ��--");
		        		new Thread(){
	        				public void run(){
	        					//Log.d("CONTEETTT","read current condition.......");
	        					myBTService.write("##DTC\r\n".getBytes());		
	        				}
	        			}.start();
		        	}
        			
		        }                       
		    }
		 
		 /**
		  * ���ͳ�����Ϣ
		  * @param oilWear    �ͺ�
		  * @param currentMileage  ���
		  * @param instantOilWear  ˲ʱ�ͺ�
		  * @param oilWearPerHundred  �ٹ����ͺ�
		  * @param oilMassStr     ����
		  * @param engineCoolantTemperature   ��������ȴҺ
		  * @param batteryVoltage      ��ƿ��ѹ
		  */
		private void sendVehicleCondition(String oilWear,String currentMileage,String instantOilWear,
				                          String oilWearPerHundred,String oilMassStr,String engineCoolantTemperature,String batteryVoltage){
			String vehicleVin = TongGouApplication.connetedVIN;
			String userNo = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME, "");
			String obdSN = TongGouApplication.connetedObdSN;
			String vehicleId = TongGouApplication.connetedVehicleID;
		
			long reportTime = System.currentTimeMillis();
	        
			String oilStr="";
			if(oilWear!=null&&!"".equals(oilWear)&&!"N/A".equals(oilWear)){
				oilStr = ",\"oilWear\":"+Double.valueOf(oilWear) ;
			}
			String currentMilStr="";
			if(currentMileage!=null&&!"".equals(currentMileage)&&!"N/A".equals(currentMileage)){
				currentMilStr = ",\"currentMileage\":"+ (int)FloatMath.floor( Float.valueOf(currentMileage) );
			}
			String instantOilWearStr="";
			if(instantOilWear!=null&&!"".equals(instantOilWear)&&!"N/A".equals(instantOilWear)){
				instantOilWearStr = ",\"instantOilWear\":"+Double.valueOf(instantOilWear);
			}
			String oilWearPerHundredStr="";
			if(oilWearPerHundred!=null&&!"".equals(oilWearPerHundred)&&!"N/A".equals(oilWearPerHundred)){
				oilWearPerHundredStr = ",\"oilWearPerHundred\":"+Double.valueOf(oilWearPerHundred) ;
			}
			String oilMassStrs="";
			if(oilMassStr!=null&&!"%".equals(oilMassStr)&&!"N/A%".equals(oilMassStr)){
				oilMassStrs = ",\"oilMass\":\""+ oilMassStr + "\"";
			}
			String engineCoolantTemStr="";
			if(engineCoolantTemperature!=null&&!"".equals(engineCoolantTemperature)&&!"N/A".equals(engineCoolantTemperature)){
				engineCoolantTemStr = ",\"engineCoolantTemperature\":"+Double.valueOf(engineCoolantTemperature);
			}
			String batteryVoltageStr="";
			if(batteryVoltage!=null&&!"".equals(batteryVoltage)&&!"N/A".equals(batteryVoltage)){
				batteryVoltageStr = ",\"batteryVoltage\":"+Double.valueOf(batteryVoltage);
			}
			
			String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/vehicle/condition";
			CommonParser commonParser = new CommonParser();
			long vehicleIdLong = 0;
			try{
				vehicleIdLong = Long.valueOf(vehicleId);
			} catch( NumberFormatException e  ) {
				TongGouApplication.showLog("���� id û�л�ȡ��");
			}
			String valuePairs = "{\"vehicleId\":"+ vehicleIdLong +
					",\"vehicleVin\":\""+vehicleVin+"\"" +
					",\"userNo\":\""+userNo+"\"" +
					",\"obdSN\":\""+obdSN+"\"" +					
					oilStr +				
					currentMilStr +					
					instantOilWearStr +
					oilWearPerHundredStr +
					oilMassStrs +
					engineCoolantTemStr +
					batteryVoltageStr +					
					",\"reportTime\":" + reportTime + "}";
			TongGouApplication.showLog("���ͱ��������     ---  " + valuePairs);
			 Network.getNetwork(this).httpPutUpdateString(url,valuePairs.getBytes(),commonParser);
			
		}
	
		
}
