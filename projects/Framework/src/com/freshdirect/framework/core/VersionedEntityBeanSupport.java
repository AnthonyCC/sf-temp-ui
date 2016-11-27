/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.core;

import java.sql.*;
import javax.ejb.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 * Abstract base class for versioned entity beans. These entites are immutable,
 * so isModified always returns false, and store(conn) throws an UnsupportedOperationException.
 * setPK enforces that the PK is an instance of VersionedPrimaryKey.
 *
 * @version    $Revision$
 * @author     $Author$*/
public abstract class VersionedEntityBeanSupport extends VersionedEntityBeanBase {
    
    private static Category LOGGER = LoggerFactory.getInstance( VersionedEntityBeanSupport.class );

    public VersionedEntityBeanSupport() {
        super();
    }

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
	 * Factory method for creating primary keys.
	 *
	 * @param pk original pk
	 * @param payload payload object
	 *
	 * @return a subclass of PrimaryKey, based on the required information in pk and the payload
	 */	
	public VersionedPrimaryKey constructPrimaryKey(VersionedPrimaryKey vpk, PayloadI payload) {
		return new VersionedPrimaryKey(vpk.getId(), vpk.getVersion(), payload);
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

	/**
	 * Create with version. The model is already set when this is called.
	 *
	 * @param conn database connection
	 * @param version entity version number to create
	 */
	public abstract VersionedPrimaryKey create(Connection conn, int version) throws SQLException;

    public VersionedPrimaryKey ejbFindByPrimaryKey(VersionedPrimaryKey vpk) throws ObjectNotFoundException, FinderException {
        Connection conn = null;
        try {
        	conn = this.getConnection();
            // found, create payload for the fat key
            PayloadI payload = this.loadRowPayload( conn, vpk );
            if (payload==null) {
                throw new ObjectNotFoundException("Unable to find PK " + vpk);
            }
            // return key
            return constructPrimaryKey(vpk, payload);
        } catch (SQLException sqle) {
            LOGGER.warn("An error occured in ejbFindByPrimaryKey", sqle);
            throw new FinderException(sqle.getMessage());
        } finally {
            try {
                if (conn!=null) {
                    conn.close();
                }
            } catch (SQLException sqle2) {
                // eat it
            }
        }
    }

    /**
     * Perform any additional tasks after this object's data has been created in the persistent store.
     *
     * @param version the batch number of this new version of this entity
     * @param model the model that was used in the corresponding create method
     */
    public void ejbPostCreate(int version, ModelI model) {
        this.unsetModified();
    }    
    
}
