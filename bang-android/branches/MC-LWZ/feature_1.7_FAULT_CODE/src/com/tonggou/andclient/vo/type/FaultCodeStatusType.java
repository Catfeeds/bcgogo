package com.tonggou.andclient.vo.type;

/**
 * ��������
 * 
 * <ul>
 * 	<li> {@link #UNTREATED}
 * 	<li> {@link #FIXED}
 * 	<li> {@link #IGNORED}
 * 	<li> {@link #DELETED}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum FaultCodeStatusType {
	/**
	 * δ����
	 */
	UNTREATED("UNTREATED"),	
	/**
	 * ���޸�
	 */
	FIXED("FIXED"),	
	
	/**
	 * ����
	 */
	IGNORED("IGNORED"),
	
	/**
	 * ɾ��
	 */
	DELETED("DELETED");
	
	private String value;
	
	private FaultCodeStatusType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
