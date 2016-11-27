package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.SQLException;

import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.PrimaryKey;


public abstract class ErpPersistentBeanSupport extends DependentPersistentBeanSupport {

	public ErpPersistentBeanSupport() {
		super();
	}

	public ErpPersistentBeanSupport(PrimaryKey pk) {
		super(pk);
	}

	/**
	 * Overriden isModified. Always returns false - this is a unmutable object.
	 */
	public final boolean isModified() {
		return false;
	}

	public final void load(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - load() should not be called due to optimized finder");
	}

	public final void store(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}

	public final void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}
	
}
