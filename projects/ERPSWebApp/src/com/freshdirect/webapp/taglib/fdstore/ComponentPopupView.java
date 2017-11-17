package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;

import com.freshdirect.fdstore.content.ContentNodeModel;

/**@author ekracoff*/
public class ComponentPopupView {
	private final ContentNodeModel category;
	private final String parentProductSku;
	private final List products;
	private final String title;
	private final String level;

	public ComponentPopupView(
		ContentNodeModel category,
		String parentProductSku,
		List products,
		String title,
		String level) {
		this.category = category;
		this.parentProductSku = parentProductSku;
		this.products = products;
		this.title = title;
		this.level = level;
	}
	
	//Constructor for when the category is the parent of the popup
	public ComponentPopupView(ContentNodeModel category, List products, String title, String level) {
			this(category, null, products, title, level);
		}

	public ContentNodeModel getCategory() {
		return category;
	}

	public String getParentProductSku() {
		return parentProductSku;
	}

	public List getProducts() {
		return products;
	}

	public String getLevel() {
		return level;
	}

	public String getTitle() {
		return title;
	}
	


}
