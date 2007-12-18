package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.nvp.NVP;
import com.freshdirect.listadmin.nvp.NVPSourceI;
import com.freshdirect.listadmin.query.QueryContextI;

public class QueryDropdownClause extends NVPClause implements NVPSourceI, Serializable {
	private StoredQuery query;
	
	public QueryDropdownClause() {}
	
	public QueryDropdownClause(String column, int operatorId) {
		super(column, operatorId);
	}
	
	public boolean getRunnable(QueryContextI context) {
		return context.get(getClauseId()) != null;
	}
	
	private List nvps = null;
	
	public Iterator iterator() {
		if(nvps == null) {
			nvps = new ArrayList();
			
			try {
				// Here's where we eat our own dogfood.  Yum!
				String sql    = query.getSql(); 
				Connection c  = JndiWrapper.getConnection(query.getDataSourceName());
				Statement st  = c.createStatement();
				ResultSet rs  = st.executeQuery(sql);

				while(rs.next()) {
					nvps.add(new NVP(rs.getObject(2).toString(),rs.getObject(1).toString()));
				}
				
				rs.close();
				st.close();
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return nvps.iterator();
	}

	public StoredQuery getQuery() {
		return query;
	}

	public void setQuery(StoredQuery query) {
		this.query = query;
	}
	
	public String toString() {
		return super.toString() + "values from query " + query.getName();
	}
	
	public String getXml() {
		return "<queryDropdown field='" + getColumn() + "' " +
		       "operatorId='" + getOperatorId()  + "' " +
		       "query='" + getQuery().getName() + 
		       "'/>";
	}
}
