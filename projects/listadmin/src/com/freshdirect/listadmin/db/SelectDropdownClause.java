package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.listadmin.nvp.NVP;
import com.freshdirect.listadmin.nvp.NVPSourceI;
import com.freshdirect.listadmin.query.QueryContextI;

public class SelectDropdownClause extends NVPClause implements NVPSourceI, Serializable {
	private String column;
	private int operatorId;
	private String tableName;
	private String nameColumn;
	private String valueColumn;
	
	public SelectDropdownClause() {}
	
	public SelectDropdownClause(String column, int operatorId, String tableName, String nameColumn, String valueColumn) {
		this.column      = column;
		this.operatorId  = operatorId;
		this.tableName   = tableName;
		this.nameColumn  = nameColumn;
		this.valueColumn = valueColumn;
	}
	
	public boolean getRunnable(QueryContextI context) {
		return context.get(getClauseId()) != null;
	}
	
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getNameColumn() {
		return nameColumn;
	}

	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getValueColumn() {
		return valueColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}
	
	private List nvps = null;
	
	public Iterator iterator() {
		if(nvps == null) {
			nvps = new ArrayList();
			// Should not be hardcoded, oh my no.
			try {
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01",
						"FDSTORE_PRDA",
				        "FDSTORE_PRDA");
				
				Statement st           = c.createStatement();
				ResultSet rs           = st.executeQuery("select " + nameColumn + "," + valueColumn + " from " + tableName);
				
				while(rs.next()) {
					nvps.add(new NVP(rs.getObject(1).toString(),rs.getObject(2).toString()));
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
}
