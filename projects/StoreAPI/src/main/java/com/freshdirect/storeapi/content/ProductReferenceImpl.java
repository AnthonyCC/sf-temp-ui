package com.freshdirect.storeapi.content;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductReferenceImpl implements ProductReference, Serializable {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductReferenceImpl.class);

    private static final long serialVersionUID = 1L;

    public static final ProductReference NULL_REF = new ProductReferenceImpl(null);

    final String productId;
    final String categoryId;
    final ContentKey key;

    transient ProductModel model;

    public ProductReferenceImpl(String categoryId, String productId) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.key = ContentKeyFactory.get(ContentType.Product, productId);
    }

    public ProductReferenceImpl(ProductModel model) {
        this.model = model;
        if (model != null) {
            //LOGGER.info("Generating ProductReferenceImpl from Product model: [" + model.getContentKey().toString() + "] with parent: " + model.getParentNode());
            this.key = model.getContentKey();
            this.productId = model.getContentKey().id;
            this.categoryId = model.getParentNode() != null ?  model.getParentNode().getContentName() : null;
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

    @Override
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
