package com.tonggou.andclient.vo;

import java.util.List;

public class ServerDetail {
	private String id;						//������ID                   long
	private String receiptNo; 				//�����ݺ�            long
	private String status;				//��״̬                 String
	private String vehicleNo;				//�����ƺ�            String
	private String vehicleContact;		//:������ϵ��    String
	public String getShopImageUrl() {
		return shopImageUrl;
	}
	public void setShopImageUrl(String shopImageUrl) {
		this.shopImageUrl = shopImageUrl;
	}
	public String getVehicleBrandModelStr() {
		return vehicleBrandModelStr;
	}
	public void setVehicleBrandModelStr(String vehicleBrandModelStr) {
		this.vehicleBrandModelStr = vehicleBrandModelStr;
	}
	private String vehicleMobile;		//:������ϵ��ʽ   String
	private String customerName;		//���ͻ���         String
	private String serviceType;			//:��������    ϴ�������������ա��鳵��ά�� String 
	private String shopId;				//������ID                  long
	private String shopName;				//����������              String
	private double shopTotalScore;			//������������      double
	private String orderType;			//����������          String ԤԼ��
	private String orderId;
	private String remark;			//:��ע��ԤԼ���ı�ע��
	private long   orderTime;				//������ʱ�䣨ԤԼʱ�䣩 long
	private String actionType;				//����������         String
	private String shopImageUrl;
	private SettleAccount settleAccounts;          //�۸񲿷�
	private List<OrderItem> orderItems;
	private String vehicleBrandModelStr;
	private String content;
	private Comment  comment; 
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public SettleAccount getSettleAccounts() {
		return settleAccounts;
	}
	public void setSettleAccounts(SettleAccount settleAccounts) {
		this.settleAccounts = settleAccounts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getVehicleContact() {
		return vehicleContact;
	}
	public void setVehicleContact(String vehicleContact) {
		this.vehicleContact = vehicleContact;
	}
	public String getVehicleMobile() {
		return vehicleMobile;
	}
	public void setVehicleMobile(String vehicleMobile) {
		this.vehicleMobile = vehicleMobile;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public double getShopTotalScore() {
		return shopTotalScore;
	}
	public void setShopTotalScore(double shopTotalScore) {
		this.shopTotalScore = shopTotalScore;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}
