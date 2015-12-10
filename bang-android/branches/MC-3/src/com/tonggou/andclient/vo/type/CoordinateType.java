package com.tonggou.andclient.vo.type;

/**
 * ��������
 * 
 * <ul>
 * 	<li> {@link #NULL}
 * 	<li> {@link #CURRENT}
 * 	<li> {@link #LAST}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum CoordinateType {
	
	/**
	 * ��ȷ��
	 */
	NULL("NULL"),
	
	/**
	 * ��ǰ
	 */
	CURRENT("CURRENT"),	
	
	/**
	 * �ϴ�
	 */
	LAST("LAST");
	
	private String value;
	
	private CoordinateType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
