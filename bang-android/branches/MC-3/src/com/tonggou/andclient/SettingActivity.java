package com.tonggou.andclient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.app.TongGouService;
import com.tonggou.andclient.network.DefaultUpdateCheck;
import com.tonggou.andclient.network.Network;
import com.tonggou.andclient.parse.LogOutParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.util.SaveDB;

public class SettingActivity extends BaseActivity{
	private static final int NETWORK_FAILD = -1;
	private static final int LOGIN_SUCCEED = 0x001;
	private static final int LOGIN_FAILD = 0x002;
	private static final int ALERT_NEW_VERSION = 0x003;
	private Handler handler;
	private String userNo, isCheck;
	private View data, voice, bindcar, feedback, version, aboutus, bindOBD, bindShop,bindobdStatus;
	private ToggleButton ifvoice;
	private View logOut;
	private AlertDialog checkAlert;
	private TextView userDataTv;
	public static final int REQUEST_CODE_BIND_OBD = 1003;
	ProgressDialog progressDialog;
	private BluetoothAdapter adapter;
	Runnable runnable,runnable2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = BluetoothAdapter.getDefaultAdapter();
		userNo = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(BaseActivity.NAME,
				null);
		isCheck = getSharedPreferences(BaseActivity.SETTING_INFOS, 0).getString(
				BaseActivity.CHECKVOICE, null);
		setContentView(R.layout.set);

		data = findViewById(R.id.data);
		userDataTv = (TextView) data.findViewById(R.id.set_tv);
		((ImageView) data.findViewById(R.id.set_iv)).setImageDrawable(getResources().getDrawable(
				R.drawable.setdata));

