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
 * provides the basic functionality of all persistent objects
 *
 * @version $Revision$
 * @author $Author$
 */ 
abstract class PersistentBeanBase extends ModelBase implements PersistentBeanI {
    
    /** indicates whether this object has been modified since it was last
     * refreshed from a persistent store
     */    
    private boolean modified;
    
    /** default constructor
     */    
    public PersistentBeanBase() {
        super();
        this.modified = false;
    }
    
    /** load constructor
     * @param pk the identity of the object to load
     * @param conn a SQLCOnnection to use when loading from a persistent store
     * @throws SQLException any problems encountered while loading a persistent
     * object from a store
     */    
    public PersistentBeanBase(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        this.load(conn);
    }
        

    /** indicates whether a persistent object has been modified
     * since it was last refreshed from a persistent store
     * @return true if the object's state should be written to the persistent store
     */    
    public boolean isModified() {
        return this.modified;
    }
    
    /** called from within a persistent object to indicate that is should be written
     * to the persistent store
     */    
    protected void setModified() {
        this.modified = true;
    }
    
    /** called from within a persistent object to indictate that it
     * no longer needs to be written to a persistent store
     */    
    protected void unsetModified() {
        this.modified = false;
    }
    
    /** gets the next unique Id to use when writing a persistent object
     * to a persistent store for the first time
     * @param conn a SQLConnection to use to find an Id
     * @throws SQLException any problems while getting a unique id
     * @return an id that uniquely identifies a persistent object
     */    
    protected String getNextId(Connection conn, String schema) throws SQLException {
		return SequenceGenerator.getNextId(conn, schema);
    }

    /** copies a persistent object's properties to a model
     * @param model the model to copy properties to
     */    
    protected void decorateModel(ModelBase model) {
        model.setPK( this.getPK() );
    }

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
    	return "PersistentBean[" + this.getClass().getName() + ":" + this.getPK() + "]";
	}    

}
