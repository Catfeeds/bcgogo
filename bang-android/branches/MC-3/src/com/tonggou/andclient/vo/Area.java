package com.tonggou.andclient.vo;

import java.io.Serializable;

public class Area implements Serializable {
	private static final long serialVersionUID = 2095901111277835835L;
	
	long id;//����                            
	String name;//����                          
	String cityCode;//��ͼ�����еĳ��б��         
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

}
