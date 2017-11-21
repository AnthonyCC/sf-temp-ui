package com.freshdirect.storeapi.multistore;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * [FDX] various properties to control FDX / MultiStore features
 *
 * Properties are stored in freshdirect.properties file
 *
 * @author segabor
 *
 */
@CmsLegacy
public class MultiStoreProperties {

    private static final String FRESHDIRECT_PROPERTIES_FILE_NAME = "freshdirect.properties";
    private static final Logger LOGGER = LoggerFactory.getInstance(MultiStoreProperties.class);
    private static final String PROP_STORE_ID = "multistore.store.id";

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
     * Returns the ID part of CMS Store key (if set) Defaulted to FreshDirect.
     *
     * @return
     */
    public static String getCmsStoreId() {
        return properties.getProperty(PROP_STORE_ID, EnumEStoreId.FD.getContentId());
    }

    /**
     * Check if property set contains CMS Store ID
     * 
     * @return
     */
    public static boolean hasCmsStoreID() {
        return properties.keySet().contains(PROP_STORE_ID);
    }
}
