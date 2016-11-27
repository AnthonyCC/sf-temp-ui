package com.freshdirect.athena.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.exception.ConfigException;
import com.freshdirect.athena.exception.ConfigExceptionType;

public class ConfigLoader {

	private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class);

	public static ConfigGroup loadConfigs() throws ConfigException {
		
		LOGGER.debug("ConfigReloadService ::: loadConfigs");
		Serializer serializer = new Persister();
		try {
			AthenaConfig athenaConfig = serializer.read(AthenaConfig.class, ClassLoader.getSystemResourceAsStream("athena-config.xml"));

			List<DatasourceConfig> dsConfigs = new ArrayList<DatasourceConfig>();
			List<ApiConfig> serviceConfigs = new ArrayList<ApiConfig>();

			if(athenaConfig != null) {
				List<String> datasourceFiles = athenaConfig.getDatasources();
				if(datasourceFiles != null) {
					for(String file : datasourceFiles) {
						try {
							DatasourceConfig dbConfig = serializer.read(DatasourceConfig.class
									, ClassLoader.getSystemResourceAsStream(file));

							dsConfigs.add(dbConfig);
						} catch (Exception e) {
							LOGGER.debug("ConfigLoader ::: FailedToLoad>"+file);
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
							LOGGER.debug("ConfigLoader ::: FailedToLoad>"+file);
							SystemMessageManager.getInstance().addMessage("API Config Load Failed:"+file);
							e.printStackTrace();
						} 
					}
				}
			}
			ConfigGroup result = new ConfigGroup(athenaConfig, dsConfigs, serviceConfigs); 
			//LOGGER.debug("ConfigLoader ::: result"+result);
			return result;
		} catch (Exception e) {
			throw new ConfigException(e, ConfigExceptionType.LOAD_FAILED);
		}
	}
}
