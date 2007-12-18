package com.freshdirect.listadmin.db;

import java.io.Serializable;

public class TemplateField implements Serializable {
    private String queryFieldId;
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
	public String getTemplateFieldId() {
		return queryFieldId;
	}
	public void setTemplateFieldId(String queryFieldId) {
		this.queryFieldId = queryFieldId;
	}
	public String getTemplateId() {
		return queryId;
	}
	public void setTemplateId(String queryId) {
		this.queryId = queryId;
	}
}


