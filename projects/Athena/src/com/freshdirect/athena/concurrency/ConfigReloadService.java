package com.freshdirect.athena.concurrency;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.freshdirect.athena.config.ConfigLoader;
import com.freshdirect.athena.config.ConfigManager;
import com.freshdirect.athena.exception.ConfigException;
import com.freshdirect.athena.util.AthenaProperties;
import com.google.common.util.concurrent.AbstractScheduledService;

public class ConfigReloadService  extends AbstractScheduledService {

	private static final Logger LOGGER = Logger.getLogger(ConfigReloadService.class);
	
	protected void startUp() throws ConfigException {		
		LOGGER.debug("ConfigReloadService ::: startUp");
		loadConfigs();		
	}

	protected void runOneIteration() throws ConfigException {	
		
		LOGGER.debug("ConfigReloadService ::: runOneIteration");
		loadConfigs();
	}
	
	public void loadConfigs() throws ConfigException  {
		ConfigManager.getInstance().adjustConfig(ConfigLoader.loadConfigs());
	}

	
	protected void shutDown() throws ConfigException {
		LOGGER.debug("ConfigReloadService ::: shutDown");
	}

	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(0, AthenaProperties.getConfigRefreshFrequency(), TimeUnit.MILLISECONDS);
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
