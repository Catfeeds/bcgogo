package com.tonggou.andclient.vo;
/**
 * ������Ϣ��
 * @author think
 *
 */
public class CarCondition {
		private  String alarmId;
	    public String getAlarmId() {
			return alarmId;
		}
		public void setAlarmId(String alarmId) {
			this.alarmId = alarmId;
		}
		private  String userID ;       				//���ڲ�ͬ�û��˺Ų���
	    private  String faultCode ;       			//������ ����ж�����������Զ��� ,�ֿ�
	    private  String name = "UNREAD";       				//�Ƿ����
		private  String content ;    				//��������
		private  String type ;       				//����  
	    private  String vehicleVin ;       			//����Ψһ��ʶ��
		private  String obdSN ;    					//obdΨһ��ʶ��
		private  String reportTime ;      			//����ʱ��
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public String getFaultCode() {
			return faultCode;
		}
		public void setFaultCode(String faultCode) {
			this.faultCode = faultCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public String getVehicleVin() {
			return vehicleVin;
		}
		public void setVehicleVin(String vehicleVin) {
			this.vehicleVin = vehicleVin;
		}
		public String getObdSN() {
			return obdSN;
		}
		public void setObdSN(String obdSN) {
			this.obdSN = obdSN;
		}
		public String getReportTime() {
			return reportTime;
		}
		public void setReportTime(String reportTime) {
			this.reportTime = reportTime;
		}
}
