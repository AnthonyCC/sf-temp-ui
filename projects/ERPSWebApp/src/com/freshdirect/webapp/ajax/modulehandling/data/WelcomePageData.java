package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WelcomePageData implements Serializable {

    private static final long serialVersionUID = -6067484315686608240L;

    private Map<String, ModuleData> data;
    private List<ModuleConfig> config;

    public Map<String, ModuleData> getData() {
        return data;
    }

    public void setData(Map<String, ModuleData> data) {
        this.data = data;
    }

    public List<ModuleConfig> getConfig() {
        return config;
    }

    public void setConfig(List<ModuleConfig> config) {
        this.config = config;
    }
}
