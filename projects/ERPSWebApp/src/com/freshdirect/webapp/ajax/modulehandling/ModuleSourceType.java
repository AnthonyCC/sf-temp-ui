package com.freshdirect.webapp.ajax.modulehandling;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum ModuleSourceType {
    PRODUCT_CAROUSEL_MODULE("PRODUCT_CAROUSEL_MODULE"),
    PRODUCT_LIST_MODULE("PRODUCT_LIST_MODULE"),
    IMAGEGRID_MODULE("IMAGEGRID_MODULE"),
    ICON_CAROUSEL_MODULE("ICON_CAROUSEL_MODULE"),
    OPENHTML_MODULE("OPENHTML_MODULE"),
    EDITORIAL_MODULE("EDITORIAL_MODULE");

    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleSourceType.class);

    private ModuleSourceType(String moduleSourceTypeValue) {
        this.setModuleSourceTypeValue(moduleSourceTypeValue);
    }

    private String moduleSourceTypeValue;

    public String getModuleSourceTypeValue() {
        return moduleSourceTypeValue;
    }

    public void setModuleSourceTypeValue(String moduleSourceTypeValue) {
        this.moduleSourceTypeValue = moduleSourceTypeValue;
    }

    public static ModuleSourceType forValue(String moduleSourceValue) {
        for (ModuleSourceType moduleSourceType : values()) {
            if (moduleSourceType.getModuleSourceTypeValue().equals(moduleSourceValue)) {
                return moduleSourceType;
            }
        }
        LOGGER.warn(MessageFormat.format("No datasource exists with value[{0}] in the DatasourceType Enum", moduleSourceValue));
        return null;
    }
}
