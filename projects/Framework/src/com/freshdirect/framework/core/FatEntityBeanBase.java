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
 *
 * @version    $Revision$
 * @author     $Author$*/
public abstract class FatEntityBeanBase extends EntityBeanBase {
    
	/**
	 * Optimized ejbLoad, using fat PK.
	 * If payload is set, calls setFromPayload(payload), otherwise calls load(connection).
	 */
	public void ejbLoad() {
		FatPrimaryKey fatpk = (FatPrimaryKey)this.getEntityContext().getPrimaryKey();
		PayloadI payload = fatpk.getPayload();
		if (payload==null) {
			super.ejbLoad();
		} else {
			this.setPK( fatpk );
			this.setFromPayload( payload );
		}
	}

	public void load(Connection conn) throws SQLException {
		PayloadI payload = this.loadRowPayload(conn, this.getPK());
		if (payload==null) {
			throw new SQLException("Row not found, pk="+this.getPK());
		}
		this.setFromPayload( payload );
	}

	/**
	 * Set from payload.
	 *
	 * @param payload payload object
	 */
	public abstract void setFromPayload(PayloadI payload);

	/**
	 * Load database row with specified PK into a payload object.
	 *
	 * @param conn database connection
	 * @param pk primary key
	 *
	 * @return payload object, null if row was not found
	 *
	 * @throws SQLException if a database error occured
	 */
	public abstract PayloadI loadRowPayload(Connection conn, PrimaryKey pk) throws SQLException;


}
