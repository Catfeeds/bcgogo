package com.tonggou.andclient.vo;

import java.io.Serializable;

public class VehicleType implements Serializable {

	private static final long serialVersionUID = 2273704046173584452L;
	
	private String id;			// ���� ID
	private String car;			// ������
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
}
