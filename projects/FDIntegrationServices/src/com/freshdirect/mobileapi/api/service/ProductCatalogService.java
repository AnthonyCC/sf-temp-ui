package com.freshdirect.mobileapi.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo.CatalogId;
import com.freshdirect.mobileapi.catalog.model.CatalogKey;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.BrowseUtil;

@Component
public class ProductCatalogService {

    public String getPlantId(HttpServletRequest request, SessionUser user) {
        ErpAddressModel address = null;
        if (user != null && user.getShoppingCart() != null && user.getShoppingCart().getDeliveryAddress() != null) {
            address = user.getShoppingCart().getDeliveryAddress();
        }

        String plantid;
        if (address != null) {
            CatalogInfo catalogInfo = null;
            if (address.getZipCode() != null && address.getZipCode().trim().length() > 0) {
                catalogInfo = BrowseUtil.getCatalogInfoAddr(address, user);
            } else {
                catalogInfo = BrowseUtil.getCatalogInfo(user);
            }
            CatalogId catalogId = catalogInfo.getKey();
            plantid = catalogId.getPlantId();
        } else {
            plantid = FulfillmentContext.MANHATTAN_DEFAULT_PLANT_ID;
        }
        return plantid;
    }

    public CatalogKey getCatalogKey(String catalogKey, SessionUser user) {
        CatalogKey key = null;
        if (catalogKey != null) {
            key = CatalogKey.parse(catalogKey);
        } else {
            user.setUserContext();
            key = new CatalogKey();
            key.setPlantId(Long.parseLong(user.getFDSessionUser().getUserContext().getFulfillmentContext().getPlantId()));
            key.setPricingZone(user.getFDSessionUser().getUserContext().getPricingContext().getZoneInfo());
            key.seteStore(ContentFactory.getInstance().getStoreKey().getId());
        }
        return key;
    }
}
