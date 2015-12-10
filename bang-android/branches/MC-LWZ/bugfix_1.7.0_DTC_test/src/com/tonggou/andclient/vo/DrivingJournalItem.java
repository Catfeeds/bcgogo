package com.tonggou.andclient.vo;

import java.io.Serializable;

public class DrivingJournalItem implements Serializable {
	private static final long serialVersionUID = 8202533025890171645L;
	
	/**
	 * ûд�����ݿ���*ע��
	 */
	private String appDriveLogId; // app���¼���г���־��Id
	private String appUserNo; // �û���
	private String vehicleNo; // ��ǰ��־�ĳ��ƺ�
	private Long startTime; // ��ʼʱ��
	private String startLat; // ��ʼά��
	private String startLon; // ��ʼ����
	private String startPlace; // ��ʼ��ַ
	private Long endTime; // ����ʱ��
	private String endLat; // ����ά��
	private String endLon; // ��������
	private String endPlace; // ������ַ
	private long travelTime; // ��ʻʱ��
	private Double distance; // ·�� ��ǧ�ף�
	private Double oilWear; // �ͺ�
	private String oilKind; // ��Ʒ
	private Double oilPrice; // �ͼ�
	private Double totalOilMoney; // ��Ǯ
	private String placeNotes; // �ȵ���Ϣ
	private long lastUpdateTime; // ���¸���ʱ��
	private String status; // ��־��״̬
	private String appPlatform; /**ϵͳƽ̨( ANDROID,IOS)**/

	public String getAppUserNo() {
		return appUserNo;
	}

	public void setAppUserNo(String appUserNo) {
		this.appUserNo = appUserNo;
	}

	public String getAppDriveLogId() {
		return appDriveLogId;
	}

	public void setAppDriveLogId(String appDriveLogId) {
		this.appDriveLogId = appDriveLogId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getStartLat() {
		return startLat;
	}

	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}

	public String getStartLon() {
		return startLon;
	}

	public void setStartLon(String startLon) {
		this.startLon = startLon;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getEndLat() {
		return endLat;
	}

	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}

	public String getEndLon() {
		return endLon;
	}

	public void setEndLon(String endLon) {
		this.endLon = endLon;
	}

	public String getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}

	public long getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getOilWear() {
		return oilWear;
	}

	public void setOilWear(Double oilWear) {
		this.oilWear = oilWear;
	}

	public Double getOilPrice() {
		return oilPrice;
	}

	public void setOilPrice(Double oilPrice) {
		this.oilPrice = oilPrice;
	}

	public String getOilKind() {
		return oilKind;
	}

	public void setOilKind(String oilKind) {
		this.oilKind = oilKind;
	}

	public Double getTotalOilMoney() {
		return totalOilMoney;
	}

	public void setTotalOilMoney(Double totalOilMoney) {
		this.totalOilMoney = totalOilMoney;
	}

	public String getPlaceNotes() {
		return placeNotes;
	}

	public void setPlaceNotes(String placeNotes) {
		this.placeNotes = placeNotes;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppPlatform() {
		return appPlatform;
	}

	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}

	@Override
	public String toString() {
		return "DrivingJournalItem [appDriveLogId=" + appDriveLogId + ", appUserNo=" + appUserNo
				+ ", vehicleNo=" + vehicleNo + ", startTime=" + startTime + ", startLat="
				+ startLat + ", startLon=" + startLon + ", startPlace=" + startPlace + ", endTime="
				+ endTime + ", endLat=" + endLat + ", endLon=" + endLon + ", endPlace=" + endPlace
				+ ", travelTime=" + travelTime + ", distance=" + distance + ", oilWear=" + oilWear
				+ ", oilKind=" + oilKind + ", oilPrice=" + oilPrice + ", totalOilMoney="
				+ totalOilMoney + ", placeNotes=" + placeNotes + ", lastUpdateTime="
				+ lastUpdateTime + ", status=" + status + ", appPlatform=" + appPlatform + "]";
	}

}
