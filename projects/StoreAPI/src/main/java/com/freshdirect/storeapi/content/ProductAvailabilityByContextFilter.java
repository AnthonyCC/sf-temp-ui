package com.freshdirect.storeapi.content;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

/**
 * check availabilities light version by user context. Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
 */

public class ProductAvailabilityByContextFilter extends AbstractProductFilter {

    @Override
    public boolean applyTest(ProductModel product) throws FDResourceException {
        return !isAlcoholRestrictedByContextAndSku(product); // For now only this later on plant and other factor will play
    }

    private boolean isAlcoholRestrictedByContextAndSku(ProductModel product) throws FDResourceException {
        boolean isAlcohol;
        try {
            // Figuring out if product contains alcohol is like finding life in mars...
            isAlcohol = ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().isAlcoholRestricted() && product.isAlcoholProduct();
        } catch (FDSkuNotFoundException e) {
            isAlcohol = false;
        }
        return isAlcohol;
    }
}
