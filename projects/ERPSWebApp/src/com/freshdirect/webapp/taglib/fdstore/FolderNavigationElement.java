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
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;

public class FolderNavigationElement extends NavigationElement {

	private final CategoryModel category;
	private final boolean breakAfter;
	private final String url;

	public FolderNavigationElement(int depth, CategoryModel f, boolean isExpanded) throws FDResourceException {
		super(depth, f);
		this.category = f;
		this.breakAfter = isExpanded;

                ContentNodeModel attr = this.category.getAlias();
		String catId;
		if (attr==null) {
			catId = this.category.getContentName();
		} else {
			// it's an aliased folder
			// !!! this lookup will not be neccessary, when we got rid of PK-based ContentRefs
		    catId = attr.getContentKey().getId();
			//ContentNodeModel alias = ContentFactory.getInstance().getContentNode( ref.getCategoryId() );
			//catId = alias.getContentName();
		}
		
		this.url = "/category.jsp?catId="+catId+"&trk=snav";
	}

	public boolean isAvailable() {
		return true;
	}

	public boolean isProduct() {
		return false;
	}

	public int getPriority() {
		return this.category.getSideNavPriority();
	}

	public String getURL() {
		return this.url;
	}


	public boolean showLink() {
		return this.category.getSideNavLink();
	}

	public boolean isBold() {
		return this.category.getSideNavBold();
	}

	public boolean breakBefore() {
		return false;
	}

	public boolean breakAfter() {
		return breakAfter;
	}

}  // end of class FolderNavigation Line
