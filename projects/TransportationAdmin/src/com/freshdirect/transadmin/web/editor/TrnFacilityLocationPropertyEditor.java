package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnFacilityLocation;

public class TrnFacilityLocationPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnFacilityLocation p = (TrnFacilityLocation) getValue();
		return "" + p.getCode();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
			setValue(null);
		} else {
			try {
				TrnFacilityLocation view = new TrnFacilityLocation();
				view.setCode(text);
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(
						"Invalid id for TrnFacilityLocation: " + text);
			}
		}
	}
}
