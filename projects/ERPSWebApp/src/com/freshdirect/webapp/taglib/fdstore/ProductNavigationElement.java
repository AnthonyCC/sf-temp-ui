/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.fdstore.content.ProductModel;

public class ProductNavigationElement extends NavigationElement {

	private final ProductModel product;
	private final String url;

	public ProductNavigationElement(int depth, ProductModel product) {
		super(depth, product);
		this.product = product;
		this.url = "/product.jsp?productId=" + product.getContentName() + "&catId=" + product.getParentNode().getContentName() + "&trk=snav";
	}

	public boolean isAvailable() {
		return !this.product.isUnavailable();
	}

	public boolean isProduct() {
		return true;
	}

	public int getPriority() {
		return 0;
	}

	public String getURL() {
		return this.url;
	}

	public boolean showLink() {
		return true;
	}

	public boolean isBold() {
		return false;
	}

	public boolean breakBefore() {
		return false;
	}

	public boolean breakAfter() {
		return false;
	}

}
