package com.freshdirect.athena.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.config.ApiConfig;
import com.freshdirect.athena.config.AthenaConfig;
import com.freshdirect.athena.config.ConfigManager;
import com.freshdirect.athena.config.DatasourceConfig;
import com.freshdirect.athena.exception.ConfigException;
import com.freshdirect.athena.exception.ConfigExceptionType;
import com.freshdirect.athena.util.AthenaProperties;
import com.google.common.util.concurrent.AbstractScheduledService;

public class ConfigReloadService  extends AbstractScheduledService {

	private static final Logger LOGGER = Logger.getLogger(ConfigReloadService.class);
	
	List<DatasourceConfig> dsConfigs = null;
	
	List<ApiConfig> serviceConfigs = null;
	
	AthenaConfig athenaConfig = null;

	protected void startUp() throws ConfigException {		
		LOGGER.debug("ConfigReloadService ::: startUp");
		loadConfigs();		
	}

	protected void runOneIteration() throws ConfigException {	
		
		LOGGER.debug("ConfigReloadService ::: runOneIteration");
		loadConfigs();
	}
	
	public void loadConfigs() throws ConfigException {
		
		LOGGER.debug("ConfigReloadService ::: loadConfigs");
		Serializer serializer = new Persister();
		try {
			athenaConfig = serializer.read(AthenaConfig.class, ClassLoader.getSystemResourceAsStream("athena-config.xml"));
			
			dsConfigs = new ArrayList<DatasourceConfig>();
			serviceConfigs = new ArrayList<ApiConfig>();
			
			if(athenaConfig != null) {
				List<String> datasourceFiles = athenaConfig.getDatasources();
				if(datasourceFiles != null) {
					for(String file : datasourceFiles) {
						try {
							DatasourceConfig dbConfig = serializer.read(DatasourceConfig.class
																, ClassLoader.getSystemResourceAsStream(file));
						
							dsConfigs.add(dbConfig);
						} catch (Exception e) {
							SystemMessageManager.getInstance().addMessage("Datasource Config Load Failed:"+file);
							e.printStackTrace();
						}
					}
				}
				
				List<String> serviceFiles = athenaConfig.getServices();
				if(serviceFiles != null) {
					for(String file : serviceFiles) {
						try {
							ApiConfig apiConfig = serializer.read(ApiConfig.class
																, ClassLoader.getSystemResourceAsStream(file));
							
							serviceConfigs.add(apiConfig);
						} catch (Exception e) {
							SystemMessageManager.getInstance().addMessage("API Config Load Failed:"+file);
							e.printStackTrace();
						} 
					}
				}
			}
			LOGGER.debug("ConfigReloadService ::: adjustConfig");
			ConfigManager.getInstance().adjustConfig();
		} catch (Exception e) {
			throw new ConfigException(e, ConfigExceptionType.LOAD_FAILED);
		}
	}

	protected void shutDown() throws ConfigException {
		LOGGER.debug("ConfigReloadService ::: shutDown");
	}

	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(0, AthenaProperties.getConfigRefreshFrequency(), TimeUnit.MILLISECONDS);
	}

	public List<DatasourceConfig> getDsConfigs() {
		return dsConfigs;
	}

	public List<ApiConfig> getServiceConfigs() {
		return serviceConfigs;
	}
	
	public AthenaConfig getAthenaConfig() {
		return athenaConfig;
	}

	public static void main(String args[]) throws ConfigException {
		try {
			ConfigReloadService x = new ConfigReloadService();
			x.runOneIteration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
