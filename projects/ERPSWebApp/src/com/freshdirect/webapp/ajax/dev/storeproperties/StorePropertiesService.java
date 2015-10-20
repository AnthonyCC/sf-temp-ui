package com.freshdirect.webapp.ajax.dev.storeproperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.freshdirect.fdstore.FDStoreProperties;

public class StorePropertiesService {

    private static final StorePropertiesService INSTANCE = new StorePropertiesService();

    private StorePropertiesService() {
    }

    public static StorePropertiesService defaultService() {
        return INSTANCE;
    }

    public StorePropertiesData loadStoreProperties() {
        Properties properties = FDStoreProperties.getConfig();
        StorePropertiesData propertiesData = decorateCurrentStoreProperties(properties);
        return propertiesData;
    }

    private StorePropertiesData decorateCurrentStoreProperties(Properties properties) {
        StorePropertiesData propertiesData = new StorePropertiesData();
        List<StoreProperty> propertyList = new ArrayList<StoreProperty>();

        for (String key : properties.stringPropertyNames()) {
            StoreProperty prop = new StoreProperty();
            prop.setName(key);
            prop.setValue(properties.getProperty(key));

            propertyList.add(prop);
        }

        propertiesData.setStoreProperties(propertyList);
        return propertiesData;
    }

    public void refreshStoreProperties() {
        FDStoreProperties.refresh(true);
    }
}
