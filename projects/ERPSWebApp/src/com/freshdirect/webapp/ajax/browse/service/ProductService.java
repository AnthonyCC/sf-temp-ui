package com.freshdirect.webapp.ajax.browse.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductModel;

public class ProductService {

    private static final ProductService INSTANCE = new ProductService();

    private ProductService() {
    }

    public static ProductService defaultService() {
        return INSTANCE;
    }

    public void removeContentKeyDuplicates(List<FilteringProductItem> products) {
        Set<ContentKey> contentKeys = new HashSet<ContentKey>();
        Iterator<FilteringProductItem> productIterator = products.iterator();
        while (productIterator.hasNext()) {
            FilteringProductItem productItem = productIterator.next();
            if (productItem != null) {
                ProductModel productModel = productItem.getProductModel();
                if (productModel != null) {
                    ContentKey contentKey = productModel.getContentKey();
                    if (contentKeys.contains(contentKey)) {
                        productIterator.remove();
                    } else {
                        contentKeys.add(contentKey);
                    }
                }
            }
        }
    }

}
