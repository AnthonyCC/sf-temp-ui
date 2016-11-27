package com.freshdirect.athena.connection;

import com.freshdirect.athena.api.DBCall;
import com.freshdirect.athena.api.ICall;
import com.freshdirect.athena.config.Datasource;
import com.freshdirect.athena.exception.PoolException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPool extends BasePool {
	
	ComboPooledDataSource pool;
	
	public DBPool(Datasource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void init()  throws PoolException {
		try {
			pool = new ComboPooledDataSource(); 
			
			pool.setDriverClass( this.getDataSource().getDriver() ); 
			//loads the jdbc driver 
			pool.setJdbcUrl( this.getDataSource().getUrl() ); 
			pool.setUser(this.getDataSource().getUser()); 
			pool.setPassword(this.getDataSource().getPassword()); 
			// the settings below are optional -- c3p0 can work with defaults 
			//pool.setMinPoolSize(5); 
			//pool.setAcquireIncrement(5); 
			pool.setMaxPoolSize(this.getDataSource().getPoolsize());
			pool.setInitialPoolSize(1);
		} catch (Exception e) {
			throw new PoolException(e);
		}
		
	}

	@Override
	public void complete()  throws PoolException {
		// TODO Auto-generated method stub
		try {
			pool.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}


	@Override
	public ICall getCall() throws PoolException {
		// TODO Auto-generated method stub
		try {
			return new DBCall(pool.getConnection());
		} catch (Exception e) {
			throw new PoolException(e);
		}
	}

}
