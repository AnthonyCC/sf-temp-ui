/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class DomainValue extends ContentNodeModelImpl {

	public DomainValue(ContentKey cKey) {
		super(cKey);
	}

	public String getLabel() {
		return getAttribute("Label", this.getCMSNode().getKey().getId());
	}

	/**
	 *  This is not getting the value [-batchley 20090710]
	 */
	public String getValue() { /*do this so we dont have to do it on the jsp level */
		return getAttribute("Label", this.getCMSNode().getKey().getId());
	}
	
	/**
	 *  Actually get the value 
	 */
	public String getTheValue() {
		return getAttribute("VALUE", this.getCMSNode().getKey().getId());
	}
	
	public String getID() {
		return this.getCMSNode().getKey().getId();
	}

	public Domain getDomain() {
		return (Domain) this.getParentNode();
	}

	public String toString() {
		return "DomainValue[" + getDomain().toString() + ", " + getLabel() + ", " + getValue() + ", " + getPriority() + "]";
	}
	
	public boolean equals(Object obj){
		if(obj!= null && obj instanceof DomainValue){
			DomainValue dv = (DomainValue)obj;
			return (this.getContentName().equals(dv.getContentName()) && this.getValue().equals(dv.getValue())); 
		}
		return false;
	}

}
