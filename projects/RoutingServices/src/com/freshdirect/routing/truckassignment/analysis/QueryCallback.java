/**
 * 
 */
package com.freshdirect.routing.truckassignment.analysis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface QueryCallback {
	public void meta(ResultSetMetaData metaData) throws SQLException;

	public void row(ResultSet row) throws SQLException;

	public int getCurrentRow();

	public void done();
}