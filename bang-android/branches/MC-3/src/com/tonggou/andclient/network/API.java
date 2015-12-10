package com.tonggou.andclient.network;

import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.vo.type.DataKindType;

public class API {
	
	public static class JUHE_TRANSGRESS {
		private static final String KEY = "60ad2a9b3c7bcda13b781dabe01fe843";
		public static final String GET_SUPPORT_CITYS = "http://v.juhe.cn/wz/citys?key=" + KEY;
		public static final String TRANSGRESS_QUERY = "http://v.juhe.cn/weizhang/query?key=" + KEY;
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
