package com.tonggou.andclient;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tonggou.andclient.app.BaseConnectOBDService;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.app.TongGouService;
import com.tonggou.andclient.app.UpdateFaultDic;
import com.tonggou.andclient.db.VehicleManager;
import com.tonggou.andclient.jsonresponse.AddBindCarResponse;
import com.tonggou.andclient.jsonresponse.BaseResponse;
import com.tonggou.andclient.jsonresponse.VehicleResponse;
import com.tonggou.andclient.network.NetworkState;
import com.tonggou.andclient.network.parser.AsyncJsonBaseResponseParseHandler;
import com.tonggou.andclient.network.request.ObdBindingRequest;
import com.tonggou.andclient.network.request.QuerySingleVehicleRequest;
import com.tonggou.andclient.network.request.StoreVehicleInfoRequest;
import com.tonggou.andclient.util.NetworkUtil;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.VehicleInfo;

public class ChangeBindCarsActivity extends BaseActivity {
	
	public static final String EXTRA_VEHICLE_INFO = "vehicle_info";

	private String vehicleNoStr, bindcarnextmileStr, bindcarcurrentmileStr, bindcarnexttimeStr,
			bindcarnexttime2Str, vehicleModelStr, vehicleBrandStr, vehicleEnginNoStr,
			vehicleRegistNoStr;
	private TextView bindcar_submit, bindcarnexttime, bindcarnexttime2, bindcarnum1, bindcarnum2,
			mTxtOBDBinded, mTxtShopBinded;
	private View back;

	private VehicleInfo mVehicleInfo;
	private EditText bindcarnum, bindcarmilenow, bindcarnextmile;
	private EditText bindcarvehiclevin, bindcarengineno, bindcarregistno;
	private ProgressBar progress;

	// private String shop2DCodeStr=""; //���̶�ά��
	private String shop2DCodeId = ""; // ����id
	private String vehicleBrandIdStr; // Ʒ��id
	private String vehicleModelIdStr; // ����id
	private String vehicleIdStr;
	private String ok = "no";
	private String obdSNStr; // ��ǰ��������װ��obd��Ψһ��ʶ��
	// private String obdVin = "NULL";
	private String vehicleVinStr;

