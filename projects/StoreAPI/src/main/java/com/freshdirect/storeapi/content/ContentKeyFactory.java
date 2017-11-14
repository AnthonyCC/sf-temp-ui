package com.freshdirect.storeapi.content;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

@CmsLegacy
public class ContentKeyFactory {

    private static final String PRES_PICKS_CAT_ID = "picks_love";
    private static final String PRES_PICKS_FDX_CAT_ID = "picks_love_fdx";

    /**
     * @param estoreId
     * @return
     * @throws InvalidContentKeyException
     */
    public static ContentKey getECouponsCategoryKey(EnumEStoreId estoreId) {
        ContentKey key = null;

        switch (estoreId) {
            case FD:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, FDCouponProperties.getCouponCMSCategory());
                break;
            case FDX:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, FDCouponProperties.getCouponCMSCategoryFDX());
                break;
        }

        return key;
    }

    public static ContentKey getECouponsCategoryKey() {
        return getECouponsCategoryKey(CmsManager.getInstance().getEStoreEnum());
    }

    public static ContentKey getNewProductsCategoryKey(EnumEStoreId estoreId) {
        ContentKey key = null;

        switch (estoreId) {
            case FD:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, FDStoreProperties.getNewProductsCatId());
                break;
            case FDX:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, FDStoreProperties.getNewProductsCatFDX());
                break;
        }

        return key;

    }

    public static ContentKey getNewProductsCategoryKey() {
        return getNewProductsCategoryKey(CmsManager.getInstance().getEStoreEnum());
    }

    public static ContentKey getPresidentsPicksCategoryKey(EnumEStoreId estoreId) {
        ContentKey key = null;

        switch (estoreId) {
            case FD:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, PRES_PICKS_CAT_ID);
                break;
            case FDX:
                key = com.freshdirect.cms.core.domain.ContentKeyFactory.get(FDContentTypes.CATEGORY, PRES_PICKS_FDX_CAT_ID);
                break;
        }

        return key;

    }

    public static ContentKey getPresidentsPicksCategoryKey() {
        return getPresidentsPicksCategoryKey(CmsManager.getInstance().getEStoreEnum());
    }
}
