package com.freshdirect.athena.connection;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class ConnectionManager {
	
	private static ConnectionManager instance = null;
	
	private ConcurrentHashMap<String, BasePool> poolMapping = null;
	
	private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class);
	
	protected ConnectionManager() {		
		poolMapping = new ConcurrentHashMap<String, BasePool>();
	}
	
	public static ConnectionManager getInstance() {
		if(instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

}
