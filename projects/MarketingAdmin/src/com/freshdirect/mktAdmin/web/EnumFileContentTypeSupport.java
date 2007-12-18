package com.freshdirect.mktAdmin.web;

import java.beans.PropertyEditorSupport;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;

public class EnumFileContentTypeSupport extends PropertyEditorSupport {
    public String getAsText() {
      Object value = getValue();
      return value == null ? "" : ((EnumFileContentType)value).getName();
    }

    public void setAsText(String string)
        throws IllegalArgumentException {
      try {
        
    	  EnumFileContentType level=EnumFileContentType.getEnum(string);        
        if (level == null) {
          throw new IllegalArgumentException("Invalid argument for EnumFileContentType: " + string);
        }
         setValue(level);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Invalid id for EnumFileContentType: " + string);
      }
    }
  }
