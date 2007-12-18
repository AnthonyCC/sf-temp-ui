package com.freshdirect.listadmin.db;

import java.io.Serializable;

public class TemplateGroupBy implements Serializable {
    private String queryGroupById;
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
	public String getTemplateGroupById() {
		return queryGroupById;
	}
	public void setTemplateGroupById(String queryGroupById) {
		this.queryGroupById = queryGroupById;
	}
	public String getTemplateId() {
		return queryId;
	}
	public void setTemplateId(String queryId) {
		this.queryId = queryId;
	}
}


