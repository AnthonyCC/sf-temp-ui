/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract base class for PersistentBeans.
 *
 * Application needs to implement the following methods:
 * <UL>
 * <LI>load()
 * <LI>store()
 * <LI>create()
 * <LI>remove()
 * <LI>getModel(), that can call decorateModel()
 * <LI>decorateModel, that can call super.decorateModel()
 * <LI>setFromModel()
 * </UL>
 *
 * @version     $Revision$
 * @author      $Author$
 */ 
public abstract class PersistentBeanSupport extends PersistentBeanBase {

    /** default constructor
     */    
	public PersistentBeanSupport() {
		super();
	}
	
    /** load constructor
     * @param id the identity of the object to read from the
     * persistent store
     * @param conn a SQLConnection to use to read the object from the
     * persistent store
     * @throws SQLException any problems encountered while reading the object's
     * properties from the persistent store
     */    
	public PersistentBeanSupport(PrimaryKey id, Connection conn) throws SQLException {
		super(id, conn);
	}

    /** ModelConsumer, without RemoteException
     * @param model the model to set the object's properties from
     */    
	public abstract void setFromModel(ModelI model);

}