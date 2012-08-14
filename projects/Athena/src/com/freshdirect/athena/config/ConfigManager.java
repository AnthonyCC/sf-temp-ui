package com.freshdirect.athena.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.freshdirect.athena.cache.CacheManager;
import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.concurrency.ConfigReloadService;
import com.freshdirect.athena.connection.BasePool;
import com.freshdirect.athena.exception.PoolException;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;

public class ConfigManager {

	private static ConfigManager instance = null;
	private ConfigReloadService reloadService = new ConfigReloadService();
	
	private static final Logger LOGGER = Logger.getLogger(ConfigManager.class);
	
	private Map<String, Datasource> dataSourceMapping = new ConcurrentHashMap<String, Datasource>();
	private Map<String, Api> serviceMapping = new ConcurrentHashMap<String, Api>();
	
	private Map<String, String> systemProperties = new ConcurrentHashMap<String, String>();
	
	protected ConfigManager() {		
		
	}
	
	public static ConfigManager getInstance() {
		if(instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}
	
	public void init() {
		LOGGER.debug("ConfigManager ::: init");
		
		ListenableFuture<Service.State>  future = reloadService.start();	// Start the reload Process	
		/*try {
			reloadService.loadConfigs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void adjustConfig() {
		
		AthenaConfig athenaConfig = reloadService.getAthenaConfig();
		if(athenaConfig.getProperties() != null) {
			for(Entry entry : athenaConfig.getProperties()) {
				systemProperties.put(entry.getKey(), entry.getValue());
			}
		}
		
		List<DatasourceConfig> dsConfigs = reloadService.getDsConfigs();
		if(dsConfigs != null) {
			for(DatasourceConfig dsConfig : dsConfigs) {
				List<Datasource> datasources = dsConfig.getDatasources();
				if(datasources != null) {
					for(Datasource ds : datasources) {
						dataSourceMapping.put(ds.getName(), ds);
					}
				}
			}
		}
		
		List<ApiConfig> serviceConfigs = reloadService.getServiceConfigs();
		
		if(serviceConfigs != null) {
			for(ApiConfig serviceConfig : serviceConfigs) {
				List<Api> apis = serviceConfig.getApis(); 
				if(apis != null) {
					for(Api api : apis) {
						serviceMapping.put(api.getName(), api);
					}
				}
			}
		}
		
		SystemMessageManager.getInstance().addMessage("Config Reload Occured");
		CacheManager.getInstance().adjustCache(dataSourceMapping, serviceMapping);
		//LOGGER.debug("ConfigManager ::: Reload ReEvent\n"+dataSourceMapping+"\n"+serviceMapping);		
	}
	
	public int getDefaultCacheFrequency() {
		return Integer.parseInt(systemProperties.get(ISystemProperty.DEFAULT_CACHE_FEQUENCY));
	}
	
	public int getConfigRefreshFrequency() {
		return Integer.parseInt(systemProperties.get(ISystemProperty.CONFIG_REFRESH_FREQUENCY));
	}
	
	public Map<String, Datasource> getDataSourceMapping() {
		return dataSourceMapping;
	}

	public Map<String, Api> getServiceMapping() {
		return serviceMapping;
	}
	
	public void shutdown() {
		this.reloadService.stopAndWait();
	}
	
	public static void main(String args[]) throws Exception {
		try {
			ConfigManager.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
