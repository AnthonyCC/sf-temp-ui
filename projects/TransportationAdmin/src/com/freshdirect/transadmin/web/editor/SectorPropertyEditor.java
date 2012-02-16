package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.Sector;

public class SectorPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		Sector p = (Sector) getValue();
		return "" + p.getName();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
			setValue(null);
		} else {
			try {
				Sector view = new Sector();
				view.setName(text);
				if (view == null) {
					throw new IllegalArgumentException(
							"Invalid argument for Neighbourhood: " + text);
				}
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(
						"Invalid id for Neighbourhood: " + text);
			}
		}
	}
}