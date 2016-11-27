/*
 * Created on Feb 24, 2005
 */
package com.freshdirect.cms.application.service.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.util.DbMappingUtil;

/**
 * Implementation of {@link com.freshdirect.cms.ITable} based on JDBC
 * metadata.
 */
public class DbTable implements ITable {

	private final AttributeDefI[] columnDefinitions;
	private final List<Row> rows;

	private DbTable(AttributeDefI[] columnDefinitions, List<Row> rows) {
		this.columnDefinitions = columnDefinitions;
		this.rows = rows;
	}

	public AttributeDefI[] getColumnDefinitions() {
		return columnDefinitions;
	}

	public List<Row> getRows() {
		return rows;
	}

	/**
	 * Factory method to create a {@link DbTable} based on a JDBC result set.
	 * 
	 * @param rs JDBC result set
	 * @return the resulting {@link DbTable}
	 * @throws SQLException
	 */
	public static DbTable createFromResultSet(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();

		AttributeDefI[] columnDefinitions = DbMappingUtil.getDefinitions(meta);

		int cols = meta.getColumnCount();

		List<Row> rows = new ArrayList<Row>();
		while (rs.next()) {
			Object[] row = new Object[cols];
			for (int i = 0; i < cols; i++) {
				row[i] = ContentTypeUtil.coerce(columnDefinitions[i].getAttributeType(), rs.getObject(i + 1));
			}
			rows.add(new RowImpl(row));
		}

		return new DbTable(columnDefinitions, rows);
	}

	private static class RowImpl implements Row {
		private final Object[] values;

		public RowImpl(Object[] values) {
			this.values = values;
		}

		public Object[] getValues() {
			return values;
		}
	}

}