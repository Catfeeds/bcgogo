package com.tonggou.andclient.vo.type;

/**
 * ��������
 * 
 * <ul>
 * 	<li> {@link #DISTANCE}
 * 	<li> {@link #EVALUATION}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum SortType {
	/**
	 * ����������
	 */
	DISTANCE("DISTANCE"),	
	
	/**
	 * ���۸�����
	 */
	EVALUATION("EVALUATION");
	
	private String value;
	
	private SortType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
