package com.tonggou.andclient.vo;

public class MemberService {
	long  serviceId;//����ID����̨����������  
	String  consumeType;//����������        
	int times;//ʣ�����     
	String timesStr;
	long  deadline;//��Ч��   
	String deadlineStr;
	public String getTimesStr() {
		return timesStr;
	}
	public void setTimesStr(String timesStr) {
		this.timesStr = timesStr;
	}
	public String getDeadlineStr() {
		return deadlineStr;
	}
	public void setDeadlineStr(String deadlineStr) {
		this.deadlineStr = deadlineStr;
	}
	String  serviceName;//��������        
	String vehicles;//�޶�������        
	String  status;//״̬         
	boolean expired;//�Ƿ����       
	public long getServiceId() {
		return serviceId;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	public String getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getVehicles() {
		return vehicles;
	}
	public void setVehicles(String vehicles) {
		this.vehicles = vehicles;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

}
