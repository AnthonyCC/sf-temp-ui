package com.freshdirect.cms.application.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.freshdirect.cms.CmsRuntimeException;

/**
 * Supporting base class for services accessing a JDBC data source.
 * Provides a template class (DbTxCommand) for managing transactions.
 */
public class DbService {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	protected abstract class DbTxCommand {
		public final void execute() {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
				run(conn);
				conn.commit();
			} catch (SQLException e) {
				throw new CmsRuntimeException(e);
			} finally {
				try {
					if (conn != null) {
						conn.rollback();
						conn.close();
					}
				} catch (SQLException e) {
					throw new CmsRuntimeException(e);
				}
			}
		}

		protected abstract void run(Connection conn) throws SQLException;
	}

}
