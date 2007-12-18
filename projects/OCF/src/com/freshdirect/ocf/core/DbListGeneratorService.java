/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.ocf.core;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.freshdirect.fdstore.FDRuntimeException;



public class DbListGeneratorService implements ListGeneratorServiceI{
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(DataSource datasource) {
		this.dataSource = datasource;
	}
	
	public OcfTableI getCustomers(Object criteria) {
		QueryFactory qf = new QueryFactory((String) criteria);
		return (OcfTableI) qf.create();
	}

	public OcfTableI getCustomers(Object dataSource, Object criteria) {
		setDataSource((DataSource) this.dataSource);
		QueryFactory qf = new QueryFactory((String) criteria);
		return (OcfTableI) qf.create();
	}

	private class QueryFactory implements Serializable {
		private final String query;

		private QueryFactory(String query) {
			this.query = query;
		}

		public Object create()  {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);

				ResultSet rs = ps.executeQuery();
				OcfTableI table = DbOcfTable.createFromResultSet(rs);
				
				rs.close();
				ps.close();

				return table;
			} catch (SQLException e) {
				throw new FDRuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}




}
