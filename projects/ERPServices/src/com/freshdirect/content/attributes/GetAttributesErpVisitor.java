/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.erp.ErpModelSupport;

/**
 * Visitor to build a FlatAttributeCollection from an Erp tree.
 *
 * @version $Revision$
 * @author $Author$
 */
public class GetAttributesErpVisitor extends ErpIdPathVisitor {

	private final List flatAttributes = new ArrayList();

	protected void visit(ErpModelSupport model, String[] idPath) {
		Map attributes = model.getAttributes().getMap();
		for (Iterator i=attributes.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Entry) i.next();
			FlatAttribute attr = new FlatAttribute(idPath, (String)entry.getKey(), entry.getValue());
			this.flatAttributes.add( attr );
		}
	}

	public FlatAttributeCollection getAttributes() {
		return new FlatAttributeCollection( this.flatAttributes );
	}

	
}
