/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlatAttributeCollection implements Serializable {

	private final FlatAttribute[] flatAttributes;

	public FlatAttributeCollection() {
		this.flatAttributes = new FlatAttribute[0];
	}

	public FlatAttributeCollection(List list) {
		this.flatAttributes = (FlatAttribute[]) list.toArray( new FlatAttribute[list.size()] );		
	}
	
	public FlatAttribute[] getFlatAttributes() {
		return this.flatAttributes;
	}

	public Map getAttributeMap(String[] ids) {
		Map attribs = new HashMap();
		for (int i=0; i<this.flatAttributes.length; i++) {
			FlatAttribute ra = this.flatAttributes[i];
			if (ra.isSameIdPath(ids)) {
				attribs.put(ra.getName(), ra.getValue());
			}
		}
		return attribs;
	}

}
