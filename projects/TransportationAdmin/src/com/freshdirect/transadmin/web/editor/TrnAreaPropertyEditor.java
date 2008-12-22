package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnEmployee;

public class TrnAreaPropertyEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		TrnArea p = (TrnArea) getValue();
		return "" + p.getCode();
	}

	
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text)) {
		setValue(null);
		} else {
					 		
		  try {
		        
			  TrnArea view = new TrnArea();
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