package com.tonggou.andclient.vo;
/**
 * ��������--�����б���
 * @author think
 *
 */
public class OrderItem {
	 private String content;       //������        String
     private String type;         //������           String
    private String  amount;     //�����         double
    
    
	public OrderItem() {
		super();
	}
	
	public OrderItem(String content, String type, String amount) {
		super();
		this.content = content;
		this.type = type;
		this.amount = amount;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

}
