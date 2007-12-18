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

import javax.ejb.CreateException;

/**
 * Abstract base class for versioned entity beans. These entites are unmutable,
 * so isModified always returns false, and store(conn) throws an UnsupportedOperationException.
 * setPK enforces that the PK is an instance of VersionedPrimaryKey.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public abstract class VersionedEntityBeanBase extends FatEntityBeanBase {
    
	/**
	 * Sets the primary key, enforcing a VersionedPrimaryKey.
	 *
	 * @param pk the primary key to set
	 *
	 * @throws ClassCastException if pk is not an instance of VersionedPrimaryKey
	 */
	protected void setPK(VersionedPrimaryKey vpk) {
		super.setPK(vpk);
	}
	
	/**
	 * Overriden isModified. Always returns false - this is a unmutable object.
	 */
	public boolean isModified() {
		return false;
	}

	/**
	 * Creates an entity from data provided by a model object and a version.
	 * Writes a new instance of this entity to the persistent store.
	 * 
	 * @param version entity version
	 * @param model the data to use to create the new entity
	 *
	 * @throws CreateException any problems during create, such as an inability to insert a row in a database table
	 *
	 * @return the primary key of the newly created entity
     */    
	public VersionedPrimaryKey ejbCreate(int version, ModelI model) throws CreateException {
        Connection conn = null;
        try {
        	initialize();
            setFromModel(model);
            conn = getConnection();
			VersionedPrimaryKey vpk = create(conn, version);
            return vpk;
        } catch (SQLException sqle) {
			throw new CreateException("SQLException in ejbCreate(ModelI)" + sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    // eat it
                }
            }
        }
    }
    
	public PrimaryKey create(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - this method should not be reachable, use create with version.");
	}

	public void store(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}

	/**
	 * Create with version. The model is already set when this is called.
	 *
	 * @param conn database connection
	 * @param version entity version number to create
	 */
	public abstract VersionedPrimaryKey create(Connection conn, int version) throws SQLException;

    
}
