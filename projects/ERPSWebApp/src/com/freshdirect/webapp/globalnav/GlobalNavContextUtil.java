package com.freshdirect.webapp.globalnav;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.GlobalNavigationModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;

public class GlobalNavContextUtil {

    public static GlobalNavigationModel getGlobalNavigationModel(FDUserI user) throws FDResourceException {

        String globalNavId = "";
        final EnumEStoreId eStore = CmsManager.getInstance().getEStoreEnum();

        // Plain and ugly store ID check. Good until we have e-store IDs
        if (EnumEStoreId.FDX == eStore) {
            globalNavId = "GlobalNavFdx"; // simple logic, export this into CMS if necessary
        } else {
            final boolean isFreeToHaveBeers = user != null && user.getZipCode() != null && !FDUserUtil.isAlcoholRestricted(user.getZipCode());
            if (isFreeToHaveBeers || !FDStoreProperties.isCoreNonCoreGlobalNavSwitchEnabled() || user.getLevel() == FDUserI.GUEST) {
                globalNavId = "GlobalNavWithWine"; // with wine
            } else {
                globalNavId = "GlobalNavWithoutWine"; // w/o wine
            }
        }
        return (GlobalNavigationModel) ContentFactory.getInstance().getContentNode(FDContentTypes.GLOBAL_NAVIGATION, globalNavId);
    }

}