	private boolean meterOk = true, meterOk2 = true, timeOk = true, timeOk2 = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changebindcars);

		// ifAddInterface = false;
		findViewById(R.id.back).setFocusableInTouchMode(true);
		bindcarvehiclevin = (EditText) findViewById(R.id.bindcarvehiclevin);// ���ܺ�
		bindcarengineno = (EditText) findViewById(R.id.bindcarengineno); // ��������
		bindcarregistno = (EditText) findViewById(R.id.bindcarregistno); // �Ǽ�֤���
		progress = (ProgressBar) findViewById(R.id.shopdetilmappro);
		progress.setVisibility(View.GONE);
		mVehicleInfo = getIntent().getParcelableExtra(EXTRA_VEHICLE_INFO);
		
		vehicleIdStr = mVehicleInfo.getVehicleId();
		bindcarnum = (EditText) findViewById(R.id.bindcar_num);
		bindcarmilenow = (EditText) findViewById(R.id.bindcarmilenow);
		bindcarnextmile = (EditText) findViewById(R.id.bindcarnextmile);
		bindcarnexttime = (TextView) findViewById(R.id.bindcarnexttime);
		bindcarnexttime2 = (TextView) findViewById(R.id.bindcarnexttime2);
		mTxtOBDBinded = (TextView) findViewById(R.id.txt_Change_bindcars_OBDBinded);
		mTxtShopBinded = (TextView) findViewById(R.id.txt_Change_bindcars_ShopBinded);

		bindcarnum.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
					bindcarvehiclevin.requestFocus();
				}
				return false;
			}
		});

		bindcarvehiclevin.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
					bindcarengineno.requestFocus();
				}
				return false;
			}
		});
		bindcarengineno.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
					bindcarregistno.requestFocus();
				}
				return false;
			}
		});
		bindcarregistno.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
					bindcarmilenow.requestFocus();
				}
				return false;
			}
		});

		bindcarmilenow.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
					bindcarnextmile.requestFocus();
				}
				return false;
			}
		});

		bindcarnum.clearFocus();
		bindcarvehiclevin.clearFocus();
		bindcarengineno.clearFocus();
		bindcarregistno.clearFocus();
		bindcarmilenow.clearFocus();

		bindcar_submit = (TextView) findViewById(R.id.bindcar_submit);
		bindcar_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vehicleNoStr = bindcarnum.getText().toString().toUpperCase();
				if( !SomeUtil.isVehicleNo(vehicleNoStr) ) {
					TongGouApplication.showToast( "��������ȷ�ĳ��ƺ�");
					return;
				}

				vehicleModelStr = bindcarnum1.getText().toString().trim();
				vehicleBrandStr = bindcarnum2.getText().toString().trim();

				bindcarnextmileStr = bindcarnextmile.getText().toString().trim();
				bindcarcurrentmileStr = bindcarmilenow.getText().toString().trim();
				bindcarnexttimeStr = bindcarnexttime.getText().toString().trim();
				bindcarnexttime2Str = bindcarnexttime2.getText().toString().trim();

				vehicleVinStr = bindcarvehiclevin.getText().toString().trim();
				vehicleEnginNoStr = bindcarengineno.getText().toString().trim();
				vehicleRegistNoStr = bindcarregistno.getText().toString().trim();

				Time localTime = new Time("Asia/Hong_Kong");
				localTime.setToNow();
				long nextInsuranceTimeLong = SomeUtil.StringDateToLong(bindcarnexttimeStr); // ����ʱ��
				long nowTime = SomeUtil.StringDateToLong(localTime.format("%Y-%m-%d %H:%M")
						.toString()); // ����ʱ��
				long nextExamineTimeLong = SomeUtil.StringDateToLong(bindcarnexttime2Str); // �鳵ʱ��

				if (vehicleModelStr == null || "".equals(vehicleModelStr)) {
					Toast.makeText(ChangeBindCarsActivity.this, "��ѡ����Ʒ��", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (vehicleBrandStr == null || "".equals(vehicleBrandStr)) {
					Toast.makeText(ChangeBindCarsActivity.this, "��ѡ����", Toast.LENGTH_SHORT).show();
					return;
				}

				if (vehicleVinStr != null && !"".equals(vehicleVinStr)) {
					try {
						Integer.parseInt(vehicleVinStr);
						meterOk = true;
					} catch (Exception e) {
						meterOk = false;
					}
				} else {
					meterOk = true;
				}
				if (!TextUtils.isEmpty(vehicleEnginNoStr)) {
					try {
						Integer.parseInt(vehicleEnginNoStr);
						meterOk = true;
					} catch (Exception e) {
						meterOk = false;
					}
				} else {
					meterOk = true;
				}
				if (vehicleRegistNoStr != null && !"".equals(vehicleRegistNoStr)) {
					try {
						Integer.parseInt(vehicleRegistNoStr);
						meterOk = true;
					} catch (Exception e) {
						meterOk = false;
					}
				} else {
					meterOk = true;
				}

				if (bindcarcurrentmileStr != null && !"".equals(bindcarcurrentmileStr)) {
					try {
						Integer.parseInt(bindcarcurrentmileStr);
						meterOk = true;
					} catch (Exception e) {
						meterOk = false;
						Toast.makeText(ChangeBindCarsActivity.this, "��ǰ������벻��ȷ", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					meterOk = true;
				}

				if (meterOk) {
					if (bindcarnextmileStr != null && !"".equals(bindcarnextmileStr)) {
						try {
							Integer.parseInt(bindcarnextmileStr);
							meterOk2 = true;
						} catch (Exception e) {
							meterOk2 = false;
							Toast.makeText(ChangeBindCarsActivity.this, "�´α���������벻��ȷ",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						meterOk2 = true;
					}
				}
				if (meterOk && meterOk2) {
					if (bindcarnexttimeStr != null && !"".equals(bindcarnexttimeStr)) {
						if (nextInsuranceTimeLong >= nowTime) {
							timeOk = true;
						} else {
							timeOk = false;
							Toast.makeText(ChangeBindCarsActivity.this, "�´α��յ�ʱ�䲻�����ڵ�ǰʱ��",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						timeOk = true;
					}

				}
				if (timeOk && meterOk && meterOk2) {

					if (bindcarnexttime2Str != null && !"".equals(bindcarnexttime2Str)) {
						if (nextExamineTimeLong >= nowTime) {
							timeOk2 = true;
						} else {
							timeOk2 = false;
							Toast.makeText(ChangeBindCarsActivity.this, "�´��鳵��ʱ�䲻�����ڵ�ǰʱ��",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						timeOk2 = true;
					}

				}

				if (meterOk && meterOk2 && timeOk && timeOk2) {
					
					TongGouApplication.showLog( "@@@ " + bindcarnexttime.getText().toString() + "   " + bindcarnexttime2.getText().toString() );
					mVehicleInfo.setVehicleVin(vehicleVinStr);
					mVehicleInfo.setNextInsuranceTime( "" + SomeUtil.StringDateToLong(bindcarnexttime.getText().toString()));
					mVehicleInfo.setNextExamineTime( "" + SomeUtil.StringDateToLong( bindcarnexttime2.getText().toString()));
					mVehicleInfo.setNextMaintainMileage(bindcarnextmileStr);
					mVehicleInfo.setCurrentMileage( bindcarcurrentmileStr);
					mVehicleInfo.setEngineNo(vehicleEnginNoStr);
					mVehicleInfo.setRegistNo(vehicleRegistNoStr);
					mVehicleInfo.setVehicleBrand(vehicleBrandStr);
					mVehicleInfo.setVehicleModel(vehicleModelStr);
					mVehicleInfo.setVehicleModelId(vehicleModelIdStr);
					mVehicleInfo.setVehicleNo( vehicleNoStr );
					
					// �ж��û��Ƿ��¼
					if( !TongGouApplication.getInstance().isLogin() ) {
						
						VehicleManager manager = new VehicleManager();
						if(manager.update(mVehicleInfo)) {
							Intent intent = new Intent();
							intent.putExtra("tonggou.isOk", "yes");
							setResult(Activity.RESULT_OK, intent);
							finish();
						}
						return;
					}
					// ��¼�û��ж��Ƿ�������
					if( !NetworkUtil.isNetworkConnected(ChangeBindCarsActivity.this) ) {
						TongGouApplication.showToast(NetworkState.ERROR_CLIENT_ERROR_TIMEOUT_MESSAGE);
						return;
					}
					
					if (obdSNStr != null && !"".equals(obdSNStr)
							&& !"null".equals(obdSNStr)) {
						showLoadingDialog("������...");
//						new Thread() {
//							public void run() {
//								
//									// ͣ������obd
//									Intent intentS = new Intent();
//									intentS.setAction(TongGouService.TONGGOU_ACTION_START);
//									intentS.putExtra("com.tonggou.server", "STOP");
//									sendBroadcast(intentS);
//	
//							}
//						}.start();
						bindObd(vehicleIdStr, vehicleVinStr, vehicleNoStr, vehicleModelStr, vehicleModelIdStr,
								vehicleBrandStr, vehicleBrandIdStr, obdSNStr,
								sharedPreferences.getString(BaseActivity.NAME, ""));
					} else {
						updateVehicleInfo(vehicleIdStr, mVehicleInfo.getVehicleVin(), vehicleNoStr,
								vehicleModelStr, vehicleModelIdStr, vehicleBrandStr, vehicleBrandIdStr,
								obdSNStr, currentUsername);
					}
				}
			}
		});
		back = findViewById(R.id.left_button);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangeBindCarsActivity.this.finish();

				// ����obd
				BaseConnectOBDService.addingCar = false;
				Intent intent = new Intent();// ����Intent����
				intent.setAction(TongGouService.TONGGOU_ACTION_START);
				intent.putExtra("com.tonggou.server", "SCAN_OBD");
				sendBroadcast(intent);// ���͹㲥

			}
		});
		bindcarnexttime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ChangeBindCarsActivity.this);
				View view = LayoutInflater.from(ChangeBindCarsActivity.this).inflate(
						R.layout.date_time_dialog, null);
				final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
				final TimePicker timePicker = (android.widget.TimePicker) view
						.findViewById(R.id.time_picker);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH), null);

				timePicker.setIs24HourView(true);
				timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
				builder.setView(view);

				builder.setTitle("ѡȡʱ��");
				builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						timePicker.clearFocus();
						StringBuffer sb = new StringBuffer();
						sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
								datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
						sb.append("  ");
						if (timePicker.getCurrentHour() < 10) {
							sb.append("0");
						}
						sb.append(timePicker.getCurrentHour()).append(":");// .append(timePicker.getCurrentMinute());
						if (timePicker.getCurrentMinute() < 10) {
							sb.append("0");
						}
						sb.append(timePicker.getCurrentMinute());

						bindcarnexttime.setText(sb);
						// etEndTime.requestFocus();

						dialog.cancel();
					}
				});
				Dialog dialog = builder.create();
				dialog.show();
			}
		});
		bindcarnexttime2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ChangeBindCarsActivity.this);
				View view = LayoutInflater.from(ChangeBindCarsActivity.this).inflate(
						R.layout.date_time_dialog, null);
				final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
				final TimePicker timePicker = (android.widget.TimePicker) view
						.findViewById(R.id.time_picker);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH), null);

				timePicker.setIs24HourView(true);
				timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
				builder.setView(view);

				builder.setTitle("ѡȡʱ��");
				builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						timePicker.clearFocus();
						StringBuffer sb = new StringBuffer();
						sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
								datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
						sb.append("  ");
						if (timePicker.getCurrentHour() < 10) {
							sb.append("0");
						}
						sb.append(timePicker.getCurrentHour()).append(":");
						if (timePicker.getCurrentMinute() < 10) {
							sb.append("0");
						}
						sb.append(timePicker.getCurrentMinute());

						bindcarnexttime2.setText(sb);

						dialog.cancel();
					}
				});
				Dialog dialog = builder.create();
				dialog.show();
			}
		});
		bindcarnum2 = (TextView) findViewById(R.id.changebindcars_bindcarnum2);
		bindcarnum2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChangeBindCarsActivity.this,
						AppointmentNetWorkSearch.class);
				intent.putExtra("tonggou.from", "pinpai");
				intent.putExtra("tonggou.pinpai", "");
				startActivityForResult(intent, 1010);
			}
		});
		bindcarnum1 = (TextView) findViewById(R.id.changebindcars_bindcarnum1);
		bindcarnum1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vehicleBrandIdStr != null && !"".equals(vehicleBrandIdStr)) {
					Intent intent = new Intent(ChangeBindCarsActivity.this,
							AppointmentNetWorkSearch.class);
					intent.putExtra("tonggou.from", "chexing");
					intent.putExtra("tonggou.pinpai", vehicleBrandIdStr);
					startActivityForResult(intent, 2020);
				} else {
					Toast.makeText(ChangeBindCarsActivity.this, getString(R.string.brand_first),
							Toast.LENGTH_SHORT).show();

				}
			}
		});


		// if( mVehicleInfo != null ) {
		// setVehicleInfoValue();
		// } else {
		
