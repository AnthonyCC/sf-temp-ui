/**
 * 
 */
package com.freshdirect.routing.truckassignment.analysis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class AbstractQueryCallback implements QueryCallback {

	private int r = 0;

	protected abstract void rowImpl(ResultSet row) throws SQLException;

	protected void incRow() {
		++r;
	}

	protected void init() throws Exception {
	}

	protected AbstractQueryCallback() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public final void row(ResultSet row) throws SQLException {
		rowImpl(row);
		incRow();
	}

	public final int getCurrentRow() {
		return r;
	}

	@Override
	public void done() {

	}

	@Override
	public void meta(ResultSetMetaData metaData) throws SQLException {

	}
}