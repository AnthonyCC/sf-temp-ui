package com.freshdirect.mktAdmin.web;

import java.beans.PropertyEditorSupport;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;

public class EnumRestrictionListActionTypeSupport extends PropertyEditorSupport {
    public String getAsText() {
      Object value = getValue();
      return value == null ? "" : ((EnumListUploadActionType)value).getName();
    }

    public void setAsText(String string)
        throws IllegalArgumentException {
      try {
        
    	  EnumListUploadActionType level=EnumListUploadActionType.getEnum(string);        
        if (level == null) {
          throw new IllegalArgumentException("Invalid argument for EnumListUploadActionType: " + string);
        }
         setValue(level);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Invalid id for EnumListUploadActionType: " + string);
      }
    }
  }
