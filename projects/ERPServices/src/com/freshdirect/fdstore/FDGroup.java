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

/**
 * Lightweight class representing a SKU code / version pair.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDGroup implements Serializable {

	/** SKU code */
	private final String groupId;
	
	/** Business object version */
	private final int version;
    
    
    public FDGroup(FDGroup group) {
    	this(group.getGroupId(), group.getVersion()); 
    }

	public FDGroup(String groupId, int version) {
		if(groupId == null)
			throw new IllegalArgumentException("Group ID cannot be null");
		this.groupId = groupId;
		this.version = version;
	}
	
	/**
	 * Get Group Id.
	 *
	 * @return Group Id
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * Get the business object version.
	 *
	 * @return version number
	 */
	public int getVersion() {
		return this.version;
	}
	
	public String toString() {
		return "FDGroup[" + this.groupId + ", " + this.version + "]";
	}
	
	public final int hashCode() {
		return this.groupId.hashCode() ^ this.version;
	}

	public final boolean equals(Object o) {
		if (!(o instanceof FDGroup)) return false;
		FDGroup group = (FDGroup)o;
		return this.groupId.equals(group.groupId) && (this.version==group.version);
	}

}