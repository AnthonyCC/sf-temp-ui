package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public interface ProductReference {
	public ProductModel lookupProductModel();
	
	public String getCategoryId();
	public String getProductId();
	public ContentKey getContentKey();
}
