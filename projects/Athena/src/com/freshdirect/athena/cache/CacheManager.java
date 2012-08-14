package com.freshdirect.athena.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.ConfigManager;
import com.freshdirect.athena.config.Datasource;
import com.freshdirect.athena.config.Parameter;
import com.freshdirect.athena.connection.BasePool;
import com.freshdirect.athena.connection.ConnectionType;
import com.freshdirect.athena.connection.DBPool;
import com.freshdirect.athena.connection.JCOPool;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.exception.PoolException;


public class CacheManager {
	
	private static final Logger LOGGER = Logger.getLogger(CacheManager.class);
	
	private static CacheManager instance = null;
	
	private Map<String, BasePool> dsCache = new ConcurrentHashMap<String, BasePool>();
	
	private Map<String, Api> apiConfigCache = null;
	
	private Map<String, ApiCache> apiCache = new ConcurrentHashMap<String, ApiCache>();
	
	protected CacheManager() {		
		
	}
	
	public static CacheManager getInstance() {
		if(instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}
	
	public void init() {
		LOGGER.debug("CacheManager ::: init");
	}
		
	public void adjustCache(Map<String, Datasource> dataSourceMapping, Map<String, Api> serviceMapping) {
		initDBPool(dataSourceMapping);	
		initApiConfig(serviceMapping);
	}
	
	public void initApiConfig(Map<String, Api> serviceMapping) {
		if(serviceMapping != null) {
			
			for(Map.Entry<String, Api> apiEntry : serviceMapping.entrySet()) {
				if(apiCache.containsKey(apiEntry.getKey())) {
					if(!apiEntry.getValue().equals(apiCache.get(apiEntry.getKey()).getApi())) {
						apiCache.get(apiEntry.getKey()).getCache().invalidateAll();
						apiCache.remove(apiEntry.getKey());
						SystemMessageManager.getInstance().addMessage("Api Reloaded:"+apiEntry.getKey());
					}
				}
				
				if(!apiCache.containsKey(apiEntry.getKey())) {
					apiCache.put(apiEntry.getKey(), new ApiCache(apiEntry.getValue()));
				}
			}
		}
		apiConfigCache = serviceMapping;
	}
	
	public void initDBPool(Map<String, Datasource> dataSourceMapping) {
		
		if(dataSourceMapping != null) {
			
			for(Map.Entry<String, Datasource> dbEntry : dataSourceMapping.entrySet()) {
				if(dsCache.containsKey(dbEntry.getKey())) {
					if(!dbEntry.getValue().equals(dsCache.get(dbEntry.getKey()).getDataSource())) {
						try {
							dsCache.get(dbEntry.getKey()).complete();
						} catch (PoolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dsCache.remove(dbEntry.getKey());
						SystemMessageManager.getInstance().addMessage("Datasource Reloaded:"+dbEntry.getKey());
					}
				}
				
				//Clean Done now please reinit
				if(!dsCache.containsKey(dbEntry.getKey())) {
					if(ConnectionType.DB.equals(dbEntry.getValue().getConnectionType())) {
						DBPool pool = new DBPool(dbEntry.getValue());
						try {
							pool.init();
							dsCache.put(dbEntry.getKey() , pool);
						} catch (PoolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(ConnectionType.JCO.equals(dbEntry.getValue().getConnectionType())) {
						JCOPool pool = new JCOPool(dbEntry.getValue());
						try {
							pool.init();
							dsCache.put(dbEntry.getKey() , pool);
						} catch (PoolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}
	
	public BasePool getPool(String dsName) {
		return dsCache.get(dsName);
	}
	
	public Map<String, Api> getApiConfigCache() {
		return apiConfigCache;
	}
	
	public boolean isSupportedApi(String apiName) {
		return apiConfigCache != null ? apiConfigCache.containsKey(apiName) : false;
	}
	
	public Data getData(String apiName, Map<String, String> params) {
		
		ApiCache cache = apiCache.get(apiName);
		if(cache != null) {
			Api api = cache.getApi();
			//ApiKey apiKeyForCache = new ApiKey();
			List<Parameter> actualParameters = new ArrayList<Parameter>();
			for(Parameter param : api.getParameters()) {
				try {
					Parameter actualParam = (Parameter)param.clone();
					actualParameters.add(actualParam);
					if(params.containsKey(actualParam.getName())) {
						actualParam.setValue(params.get(actualParam.getName()));
					}
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ApiKey apiKey = new ApiKey(api.getName(), actualParameters);
			return cache.getData(apiKey);
		}
		return new Data();
	}
	
	public void shutdown() {
		for(Map.Entry<String, BasePool> dbPoolEntry : dsCache.entrySet()) {
			try {
				dbPoolEntry.getValue().complete();
			} catch (PoolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dsCache.remove(dbPoolEntry.getKey());
		}
	}
}