		voice = findViewById(R.id.voice);
		((TextView) voice.findViewById(R.id.set_tv)).setText(getString(R.string.set_voice));
		((ImageView) voice.findViewById(R.id.set_iv)).setImageDrawable(getResources().getDrawable(
				R.drawable.setvoice));
		voice.findViewById(R.id.set_more).setVisibility(View.GONE);
		ifvoice = (ToggleButton) voice.findViewById(R.id.set_toggle_button);
		if (!"NO".equals(isCheck)) {
			ifvoice.setChecked(true);
		}
		ifvoice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
						.putString(BaseActivity.CHECKVOICE, 
								ifvoice.isChecked() ? "YES" : "NO").commit();
			}
		});


		ifvoice.setVisibility(View.VISIBLE);

		bindcar = findViewById(R.id.bindcar);
		((TextView) bindcar.findViewById(R.id.set_tv)).setText("��������");
		((ImageView) bindcar.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.setbindcar));
		bindcar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, BindCarsActivity.class);
				startActivity(intent);
			}
		});

		bindOBD = findViewById(R.id.bind_obd);
		((TextView) bindOBD.findViewById(R.id.set_tv)).setText("��OBD");
		((ImageView) bindOBD.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.bind_obd));
		bindOBD.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if( !TongGouApplication.getInstance().isLogin() )  {
					toLogin();
				} else {
					Intent intent = new Intent(SettingActivity.this, ConnectOBDDialogActivity.class);
					intent.putExtra("tonggou.connectobd.from", "fromSettings");
					startActivity(intent);
				}
			}
		});

		bindShop = findViewById(R.id.bind_shop);
		((TextView) bindShop.findViewById(R.id.set_tv)).setText("�󶨵���");
		((ImageView) bindShop.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.bind_shop));
		bindShop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if( !TongGouApplication.getInstance().isLogin() )  {
					toLogin();
				} else {
					Intent intent = new Intent(SettingActivity.this, CaptureActivity.class);
					intent.putExtra("tonggou.connectobd.from", "fromSettings");
					startActivity(intent);
				}
			}
		});
		
		/*
		 *  @add
		 *  @author  fanbaolong   2014-1-2 
		 */		
		bindobdStatus = findViewById(R.id.bindOBD_status);
		((ImageView) bindobdStatus.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.bigbluetooth));

		if(TongGouApplication.connetedOBD){
			((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�Ͽ��豸");
			((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth2));
		}else{
			((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�����豸");
			((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth1));
		}

		bindobdStatus.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				 
				//�ж������Ƿ��Ѿ���
				if(adapter.isEnabled()){          
					if(TongGouApplication.connetedOBD){
						// ͣ������obd
						Intent intentS = new Intent();
						intentS.setAction(TongGouService.TONGGOU_ACTION_START);
						intentS.putExtra("com.tonggou.server","STOP");
						sendBroadcast(intentS);

						TongGouApplication.showLog(TongGouApplication.connetedOBD);
						Toast.makeText(SettingActivity.this, "�豸�ѶϿ�", Toast.LENGTH_SHORT).show();

						((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
								.getDrawable(R.drawable.bluetooth1));
						((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�����豸");

					}else{
                       
						showLoadingDialog("���������豸...");
						//��������OBD
						Intent intent = new Intent();// ����Intent����
						intent.setAction(TongGouService.TONGGOU_ACTION_START);
						intent.putExtra("com.tonggou.server", "SCAN_OBD");
						sendBroadcast(intent);// ���͹㲥

						//��ʱ�����߳�
						Runnable mRunnable = new Runnable() {
							public void run() {
								if(!TongGouApplication.connetedOBD){
									Toast.makeText(SettingActivity.this, "�豸����ʧ��,�����Ƿ���豸", Toast.LENGTH_SHORT).show();
									dismissAllDialog();
								}else{
									Toast.makeText(SettingActivity.this, "�豸������", Toast.LENGTH_SHORT).show();
									dismissAllDialog();
								}
								handler.removeCallbacks(this); // �Ƴ���ʱ����
							}
						};
						handler.postDelayed(mRunnable, 3100); //��3.1���ִ��this����runable
					}

				}else{
					Toast.makeText(SettingActivity.this, "���ȴ������豸...", Toast.LENGTH_SHORT).show();
				}
			}
		});

		feedback = findViewById(R.id.feedback);
		((TextView) feedback.findViewById(R.id.set_tv)).setText(getString(R.string.set_feedback));
		((ImageView) feedback.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.setfeedback));
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
				startActivity(intent);
			}
		});

		version = findViewById(R.id.version);
		((TextView) version.findViewById(R.id.set_tv)).setText(getString(R.string.set_version));
		((ImageView) version.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.setversion));
		version.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doCheckUpdate();
			}
		});

		aboutus = findViewById(R.id.aboutus);
		((TextView) aboutus.findViewById(R.id.set_tv)).setText(getString(R.string.set_aboutus));
		((ImageView) aboutus.findViewById(R.id.set_iv)).setImageDrawable(getResources()
				.getDrawable(R.drawable.setaboutus));
		aboutus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
				startActivity(intent);
			}
		});

		logOut = findViewById(R.id.register_next);
		logOut.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ////////////////////////////////////
				exit();
				TongGouApplication.getInstance().deInit();
				// /////////////////////////////////////
				new Thread() {
					public void run() {
						SaveDB.getSaveDB(SettingActivity.this).closeMyDb();
						// ͣ������obd
						Intent intent = new Intent();
						intent.setAction(TongGouService.TONGGOU_ACTION_START);
						intent.putExtra("com.tonggou.server", "STOP");
						sendBroadcast(intent);

						logout();
					}
				}.start();

				MainActivity.ifAutoLogin = false;
				TongGouApplication.getInstance().setLogin(false);
				Intent toLogin = new Intent(SettingActivity.this, LoginActivity.class);
				toLogin.putExtra(LoginActivity.ARG_KEY_IS_LOGOUT, true);
				startActivity(toLogin);

			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NETWORK_FAILD:
					Toast.makeText(SettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;
				case LOGIN_SUCCEED:
					Toast.makeText(SettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;
				case LOGIN_FAILD:
					Toast.makeText(SettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};

	}
	
	private void doCheckUpdate() {
		DefaultUpdateCheck updateCheck = new DefaultUpdateCheck(this);
		updateCheck.doCheckUpdate(updateCheck.new OnUpdateCheckListener() {
			@Override
			public void onNothingToUpdate() {
				showSuccessMessageDialog("��ǰ�汾�������°汾");
			}
			
		});
	}
	
	private void toLogin() {
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, LoginActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		task();  //��ʱˢ���豸����״̬

		if(TongGouApplication.connetedOBD){
			((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�Ͽ��豸");
			((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth2));
		}else{
			((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�����豸");
			((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth1));
		}
		
		if( !TongGouApplication.getInstance().isLogin() ) {
			logOut.setVisibility(View.GONE);
			userDataTv.setText("�û���¼");
			data.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toLogin();
				}
			});
		} else {
			logOut.setVisibility(View.VISIBLE);
			userDataTv.setText(getString(R.string.set_data));
			data.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					Intent intent = new Intent(SettingActivity.this, UserDataActivity.class);
					startActivity(intent);
				}
			});
		}
		
		
