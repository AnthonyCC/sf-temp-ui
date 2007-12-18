/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ProductGroupTag extends AbstractGetterTag {
	
	private String categoryId;
	private String productId;
	
	public void setCategoryId(String catId) {
		this.categoryId = catId;	
	}
	
	public void setProductId(String pid) {
		this.productId = pid;
	}
	
	protected Object getResult() throws FDResourceException {
		return ContentFactory.getInstance().getProductByName( this.categoryId, this.productId );
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.content.ProductModel";
		}

	}

}
