package com.freshdirect.properties;

import java.io.IOException;
import java.util.Properties;

import com.freshdirect.framework.util.ConfigHelper;

public class FDStorePropertyResolver {

    public static String getPropertyValue(String propertyName) throws IOException {
        Properties properties = ConfigHelper.getPropertiesFromClassLoader("fdstore.properties");
        String propertyValue = null;
        if (properties.contains(propertyName)) {
            propertyValue = properties.getProperty(propertyName);
        }
        return propertyValue;
    }

}
