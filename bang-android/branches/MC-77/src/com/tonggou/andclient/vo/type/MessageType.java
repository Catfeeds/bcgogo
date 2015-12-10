package com.tonggou.andclient.vo.type;

/**
 * ��Ϣ����
 * 
 * <ul>
 * 	<li> {@link #ALL}
 * 	<li> {@link #SHOP_CHANGE_APPOINT}
 * 	<li> {@link #SHOP_FINISH_APPOINT}
 * 	<li> {@link #SHOP_ACCEPT_APPOINT}
 * 	<li> {@link #SHOP_CANCEL_APPOINT}
 * 	<li> {@link #SHOP_REJECT_APPOINT}
 * 	<li> {@link #OVERDUE_APPOINT_TO_APP}
 * 	<li> {@link #APP_VEHICLE_MAINTAIN_MILEAGE}
 * 	<li> {@link #APP_VEHICLE_INSURANCE_TIME}
 * 	<li> {@link #APP_VEHICLE_EXAMINE_TIME}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum MessageType {
	
	/**
	 * ��������
	 */
	ALL("NULL"),
	
	/**
	 * ����ԤԼ�޸���Ϣ
	 */
    SHOP_CHANGE_APPOINT("SHOP_CHANGE_APPOINT"),	
    
    /**
     * ����ԤԼ������Ϣ
     */
    SHOP_FINISH_APPOINT("SHOP_FINISH_APPOINT"),	
    
    /**
     * ���̽���ԤԼ��
     */
    SHOP_ACCEPT_APPOINT("SHOP_ACCEPT_APPOINT"),	
    
    /**
     * ����ԤԼ�ܾ���Ϣ
     */
    SHOP_CANCEL_APPOINT("SHOP_CANCEL_APPOINT"),	
    
    /**
     * ����ԤԼȡ����Ϣ
     */
    SHOP_REJECT_APPOINT("SHOP_REJECT_APPOINT"),	
    
    /**
     * APP����ԤԼ��
     */
    OVERDUE_APPOINT_TO_APP("OVERDUE_APPOINT_TO_APP"),	
    
    /**
     * �������
     */
    APP_VEHICLE_MAINTAIN_MILEAGE("APP_VEHICLE_MAINTAIN_MILEAGE"),	
    
    /**
     * ����ʱ��
     */
    APP_VEHICLE_MAINTAIN_TIME("APP_VEHICLE_MAINTAIN_TIME"),	
    
    /**
     * ����ʱ��
     */
    APP_VEHICLE_INSURANCE_TIME("APP_VEHICLE_INSURANCE_TIME"),	
	
    /**
     * �鳵ʱ��
     */
    APP_VEHICLE_EXAMINE_TIME("APP_VEHICLE_EXAMINE_TIME");
    
	
	private String value;
	
	private MessageType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	
}
