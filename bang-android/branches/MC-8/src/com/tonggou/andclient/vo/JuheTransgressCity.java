package com.tonggou.andclient.vo;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class JuheTransgressCity implements Serializable {

	private static final long serialVersionUID = -6899964585941209882L;

	private String provinceName;		// ʡ
	private String cityName; 			// ��������
	private String cityCode; 			// ���б���
	private String abbr; 				// ʡ��д
	private boolean isNeedEngine; 		// �Ƿ���Ҫ��������
	private String engineno; 			// �������ŴӺ�����λ��
	private boolean isNeedClass; 		// �Ƿ���Ҫ���ܺ�
	private String classno; 			// ���ܺŴӺ�����λ��
	private boolean isNeedRegist; 		// �Ƿ���Ҫ�Ǽ�֤���
	private String registno;			// �Ǽ�֤��Ӻ�����λ��
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public boolean isNeedEngine() {
		return isNeedEngine;
	}
	public void setNeedEngine(boolean isNeedEngine) {
		this.isNeedEngine = isNeedEngine;
	}
	public String getEngineno() {
		return engineno;
	}
	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}
	public boolean isNeedClass() {
		return isNeedClass;
	}
	public void setNeedClass(boolean isNeedClass) {
		this.isNeedClass = isNeedClass;
	}
	public String getClassno() {
		return classno;
	}
	public void setClassno(String classno) {
		this.classno = classno;
	}
	public boolean isNeedRegist() {
		return isNeedRegist;
	}
	public void setNeedRegist(boolean isNeedRegist) {
		this.isNeedRegist = isNeedRegist;
	}
	public String getRegistno() {
		return registno;
	}
	public void setRegistno(String registno) {
		this.registno = registno;
	} 
	
	@Override
	public String toString() {
		return cityName == null ? "" : cityName;
	}
	
	public void parse( JSONObject cityJsonObject ) throws JSONException {
		setCityName( cityJsonObject.getString("city_name"));
		setCityCode( cityJsonObject.getString("city_code"));
		setAbbr( cityJsonObject.getString("abbr"));
		setNeedClass( "1".equals( cityJsonObject.getString("classa") ) );
		setClassno( cityJsonObject.getString("classno") );
		setNeedEngine( "1".equals( cityJsonObject.getString("engine") ) );
		setEngineno( cityJsonObject.getString("engineno") );
		setNeedRegist( "1".equals(cityJsonObject.getString("regist") ) );
		setRegistno( cityJsonObject.getString("registno") );
	}
}
