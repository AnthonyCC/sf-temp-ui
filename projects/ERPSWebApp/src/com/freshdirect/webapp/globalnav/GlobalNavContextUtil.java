package com.freshdirect.webapp.globalnav;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.GlobalNavigationModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class GlobalNavContextUtil {
	
	public static GlobalNavigationModel getGlobalNavigationModel(FDUserI user) {

		String globalNavId = "";
		final String storeId = ContentFactory.getInstance().getStoreKey().getId();

		// Plain and ugly store ID check. Good until we have e-store IDs
		if (EnumEStoreId.FDX.toString().equals( storeId )) {
			globalNavId = "GlobalNavFdx"; //simple logic, export this into CMS if necessary
		} else {
			final boolean isFreeToHaveBeers = user == null
					|| user.getUserContext() == null
					|| !user.getUserContext().getFulfillmentContext().isAlcoholRestricted();

			if (isFreeToHaveBeers) {
				globalNavId = "GlobalNavWithWine"; //with wine
			} else {
				globalNavId = "GlobalNavWithoutWine"; //w/o wine
			}
		}
		return (GlobalNavigationModel) ContentFactory.getInstance()
				.getContentNode( FDContentTypes.GLOBAL_NAVIGATION, globalNavId );
	}

}
