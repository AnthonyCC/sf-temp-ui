package com.freshdirect.erp.model;

import java.sql.Timestamp;

public class ActivityLog {
	
	private String matId;
	private String skuCode;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private Timestamp rowAddedDttm;
	private String user;
	private String rootId;
	private String child1Id;
	private String child2Id;
	private String atrType;
	private String atrName;
	private String nutInfoType;
	private String nutritionType;
	private int nutritionPriority;
	
	
	public ActivityLog(String matId, String skuCode, String fieldName, String oldValue, String newValue, Timestamp date, String user) {
		this.matId = matId;
		this.skuCode = skuCode;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.rowAddedDttm = date;
		this.user = user;
	}

	public String getMatId() {
		return matId;
	}
	
	public String getSkuCode() {
		return skuCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public Timestamp getRowAddedDttm() {
		return rowAddedDttm;
	}
	
	public String getUser() {
		return user;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getChild1Id() {
		return child1Id;
	}

	public void setChild1Id(String child1Id) {
		this.child1Id = child1Id;
	}

	public String getChild2Id() {
		return child2Id;
	}

	public void setChild2Id(String child2Id) {
		this.child2Id = child2Id;
	}

	public String getAtrType() {
		return atrType;
	}

	public void setAtrType(String atrType) {
		this.atrType = atrType;
	}
	
	public String getAtrName() {
		return atrName;
	}

	public void setAtrName(String atrName) {
		this.atrName = atrName;
	}

	public String getNutInfoType() {
		return nutInfoType;
	}

	public void setNutInfoType(String nutInfoType) {
		this.nutInfoType = nutInfoType;
	}

	public String getNutritionType() {
		return nutritionType;
	}

	public int getNutritionPriority() {
		return nutritionPriority;
	}

	public void setNutritionPriority(int nutritionPriority) {
		this.nutritionPriority = nutritionPriority;
	}

	public void setNutritionType(String nutritionType) {
		this.nutritionType = nutritionType;
	}

	@Override
	public String toString() {
		return "ActivityLog [atrName=" + atrName + ", atrType=" + atrType
				+ ", child1Id=" + child1Id + ", child2Id=" + child2Id
				+ ", fieldName=" + fieldName + ", matId=" + matId
				+ ", newValue=" + newValue + ", nutInfoType=" + nutInfoType
				+ ", nutritionPriority=" + nutritionPriority
				+ ", nutritionType=" + nutritionType + ", oldValue=" + oldValue
				+ ", rootId=" + rootId + ", rowAddedDttm=" + rowAddedDttm
				+ ", skuCode=" + skuCode + ", user=" + user + "]";
	}
	
}