//		Intent intentS = new Intent();
//		intentS.setAction(TongGouService.TONGGOU_ACTION_START);
//		intentS.putExtra("com.tonggou.server","STOP");
//		sendBroadcast(intentS);
	}

	// private void deInit(){
	// TongGouService.allowPollingMessage = false;
	// if(TongGouApplication.obdLists!=null){
	// TongGouApplication.obdLists.clear();
	// }
	// MainActivity.haveFaultCode = false;
	// sharedPreferences.edit().putBoolean(BaseActivity.LOGINED,
	// false).commit();
	// getSharedPreferences(BaseActivity.SETTING_INFOS, 0).edit()
	// // .putString(BaseActivity.NAME, null)
	// // .putString(BaseActivity.PASSWORD, null)
	// .putString(BaseActivity.PHONENAME, null)
	// .putString(BaseActivity.SERVICE, null)
	// .putString(BaseActivity.PHONE, null)
	// .putString(BaseActivity.BRAND, null)
	// .putString(BaseActivity.MODEL, null)
	// .putString(BaseActivity.VEHICLENUM, null)
	// .putString(BaseActivity.CHECKVOICE, null)
	// .putString(BaseActivity.VEHICLE_MODE_ID, "")
	// .putString(BaseActivity.APPCONFIG_OIL_LAST_STATUS, "2")
	// .putInt(BaseActivity.NEW_MESSAGE_COUNT,0).commit();
	//
	// CarConditionQueryActivity.ssyhStr = "- -";
	// CarConditionQueryActivity.pjyhStr = "- - l/h";
	// CarConditionQueryActivity.syylStr = "- -";
	// CarConditionQueryActivity.sxwdStr = "- -";
	// TongGouApplication.connetedVehicleName = "";
	// TongGouApplication.connetedVIN = "";
	// TongGouApplication.connetedObdSN = "";
	// MainActivity.defaultBrandAndModle = "";
	// BaseConnectOBDService.cmile = null;
	// //TongGouApplication.loginSuccess = false;
	// }

	private void logout() {
		String url = INFO.HTTP_HEAD + INFO.HOST_IP + "/logout";
		LogOutParser logOutParser = new LogOutParser();
		String valuePairs = "{\"userNo\":\"" + userNo + "\"}";
		Network.getNetwork(SettingActivity.this).httpPutUpdateString(url, valuePairs.getBytes(),
				logOutParser);
	}

	protected void sendMessage(int what, String content) {
		if (what < 0) {
			what = BaseActivity.SEND_MESSAGE;
		}
		Message msg = Message.obtain(handler, what, content);
		if (msg != null) {
			msg.sendToTarget();
		}
	}
	

	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable2); //�����߳�
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnable2); //�����߳�
	}
	
	
	// taskΪѭ�������߳�, ��ÿ��3s����һ��
		public void task(){
			runnable2 = new Runnable() {
				@Override
				public void run() {
					update(); //����ͼ��
					handler.postDelayed(this, 3000); // 3s��ִ��this����runable
				}
			};
			handler.postDelayed(runnable2, 3000);// �򿪶�ʱ����3s��ִ��runnable����
		}

		public void update() {

			handler.post(new Runnable() {

				public void run() {
					
				if(adapter.isEnabled()){
					if(TongGouApplication.connetedOBD){
						
						((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�Ͽ��豸");
						((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
								.getDrawable(R.drawable.bluetooth2));
					}else{
						((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�����豸");
						((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
								.getDrawable(R.drawable.bluetooth1));
					}
				}else {
					Toast.makeText(SettingActivity.this, "����δ��", Toast.LENGTH_SHORT).show();
					((TextView) bindobdStatus.findViewById(R.id.set_tv)).setText("�����豸");
					((ImageView) bindobdStatus.findViewById(R.id.set_more)).setImageDrawable(getResources()
							.getDrawable(R.drawable.bluetooth1));
					handler.removeCallbacks(runnable2);
				}

				}

			});
			TongGouApplication.showLog("�Ƿ��"+TongGouApplication.connetedOBD);
		}

}
