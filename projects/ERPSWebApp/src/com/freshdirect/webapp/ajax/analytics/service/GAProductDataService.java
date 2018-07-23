package com.freshdirect.webapp.ajax.analytics.service;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.analytics.data.GAProductData;

public class GAProductDataService {

    private static final GAProductDataService INSTANCE = new GAProductDataService();
    private static final Logger LOGGER = LoggerFactory.getInstance(GAProductData.class);
    private static final Integer GA_QUANTITY_LIMIT = 200;

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
        data.setPrice(ProductModel.CURRENCY_FORMAT.format(product.getPriceCalculator().getDefaultPriceValue()));
        data.setBrand(product.getPrimaryBrandName());
        data.setCategory(product.getCategory().getContentName());
        data.setVariant(cartLine.getVariantId());
        data.setNewProduct(Boolean.toString(product.isNew()));
        data.setSku(product.getDefaultSkuCode());
        data.setQuantity(roundQuantity(quantity));
        if (!cartLine.isEstimatedPrice()) {
            data.setConfiguredPrice(data.getPrice());
        } else {
            data.setConfiguredPrice(ProductModel.CURRENCY_FORMAT.format(cartLine.getConfiguredPrice()));
        }
        data.setActualPrice(ProductModel.CURRENCY_FORMAT.format(cartLine.getActualPrice()));
        return data;
    }

    private double roundQuantity(String quantity) {
        double result = 0;
        try {
        Double doubleQuantity = Double.parseDouble(quantity);

        if (doubleQuantity < 0) {
            doubleQuantity = Math.floor(doubleQuantity);
                doubleQuantity = (doubleQuantity < -GA_QUANTITY_LIMIT) ? -GA_QUANTITY_LIMIT : doubleQuantity;
        } else if (doubleQuantity > 0) {
            doubleQuantity = Math.ceil(doubleQuantity);
                doubleQuantity = (doubleQuantity > GA_QUANTITY_LIMIT) ? GA_QUANTITY_LIMIT : doubleQuantity;
        } else {
                doubleQuantity = 1d;
            }

            result = doubleQuantity.intValue();

        } catch (NumberFormatException e) {
            LOGGER.error("Quantity is not a number: ", e);
        }


        return result;
    }

}
