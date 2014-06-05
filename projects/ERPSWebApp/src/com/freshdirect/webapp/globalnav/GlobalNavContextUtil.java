package com.freshdirect.webapp.globalnav;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.GlobalNavigationModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class GlobalNavContextUtil {
	
	public static GlobalNavigationModel getGlobalNavigationModel(FDUserI user) {
		
		String globalNavId = "";
		if (user == null || user.getPricingContext() == null || user.getPricingContext().getUserContext() == null || !user.getPricingContext().getUserContext().isAlcoholRestricted()) {
			globalNavId = "GlobalNavWithWine"; //with wine
		} else {
			globalNavId = "GlobalNavWithoutWine"; //w/o wine
		}
		
		return (GlobalNavigationModel)ContentFactory.getInstance().getContentNode("GlobalNavigation", globalNavId);

	}

}
