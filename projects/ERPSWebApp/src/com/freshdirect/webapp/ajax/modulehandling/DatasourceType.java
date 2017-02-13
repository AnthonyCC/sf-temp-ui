package com.freshdirect.webapp.ajax.modulehandling;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum DatasourceType {
    GENERIC("GENERIC"),
    BROWSE("BROWSE"),
    TOP_ITEMS("TOP_ITEMS"),
    PRES_PICKS("PRES_PICKS"),
    FEATURED_RECOMMENDER("FEATURED_RECOMMENDER"),
    MOST_POPULAR_PRODUCTS("MOST_POPULAR_PRODUCTS");

    private static final Logger LOGGER = LoggerFactory.getInstance(DatasourceType.class);

    private DatasourceType(String datasourceTypeValue) {
        this.datasourceTypeValue = datasourceTypeValue;
    }

    private String datasourceTypeValue;

    public String getDatasourceTypeValue() {
        return datasourceTypeValue;
    }

    public void setDatasourceTypeValue(String datasourceTypeValue) {
        this.datasourceTypeValue = datasourceTypeValue;
    }

    public static DatasourceType forValue(String datasourceValue) {
        for (DatasourceType datasourceType : values()) {
            if (datasourceType.getDatasourceTypeValue().equals(datasourceValue)) {
                return datasourceType;
            }
        }
        LOGGER.warn(MessageFormat.format("No datasource exists with value[{0}] in the DatasourceType Enum", datasourceValue));
        return null;
    }
}
