/*
 * CategroyRef.java
 *
 * Created on March 19, 2002, 10:46 AM
 */

package com.freshdirect.fdstore.content;

/**
 *
 * @author  rgayle
 * @version 
 */
public class CategoryRef extends ContentRef {

	/** Creates new CategroyRef */
	public CategoryRef(String refName) {
		super(ContentNodeI.TYPE_CATEGORY);
		if (refName == null) {
			throw new IllegalArgumentException("Category Id cannot be null");
		}
		this.refName = refName;
	}

	public String getCategoryName() {
		return this.refName;
	}

	public CategoryModel getCategory() {
		return (CategoryModel) ContentFactory.getInstance().getContentNode(this.refName);
	}

}
