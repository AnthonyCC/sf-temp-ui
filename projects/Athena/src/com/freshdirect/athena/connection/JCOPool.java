package com.freshdirect.athena.connection;

import java.util.Properties;

import com.freshdirect.athena.api.ICall;
import com.freshdirect.athena.api.JCOCall;
import com.freshdirect.athena.config.Datasource;
import com.freshdirect.athena.exception.PoolException;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;

public class JCOPool extends BasePool {

	IRepository repository = null;
	
	public JCOPool(Datasource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void init() throws PoolException {
		// TODO Auto-generated method stub
		Properties defaults = new Properties();
		defaults.put("sap.jco.poolsize", "20");
		defaults.put("sap.jco.dump", "critical");
		
		defaults.put("jco.client.trace", "false");
		defaults.put("jco.client.client", this.getDataSource().getClient());
		
		defaults.put("jco.client.user", this.getDataSource().getUser());
		defaults.put("jco.client.passwd", this.getDataSource().getPassword());
		
		defaults.put("jco.client.sysnr", this.getDataSource().getSystemno() );
		defaults.put("jco.client.ashost", this.getDataSource().getUrl());
		
		JCO.addClientPool(this.getDataSource().getName(), this.getDataSource().getPoolsize(), defaults);
		repository = JCO.createRepository("FDRepository", this.getDataSource().getName());
	}

	@Override
	public void complete() throws PoolException {
		// TODO Auto-generated method stub
		JCO.removeClientPool(this.getDataSource().getName());
	}


	@Override
	public ICall getCall() throws PoolException {
		// TODO Auto-generated method stub
		return new JCOCall(repository);
	}

}
