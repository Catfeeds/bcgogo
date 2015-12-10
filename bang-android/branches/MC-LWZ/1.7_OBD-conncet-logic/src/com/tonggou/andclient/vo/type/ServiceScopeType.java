package com.tonggou.andclient.vo.type;

/**
 * �������� ö����
 * <ul>
 * 	<li>{@link #ALL}
 * 	<li>{@link #OVERHAUL_AND_MAINTENANCE}
 * 	<li>{@link #DECORATION_BEAUTY}
 * 	<li>{@link #PAINTING}
 * 	<li>{@link #INSURANCE}
 * 	<li>{@link #WASH}
 * </ul>
 * @author lwz
 *
 */
public enum ServiceScopeType {
	/**
	 * ���еķ���
	 */
	ALL("NULL", "���з���"),
	
	/**
	 * ���ޱ���
	 */
	OVERHAUL_AND_MAINTENANCE("OVERHAUL_AND_MAINTENANCE", "���ޱ���"),
	
	/**
	 * ����װ��
	 */
	DECORATION_BEAUTY("DECORATION_BEAUTY", "����װ��"),
	
	/**
	 * �ӽ�����
	 */
	PAINTING("PAINTING", "�ӽ�����"),
	
	/**
	 * �����鳵
	 */
	INSURANCE("INSURANCE", "�����鳵"),
	
	/**
	 * ϴ������
	 */
	WASH("WASH", "ϴ��");
	
	private final String type;
	private final String name;
	
	private ServiceScopeType(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	/**
	 * �õ����͵�ֵ
	 * @return
	 */
	public String getTypeValue() {
		return type;
	}
	
	/**
	 * �õ�������
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getTypeValue();
	}
}
