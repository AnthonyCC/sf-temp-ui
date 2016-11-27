/*
 * $Workfile: DependentPersistentBeanI.java$
 *
 * $Date: 8/1/2001 11:20:03 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

/**
 * Persistent Bean Interface with reference to a Parent PK
 *
 * @version     $Revision: 2$
 * @author      $Author: Viktor Szathmary$
 *
 */
public interface DependentPersistentBeanI extends PersistentBeanI {

    /** sets the primary key of the parent object
     * @param id the primary key of the parent object
     */    
	public void setParentPK(PrimaryKey id);

    /** gets the primary key of the parent object
     * @return the primary key of the parent object
     */    
	public PrimaryKey getParentPK();

}

