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
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ProductGroupTag extends AbstractGetterTag<ProductModel> {
	private static final long serialVersionUID = 1L;


	private String categoryId;
	private String productId;
	private String skuCode;
	
	public void setCategoryId(String catId) {
		this.categoryId = catId;	
	}
	
	public void setProductId(String pid) {
		this.productId = pid;
	}
	
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	protected ProductModel getResult() throws FDResourceException {
		ProductModel pm = ContentFactory.getInstance().getProductByName( this.categoryId, this.productId );
		//Convert to Product Pricing Adapter for Zone Pricing.
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		if(pm == null && skuCode!=null && !"".equalsIgnoreCase(skuCode)){
			try {
				pm= ContentFactory.getInstance().getProduct(skuCode);
			} catch (FDSkuNotFoundException e) {				
			}
		}
		if(pm != null && user != null)
			return ProductPricingFactory.getInstance().getPricingAdapter(pm, user.getPricingContext());
		else
			return null;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.content.ProductModel";
		}
	}
}
