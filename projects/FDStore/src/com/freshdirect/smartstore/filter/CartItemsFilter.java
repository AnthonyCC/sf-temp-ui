package com.freshdirect.smartstore.filter;

import java.util.Collection;

import com.freshdirect.fdstore.content.ProductModel;

/**
 * Filter out items which are on the cart already.
 * 
 * @author zsombor
 *
 */
public final class CartItemsFilter extends ProductFilter {
    private final Collection cartItems;

    public CartItemsFilter(Collection cartItems) {
        this.cartItems = cartItems;
    }

    public ProductModel filter(ProductModel model) {
    	return model == null || cartItems.contains(model.getContentKey()) ? null : model;
    }
}