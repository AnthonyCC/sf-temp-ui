/*
 * $Workfile: DurableModelI.java$
 *
 * $Date: 8/20/2001 6:01:47 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

/**
 * Interface for model objects with durable (long-lived) IDs.
 * The durable ID is constant accross various versions of the business object (eg. SAP assigned identifiers).
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public interface DurableModelI {
	
	/**
	 * Get the durable (long-lived) ID for the business object.
	 *
	 * @return durable ID
	 */
	public abstract String getDurableId();

}