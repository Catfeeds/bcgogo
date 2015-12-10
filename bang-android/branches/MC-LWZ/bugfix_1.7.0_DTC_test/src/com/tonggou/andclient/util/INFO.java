package com.tonggou.andclient.util;

import android.os.Build;

import com.tonggou.andclient.app.TongGouApplication;


public class INFO {
	public static final String APPNAME = "tonggou";	
	
	public static final String VERSION = TongGouApplication.getInstance().getVersionName();
	public static final String MOBILE_PLATFORM = "ANDROID";
	public static final String MOBILE_PLATFORM_VERSION = Build.VERSION.RELEASE;	// �û��ֻ�ϵͳƽ̨�汾 3.0 4.0
	public static final String MOBILE_MODEL = Build.MODEL;       //�û��ֻ��ͺ�
	public static final String IMAGE_VERSION =  
			TongGouApplication.getInstance().getImageVersion();   //�ֻ�Ӳ���ֱ���  480 X 800

    public static  String PARTNERID =  "0000" ;
	
    public static final String HTTP_HEAD = "https://"; 
	public static final String HOST_IP = "phone.bcgogo.cn:1443/api";      //����
    
//    public static final String HTTP_HEAD = "http://";
//	public static final String HOST_IP = "192.168.1.33:8080/api";      //����
	
//	public static final String HOST_IP = "shop.bcgogo.com/api";    //��ʵ��Ӫ
//	public static  int HOST_PORT = 443;
	
	public static final int ITEMS_PER_PAGE = 10;

}
