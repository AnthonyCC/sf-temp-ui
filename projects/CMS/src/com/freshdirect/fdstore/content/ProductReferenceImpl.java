package com.freshdirect.fdstore.content;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class ProductReferenceImpl implements ProductReference, Serializable {

    private static final long serialVersionUID = 1L;    
    
    public static final ProductReference NULL_REF = new ProductReferenceImpl(null); 
    
    final String productId;
    final String categoryId;
    final ContentKey key;

    transient ProductModel model;

    public ProductReferenceImpl(String categoryId, String productId) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.key = new ContentKey(FDContentTypes.PRODUCT, productId);
    }
    
    public ProductReferenceImpl(ProductModel model) {
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
    

    @Override
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    public ContentKey getContentKey() {
        return key;
    }

    @Override
    public ProductModel lookupProductModel() {
        if (model == null) {
            model = ContentFactory.getInstance().getProductByName(categoryId, productId);
        }
        return model;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductReferenceImpl other = (ProductReferenceImpl) obj;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
    
    @Override
    public String toString() {
        return "ProductReference[" + categoryId + '/' + productId + ']';
    }

}
