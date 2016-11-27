package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.DispatchGroup;

public class DispatchGroupPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		DispatchGroup p = (DispatchGroup) getValue();
		return "" + p.getDispatchGroupId();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
			setValue(null);
		} else {
			try {
				DispatchGroup view = new DispatchGroup();
				view.setDispatchGroupId(text);
				setValue(view);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid id for Region: " + text);
			}
		}
	}
}