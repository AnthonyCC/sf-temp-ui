package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class ModuleContainerRequest extends Message {

    private String moduleContainerId;

    public String getModuleContainerId() {
        return moduleContainerId;
    }

    public void setModuleContainerId(String moduleContainerId) {
        this.moduleContainerId = moduleContainerId;
    }

}
