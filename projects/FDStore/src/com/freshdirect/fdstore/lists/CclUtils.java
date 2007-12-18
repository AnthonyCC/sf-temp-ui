package com.freshdirect.fdstore.lists;

import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class CclUtils {
	
	public static final String CC_LIST_ID = "ccListId";
	public static final String STARTER_LIST_ID = "starterListId";
    
	public static boolean isCCLInExperienced(FDUserI user, List lists) throws FDResourceException {
			
		if (user.getLevel() == FDUserI.GUEST) {
			return true;
		}
		
		if (lists == null) {
			return false;
		}
		
		if (lists.size() > 1) {
			return false;
		}
		FDCustomerCreatedList l = (FDCustomerCreatedList) lists.get(0);
		if (!l.getCreateDate().equals(l.getModificationDate())) {
			return false;
		}
		return true;
	}

	public static boolean isCCLEnabled(FDUserI user) {	
		return FDStoreProperties.isCclEnabled() || EnumSiteFeature.CCL.isEnabled(user);
	}	
}
