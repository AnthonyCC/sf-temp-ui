/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MultiAttribute extends Attribute {
    
	private List valueList;

    public MultiAttribute(EnumAttributeType type, String key) {
		this(type, key, false);
    }

    public MultiAttribute(EnumAttributeType type, String key, boolean inheritable) {
		super(type, key, inheritable);
		this.valueList = new ArrayList();
    }
    
    public Object getValue() {
		return this.getValues();
    }
    
    public int numberOfValues() {
        return this.valueList.size();
    }

	public Object getValue(int idx) {
		return this.valueList.get(idx);
	}
	
	public void setValue(Object valueList){
		this.valueList = (List) valueList;
	}

	public void addValue(Object value) {
		// !!! check value for EnumAttributeType -> throw new IllegalArgumentException();
		this.valueList.add(value);
	}
    
    public List getValues() {
        return Collections.unmodifiableList( this.valueList );
    }
	
    public String toString() {
        return "MultiAttribute[" + this.getKey() + " - " + this.getType().getName() + " - " + this.numberOfValues() + " values ]";
    }
	
	public Attribute copy() {
		MultiAttribute ma = new MultiAttribute(this.getType(),this.getKey(),this.isInheritable());
		for (Iterator vItr = this.valueList.iterator(); vItr.hasNext();) {
			ma.addValue(vItr.next());
		}
		return ma;
	}
}
