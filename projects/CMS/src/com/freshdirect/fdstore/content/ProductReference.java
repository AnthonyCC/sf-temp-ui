package com.freshdirect.fdstore.content;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class ProductReference implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    final String productId;
    final String categoryId;
    final ContentKey key;

    transient ProductModel model;

    public ProductReference(String categoryId, String productId) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.key = new ContentKey(FDContentTypes.PRODUCT, productId);
    }
    
    public ProductReference(ProductModel model) {
        this.model = model;
        if (model != null) {
            this.key = model.getContentKey();
            this.productId = model.getContentKey().getId();
            this.categoryId = model.getParentNode().getContentName();
        } else {
            this.categoryId = null;
            this.productId = null;
            this.key = null;
        }
    }
    

    public String getCategoryId() {
        return categoryId;
    }

    public String getProductId() {
        return productId;
    }
    
    public ContentKey getContentKey() {
        return key;
    }

    public ProductModel lookupProductModel() {
        if (model == null) {
            model = ContentFactory.getInstance().getProduct(categoryId, productId);
        }
        return model;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductReference) {
            ProductReference p = (ProductReference) obj;
            return productId.equals(p.productId) && categoryId.equals(p.categoryId);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "ProductReference[" + categoryId + '/' + productId + ']';
    }

}
