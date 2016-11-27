package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.ParkingLocation;

public class ParkingLocationPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "";
		}
		ParkingLocation p = (ParkingLocation) getValue();
		return "" + p.getLocationName();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("".equals(text)) {
			setValue(null);
		} else {
			try {
				ParkingLocation view = new ParkingLocation();
				view.setLocationName(text);
				if (view == null) {
					throw new IllegalArgumentException(
							"Invalid argument for ParkingLocation: " + text);
				}
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid id for ParkingLocation: "+ text);
			}
		}
	}
}