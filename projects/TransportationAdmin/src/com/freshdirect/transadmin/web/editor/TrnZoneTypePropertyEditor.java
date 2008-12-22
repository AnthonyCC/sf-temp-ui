package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnZoneType;

public class TrnZoneTypePropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnZoneType p = (TrnZoneType) getValue();
		return "" + p.getZoneTypeId();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
		setValue(null);
		} else {
					 		
		  try {
		        
			  TrnZoneType view = new TrnZoneType();
		        view.setZoneTypeId(text);
		        if (view == null) {
		          throw new IllegalArgumentException("Invalid argument for TrnZoneType: " + text);
		        }
		        setValue(view);
		      } catch (NumberFormatException ex) {
		        throw new IllegalArgumentException("Invalid id for TrnZoneType: " + text);
		      }
		}
	}
}