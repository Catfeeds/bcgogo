package com.tonggou.andclient.jsonresponse;

import java.io.Serializable;

/**
 * ��������Ӧ JSON �����ֶ�
 * @author lwz
 *
 */
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -8136329065223334889L;

	private String status; 		// ״̬��  SUCCESS | FAIL
	private int msgCode;  		// ������
	private String message ; 	// ������Ϣ
	
	/**
	 * ���״̬��
	 * @return SUCCESS �ɹ� | FAIL ʧ��
	 */
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * �Ƿ�����ɹ�
	 * @return true �ɹ�
	 */
	public final boolean isSuccess() {
		return "SUCCESS".equals(getStatus());
	}
	
	/**
	 * ��ô�����
	 * @return
	 */
	public int getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}
	
	/**
	 * ���������Ϣ
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
