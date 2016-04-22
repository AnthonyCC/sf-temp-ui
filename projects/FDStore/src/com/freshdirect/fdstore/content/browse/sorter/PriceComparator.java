package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PriceComparator implements Comparator<FilteringProductItem> {

    private static final Logger LOGGER = LoggerFactory.getInstance(PriceComparator.class);

    @Override
    public int compare(FilteringProductItem productItem1, FilteringProductItem productItem2) {
        boolean unavailable1 = productItem1.getProductModel().isUnavailable();
        boolean unavailable2 = productItem2.getProductModel().isUnavailable();
        if (unavailable1 && unavailable2) {
            return 0;
        } else if (unavailable1) {
            return 1;
        } else if (unavailable2) {
            return -1;
        }

        ZoneInfo zoneInfo = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo();
        double price1 = getProductInfoDefaultPrice(productItem1, zoneInfo);
        double price2 = getProductInfoDefaultPrice(productItem2, zoneInfo);
        return price1 > price2 ? 1 : (price1 < price2 ? -1 : 0);
    }

    private double getProductInfoDefaultPrice(FilteringProductItem productItem, ZoneInfo zoneInfo) {
        double price = 0;
        try {
            FDProductInfo productInfo = productItem.getFdProductInfo();
            price = productInfo.getZonePriceInfo(zoneInfo).getDefaultPrice();
        } catch (FDResourceException e) {
            LOGGER.error("Failed to obtain fdProductInfo for product " + productItem.getProductModel().getContentName(), e);
        }
        return price;
    }

}
