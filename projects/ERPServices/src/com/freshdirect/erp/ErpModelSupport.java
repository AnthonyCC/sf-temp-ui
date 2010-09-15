/*
 * $Workfile: ErpModelSupport.java$
 *
 * $Date: 9/4/2001 5:47:45 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import com.freshdirect.content.attributes.ErpsAttributeContainerI;
import com.freshdirect.content.attributes.ErpsAttributesKey;
import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.fdstore.FDAttributesCache;
import com.freshdirect.framework.core.ModelSupport;

/**
 * Base class for ERP Model objects.
 * 
 * @version $Revision: 7$
 * @author $Author: Viktor Szathmary$
 */
public abstract class ErpModelSupport extends ModelSupport implements ErpsAttributeContainerI {
	private static final long serialVersionUID = -2034945212493970174L;

	private ErpsAttributesKey attributesKey;

	private ErpsAttributes erpsAttributes;
	
	public void setAttributesKey(ErpsAttributesKey attributesKey) {
		this.attributesKey = attributesKey;
	}

	public ErpsAttributesKey getAttributesKey() {
		return attributesKey;
	}

	public ErpsAttributes getAttributes() {
		return erpsAttributes == null ? FDAttributesCache.getInstance().getAttributes(attributesKey) : erpsAttributes;
	}
	
	/**
	 * This is used during collecting changes before persisting in ERPSy-Daisy.
	 * @param erpsAttributes
	 */
	public void setChangedAttributes(ErpsAttributes erpsAttributes) {
            this.erpsAttributes = erpsAttributes;
        }
	
	
	public ErpsAttributes getChangedAttributes() {
            return erpsAttributes;
        }

	/**
	 * Accept an ErpVisitor. Calls the template method visitChildren().
	 */
	public final void accept(ErpVisitorI visitor) {
		visitor.pushModel(this);
		this.visitChildren(visitor);
		visitor.popModel();
	}

	/**
	 * Template method to visit the children of this ErpModel. It should call
	 * accept(visitor) on these (or do nothing).
	 * 
	 * @param visitor
	 *            visitor instance to pass around
	 */
	public abstract void visitChildren(ErpVisitorI visitor);
}