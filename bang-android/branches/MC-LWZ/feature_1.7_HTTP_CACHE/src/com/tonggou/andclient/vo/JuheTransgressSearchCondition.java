package com.tonggou.andclient.vo;

import java.io.Serializable;

public class JuheTransgressSearchCondition implements Serializable {

	private static final long serialVersionUID = 6354739877014307709L;

	private String provinceName;	// ʡ
	private String provinceCode;	// ʡ���
	private String cityName; 		// ��������
	private String cityCode; 		// ���б���
	private int engine; 			// �Ƿ���Ҫ�������� 0 ����Ҫ
	private int engineno; 			// �������ŴӺ�����λ��
	private int classa; 			// �Ƿ���Ҫ���ܺ� 0 ����Ҫ
	private int classno; 			// ���ܺŴӺ�����λ��
	private int regist; 			// �Ƿ���Ҫ�Ǽ�֤��� 0 ����Ҫ
	private int registno;			// �Ǽ�֤��Ӻ�����λ��
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
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
	public int getEngine() {
		return engine;
	}
	public void setEngine(int engine) {
		this.engine = engine;
	}
	public int getEngineno() {
		return engineno;
	}
	public void setEngineno(int engineno) {
		this.engineno = engineno;
	}
	public int getClassa() {
		return classa;
	}
	public void setClassa(int classa) {
		this.classa = classa;
	}
	public int getClassno() {
		return classno;
	}
	public void setClassno(int classno) {
		this.classno = classno;
	}
	public int getRegist() {
		return regist;
	}
	public void setRegist(int regist) {
		this.regist = regist;
	}
	public int getRegistno() {
		return registno;
	}
	public void setRegistno(int registno) {
		this.registno = registno;
	}
}
