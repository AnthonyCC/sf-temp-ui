/*
 * $Workfile: DependentPersistentBeanSupport.java$
 *
 * $Date: 8/13/2001 5:24:03 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract base class for PersistentBeans with reference to a Parent PK.
 *
 * Application needs to implement the following methods:
 * <UL>
 * <LI>load()
 * <LI>store()
 * <LI>create()
 * <LI>remove()
 * <LI>getModel(), that can call decorateModel()
 * <LI>decorateModel(), that can call super.decorateModel()
 * <LI>setFromModel()
 * </UL>
 *
 * @version     $Revision: 3$
 * @author      $Author: Viktor Szathmary$
 */
public abstract class DependentPersistentBeanSupport extends PersistentBeanSupport implements DependentPersistentBeanI {

    /** the primary key of the parent object
     */    
	private PrimaryKey parentPK;

    /** default constructor
     */    
	public DependentPersistentBeanSupport() {
		super();
	}
	
	/** Load constructor
     * @param pk the primary key of the object to load
     * @param conn a SQLConnection to use for loading
     * @throws SQLException any problems encountered during loading
     */
	public DependentPersistentBeanSupport(PrimaryKey pk, Connection conn) throws SQLException {
		super(pk, conn);
	}
	
    /**
     * Default constructor that just sets the primary key.
     * @param pk the object's primary key
     */    
	public DependentPersistentBeanSupport(PrimaryKey pk) {
		super();
		this.setPK(pk);
	}
	
    /** sets the primary key of the parent object
     * @param pk the primary key of the parent object
     */    
	public void setParentPK(PrimaryKey pk) {
		this.parentPK = pk;
	}
	
    /** gets the primary ket of the parent object
     * @return the primary key of the parent object
     */    
	public PrimaryKey getParentPK() {
		return this.parentPK;
	}


}