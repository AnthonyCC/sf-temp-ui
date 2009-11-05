/*
 * ProductRef.java
 *
 * Created on March 19, 2002, 10:42 AM
 */

package com.freshdirect.fdstore.content;

import weblogic.auddi.util.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;

/**
 *
 * @author  rgayle
 * @version 
 */
public class ProductRef extends ContentRef {

	/** Creates new ProductRef */
	public ProductRef(String categoryName, String productName) throws NullPointerException {
		super(ContentNodeI.TYPE_PRODUCT);
		if (categoryName == null) {
			throw new IllegalArgumentException("Category Id cannot be null");
		}
		if (productName == null) {
			throw new IllegalArgumentException("Product Id cannot be null");
		}

		this.refName = categoryName;
		this.refName2 = productName;
	}

	public String getCategoryName() {
		return this.refName;
	}

	public String getProductName() {
		return this.refName2;
	}

	public CategoryModel lookupCategory() {
		return (CategoryModel) ContentFactory.getInstance().getContentNode(this.refName);
	}

	public ProductModel lookupProduct() {
		CategoryModel cm = this.lookupCategory();
		if(cm != null) {
			return cm.getProductByName(this.refName2);
		} else {
			Logger.error("ProductRef:  lookupProduct() - CategoryModel is null!");
		}
		return null;
	}

	/**
	 * Please do not call getProductContentKey().getContentNode() instead of lookupProduct, because lookupProduct slower, 
	 * but returns product with the proper parent.
	 *  
	 * @return
	 */
	public ContentKey getProductContentKey() {
	    return new ContentKey(FDContentTypes.PRODUCT, getProductName());
	}

}
