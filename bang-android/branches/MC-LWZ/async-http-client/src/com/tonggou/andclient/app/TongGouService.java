package com.tonggou.andclient.app;




import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
//import android.util.Log;

import com.tonggou.andclient.BaseActivity;
import com.tonggou.andclient.HomePageActivity;
import com.tonggou.andclient.R;
import com.tonggou.andclient.network.Network;
import com.tonggou.andclient.network.NetworkState;
import com.tonggou.andclient.parse.PollingMessagesParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.util.SaveDB;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.TonggouMessage;


public class TongGouService extends BaseConnectOBDService {
	private UpdateNoticeAndMessageTask getMessageTask;   //��ѯ
	private ReadOBDTask readOBDTask;   					 //��ѯ��ȡobd
	private Timer getMessageTimer;
	private Timer readOBDTimer;
	
	public static boolean allowPollingMessage = true;


	private long pollingMessageInterval = 600000;   //Ĭ��10����
	private long readOBDInterval = 600000;   		//Ĭ��10����
	private static final long CONNECT_OBD_INTERVAL = 15 * 1000; // Ĭ��15��
	//public static long alertFaultInterval = 12;           //Ĭ��12Сʱ
	public static int interOne = 15;
	public static int interTwo = 25;

	private StartSacnReceiver startReceiver;
	
	public void onCreate(){
		//Log.d("CONTEETTT","oncreate.....");
		super.onCreate();
		

		startReceiver = new StartSacnReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(TongGouService.TONGGOU_ACTION_START);
		//ע��Broadcast Receiver
		registerReceiver(startReceiver, filter);
	}


	public void onStart(Intent intent, int startId) {
		//Log.d("CONTEETTT","onStart.....");
	}




	public void onDestroy() {
		//Log.d("CONTEETTT","onDestroy.....");
		super.onDestroy();
		this.unregisterReceiver(startReceiver);//ȡ��ע��Receiver
		try {
			Thread.sleep(10000);
			Intent intentSer = new Intent(this, com.tonggou.andclient.app.TongGouService.class);
			startService(intentSer);
		} catch (InterruptedException e) {
		}
	}


	public IBinder onBind(Intent arg0) {
		return null;
	}



