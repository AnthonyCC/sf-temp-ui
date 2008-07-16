package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;

public class DYFUtil {
	/**
	 * This method verifies whether customer is eligible for DYF Recommendation Service
	 * 
	 * @author segabor
	 * 
	 * @param user <{@link FDUserI}> current customer
	 * @return The result of test
	 * @throws FDResourceException 
	 * 
	 */
	public static boolean isCustomerEligible(FDUserI user) throws FDResourceException {
		if (user.getLevel()==FDUserI.GUEST || !user.isDYFEnabled()) {
			return false;
		}

		// throw customers having only one or two orders
		if (user.getOrderHistory().getValidOrderCount() < 2) {
			return false;
		}
		
		return true;
	}
}
