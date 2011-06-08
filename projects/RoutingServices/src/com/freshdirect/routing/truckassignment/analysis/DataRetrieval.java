package com.freshdirect.routing.truckassignment.analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class DataRetrieval {

	private Connection conn = null;
	private ConnectionInfo connectionInfo;
	private String query;
	private QueryParam[] params;
	private int fetchSize = 0;

	public DataRetrieval(ConnectionInfo info) {
		this.connectionInfo = info;
	}

	protected Connection getConnection() throws SQLException {
		if (conn == null) {
			conn = connectionInfo.getNewConnection();
		}
		return conn;
	}

	public void setQuery(String query, QueryParam[] params) {
		this.query = query;
		this.params = params;
	}

	public String getQueryString() {
		return query;
	}

	public QueryParam[] getQueryParams() {
		return params;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public long executeQuery(QueryCallback callback) throws SQLException {
		getConnection();
		PreparedStatement ps = conn.prepareStatement(query);
		addParameters(ps);
		if (fetchSize > 0) {
			ps.setFetchSize(fetchSize);
		}

		long c = 0;

		try {
			ResultSet rs = ps.executeQuery();

			callback.meta(rs.getMetaData());
			try {
				while (rs.next()) {
					callback.row(rs);
					++c;
					if (fetchSize > 0 && c % fetchSize == 0)
						System.err.println("processed " + c + " records");
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			ps.close();
		}
		callback.done();

		return c;
	}

	private void addParameters(PreparedStatement ps) throws SQLException {
		if (params == null)
			return;

		for (int i = 0; i < params.length; i++) {
			int pos = i + 1;
			int type = params[i].getType();
			Object value = params[i].getValue();
			if (value == null)
				ps.setNull(pos, type);
			else
				switch (type) {
					case Types.DATE:
						ps.setDate(pos, new java.sql.Date(((Date) value).getTime()));
						break;
					case Types.DOUBLE:
						ps.setDouble(pos, (Double) value);
						break;
					case Types.FLOAT:
						ps.setDouble(pos, (Float) value);
						break;
					case Types.INTEGER:
						ps.setInt(pos, (Integer) value);
						break;
					case Types.NUMERIC:
						ps.setInt(pos, (Integer) value);
						break;
					case Types.TIME:
						ps.setDate(pos, new java.sql.Date(((Date) value).getTime()));
						break;
					case Types.TIMESTAMP:
						ps.setDate(pos, new java.sql.Date(((Date) value).getTime()));
						break;
					case Types.VARCHAR:
						ps.setString(pos, value.toString());
						break;
					default:
						throw new SQLDataException("unsupported SQL type for query parameter: " + params[i]);
				}
		}
	}
}