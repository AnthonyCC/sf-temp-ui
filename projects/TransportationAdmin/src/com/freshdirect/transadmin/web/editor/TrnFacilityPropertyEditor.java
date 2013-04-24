package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityType;
public class TrnFacilityPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "";
		}
		TrnFacility p = (TrnFacility) getValue();
		return "" + p.getFacilityId();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("".equals(text)) {
			setValue(null);
		} else {
			try {
				TrnFacility view = new TrnFacility();
				view.setFacilityId(text);
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(
						"Invalid id for TrnFacility: " + text);
			}
		}
	}
}
