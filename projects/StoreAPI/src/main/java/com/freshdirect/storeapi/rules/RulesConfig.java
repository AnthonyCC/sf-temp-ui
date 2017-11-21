package com.freshdirect.storeapi.rules;

import java.util.ArrayList;
import java.util.List;

public class RulesConfig {

    private String subsystem;

    private String serviceName;

    /** List of ClassDescriptors */
    private List<ClassDescriptor> conditionTypes = new ArrayList<ClassDescriptor>();

    /** List of ClassDescriptors */
    private List<ClassDescriptor> outcomeTypes = new ArrayList<ClassDescriptor>();

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 
     * @return Map of String (label) -> Class
     */
    public List<ClassDescriptor> getConditionTypes() {
        return conditionTypes;
    }

    public void setConditionTypes(List<ClassDescriptor> conditionTypes) {
        this.conditionTypes = conditionTypes;
    }

    public void addConditionType(ClassDescriptor conditionType) {
        this.conditionTypes.add(conditionType);
    }

    /**
     * @return Map of String (label) -> Class
     */
    public List<ClassDescriptor> getOutcomeTypes() {
        return outcomeTypes;
    }

    public void setOutcomeTypes(List<ClassDescriptor> outcomeTypes) {
        this.outcomeTypes = outcomeTypes;
    }

    public void addOutcomeType(ClassDescriptor outcomeType) {
        this.outcomeTypes.add(outcomeType);
    }

}
