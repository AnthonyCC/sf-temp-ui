package com.freshdirect.fdstore.content;

import java.io.Serializable;

public class ProductReference implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    String productId;
    String categoryId;

    transient ProductModel model;

    public ProductReference(String categoryId, String productId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }
    
    public ProductReference(ProductModel model) {
        this.model = model;
        if (model != null) {
            this.productId = model.getContentKey().getId();
            this.categoryId = model.getParentNode().getContentName();
        }
    }
    

    public String getCategoryId() {
        return categoryId;
    }

    public String getProductId() {
        return productId;
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

}
