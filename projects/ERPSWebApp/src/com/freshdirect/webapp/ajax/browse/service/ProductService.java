package com.freshdirect.webapp.ajax.browse.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

public class ProductService {

    private static final ProductService INSTANCE = new ProductService();

    private ProductService() {
    }

    public static ProductService defaultService() {
        return INSTANCE;
    }

    public void removeSkuDuplicates(List<FilteringProductItem> products) {
        Set<String> skuCodes = new HashSet<String>();
        Iterator<FilteringProductItem> productIterator = products.iterator();
        while (productIterator.hasNext()) {
            FilteringProductItem productItem = productIterator.next();
            if (productItem != null) {
                ProductModel productModel = productItem.getProductModel();
                if (productModel != null) {
                    SkuModel skuModel = productModel.getDefaultSku();
                    if (skuModel != null) {
                        String skuCode = skuModel.getSkuCode();
                        if (skuCodes.contains(skuCode)) {
                            productIterator.remove();
                        } else {
                            skuCodes.add(skuCode);
                        }
                    }
                }
            }
        }
    }

}
