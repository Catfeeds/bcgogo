package com.tonggou.andclient.vo;
/**
 * ͳ����Ϣ
 * @author think
 *
 */
public class TonggouMessage {
	private String id ;          //Ψһ��ʶ��      long
	private String type ;        //��Ϣ����      String
	private String content;      //��������   String
	private String actionType;   //��������       String	
	private String searchShop;       	//��ת�����̲�ѯ
	private String serviceDetail;    	//��ת������ķ���
	private String cancelOrder;      	//ȡ������
	private String orderDetail;      	//�鿴��������
	private String commentShop;    		//���۵���
	private String params;         //��actionType��������ҵ������      String
	private String time;
	private String title;         //����
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSearchShop() {
		return searchShop;
	}
	public void setSearchShop(String searchShop) {
		this.searchShop = searchShop;
	}
	public String getServiceDetail() {
		return serviceDetail;
	}
	public void setServiceDetail(String serviceDetail) {
		this.serviceDetail = serviceDetail;
	}
	public String getCancelOrder() {
		return cancelOrder;
	}
	public void setCancelOrder(String cancelOrder) {
		this.cancelOrder = cancelOrder;
	}
	public String getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}
	public String getCommentShop() {
		return commentShop;
	}
	public void setCommentShop(String commentShop) {
		this.commentShop = commentShop;
	}
	
	
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	

}
