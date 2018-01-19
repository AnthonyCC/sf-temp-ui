package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Date;
import java.util.Map;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductModel;

public class RecencyComparator extends OptionalObjectComparator<FilteringProductItem, Long> {

    @Override
    protected Long getValue(FilteringProductItem obj) {
        ProductModel prod = obj.getProductModel();
        String key = getProductNewnessKey(prod);
        Date addedDate = null;
        Map<ContentKey, Map<String, Date>> newProducts = ContentFactory.getInstance().getNewProducts();
        if (newProducts.containsKey(prod.getContentKey())) {
            addedDate = newProducts.get(prod.getContentKey()).get(key);
        }

        if (addedDate == null) {
            newProducts = ContentFactory.getInstance().getBackInStockProducts();
            if (newProducts.containsKey(prod.getContentKey())) {
                addedDate = newProducts.get(prod.getContentKey()).get(key);
            }
        }

        return addedDate == null ? null : -1 * addedDate.getTime(); // default is descending
    }

    private String getProductNewnessKey(ProductModel product) {
        String key = "";
        ZoneInfo zone = product.getUserContext().getPricingContext().getZoneInfo();
        if (zone != null) {
            key = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        return key;
    }

}
