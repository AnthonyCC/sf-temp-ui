package com.freshdirect.transadmin.web.ui;

import com.freshdirect.transadmin.web.model.DispatchResourceInfo;

public class FDDispatchSummaryResourceCell extends FDDispatchResourceCell  {
		
    
    public String getResourceName(DispatchResourceInfo resource) {
    	if(resource != null) {
    		return resource.getNameWithFirstInitial();
    	} else {
    		return "";
    	}
    }
    
}