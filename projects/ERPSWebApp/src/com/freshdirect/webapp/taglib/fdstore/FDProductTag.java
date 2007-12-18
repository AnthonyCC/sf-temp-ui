/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDProductTag extends AbstractGetterTag {

	private static Category LOGGER = LoggerFactory.getInstance( FDProductTag.class );
	
	private FDProductInfo productInfo = null;
	
	public void setProductInfo(FDProductInfo pi) {
		this.productInfo = pi;
	}
	
	protected Object getResult() throws FDResourceException {
		FDProduct product = null;
		if (productInfo != null) {
			try {
				product = FDCachedFactory.getProduct(productInfo);
			} catch (FDSkuNotFoundException ex) {
				// safe to ignore
				LOGGER.info(ex);
			}
		}
		return product;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.FDProduct";
		}

	}
	
}
