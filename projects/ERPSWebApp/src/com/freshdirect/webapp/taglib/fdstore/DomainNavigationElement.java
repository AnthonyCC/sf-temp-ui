package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.CategoryRef;

public class DomainNavigationElement extends NavigationElement {

	private final CategoryModel category;
	private final boolean breakAfter;
	private final String url;
	private final DomainValue domainValue;
	

	public DomainNavigationElement(int depth, CategoryModel f, boolean isExpanded, DomainValue domainValue) throws FDResourceException {
		super(depth, f);
		this.category = f;
		this.breakAfter = isExpanded;
		this.domainValue=domainValue;
		Attribute attr = this.category.getAttribute("ALIAS");
		String catId;
		if (attr==null) {
			catId = this.category.getContentName();
		} else {
			// it's an aliased folder
			// !!! this lookup will not be neccessary, when we got rid of PK-based ContentRefs
			catId = ((CategoryRef)attr.getValue()).getCategoryName();
			//ContentNodeModel alias = ContentFactory.getInstance().getContentNode( ref.getCategoryId() );
			//catId = alias.getContentName();
		}
		this.url = "/category.jsp?catId="+catId+"&domainName="+domainValue.getDomain().getName()+"&domainValue="+domainValue.getContentName()+"&trk=snav";
		
	}

	public boolean isAvailable() {
		return true;
	}

	public boolean isProduct() {
		return false;
	}
	
	public boolean isDomainValue() {
		return true;
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
		return false;
	}

	public boolean breakBefore() {
		return false;
	}

	public boolean breakAfter() {
		return breakAfter;
	}
	public DomainValue getDomainValue() {
		return domainValue;
	}

	public String getDisplayString() {
		return domainValue!=null? domainValue.getLabel():"";
	}
}  // end of class FolderNavigation Line
