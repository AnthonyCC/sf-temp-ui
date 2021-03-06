package com.freshdirect.webapp.globalnav;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.GlobalNavigationModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;

public class GlobalNavContextUtil {

    public static GlobalNavigationModel getGlobalNavigationModel(FDUserI user) throws FDResourceException {

        String globalNavId = "";
        final EnumEStoreId eStore = CmsManager.getInstance().getEStoreEnum();

        // Plain and ugly store ID check. Good until we have e-store IDs
        if (EnumEStoreId.FDX == eStore) {
            globalNavId = "GlobalNavFdx"; // simple logic, export this into CMS if necessary
        } else {
            final boolean isFreeToHaveBeers = user != null && user.getZipCode() != null && !FDUserUtil.isAlcoholRestricted(user.getZipCode());
            boolean isCosRedesign2017 = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.cosRedesign2017, user);
            boolean coreNonCoreSwitch = isFreeToHaveBeers || !FDStoreProperties.isCoreNonCoreGlobalNavSwitchEnabled() || user.getLevel() == FDUserI.GUEST;

            if (isCosRedesign2017 && user.isCorporateUser()) {
                if (coreNonCoreSwitch) {
                    globalNavId = "CosGlobalNavWithWine"; // with wine
                } else {
                    globalNavId = "CosGlobalNavWithoutWine"; // w/o wine
                }
            }

            else if (coreNonCoreSwitch) {
                globalNavId = "GlobalNavWithWine"; // with wine
            } else {
                globalNavId = "GlobalNavWithoutWine"; // w/o wine
            }
        }
        return (GlobalNavigationModel) ContentFactory.getInstance().getContentNode(FDContentTypes.GLOBAL_NAVIGATION, globalNavId);
    }

}
