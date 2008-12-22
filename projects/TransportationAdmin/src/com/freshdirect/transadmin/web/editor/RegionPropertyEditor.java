package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnEmployee;

public class RegionPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		Region p = (Region) getValue();
		return "" + p.getCode();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
		setValue(null);
		} else {
					 		
		  try {
		        
			  Region view = new Region();
		        view.setCode(text);
		        if (view == null) {
		          throw new IllegalArgumentException("Invalid argument for TrnArea: " + text);
		        }
		        setValue(view);
		      } catch (NumberFormatException ex) {
		        throw new IllegalArgumentException("Invalid id for TrnArea: " + text);
		      }
		}
	}
}