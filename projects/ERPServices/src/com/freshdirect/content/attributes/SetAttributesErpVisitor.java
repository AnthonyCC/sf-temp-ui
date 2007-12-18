/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.util.Map;

import com.freshdirect.erp.ErpModelSupport;

/**
 * Apply a FlatAttributeCollection to an Erp tree. 
 *
 * @version $Revision$
 * @author $Author$
 */
public class SetAttributesErpVisitor extends ErpIdPathVisitor {

	private final FlatAttributeCollection attributes;

	public SetAttributesErpVisitor(FlatAttributeCollection attributes) {
		this.attributes = attributes;
	}
    
	protected void visit(ErpModelSupport model, String[] idPath) {
		Map attribMap = this.attributes.getAttributeMap(idPath);
		model.setAttributes( new AttributeCollection(attribMap) );
	}		

}
