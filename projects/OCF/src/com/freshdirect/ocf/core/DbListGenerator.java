/**
 * @author ekracoff
 * Created on Apr 21, 2005*/

package com.freshdirect.ocf.core;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.listadmin.db.StoredQuery;


public class DbListGenerator extends Entity implements ListGeneratorI {
	private String name;
	private String description;
	private String query;
	private String dsKey;
	private StoredQuery storedQuery;
	
	public DbListGenerator(){};

	public DbListGenerator(String query) {
		this.query = query;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	

	public StoredQuery getStoredQuery() {
		return storedQuery;
	}

	public void setStoredQuery(StoredQuery storedQuery) {
		this.storedQuery = storedQuery;
	}
	
	public OcfTableI getList() {
		ListGeneratorServiceI cust = (ListGeneratorServiceI) FDRegistry.getInstance().getService(ListGeneratorServiceI.class);
		
		if(storedQuery == null) {
			return cust.getCustomers(query);
		}
		
		
		Object source = null;
		
		try {
			source = storedQuery.getDataSource();
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
		
		return cust.getCustomers(source,storedQuery.getSql());
	}
}
