package com.tonggou.andclient.vo.type;

/**
 * Guest ģʽ�����󻷾�����
 * 
 * <ul>
 * 	<li> {@link #TEST}
 * 	<li> {@link #OFFICIAL}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum DataKindType {
	/**
	 * ����
	 */
	TEST("TEST"),	
	
	/**
	 * ��ʽ
	 */
	OFFICIAL("OFFICIAL");
	
	private String value;
	
	private DataKindType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
