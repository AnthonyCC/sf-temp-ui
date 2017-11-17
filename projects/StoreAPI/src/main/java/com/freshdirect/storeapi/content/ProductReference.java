package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;

public interface ProductReference {
	public ProductModel lookupProductModel();

	public String getCategoryId();
	public String getProductId();
	public ContentKey getContentKey();
}
