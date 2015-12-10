package com.tonggou.andclient.guest;

import com.tonggou.andclient.guest.VehicleDBUtil.DefaultType;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VehicleDatabaseHelper extends SQLiteOpenHelper {

	private static VehicleDatabaseHelper mInstance = null;
	// ���ݿ�����
	public static final String DATABASE_NAME = "guest_vehicle.db";
	// ���ݿ�汾��
	private static final int DATABASE_VERSION = 1;
	// ���ݿ�SQL��� ���һ����---->���� ������
	private static final String NATIVE_GOODS_TABLE = 
			"CREATE TABLE "  + VehicleDBUtil.TABLE_NAME_GUEST_VEHICLE 
			+ "("
			+ VehicleDBUtil.VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ VehicleDBUtil.IS_DEFAULT + " VARCHAR2(5) NOT NULL DEFAULT '"+ DefaultType.NORMAL.getValue() +"'," 
			+ VehicleDBUtil.VEHICLE_JSON_DATA + " TEXT NOT NULL"
			+ ");";

	public VehicleDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** ����ģʽ **/
	public static synchronized VehicleDatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VehicleDatabaseHelper(context);
		}
		return mInstance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ����������ӱ�
		db.execSQL(NATIVE_GOODS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/** �����õ���ǰ���ݿ�İ汾��Ϣ ��֮ǰ���ݿ�İ汾��Ϣ �����������ݿ� **/
		if (newVersion > oldVersion) {
			String sqlGuestVehicle = "DROP TABLE IF EXISTS " + VehicleDBUtil.TABLE_NAME_GUEST_VEHICLE;
			db.execSQL(sqlGuestVehicle);
		}
	}
	
	/**
	 * ɾ�����ݿ�
	 * 
	 * @param context
	 * @return
	 */
	public static boolean deleteDataBase(Context context) {
		return context.deleteDatabase(DATABASE_NAME);
	}

}
