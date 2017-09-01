package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.webapp.ajax.analytics.data.GAProductData;

public class GAProductDataService {

    private static final GAProductDataService INSTANCE = new GAProductDataService();

    private GAProductDataService() {

    }

    public static GAProductDataService defaultService() {
        return INSTANCE;
    }

    public GAProductData populateProductData(FDCartLineI cartLine, String quantity) {

        ProductModel product = cartLine.lookupProduct();

        GAProductData data = new GAProductData();

        data.setId(product.getContentName());
        data.setName(product.getFullName());
        data.setPrice(Double.toString(cartLine.getPrice()));
        data.setBrand(product.getPrimaryBrandName());
        data.setCategory(product.getCategory().getContentName());
        data.setVariant(cartLine.getVariantId());
        data.setNewProduct(Boolean.toString(product.isNew()));
        data.setSku(product.getDefaultSkuCode());
        data.setQuantity(roundQuantity(quantity));
        return data;
    }

    private String roundQuantity(String quantity) {
        Double doubleQuantity = Double.parseDouble(quantity);

        if (doubleQuantity < 0) {
            doubleQuantity = Math.floor(doubleQuantity);
            doubleQuantity = (doubleQuantity > 200) ? 200 : doubleQuantity;
        } else if (doubleQuantity > 0) {
            doubleQuantity = Math.ceil(doubleQuantity);
            doubleQuantity = (doubleQuantity < -200) ? -200 : doubleQuantity;
        } else {
            doubleQuantity = (double) 1;
        }

        return String.valueOf(doubleQuantity.intValue());
    }

}