//		if( TongGouApplication.getInstance().isLogin() ) {
//			doQuerySingleVehicleInfo(vehicleIdStr);
//			
//		} else { //�����οͣ�������ݿ��в�ѯ����
//			VehicleManager manager = new VehicleManager();
//			mVehicleInfo = manager.getVehicle(null, mVehicleInfo.getVehicleNo());
//			setVehicleInfoValue();
//		}
		
		setVehicleInfoValue();

		// ͣ������obd
//		BaseConnectOBDService.addingCar = true;
//		Intent intentS = new Intent();
//		intentS.setAction(TongGouService.TONGGOU_ACTION_START);
//		intentS.putExtra("com.tonggou.server", "STOP");
//		sendBroadcast(intentS);

	}

	private void setVehicleInfoValue() {
		progress.setVisibility(View.GONE);
		vehicleBrandIdStr = mVehicleInfo.getVehicleBrandId();
		vehicleModelIdStr = mVehicleInfo.getVehicleModelId();
		if (mVehicleInfo.getObdSN() != null && !"null".equals(mVehicleInfo.getObdSN())) {
			mTxtOBDBinded.setVisibility(View.VISIBLE);
			bindcarvehiclevin.setText(mVehicleInfo.getVehicleVin() + "");// ���ܺ�
			vehicleVinStr = mVehicleInfo.getVehicleVin();
			vehicleIdStr = mVehicleInfo.getVehicleId();
			// ifAddInterface = true;
		}
		obdSNStr = mVehicleInfo.getObdSN();
		bindcarnum.setText(mVehicleInfo.getVehicleNo() + "");

		if (mVehicleInfo.getVehicleBrand() != null && !"".equals(mVehicleInfo.getVehicleBrand())
				&& !"null".equals(mVehicleInfo.getVehicleBrand())) {
			bindcarnum2.setText(mVehicleInfo.getVehicleBrand() + "");
		}
		if (mVehicleInfo.getVehicleModel() != null && !"".equals(mVehicleInfo.getVehicleModel())
				&& !"null".equals(mVehicleInfo.getVehicleModel())) {
			bindcarnum1.setText(mVehicleInfo.getVehicleModel() + "");
		}
		if (mVehicleInfo.getCurrentMileage() != null
				&& !"".equals(mVehicleInfo.getCurrentMileage())
				&& !"null".equals(mVehicleInfo.getCurrentMileage())) {

			if (mVehicleInfo.getCurrentMileage().indexOf(".") != -1) {
				String cm = mVehicleInfo.getCurrentMileage().substring(0,
						mVehicleInfo.getCurrentMileage().indexOf("."));
				bindcarmilenow.setText(cm);
			} else {
				bindcarmilenow.setText(mVehicleInfo.getCurrentMileage() + "");
			}

		}
		if (mVehicleInfo.getNextMaintainMileage() != null
				&& !"".equals(mVehicleInfo.getNextMaintainMileage())
				&& !"null".equals(mVehicleInfo.getNextMaintainMileage())) {
			if (mVehicleInfo.getNextMaintainMileage().indexOf(".") != -1) {
				String nm = mVehicleInfo.getNextMaintainMileage().substring(0,
						mVehicleInfo.getNextMaintainMileage().indexOf("."));
				bindcarnextmile.setText(nm);
			} else {
				bindcarnextmile.setText(mVehicleInfo.getNextMaintainMileage() + "");
			}

		}
		if (mVehicleInfo.getNextExamineTime() != null
				&& !"".equals(mVehicleInfo.getNextExamineTime())
				&& !"null".equals(mVehicleInfo.getNextExamineTime())
				&& !"0".equals(mVehicleInfo.getNextExamineTime())) {
			bindcarnexttime2.setText(SomeUtil.longToStringDate(mVehicleInfo.getNextExamineTime()
					+ ""));
		}
		if (mVehicleInfo.getNextInsuranceTime() != null
				&& !"".equals(mVehicleInfo.getNextInsuranceTime())
				&& !"null".equals(mVehicleInfo.getNextInsuranceTime())
				&& !"0".equals(mVehicleInfo.getNextInsuranceTime())) {
			bindcarnexttime.setText(SomeUtil.longToStringDate(mVehicleInfo.getNextInsuranceTime()
					+ ""));
		}
		if (mVehicleInfo.getRecommendShopName() != null) {
			mTxtShopBinded.setVisibility(View.VISIBLE);
			mTxtShopBinded.setText(String.format(
					getResources().getString(R.string.change_bindcars_shop_binded),
					mVehicleInfo.getRecommendShopName()));
		}

		if (mVehicleInfo.getVehicleVin() != null && !"".equals(mVehicleInfo.getVehicleVin())
				&& !"null".equals(mVehicleInfo.getVehicleVin())) {
			bindcarvehiclevin.setText(mVehicleInfo.getVehicleVin() + "");
		}
		if (mVehicleInfo.getEngineNo() != null && !"".equals(mVehicleInfo.getEngineNo())
				&& !"null".equals(mVehicleInfo.getEngineNo())) {
			TongGouApplication.showLog("mVehicleInfo.getEngineNo()  +  "
					+ mVehicleInfo.getEngineNo() + "");
			bindcarengineno.setText(mVehicleInfo.getEngineNo() + "");

		}
		if (mVehicleInfo.getRegistNo() != null && !"".equals(mVehicleInfo.getRegistNo())
				&& !"null".equals(mVehicleInfo.getRegistNo())) {
			bindcarregistno.setText(mVehicleInfo.getRegistNo() + "");
		}

		if (mTxtOBDBinded.getVisibility() == View.VISIBLE) {
			setViewMargins(mTxtOBDBinded, 0, SomeUtil.Dp2Px(this, 5), 0, 0);
		}

		if (mTxtShopBinded.getVisibility() == View.VISIBLE) {
			if (mTxtOBDBinded.getVisibility() != View.VISIBLE) {
				setViewMargins(mTxtShopBinded, 0, SomeUtil.Dp2Px(this, 5), 0, 0);
			}
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void onBackPressed() {
		ChangeBindCarsActivity.this.finish();
		if( TongGouApplication.getInstance().isLogin() ) {
			// ����obd
			BaseConnectOBDService.addingCar = false;
			Intent intent = new Intent();// ����Intent����
			intent.setAction(TongGouService.TONGGOU_ACTION_START);
			intent.putExtra("com.tonggou.server", "SCAN_OBD");
			sendBroadcast(intent);// ���͹㲥
		}

	}

	private void setViewMargins(View view, int left, int top, int right, int bottom) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
		view.setLayoutParams(params);
	}

	private void doQuerySingleVehicleInfo(String vehicleId) {
		showLoadingDialog("���س�������...");
		QuerySingleVehicleRequest request = new QuerySingleVehicleRequest();
		request.setApiParams(vehicleId);
		request.doRequest(this, new AsyncJsonBaseResponseParseHandler<VehicleResponse>() {

			@Override
			public void onParseSuccess(VehicleResponse result, String originResult) {
				super.onParseSuccess(result, originResult);
				mVehicleInfo = result.getVehicleInfo();
				if( mVehicleInfo != null ) {
					setVehicleInfoValue();
				} else {
					TongGouApplication.showToast("���ݷ���ʧ��");
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				dismissLoadingDialog();
			}
			
			@Override
			public Class<VehicleResponse> getTypeClass() {
				return VehicleResponse.class;
			}
			
		});
		
		
	}
	
	
	/**
	 * �����ֵ�
	 * @param vehicleModelId
	 */
	public void updateFaultDic(final String vehicleModelId) {
		UpdateFaultDic.getUpdateFaultDic(ChangeBindCarsActivity.this).updateFaultDic( vehicleModelId );
	}

	private void updateVehicleInfo(String vehicleId, String vehicleVin, String vehicleNo,
			String vehicleModel, final String vehicleModelId, String vehicleBrand, String vehicleBrandId,
			String obdSN, String userNo) {
		showLoadingDialog("������...");
		
		StoreVehicleInfoRequest request = new StoreVehicleInfoRequest();
		request.setRequestParams(userNo, vehicleId, vehicleNo, vehicleBrand, vehicleModel, mVehicleInfo, null);
		request.doRequest(this, new AsyncJsonBaseResponseParseHandler<AddBindCarResponse>() {

			@Override
			public void onParseSuccess(AddBindCarResponse result, String originResult) {
				super.onParseSuccess(result, originResult);
				
				updateFaultDic(vehicleModelId);
				String currenMile = bindcarmilenow.getText().toString();
				BaseConnectOBDService.cmile = currenMile;
				Log.i("Bluetooth thinks", "����cmile" + BaseConnectOBDService.cmile);

				TongGouApplication.showToast( result.getMessage());
				Intent dataIntent = new Intent();
				dataIntent.putExtra("tonggou.isOk", "yes");
				BaseConnectOBDService.addingCar = false;
				TongGouApplication.getInstance().queryVehicleList(ChangeBindCarsActivity.this);
				setResult(Activity.RESULT_OK, dataIntent);
				finish();
				
				
			}
			
			@Override
			public void onParseFailure(String errorCode, String errorMsg) {
				super.onParseFailure(errorCode, errorMsg);
				showErrorMessageDialog(errorMsg);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				dismissLoadingDialog();
			}
			
			@Override
			public Class<AddBindCarResponse> getTypeClass() {
				return AddBindCarResponse.class;
			}
			
		});
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1010) {
			if (bindcarnum2.getText().toString() != null
					&& !"".equals(bindcarnum2.getText().toString())) {
				if (!data.getStringExtra("pinpai").equals(bindcarnum2.getText().toString())) {

					bindcarnum1.setText("");
					vehicleModelIdStr = null;
				}
			}
			bindcarnum2.setText(data.getStringExtra("pinpai"));
			vehicleBrandIdStr = data.getStringExtra("pinpaiId");

		}
		if (resultCode == 2020) {
			bindcarnum1.setText(data.getStringExtra("chexing"));
			vehicleModelIdStr = data.getStringExtra("chexingId");

		}
	}

	private void bindObd(String vehicleId, String vehicleVin, String vehicleNo,
			String vehicleModel, final String vehicleModelId, String vehicleBrand, String vehicleBrandId,
			String obdSN, String userNo) {
		
		ObdBindingRequest request = new ObdBindingRequest();
		request.setRequestParams(userNo, obdSN, vehicleId, vehicleNo, vehicleVin, vehicleBrand, vehicleModel, mVehicleInfo, shop2DCodeId);
		request.doRequest(this, new AsyncJsonBaseResponseParseHandler<BaseResponse>() {

			@Override
			public void onParseSuccess(BaseResponse result, String originResult) {
				super.onParseSuccess(result, originResult);
				
				TongGouApplication.showToast(result.getMessage());
				UpdateFaultDic.getUpdateFaultDic(ChangeBindCarsActivity.this).updateFaultDic( vehicleModelId);
				ok = "yes";
				BaseConnectOBDService.cmile = bindcarmilenow.getText().toString();
				Intent backIntent = new Intent();
				backIntent.putExtra("tonggou.isOk", ok);
				TongGouApplication.getInstance().queryVehicleList(ChangeBindCarsActivity.this);
				BaseConnectOBDService.addingCar = false;
				setResult(RESULT_OK, backIntent);
				ChangeBindCarsActivity.this.finish();
			}
			
			@Override
			public Class<BaseResponse> getTypeClass() {
				return BaseResponse.class;
			}
			
		});
		
	}

}
