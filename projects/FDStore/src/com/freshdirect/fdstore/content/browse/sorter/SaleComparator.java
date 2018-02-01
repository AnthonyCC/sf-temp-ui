package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductModel;

public class SaleComparator implements Comparator<FilteringProductItem> {

    @Override
    public int compare(FilteringProductItem productItem1, FilteringProductItem productItem2) {
        ProductModel productModel1 = productItem1.getProductModel();
        ProductModel productModel2 = productItem2.getProductModel();

        boolean unavailable1 = productModel1.isUnavailable();
        boolean unavailable2 = productModel2.isUnavailable();
        if (unavailable1 && unavailable2) {
            return 0;
        } else if (unavailable1) {
            return 1;
        } else if (unavailable2) {
            return -1;
        }

        int percentage1 = productModel1.getPriceCalculator().getHighestGroupDealPercentage();
        int percentage2 = productModel2.getPriceCalculator().getHighestGroupDealPercentage();
        return percentage1 > percentage2 ? -1 : (percentage1 < percentage2 ? 1 : 0);
    }
}
