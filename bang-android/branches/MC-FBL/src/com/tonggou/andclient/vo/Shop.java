package com.tonggou.andclient.vo;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;
public class Shop{
	private long    id;//����ID                   
	private String  name;//��������          
	private String  serviceScope;//;//��Ӫ��Χ       
	private float   distance;//���루��λ����� 
	private String  coordinate;//�������꣨��γ�ȣ�
	private float   totalScore;//�����ܷ�               
	private String  bigImageUrl;//ͼƬ��ַ               
	private String  smallImageUrl;//ͼƬ��ַ        
	private Bitmap  samllbtm;//ͼƬ��ַ        
	private Bitmap  bigbtm;//ͼƬ��ַ        
	private String mobile;//�绰       
	private String landLine;//�绰  2
	public String getLandLine() {
		return landLine;
	}
	public void setLandLine(String landLine) {
		this.landLine = landLine;
	}
	private String address;//��ַ                 
	private ShopScore shopScore;//  ������������
	private String cityCode;
	private MemberInfo	memberInfo;//��Ա��Ϣ    ����ֻ��ʹ��С������Ϣ�����Ԥ�� 
	private ArrayList<ShopServiceCategoryDTO> productCategoryList;
	
	public ArrayList<ShopServiceCategoryDTO> getProductCategoryList() {
		return productCategoryList;
	}
	public void setProductCategoryList(ArrayList<ShopServiceCategoryDTO> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	public Bitmap getSamllbtm() {
		return samllbtm;
	}
	public void setSamllbtm(Bitmap samllbtm) {
		this.samllbtm = samllbtm;
	}
	public Bitmap getBigbtm() {
		return bigbtm;
	}
	public void setBigbtm(Bitmap bigbtm) {
		this.bigbtm = bigbtm;
	}
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
