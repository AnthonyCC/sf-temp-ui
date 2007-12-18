package com.freshdirect.listadmin.db;

import java.io.Serializable;

public class TemplateOrderBy implements Serializable {
    private String queryOrderById;
    private String queryId;
    private String fieldName;
    private int fieldPosition;
    
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getFieldPosition() {
		return fieldPosition;
	}
	public void setFieldPosition(int fieldPosition) {
		this.fieldPosition = fieldPosition;
	}
	public String getTemplateId() {
		return queryId;
	}
	public void setTemplateId(String queryId) {
		this.queryId = queryId;
	}
	public String getTemplateOrderById() {
		return queryOrderById;
	}
	public void setTemplateOrderById(String queryOrderById) {
		this.queryOrderById = queryOrderById;
	}
}


