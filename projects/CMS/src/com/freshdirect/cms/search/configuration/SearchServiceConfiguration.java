package com.freshdirect.cms.search.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SearchServiceConfiguration {

	private static final Logger LOGGER = LoggerFactory.getInstance(SearchServiceConfiguration.class);
	private static final String CMS_INDEX_PATH_NAME = "cms.index.path";
	
	private static class SearchServiceConfigurationHolder {
		public static final SearchServiceConfiguration instance = new SearchServiceConfiguration();
	}
	
	private String cmsIndexLocation;

	public static SearchServiceConfiguration getInstance() {
		return SearchServiceConfigurationHolder.instance;
	}

	private SearchServiceConfiguration() {
		try {
			Properties freshdirectProperties = ConfigHelper.getPropertiesFromClassLoader("freshdirect.properties");
			cmsIndexLocation = freshdirectProperties.getProperty(CMS_INDEX_PATH_NAME);
		} catch (IOException e) {
			LOGGER.error("Failed to build SearchService configuration from property. Probably missing cms.index.path");
			throw new CmsRuntimeException(e);
		}
	}


	public String getCmsIndexLocation() {
		return cmsIndexLocation;
	}
}
