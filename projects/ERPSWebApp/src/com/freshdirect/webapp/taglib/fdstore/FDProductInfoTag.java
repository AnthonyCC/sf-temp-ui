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
public class FDProductInfoTag extends AbstractGetterTag {

	private static Category LOGGER = LoggerFactory.getInstance( FDProductInfoTag.class );
	
	private String skuCode = null;
	
	public void setSkuCode(String sc) {
		this.skuCode = sc;
	}

	protected Object getResult() throws FDResourceException {
		FDProductInfo productInfo = null;
		if (skuCode != null) {
			try {
				productInfo = FDCachedFactory.getProductInfo(this.skuCode);
			} catch (FDSkuNotFoundException ex) {
				// safe to ignore
				LOGGER.info(ex);
			}
		}
		return productInfo;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.FDProductInfo";
		}

	}
	
}
