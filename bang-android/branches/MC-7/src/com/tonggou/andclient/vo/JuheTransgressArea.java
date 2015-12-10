package com.tonggou.andclient.vo;

import java.util.List;

public class JuheTransgressArea extends Area {

	private static final long serialVersionUID = 4844396861764092899L;
	
	private List<JuheTransgressArea> children;		// �ӵ���
	private String juheCityCode;					// �ۺϳ�����
	private String juheStatus;						// �ۺ�״̬��
	private JuheTransgressSearchCondition juheViolateRegulationCitySearchCondition;	// ��ѯ����
	
	public List<JuheTransgressArea> getChildren() {
		return children;
	}
	public void setChildren(List<JuheTransgressArea> children) {
		this.children = children;
	}
	public String getJuheCityCode() {
		return juheCityCode;
	}
	public void setJuheCityCode(String juheCityCode) {
		this.juheCityCode = juheCityCode;
	}
	public String getJuheStatus() {
		return juheStatus;
	}
	public void setJuheStatus(String juheStatus) {
		this.juheStatus = juheStatus;
	}

	@Override
	public String toString() {
		return name == null ? "" : name;
	}
	
	@Override
	public boolean equals(Object o) {
		if( o instanceof JuheTransgressArea ) {
			JuheTransgressArea otherArea = (JuheTransgressArea) o;
			return otherArea.getId() == this.id;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (id + "").hashCode();
	}
	
	public JuheTransgressSearchCondition getJuheViolateRegulationCitySearchCondition() {
		return juheViolateRegulationCitySearchCondition;
	}
	public void setJuheViolateRegulationCitySearchCondition(
			JuheTransgressSearchCondition juheViolateRegulationCitySearchCondition) {
		this.juheViolateRegulationCitySearchCondition = juheViolateRegulationCitySearchCondition;
	}
	@Override
	public JuheTransgressArea clone() {
		JuheTransgressArea area = new JuheTransgressArea();
		area.setId(id);
		area.setJuheCityCode(juheCityCode);
		area.setJuheStatus(juheStatus);
		area.setName(name);
		area.setChildren(getChildren());
		area.setCityCode(cityCode);
		area.setJuheViolateRegulationCitySearchCondition(
				juheViolateRegulationCitySearchCondition);
		return area;
	}
}
