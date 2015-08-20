package com.freshdirect.cms.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * [FDX] various properties to control FDX / MultiStore features
 *
 * Properties are stored in freshdirect.properties file
 * 
 * @author segabor
 *
 */
public class MultiStoreProperties {

	private static final String FRESHDIRECT_PROPERTIES_FILE_NAME = "freshdirect.properties";
	private static final Logger LOGGER = LoggerFactory.getInstance(MultiStoreProperties.class);
	private static final String PROP_STORE_ID = "multistore.store.id";
	private static final String PROP_FDX_ENABLED = "multistore.enabled";
	private static final boolean FDX_DEFAULT_ENABLED = false;
	private static Properties properties;

	static {
		try {
			properties = ConfigHelper.getPropertiesFromClassLoader(FRESHDIRECT_PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			properties = new Properties();
			LOGGER.error(e);
		}
	}

	private MultiStoreProperties() {
	}

	/**
	 * ID of Store CMS node
	 * 
	 * @return
	 */
	public static String getCmsStoreId() {
		return properties.getProperty(PROP_STORE_ID, EnumEStoreId.FD.getContentId());
	}

	/**
	 * Check if property set contains CMS Store ID
	 * @return
	 */
	public static boolean hasCmsStoreID() {
		return properties.entrySet().contains(PROP_STORE_ID);
	}
	
	
	/**
	 * Is multi-store feature enabled on storefront Defaults to false
	 * 
	 * @return
	 */
	public static boolean isCmsMultiStoreEnabled() {
		return getBooleanValue(PROP_FDX_ENABLED, FDX_DEFAULT_ENABLED);
	}

	private static boolean getBooleanValue(String key, boolean defaultValue) {
		boolean result = defaultValue;
		String property = properties.getProperty(key);
		if (property != null) {
			result = Boolean.valueOf(property);
		}
		return result;
	}
}
