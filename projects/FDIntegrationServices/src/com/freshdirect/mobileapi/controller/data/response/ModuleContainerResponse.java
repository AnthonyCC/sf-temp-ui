package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;
import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;

public class ModuleContainerResponse extends Message {

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
