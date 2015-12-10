package com.tonggou.andclient.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tonggou.andclient.BaseActivity;
import com.tonggou.andclient.vo.CarCondition;
import com.tonggou.andclient.vo.FaultCodeInfo;
import com.tonggou.andclient.vo.TonggouMessage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * ���ݿ������
 * 
 * @author Carson
 * 
 */
public class SaveDB {

	// ////////////////////////////��������ذ汾
	public static String FAULT_DIC_MODLE = "modle"; // ����id ������ common�ַ�����ʶ
	public static String FAULT_DIC_VERSION = "version"; // �汾

	public static final int DIC_DATABASE_VERSION = 1;
	public static final String DIC_VER_TABLE_NAME = "faultversion";
	public static final String DIC_VER_TIONARY_TABLE_CREATE = "CREATE TABLE " + DIC_VER_TABLE_NAME
			+ " (" + FAULT_DIC_MODLE + " TEXT, " + FAULT_DIC_VERSION + " TEXT);";

	public static final String[] DIC_VER_TABLE_CREATE2 = { FAULT_DIC_MODLE, FAULT_DIC_VERSION };
	// public FaultVerDbHelper faultVerHelper;
	// //////////////////////////////////////////////////////////////

	// ////////////////////////////���������
	public static String FAULT_TYPT = "faulttype"; // ����id ������ ͨ��common�ַ�����ʶ
	public static String FAULT_CODE = "faultcode"; // ������
	public static String FAULT_DESCRIPTION = "description"; // ����

	public static final int DATABASE_VERSION = 1;
	public static final String DICTIONARY_TABLE_NAME = "faultcodes";
	public static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE " + DICTIONARY_TABLE_NAME
			+ " (" + FAULT_TYPT + " TEXT, " + FAULT_CODE + " TEXT, " + FAULT_DESCRIPTION
			+ " TEXT);";

	public static final String[] DICTIONARY_TABLE_CREATE2 = { FAULT_TYPT, FAULT_CODE,
			FAULT_DESCRIPTION };
	// private FaultDbHelper faultHelper;
	// //////////////////////////////////////////////////////////////

	// /////////////////////////////��Ϣ���
	private static String MESSAGE_USERID = "userID"; // ���ڲ�ͬ�û��˺���Ϣ����
	private static String MESSAGE_ID = "mesID"; // Ψһ��ʶ��
	private static String MESSAGE_TYPE = "type"; // ��Ϣ����
	private static String MESSAGE_CONTENT = "content"; // ��������
	private static String MESSAGE_ACTION_TYPE = "actionType"; // ��������
	private static String MESSAGE_SEARCH_SHOP = "searchShop"; // ��ת�����̲�ѯ
	private static String MESSAGE_SERVICE_DETAIL = "serviceDetail"; // ��ת������ķ���
	private static String MESSAGE_CANCEL_ORDER = "cancelOrder"; // ȡ������
	private static String MESSAGE_ORDER_DETAIL = "orderDetail"; // �鿴��������
	private static String MESSAGE_COMMENT_SHOP = "commentShop"; // ���۵���
	private static String MESSAGE_PARAMS = "params"; // ��������ҵ������
	private static String MESSAGE_TIME = "time"; // ʱ��
	private static String MESSAGE_TITLE = "title"; // ����

