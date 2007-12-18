package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnEmployeeJobType;

public class EmployeeJobTypePropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnEmployeeJobType p = (TrnEmployeeJobType) getValue();
		return "" + p.getJobTypeId();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
		setValue(null);
		} else {
			super.setAsText(text);
		} 
	}
}

