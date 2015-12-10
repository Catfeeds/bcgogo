package com.tonggou.andclient.network;

import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.vo.type.DataKindType;

public class API {
	
	public static class JUHE {
		// Υ��
		private static final String KEY_TRANSGRESS = "60ad2a9b3c7bcda13b781dabe01fe843";
		public static final String GET_SUPPORT_CITYS = "http://v.juhe.cn/wz/citys?key=" + KEY_TRANSGRESS;
		public static final String TRANSGRESS_QUERY = "http://v.juhe.cn/weizhang/query?key=" + KEY_TRANSGRESS;
		
		// ����վ
		private static final String KEY_GAS_STATION = "c8adc805a4a1fdeb7a79798d03d06a46";
		public static final String QUERY_GAS_STATION = "http://apis.juhe.cn/oil/local?key=" + KEY_GAS_STATION;
	}
	
	private static final String BASE_URL = INFO.HTTP_HEAD + INFO.HOST_IP;
	
	/** ע�� */
	public static final String REGISTER = BASE_URL + "/user/registration";
	/** ��¼ */
	public static final String LOGIN = BASE_URL + "/login";
	/** ��ȡ�����б� */
	public static final String VEHICLE_LIST = BASE_URL + "/vehicle/list";
	/** ��ȡ����Χ */
	public static final String SERVICE_CATS_SCOPE = BASE_URL + "/serviceCategory/list";
	/** ��ȡ�����б� */
	public static final String QUERY_SHOP_LIST = BASE_URL + "/shop/list";
	/** �һ����� */
	public static final String FIND_PASSWORD = BASE_URL + "/user/password";
	/** ��ѯ��Ϣ */
	public static final String POLLING_MESSAGE = BASE_URL + "/message/polling";
	/** �汾������� */
	public static final String UPDATE_CHECK = BASE_URL + "/newVersion";
	/** ��ѯ�ۺ�֧�ֳ��еĹ��� */
	public static final String QUERY_JUHE_CITY_LIST = BASE_URL + "/violateRegulations/juhe/area/list";
	/** ���泵����Ϣ*/
	public static final String STORE_VEHICLE_INFO = BASE_URL + "/vehicle/vehicleInfo";
	/** ����һ��������Ϣ */
	public static final String QUERY_SINGLE_VEHICLE_INFO = BASE_URL + "/vehicle/singleVehicle";
	/** ɾ��������Ϣ */
	public static final String DELETE_VEHICLE_INFO = BASE_URL + "/vehicle/singleVehicle";
	/** ����Ĭ�ϳ���*/
	public static final String UPDATE_DEFAULT_VEHICLE = BASE_URL + "/vehicle/updateDefault";
	/** �������� */
	public static final String SHOP_DETAIL = BASE_URL + "/shop/detail";
	/** ���ݹؼ��ֻ�ȡ���̽����б� */
	public static final String SHOP_SUGGESTION_BY_KEYWORD = BASE_URL + "/shop/suggestions";
//	/** ��ȡ��̨������Ϣ���� */
//	public static final String QUERY_VEHICLE_INFO_SUGGESTION = BASE_URL + "/vehicle/info/suggestion";
	/** �û�����*/
	public static final String FEEDBACK = BASE_URL + "/user/feedback";
	/** �� OBD */
	public static final String OBD_BINDING = BASE_URL + "/obd/binding";
	/** ���³��������ֵ� */
	public static final String UPDATE_VEHICLE_FAULT_DIC = BASE_URL + "/vehicle/faultDic";
	/** ���ͳ��������� */
	public static final String SEND_VEHICLE_FAULT = BASE_URL + "/vehicle/fault";
	/** �޸ĳ���������״̬ */
	public static final String MODIFY_VEHICLE_FAULT_STATUS = BASE_URL + "/vehicle/faultCode";
	/** ����೵�� */
	public static final String STORE_VEHICLE_INFOS = BASE_URL + "/vehicle/list/guest";
	/**�޸�����*/
	public static final String MODIFY_PASSWORD = BASE_URL + "/user/password";
	/**���ͳ�����Ϣ*/
	public static final String SEND_VEHICLE_CONDITION = BASE_URL + "/vehicle/condition";
	/**�ǳ�*/
	public static final String LOGOUT = BASE_URL + "/logout";
	/** ��ѯ�������б� */
	public static final String QUERY_FAULT_CODE_LIST = BASE_URL + "/vehicle/faultCodeList";
	/** ����ԤԼ*/
	public static final String SEND_APPOINTMENT = BASE_URL + "/service/appointment";
	/**�ϴ��г���־*/
	public static final String UPLOAD_DJITEM = BASE_URL + "/driveLog/newDriveLog";
	/**
	 * �ο�ģʽ�� API
	 * @author lwz
	 */
	public static final class GUEST {
		public static final DataKindType CURRENT_DATA_KIND = DataKindType.OFFICIAL;
		private static final String DATA_KIND = "/" + CURRENT_DATA_KIND.getValue();
		private static final String GUEST_MODE = "/guest";
		private static final String IMAGE_VERSION = "/IV_480X800";
		
		/** ��ȡ�����б� */
		public static final String QUERY_SHOP_LIST = API.QUERY_SHOP_LIST + GUEST_MODE + DATA_KIND + IMAGE_VERSION ;
		/** ��ȡ�����б� */
		public static final String SHOP_DETAIL = API.SHOP_DETAIL + GUEST_MODE + IMAGE_VERSION ;
		/** ���ݹؼ��ֻ�ȡ���̽����б� */
		public static final String SHOP_SUGGESTION_BY_KEYWORD = BASE_URL + "/shop/suggestions" + GUEST_MODE;
		/** �û�����*/
		public static final String FEEDBACK = BASE_URL + "/guest/feedback";
	}
}