	private static final int DATABASE_MES_VERSION = 1;
	private static final String MESSAGE_TABLE_NAME = "tgmessages";
	private static final String MESSAGE_TABLE_CREATE = "CREATE TABLE " + MESSAGE_TABLE_NAME + " ("
			+ MESSAGE_USERID + " TEXT, " + MESSAGE_ID + " TEXT, " + MESSAGE_TYPE + " TEXT, "
			+ MESSAGE_CONTENT + " TEXT, " + MESSAGE_ACTION_TYPE + " TEXT, " + MESSAGE_SEARCH_SHOP
			+ " TEXT, " + MESSAGE_SERVICE_DETAIL + " TEXT, " + MESSAGE_CANCEL_ORDER + " TEXT, "
			+ MESSAGE_ORDER_DETAIL + " TEXT, " + MESSAGE_COMMENT_SHOP + " TEXT, " + MESSAGE_PARAMS
			+ " TEXT, " + MESSAGE_TIME + " TEXT, " + MESSAGE_TITLE + " TEXT);";
	private static final String[] MESSAGE_TABLE_CREATE2 = { MESSAGE_USERID, MESSAGE_ID,
			MESSAGE_TYPE, MESSAGE_CONTENT, MESSAGE_ACTION_TYPE, MESSAGE_SEARCH_SHOP,
			MESSAGE_SERVICE_DETAIL, MESSAGE_CANCEL_ORDER, MESSAGE_ORDER_DETAIL,
			MESSAGE_COMMENT_SHOP, MESSAGE_PARAMS, MESSAGE_TIME, MESSAGE_TITLE };
	// private MessageDbHelper messageHelper;
	// /////////////////////////////////////////////////////////////////

	// /////////////////////////////�����������
	private static String ALARM_ID = "alarmID"; // �����Ķ�ʱ���
	private static String ALARM_USERID = "userID"; // ���ڲ�ͬ�û��˺Ų���
	private static String ALARM_FAULTCODE = "faultCode"; // ������ ����ж�����������Զ��� ,�ֿ�
	private static String ALARM_NAME = "name"; // ��������
	private static String ALARM_CONTENT = "content"; // ��������
	private static String ALARM_TYPE = "type"; // ����
	private static String ALARM_VEHICLE_VIN = "vehicleVin"; // ����Ψһ��ʶ��
	private static String ALARM_OBDSN = "obdSN"; // obdΨһ��ʶ��
	private static String ALARM_REPORT_TIME = "reportTime"; // ����ʱ��

	private static final int DATABASE_ALARM_VERSION = 1;
	private static final String ALARM_TABLE_NAME = "tgalarm";
	private static final String ALARM_TABLE_CREATE = "CREATE TABLE " + ALARM_TABLE_NAME + " ("
			+ ALARM_ID + " TEXT, " + ALARM_USERID + " TEXT, " + ALARM_FAULTCODE + " TEXT, "
			+ ALARM_NAME + " TEXT, " + ALARM_CONTENT + " TEXT, " + ALARM_TYPE + " TEXT, "
			+ ALARM_VEHICLE_VIN + " TEXT, " + ALARM_OBDSN + " TEXT, " + ALARM_REPORT_TIME
			+ " TEXT);";
	private static final String[] ALARM_TABLE_CREATE2 = { ALARM_ID, ALARM_USERID, ALARM_FAULTCODE,
			ALARM_NAME, ALARM_CONTENT, ALARM_TYPE, ALARM_VEHICLE_VIN, ALARM_OBDSN,
			ALARM_REPORT_TIME };
	// private AlarmDbHelper alarmHelper;
	// /////////////////////////////////////////////////////////////////

	private Context currenCont;
	private static final String MY_DB_NAME = "tonggoudb";
	private MyDbHelper myDbHelper;

	private SaveDB(Context con) {
		currenCont = con;
		myDbHelper = new MyDbHelper(currenCont, MY_DB_NAME, null, DIC_DATABASE_VERSION);
		// faultVerHelper = new
		// FaultVerDbHelper(currenCont,DIC_VER_TABLE_NAME,null,DIC_DATABASE_VERSION);
		// faultHelper = new
		// FaultDbHelper(currenCont,DICTIONARY_TABLE_NAME,null,DATABASE_VERSION);
		// messageHelper = new
		// MessageDbHelper(currenCont,MESSAGE_TABLE_NAME,null,DATABASE_MES_VERSION);
		// alarmHelper = new
		// AlarmDbHelper(currenCont,ALARM_TABLE_NAME,null,DATABASE_ALARM_VERSION);
	}

