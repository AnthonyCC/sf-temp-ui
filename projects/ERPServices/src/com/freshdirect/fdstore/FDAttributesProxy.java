/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.content.attributes.ErpsAttributesKey;
import com.freshdirect.erp.ErpModelSupport;

/**
 * Attribute proxy class.
 * 
 * @version $Revision$
 * @author $Author$
 */
public abstract class FDAttributesProxy implements Serializable {
	private static final long serialVersionUID = 1005378598763443435L;
	
	private ErpsAttributesKey attributesKey;

	public FDAttributesProxy(ErpModelSupport model) {
		attributesKey = model.getAttributesKey();
	}
	
	protected FDAttributesProxy() {
	}
	
	protected void setAttributesKey(ErpsAttributesKey attributesKey) {
            this.attributesKey = attributesKey;
        }
	

	protected ErpsAttributes getAttributes() {
	    if (attributesKey == null) {
	        // used for testing
	        return new ErpsAttributes(null);
	    } else {
		return FDAttributesCache.getInstance().getAttributes(attributesKey);
	    }
	}
}
