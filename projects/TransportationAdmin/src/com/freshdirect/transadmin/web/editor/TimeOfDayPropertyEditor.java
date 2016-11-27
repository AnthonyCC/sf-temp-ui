package com.freshdirect.transadmin.web.editor;

import java.beans.PropertyEditorSupport;

import com.freshdirect.framework.util.TimeOfDay;


public class TimeOfDayPropertyEditor extends PropertyEditorSupport {

	public String getAsText() {
		if (getValue() == null) {
			return "null";
		}
		//System.out.println("inside TimeOfDayPropertyEditor getAsText "+getValue());

		TimeOfDay p = (TimeOfDay) getValue();
		return "" + p.getAsString();
	}


	public void setAsText(String text) throws IllegalArgumentException {
		 try {
			// System.out.println("inside TimeOfDayPropertyEditor setAsText "+text);
			 TimeOfDay level=new TimeOfDay(text);
	        if (level == null) {
	          throw new IllegalArgumentException("Invalid argument for EnumFileContentType: " + text);
	        }
	         setValue(level);
	      } catch (NumberFormatException ex) {
	        throw new IllegalArgumentException("Invalid id for EnumFileContentType: " + text);
	      }
	}
}