	/**
	 * ���ص�̬ʵ��
	 */
	private static SaveDB singleSaveDB;

	public static SaveDB getSaveDB(Context con) {
		if (singleSaveDB == null) {
			singleSaveDB = new SaveDB(con);
		}
		return singleSaveDB;
	}

	/**
	 * ����������ֵ�
	 * 
	 * @param codes
	 * @param vehicleModeID
	 *            ����id
	 * @return
	 */
	public boolean saveFaultCodeInfo(List<FaultCodeInfo> codes, String vehicleModeID) {
		boolean suc = true;
		if (codes == null || vehicleModeID == null || "".equals(vehicleModeID)) {
			return false;
		}

		try {
			SQLiteDatabase db = myDbHelper.getWritableDatabase();

			for (int i = 0; i < codes.size(); i++) {
				if (db != null && db.isOpen()) {
					FaultCodeInfo temp = codes.get(i);
					ContentValues cvalues = new ContentValues();
					cvalues.put(FAULT_TYPT, vehicleModeID);
					cvalues.put(FAULT_CODE, temp.getFaultCode());
					cvalues.put(FAULT_DESCRIPTION, temp.getDescription());
					db.insert(DICTIONARY_TABLE_NAME, "", cvalues);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			suc = false;
		}
		return suc;
	}

	/**
	 * ɾ�������ֵ�
	 * 
	 * @param userid
	 * @param mesId
	 */
	public boolean deleteModleFaultCodes(String modle) {
		SQLiteDatabase db = null;
		boolean suc = true;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.delete(DICTIONARY_TABLE_NAME, FAULT_TYPT + "='" + modle + "'", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			suc = false;
		}
		return suc;
	}

	/**
	 * ���¹����ֵ��������
	 */
	public boolean updateFaultCode(String modelId, String nowVersion, List<FaultCodeInfo> codes,
			boolean... isCheckingCommonFaultDic) {
		boolean suc = false;

		if (codes == null || codes.size() == 0 || nowVersion == null || "".equals(nowVersion))
			return suc;

		SQLiteDatabase db = null;
		String changeModelId;
		if (modelId == null || "".equals(modelId)) {
			changeModelId = "common";
		} else {
			changeModelId = modelId;
		}

		try {
			db = myDbHelper.getWritableDatabase();

			String versionStr = getFaultCodesVersionByModle(changeModelId);
			if (versionStr == null) {
				versionStr = "NULL";
			}

			if (db != null && db.isOpen()) {
				db.beginTransaction();

				if (false == deleteModleFaultCodes(modelId)) { // ɾ���ϵ�����
					throw new Exception();
				}
				if (false == saveFaultCodeInfo(codes, modelId)) { // �����µ�����
					throw new Exception();
				}

				// ���°汾��
				if ("NULL".equals(versionStr)) {
					if (false == saveFaultCodeVersion(modelId, nowVersion)) {
						throw new Exception();
					}
				} else {
					if (false == upDateFaultCodeVersion(modelId, nowVersion)) {
						throw new Exception();
					}
				}

				db.setTransactionSuccessful();
				if( isCheckingCommonFaultDic.length >0 ) {
					if (isCheckingCommonFaultDic[0]) {
						currenCont
								.getSharedPreferences(BaseActivity.SETTING_INFOS, Context.MODE_PRIVATE)
								.edit().putString(BaseActivity.COMMON_FAULT_DIC_VERSON, nowVersion).commit();
					}
					suc = true;
				} else {
					suc = false;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			suc = false;
		} finally {
			db.endTransaction();
		}

		return suc;
	}

	/**
	 * ȡ��ͨ�ù�����
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<FaultCodeInfo> getAllMyFaultCodes(String type) {
		ArrayList<FaultCodeInfo> all = new ArrayList<FaultCodeInfo>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				Cursor cr = db.query(DICTIONARY_TABLE_NAME, DICTIONARY_TABLE_CREATE2, FAULT_TYPT
						+ "='common'", null, null, null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return null;
					}

					do {
						FaultCodeInfo yf = new FaultCodeInfo();
						String temType = cr.getString(0);
						String temcode = cr.getString(1);
						String discrption = cr.getString(2);

						yf.setFaultCode(temcode);
						yf.setDescription(discrption);
						all.add(yf);
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception e) {
		}
		return all;
	}

	/**
	 * ���ݹ�����codeȡ��ͨ�ù����� ���п���һ��code ��Ӧ����������
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<FaultCodeInfo> getSomeFaultCodesById(String ModleId, String code) {
		ArrayList<FaultCodeInfo> all = new ArrayList<FaultCodeInfo>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				Cursor cr = db.query(DICTIONARY_TABLE_NAME, DICTIONARY_TABLE_CREATE2, FAULT_TYPT
						+ "='" + ModleId + "' AND " + FAULT_CODE + "='" + code + "'"/*
																					 * FAULT_CODE
																					 * +
																					 * "='"
																					 * +
																					 * code
																					 * +
																					 * "'"
																					 */, null,
						null, null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return null;
					}

					do {
						FaultCodeInfo yf = new FaultCodeInfo();
						String temType = cr.getString(0);
						String temcode = cr.getString(1);
						String discrption = cr.getString(2);

						yf.setFaultCode(temcode);
						yf.setDescription(discrption);
						all.add(yf);
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception e) {
		}
		return all;
	}

	/**
	 * ����汾
	 * 
	 * @param medle
	 * @param ver
	 * @return
	 */
	public boolean saveFaultCodeVersion(String medle, String ver) {
		boolean suc = false;
		if (medle == null || ver == null || "".equals(medle)) {
			return false;
		}

		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		try {
			if (db != null && db.isOpen()) {
				ContentValues cvalues = new ContentValues();
				cvalues.put(FAULT_DIC_MODLE, medle);
				cvalues.put(FAULT_DIC_VERSION, ver);
				if (-1 != db.insert(DIC_VER_TABLE_NAME, "", cvalues))
					suc = true;
			}
		} catch (Exception e) {
		}
		return suc;
	}

	/**
	 * ������һ���汾��¼
	 * 
	 * @param modleId
	 * @param vers
	 */
	public boolean upDateFaultCodeVersion(String modleId, String vers) {
		boolean suc = true;
		if (modleId == null || vers == null || "".equals(modleId)) {
			return false;
		}

		SQLiteDatabase db = null;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				ContentValues values = new ContentValues();
				values.put(FAULT_DIC_VERSION, vers);
				db.update(DIC_VER_TABLE_NAME, values, FAULT_DIC_MODLE + "='" + modleId + "'", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			suc = false;
		}
		return suc;
	}

	public String getFaultCodesVersionByModle(String cModleId) {
		String result = null;
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				Cursor cr = db.query(DIC_VER_TABLE_NAME, DIC_VER_TABLE_CREATE2, FAULT_DIC_MODLE
						+ "='" + cModleId + "'", null, null, null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return null;
					}

					do {
						result = cr.getString(1);
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * ������ѯ��Ϣ
	 * 
	 * @param messages
	 * @param userId
	 * @return
	 */
	public boolean saveMessages(List<TonggouMessage> messages, String userId) {
		if (messages == null || userId == null || "".equals(userId)) {
			return false;
		}
		SQLiteDatabase mesdb = myDbHelper.getWritableDatabase();
		try {
			if (mesdb != null && mesdb.isOpen()) {
				for (int i = 0; i < messages.size(); i++) {
					TonggouMessage temp = messages.get(i);
					ContentValues cvalues = new ContentValues();
					cvalues.put(MESSAGE_USERID, userId);
					cvalues.put(MESSAGE_ID, temp.getId());
					cvalues.put(MESSAGE_TYPE, temp.getType());
					cvalues.put(MESSAGE_CONTENT, temp.getContent());
					cvalues.put(MESSAGE_ACTION_TYPE, temp.getActionType());
					cvalues.put(MESSAGE_SEARCH_SHOP, temp.getSearchShop());
					cvalues.put(MESSAGE_SERVICE_DETAIL, temp.getServiceDetail());
					cvalues.put(MESSAGE_CANCEL_ORDER, temp.getCancelOrder());
					cvalues.put(MESSAGE_ORDER_DETAIL, temp.getOrderDetail());
					cvalues.put(MESSAGE_COMMENT_SHOP, temp.getCommentShop());
					cvalues.put(MESSAGE_PARAMS, temp.getParams());
					cvalues.put(MESSAGE_TIME, temp.getTime());
					cvalues.put(MESSAGE_TITLE, temp.getTitle());

					mesdb.insert(MESSAGE_TABLE_NAME, "", cvalues);
				}
			}
		} catch (Exception ex) {
		}

		return true;
	}

	/**
	 * ȡ��Ϣ
	 * 
	 * @param userid
	 * @return
	 */
	public ArrayList<TonggouMessage> getAllMyMessages(String userid) {
		ArrayList<TonggouMessage> all = new ArrayList<TonggouMessage>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				// Cursor cr = db.query(MESSAGE_TABLE_NAME,
				// MESSAGE_TABLE_CREATE2,MESSAGE_USERID+"='"+userid+"'", null,
				// null, null, null);
				Cursor cr = db.query(MESSAGE_TABLE_NAME, MESSAGE_TABLE_CREATE2, MESSAGE_USERID
						+ "='" + userid + "'", null, null, null, MESSAGE_TIME + " desc");
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return null;
					}

					do {
						TonggouMessage yf = new TonggouMessage();
						String id = cr.getString(1);
						String type = cr.getString(2);
						String content = cr.getString(3);
						String actionType = cr.getString(4); // �������� String
						String searchShop = cr.getString(5); // ��ת�����̲�ѯ
						String serviceDetail = cr.getString(6); // ��ת������ķ���
						String cancelOrder = cr.getString(7); // ȡ������
						String orderDetail = cr.getString(8); // �鿴��������
						String commentShop = cr.getString(9); // ���۵���
						String params = cr.getString(10); // ��actionType��������ҵ������
															// String
						String time = cr.getString(11);
						String title = cr.getString(12);

						yf.setId(id);
						yf.setType(type);
						yf.setContent(content);
						yf.setActionType(actionType);
						yf.setSearchShop(searchShop);
						yf.setServiceDetail(serviceDetail);
						yf.setCancelOrder(cancelOrder);
						yf.setOrderDetail(orderDetail);
						yf.setCommentShop(commentShop);
						yf.setParams(params);
						yf.setTime(time);
						yf.setTitle(title);
						all.add(yf);
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception ex) {
		}
		return all;
	}

	/**
	 * ��Ϣ����
	 * 
	 * @return
	 */
	public int getAllMessagesCount(String userid) {
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		int count = 0;
		try {
			if (db != null && db.isOpen()) {
				Cursor cr = db.query(MESSAGE_TABLE_NAME, MESSAGE_TABLE_CREATE2, MESSAGE_USERID
						+ "='" + userid + "'", null, null, null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return count;
					}
					do {
						count++;
					} while (cr.moveToNext());
					cr.close();
				}
			}
		} catch (Exception ex) {
		}
		return count;
	}

	/**
	 * ɾ��һ����Ϣ
	 * 
	 * @param userid
	 * @param mesId
	 */
	public void deleteOneMessage(String userid, String mesId) {
		SQLiteDatabase db = null;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.delete(MESSAGE_TABLE_NAME, MESSAGE_USERID + "='" + userid + "' AND "
						+ MESSAGE_ID + "='" + mesId + "'", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ������һ����Ϣ
	 * 
	 * @param userid
	 * @param mesId
	 */
	public void upDateOneMessage(String userid, String mesId) {
		SQLiteDatabase db = null;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				ContentValues values = new ContentValues();
				values.put(MESSAGE_ACTION_TYPE, "COMMENT");
				db.update(MESSAGE_TABLE_NAME, values, MESSAGE_USERID + "='" + userid + "' AND "
						+ MESSAGE_ID + "='" + mesId + "'", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ������n����Ϣ
	 * 
	 * @param userid
	 * @param count
	 *            ������
	 */

	public void deleteEarlyMessage(String userid, int count) {
		SQLiteDatabase db = null;
		try {
			db = myDbHelper.getWritableDatabase();
			ArrayList<String> allID = getEarlyMessage(userid, "0," + count);
			if (allID != null && allID.size() > 0) {
				for (int i = 0; i < allID.size(); i++) {
					String mesId = allID.get(i);
					db.delete(MESSAGE_TABLE_NAME, MESSAGE_USERID + "='" + userid + "' AND "
							+ MESSAGE_ID + "='" + mesId + "'", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��������ʱ�����Ϣ
	 * 
	 * @param userID
	 * @param limit
	 * @return
	 */
	public ArrayList<String> getEarlyMessage(String userID, String limit) {
		ArrayList<String> all = new ArrayList<String>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				Cursor cr = db.query(MESSAGE_TABLE_NAME, MESSAGE_TABLE_CREATE2, MESSAGE_USERID
						+ "='" + userID + "'", null, null, null, MESSAGE_TIME + " DESC", limit);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return all;
					}

					do {
						all.add(cr.getString(1)); // ȡMESSAGE_ID
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception ex) {
		}
		return all;
	}

	/**
	 * ɾ��������Ϣ
	 * 
	 * @param userid
	 */
	public boolean deleteAllMessage(String userid) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.delete(MESSAGE_TABLE_NAME, MESSAGE_USERID + "='" + userid + "'", null);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���泵����Ϣ
	 * 
	 * @param messages
	 * @param userId
	 * @return
	 */
	public boolean saveAlarm(CarCondition condition) {
		if (condition == null || condition.getUserID() == null || "".equals(condition.getUserID())) {
			return false;
		}
		int result = findAlarm(condition.getUserID(), condition.getFaultCode());
		if (result == 1) { // �½�
			SQLiteDatabase mesdb = myDbHelper.getWritableDatabase();
			try {
				if (mesdb != null && mesdb.isOpen()) {
					ContentValues cvalues = new ContentValues();
					cvalues.put(ALARM_ID, condition.getAlarmId());
					cvalues.put(ALARM_USERID, condition.getUserID());
					cvalues.put(ALARM_FAULTCODE, condition.getFaultCode());
					cvalues.put(ALARM_NAME, condition.getName());
					cvalues.put(ALARM_CONTENT, condition.getContent());
					cvalues.put(ALARM_TYPE, condition.getType());
					cvalues.put(ALARM_VEHICLE_VIN, condition.getVehicleVin());
					cvalues.put(ALARM_OBDSN, condition.getObdSN());
					cvalues.put(ALARM_REPORT_TIME, condition.getReportTime());
					mesdb.insert(ALARM_TABLE_NAME, "", cvalues);
					return true;
				}
			} catch (Exception ex) {
			}
			return false;
		} else if (result == 0) { // ����
			SQLiteDatabase db = myDbHelper.getWritableDatabase();
			try {
				ContentValues values = new ContentValues();
				values.put(ALARM_CONTENT, condition.getContent());
				values.put(ALARM_TYPE, condition.getType());
				values.put(ALARM_REPORT_TIME, condition.getReportTime());
				db.update(ALARM_TABLE_NAME, values, ALARM_USERID + "='" + condition.getUserID()
						+ "' AND " + ALARM_FAULTCODE + "='" + condition.getFaultCode() + "'", null);

			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param usid
	 * @param faultCode
	 * @return -1 ���ݿ����ʧ�� ��0 �ҵ���¼ 1 û�ҵ���¼
	 */
	private int findAlarm(String usid, String faultCode) {
		SQLiteDatabase db = myDbHelper.getReadableDatabase();

		if (db != null && db.isOpen()) {
			try {
				Cursor cr = db.query(ALARM_TABLE_NAME, ALARM_TABLE_CREATE2, ALARM_USERID + "='"
						+ usid + "' AND " + ALARM_FAULTCODE + "='" + faultCode + "'", null, null,
						null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return 1;
					}

					do {

					} while (cr.moveToNext());

					cr.close();

					return 0;
				}
			} catch (Exception ex) {
			}
			return 1;
		} else {
			return -1;
		}

	}

	/**
	 * ʹһ����Ϣ��Ϊ�Ѷ�״̬
	 * 
	 * @return -1û�ҵ�����˽�ţ�0�ɹ� 1�������
	 */
	public void readedOneAlarm(String uid, String faultCode) {
		if (faultCode == null || "".equals(faultCode)) {
			return;
		}

		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(ALARM_NAME, "READ");
			db.update(ALARM_TABLE_NAME, values, ALARM_USERID + "='" + uid + "' AND "
					+ ALARM_FAULTCODE + "='" + faultCode + "'", null);

		} catch (Exception e) {

		}

	}

	/**
	 * ����һ����Ϣ�Ѷ�ʱ��
	 * 
	 * @return -1û�ҵ�����˽�ţ�0�ɹ� 1�������
	 */
	public void readedTimeAlarm(String uid, String faultCode, String lasttime) {
		if (faultCode == null || "".equals(faultCode)) {
			return;
		}

		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(ALARM_ID, lasttime);
			db.update(ALARM_TABLE_NAME, values, ALARM_USERID + "='" + uid + "' AND "
					+ ALARM_FAULTCODE + "='" + faultCode + "'", null);

		} catch (Exception e) {

		}

	}

	/**
	 * ɾ��һ����Ϣ
	 * 
	 * @param userid
	 * @param mesId
	 */
	public void deleteOneAlarm(String userid, String faultCode) {
		SQLiteDatabase db = null;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.delete(ALARM_TABLE_NAME, ALARM_USERID + "='" + userid + "' AND "
						+ ALARM_FAULTCODE + "='" + faultCode + "'", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ�����д�����ʾ
	 * 
	 * @param userid
	 */
	public boolean deleteAllAlarm(String userid) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = myDbHelper.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.delete(ALARM_TABLE_NAME, ALARM_USERID + "='" + userid + "'", null);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<CarCondition> getAllCarConditons(String userId) {
		ArrayList<CarCondition> all = new ArrayList<CarCondition>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {
				// ��ٵ�����ǰ��
				Cursor cr = db.query(ALARM_TABLE_NAME, ALARM_TABLE_CREATE2, ALARM_USERID + "='"
						+ userId + "'", null, null, null, ALARM_ID + " DESC");
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return all;
					}

					do {
						CarCondition yf = new CarCondition();
						String tempAlarmID = cr.getString(0);
						String tempUserID = cr.getString(1); // ���ڲ�ͬ�û��˺Ų���
						String tempFAULTCODE = cr.getString(2); // ������
																// ����ж�����������Զ���
																// ,�ֿ�
						String tempNAME = cr.getString(3); // ��������
						String tempCONTENT = cr.getString(4); // ��������
						String tempTYPE = cr.getString(5); // ����
						String tempVEHICLE_VIN = cr.getString(6); // ����Ψһ��ʶ��
						String tempOBDSN = cr.getString(7); // obdΨһ��ʶ��
						String tempREPORT_TIME = cr.getString(8); // ����ʱ��
						yf.setAlarmId(tempAlarmID);
						yf.setUserID(tempUserID);
						yf.setFaultCode(tempFAULTCODE);
						yf.setName(tempNAME);
						yf.setContent(tempCONTENT);
						yf.setType(tempTYPE);
						yf.setVehicleVin(tempVEHICLE_VIN);
						yf.setObdSN(tempOBDSN);
						yf.setReportTime(tempREPORT_TIME);
						all.add(yf);
					} while (cr.moveToNext());

					cr.close();
				}
			}
		} catch (Exception ex) {
		}
		return all;
	}

	/**
	 * δ��
	 * 
	 * @param userId
	 * @return
	 */
	public ArrayList<CarCondition> getAllUnReadCarConditons(String userId) {
		ArrayList<CarCondition> all = new ArrayList<CarCondition>();
		SQLiteDatabase db = myDbHelper.getReadableDatabase();
		try {
			if (db != null && db.isOpen()) {

				Cursor cr = db.query(ALARM_TABLE_NAME, ALARM_TABLE_CREATE2, ALARM_USERID + "='"
						+ userId + "' AND " + ALARM_NAME + "='UNREAD'", null, null, null, null);
				if (cr != null) {
					if (!cr.moveToFirst()) {
						cr.close();
						return all;
					}

					do {
						CarCondition yf = new CarCondition();
						String tempAlarmID = cr.getString(0);
						String tempUserID = cr.getString(1); // ���ڲ�ͬ�û��˺Ų���
						String tempFAULTCODE = cr.getString(2); // ������
																// ����ж�����������Զ���
																// ,�ֿ�
						String tempNAME = cr.getString(3); // ��������
						String tempCONTENT = cr.getString(4); // ��������
						String tempTYPE = cr.getString(5); // ����
						String tempVEHICLE_VIN = cr.getString(6); // ����Ψһ��ʶ��
						String tempOBDSN = cr.getString(7); // obdΨһ��ʶ��
						String tempREPORT_TIME = cr.getString(8); // ����ʱ��
						yf.setAlarmId(tempAlarmID);
						yf.setUserID(tempUserID);
						yf.setFaultCode(tempFAULTCODE);
						yf.setName(tempNAME);
						yf.setContent(tempCONTENT);
						yf.setType(tempTYPE);
						yf.setVehicleVin(tempVEHICLE_VIN);
						yf.setObdSN(tempOBDSN);
						yf.setReportTime(tempREPORT_TIME);
						all.add(yf);
					} while (cr.moveToNext());

					cr.close();
				}

			}
		} catch (Exception ex) {
		}
		return all;
	}

	private class MyDbHelper extends SQLiteOpenHelper {
		public MyDbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DIC_VER_TIONARY_TABLE_CREATE);
			db.execSQL(DICTIONARY_TABLE_CREATE);
			db.execSQL(MESSAGE_TABLE_CREATE);
			db.execSQL(ALARM_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public void closeMyDb() {
		if (myDbHelper != null) {
			try {
				SQLiteDatabase db = myDbHelper.getReadableDatabase();
				if (db != null) {
					db.close();
				}
				myDbHelper.close();
			} catch (Exception ex) {
			}
		}
	}

	// private class FaultVerDbHelper extends SQLiteOpenHelper {
	// public FaultVerDbHelper(Context context, String name,CursorFactory
	// factory, int version) {
	// super(context, name, factory, version);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// db.execSQL(DIC_VER_TIONARY_TABLE_CREATE);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	//
	// }
	// }
	//
	// private class FaultDbHelper extends SQLiteOpenHelper {
	// public FaultDbHelper(Context context, String name,CursorFactory factory,
	// int version) {
	// super(context, name, factory, version);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// db.execSQL(DICTIONARY_TABLE_CREATE);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	//
	// }
	// }
	//
	// private class MessageDbHelper extends SQLiteOpenHelper {
	// public MessageDbHelper(Context context, String name,CursorFactory
	// factory, int version) {
	// super(context, name, factory, version);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// db.execSQL(MESSAGE_TABLE_CREATE);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	//
	// }
	// }
	//
	// private class AlarmDbHelper extends SQLiteOpenHelper {
	// public AlarmDbHelper(Context context, String name,CursorFactory factory,
	// int version) {
	// super(context, name, factory, version);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// db.execSQL(ALARM_TABLE_CREATE);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	//
	// }
	// }

}
