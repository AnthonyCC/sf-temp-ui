package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnEmployee;

public class EmployeePropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnEmployee p = (TrnEmployee) getValue();
		return "" + p.getEmployeeId();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
		setValue(null);
		} else {
			super.setAsText(text);
		} 
	}
}
