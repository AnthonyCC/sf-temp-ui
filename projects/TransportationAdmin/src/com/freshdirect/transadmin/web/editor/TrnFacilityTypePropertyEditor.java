package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnFacilityType;
public class TrnFacilityTypePropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnFacilityType p = (TrnFacilityType) getValue();
		return "" + p.getName();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
			setValue(null);
		} else {
			try {
				TrnFacilityType view = new TrnFacilityType();
				view.setName(text);
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(
						"Invalid id for TrnFacilityType: " + text);
			}
		}
	}
}
