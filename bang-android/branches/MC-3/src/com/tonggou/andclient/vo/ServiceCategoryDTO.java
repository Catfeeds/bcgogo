package com.tonggou.andclient.vo;

public class ServiceCategoryDTO {
	 private String id;                 //����                            long
	 private String name;				//��������                          String
	 private String parentId;				//�÷������һ��id         long
	 private String categoryType;			// FIRST_CATEGORY �� SECOND_CATEGORY
	 private String seviceScope;			// serviceScope 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getSeviceScope() {
		return seviceScope;
	}
	public void setSeviceScope(String seviceScope) {
		this.seviceScope = seviceScope;
	}

}
