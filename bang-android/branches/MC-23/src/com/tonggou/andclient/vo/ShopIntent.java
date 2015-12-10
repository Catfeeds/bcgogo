package com.tonggou.andclient.vo;

import java.io.Serializable;

import android.graphics.Bitmap;

public class ShopIntent  implements Serializable{
	long    id;//����ID                   
	String  name;//��������          
	String  serviceScope;//;//��Ӫ��Χ       
	float   distance;//���루��λ����� 
	String  coordinate;//�������꣨��γ�ȣ�
	float   totalScore;//�����ܷ�               
	String  bigImageUrl;//ͼƬ��ַ               
	String  smallImageUrl;//ͼƬ��ַ      
	String mobile;//�绰             
	String address;//��ַ                 
	ShopScore shopScore;//  ������������
	String   cityCode;
	MemberInfo	memberInfo;//��Ա��Ϣ    ����ֻ��ʹ��С������Ϣ�����Ԥ�� 
	public String getBigImageUrl() {
		return bigImageUrl;
	}
	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ShopScore getShopScore() {
		return shopScore;
	}
	public void setShopScore(ShopScore shopScore) {
		this.shopScore = shopScore;
	}
	public MemberInfo getMemberInfo() {
		return memberInfo;
	}
	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}
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
	public String getServiceScope() {
		return serviceScope;
	}
	public void setServiceScope(String serviceScope) {
		this.serviceScope = serviceScope;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
}