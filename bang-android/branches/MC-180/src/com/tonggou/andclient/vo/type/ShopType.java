package com.tonggou.andclient.vo.type;

/**
 * �̵�����
 * 
 * <ul>
 * 	<li> {@link #ALL}
 * 	<li> {@link #SHOP_4S}
 * </ul>
 * 
 * @author lwz
 *
 */
public enum ShopType {
	/**
	 * �����̵�
	 */
	ALL("ALL"),	
	
	/**
	 * 4S ��
	 */
	SHOP_4S("SHOP_4S");
	
	private String value;
	
	private ShopType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