	private synchronized void startGetMessageTimer(){
		stopGetMessageTimer();
		String pollingMessageIntervalStr = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.APPCONFIG_SERVER_READ_INTERVAL, pollingMessageInterval+"");
		if(pollingMessageIntervalStr!=null&&SomeUtil.isNumeric(pollingMessageIntervalStr)&&!"0".equals(pollingMessageIntervalStr)){
			pollingMessageInterval = Long.valueOf(pollingMessageIntervalStr);
		}
		try{
		getMessageTask = new UpdateNoticeAndMessageTask();
		getMessageTimer = new Timer();		
		getMessageTimer.schedule(getMessageTask,0,pollingMessageInterval);
		}catch(Exception ex){}
	}

	private class UpdateNoticeAndMessageTask extends TimerTask{
		public void run(){  	
			getNotification("NULL");
		}
	}
	
	private void stopGetMessageTimer(){
		if(getMessageTask != null){
			getMessageTask.cancel();
		}
		getMessageTask = null;
		if(getMessageTimer != null){
			getMessageTimer.cancel();
		}
		getMessageTimer = null;
    }

	/**
	 * ����Զ�ȡobd��Ϣ
	 */
	private synchronized void startReadOBDTimer(){
		stopReadOBDTimer();
		String readObdIntervalStr = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.APPCONFIG_OBD_READ_INTERVAL, readOBDInterval+"");
		if(readObdIntervalStr!=null&&SomeUtil.isNumeric(readObdIntervalStr)&&!"0".equals(readObdIntervalStr)){
			readOBDInterval = Long.valueOf(readObdIntervalStr);
		}
		try{
		readOBDTask = new ReadOBDTask();
		readOBDTimer = new Timer();	
		readOBDTimer.schedule(readOBDTask,0,readOBDInterval);
		}catch(Exception ex){}
	}

	private void stopReadOBDTimer(){
		if(readOBDTask != null){
			readOBDTask.cancel();
		}
		readOBDTask = null;
		if(readOBDTimer != null){
			readOBDTimer.cancel();
		}
		readOBDTimer = null;
	}

	 private class ReadOBDTask extends TimerTask{
   	 	public void run(){  	 	
   	 	//Log.d("CONTEETTT", "OBD conditon .............................");
	   	 	if(TongGouApplication.connetedOBD){	 			
	 			//��ȡ����
	   	 		Intent intent = new Intent();
		        intent.setAction(TongGouService.TONGGOU_ACTION_READ_CURRENT_RTD_CONDITION);
		        sendBroadcast(intent);
	 		}
	   	 	
   	 	}
    }
    //////////////////////////////////////////////////////////////////////////////////////////////


    
    
	    /**
		 * �������obd
		 */
		private ConnectOBDTask connectOBDTask;   					 
		private Timer connectOBDTimer;
		private synchronized void startConnectOBDTimer(){
			stopConnectOBDTimer();
			try{
			connectOBDTask = new ConnectOBDTask();
			connectOBDTimer = new Timer();			
			connectOBDTimer.schedule(connectOBDTask,0,CONNECT_OBD_INTERVAL);
			}catch(Exception ex){}
		}

		private void stopConnectOBDTimer(){
			if(connectOBDTask != null){
				connectOBDTask.cancel();
			}
			connectOBDTask = null;
			if(connectOBDTimer != null){
				connectOBDTimer.cancel();
			}
			connectOBDTimer = null;
		}

		 private class ConnectOBDTask extends TimerTask{
	   	 	public void run(){  	 	
	   	 	//Log.d("testthread", "OBD connect............................."+BaseConnectOBDService.connetState+":"+BaseConnectOBDService.addingCar);
		   	 	if(!TongGouApplication.connetedOBD){	 			
		   	 		if(BaseConnectOBDService.connetState==1&&!BaseConnectOBDService.addingCar){	 			
			   	 		//Log.d("testthread", "OBD start connecting.............................");
			   	 		new Thread(){
	        				public void run(){
	        					startConnect();
	        				}
	        			}.start();
		   	 		}
		 		}
	   	 	}
	    }
	    //////////////////////////////////////////////////////////////////////////////////////////////
    
    

	    //ȡ֪ͨ
		private void getNotification(String MessageType){
			if(!allowPollingMessage){
				 return;
			}
			String userName = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME, null);
	        //Log.d("CONTEETTT", "messaging........��"+System.currentTimeMillis()+":"+userName);	        
	        String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/message/polling/types/"+MessageType+"/userNo/"+userName;

	        PollingMessagesParser pollingMessagesParser = new PollingMessagesParser();		
			NetworkState ns = Network.getNetwork(TongGouService.this).httpGetUpdateString(url,pollingMessagesParser);	

			if(ns.isNetworkSuccess()){
				if(pollingMessagesParser.isSuccessfull()){
					List<TonggouMessage> messages = pollingMessagesParser.getPollingMessagesResponse().getMessageList();
					
					if(messages!=null&&messages.size()>0){
						////////////����Ϣ���ʱ��
						for(int i=0;i<messages.size();i++){
							messages.get(i).setTime(System.currentTimeMillis()+"");
						}
						
						////////////////////////
						
						 //Log.d("FFF", "parser ------------------------------size��"+messages.size());
						 int nowMessageCount = messages.size();
						 int allmesInDB = SaveDB.getSaveDB(this).getAllMessagesCount(userName);
						 if((allmesInDB+nowMessageCount)>100){  //����100��
							 int deleteCount = allmesInDB+nowMessageCount-100;
							 SaveDB.getSaveDB(this).deleteEarlyMessage(userName, deleteCount); //ɾ��������Ϣ
							 if(SaveDB.getSaveDB(this).saveMessages(messages,userName)){
								 //��������Ϣ��
								 getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit().putInt(BaseActivity.NEW_MESSAGE_COUNT, messages.size()).commit();
								 notice(messages.size()+"������Ϣ","����鿴");	
								 //֪ͨ��ҳ��ʾ
								 Intent intent = new Intent();
							     intent.setAction(TongGouService.TONGGOU_ACTION_NEW_MESSAGE);
							     sendBroadcast(intent);
							 }
						 }else{
							 if(SaveDB.getSaveDB(this).saveMessages(messages,userName)){
								 //��������Ϣ��
								 getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit().putInt(BaseActivity.NEW_MESSAGE_COUNT, messages.size()).commit();
								 notice(messages.size()+"������Ϣ","����鿴");	
								//֪ͨ��ҳ��ʾ
								 Intent intent = new Intent();
							     intent.setAction(TongGouService.TONGGOU_ACTION_NEW_MESSAGE);
							     sendBroadcast(intent);
							 }
						 }
						//sendMessage(ACTION_SUCCEED,getMessagesParser.getServerDetailReponse().getMessage());				
					}else{ 
						//Log.d("FFF", "parser ------------------------------no message��");
					}

				}
			}
	 }




	private void notice(String newMesContent,String message){
		NotificationManager notiManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);			
		android.app.Notification nf = new android.app.Notification(R.drawable.icon , getString(R.string.app_name) + "��Ϣ"/*С����*/,System.currentTimeMillis());
		Intent toNewMess = new Intent(TongGouService.this, HomePageActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(TongGouService.this, 0,toNewMess, 0);			
		nf.setLatestEventInfo(TongGouService.this, newMesContent/*��������ʾ�ı���*/, message/*��������ʾ������*/, contentIntent);
		nf.defaults = android.app.Notification.DEFAULT_SOUND;
		notiManager.notify(0x7f030301, nf);	

	}

	



    
    /**
     * ���ڵ�¼������ɨ��
     * @author think
     *
     */
    private class StartSacnReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent) {
        	//Log.d("testthread", "START BROADCAST....");
        	String act = intent.getStringExtra("com.tonggou.server");
        	if(act!=null&&!"".equals(act)&&"PULLING".equals(act)){
        		//������ѯ
        		startGetMessageTimer();
        		startReadOBDTimer();
        		
        	}else if(act!=null&&!"".equals(act)&&"SCAN_OBD".equals(act)){
        		//����obd
        		if(!TongGouApplication.connetedOBD){
        			new Thread(){
        				public void run(){
        					startConnect();
        				}
        			}.start();
	        	}
        		startConnectOBDTimer();
        	}else if(act!=null&&!"".equals(act)&&"STOP".equals(act)){
        		//ͣ������
        		stopConnect();
        		stopConnectOBDTimer();
        	}
        }                       
    }
    
}
