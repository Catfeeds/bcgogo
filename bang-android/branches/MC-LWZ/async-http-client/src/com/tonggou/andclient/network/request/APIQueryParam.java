package com.tonggou.andclient.network.request;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * API URL ����
 * <p>������ģ����ղ���˳��������</p>
 * ֱ�ӵ���  toString() �������õ� url ����
 * <br> Ĭ�ϵõ��� ���� url �ǰ����� �� ../userNo/{userNo} ,���� userNo Ϊ����{userNo} Ϊֵ
 * <br> ���벻����������ô����ʹ�� �������Ĺ��췽��  {@link #APIQueryParam(boolean isParamsUrlContainKey)}
 * <br>
 * <p>
 * 	�ڲ���ʹ�� LinkedHashMap��ʵ�ֵ�
 * </p>
 * @author lwz
 *
 */
public class APIQueryParam extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -7262235753079488732L;
	
	// url �Ƿ������ �� ../userNo/{userNo} ,���� userNo Ϊ����{userNo} Ϊֵ
	private boolean isParamsUrlContainKey = true;
	
	/**
	 * ֱ�ӵ���  toString() �������õ� url ����
	 * <br> Ĭ�ϵõ��� ���� url �ǰ����� �� ../userNo/{userNo} ,���� userNo Ϊ����{userNo} Ϊֵ
	 * <br> ���벻����������ô����ʹ�� �������Ĺ��췽��  {@link #APIQueryParam(boolean isParamsUrlContainKey)}
	 */
	public APIQueryParam() {
	}
	
	/**
	 * ֱ�ӵ���  toString() �������õ� url ����
	 * @param isParamsUrlContainKey
	 * 			url �Ƿ������ �� ../userNo/{userNo} ,���� userNo Ϊ����{userNo} Ϊֵ,Ĭ��Ϊ true
	 */
	public APIQueryParam(boolean isParamsUrlContainKey) {
		this.isParamsUrlContainKey = isParamsUrlContainKey;
	}
	
	@Override
	public Object put(String key, Object value) {
		if( value == null ) {
			value = "NULL";
		}
		return super.put(key, value);
	}
	
	@Override
	public String toString() {
		Set<Entry<String, Object>> entries = entrySet();
		StringBuffer sb = new StringBuffer("/");
		for( Entry<String, Object> entry : entries ) {
			if( isParamsUrlContainKey ) {
				sb.append(entry.getKey() + "/");
			}
			sb.append(entry.getValue() + "/");
		}
		return sb.deleteCharAt(sb.lastIndexOf("/")).toString();
	}
	
}
