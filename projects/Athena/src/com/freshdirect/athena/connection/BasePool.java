package com.freshdirect.athena.connection;

import com.freshdirect.athena.api.ICall;
import com.freshdirect.athena.config.Datasource;
import com.freshdirect.athena.exception.PoolException;

public abstract class BasePool {
	
	private Datasource dataSource;
	
	public BasePool(Datasource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	
	public Datasource getDataSource() {
		return dataSource;
	}
	
	public abstract ICall getCall() throws PoolException;
	
	public abstract void init() throws PoolException;
	
	public abstract void complete() throws PoolException;
}
