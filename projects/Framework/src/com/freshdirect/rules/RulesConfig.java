package com.freshdirect.rules;

import java.util.ArrayList;
import java.util.List;

public class RulesConfig {

	private String subsystem;

	private String serviceName;

	/** List of ClassDescriptors */
	private List conditionTypes = new ArrayList();

	/** List of ClassDescriptors */
	private List outcomeTypes = new ArrayList();;

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
	public List getConditionTypes() {
		return conditionTypes;
	}

	public void setConditionTypes(List conditionTypes) {
		this.conditionTypes = conditionTypes;
	}

	public void addConditionType(ClassDescriptor conditionType) {
		this.conditionTypes.add(conditionType);
	}

	/**
	 * @return Map of String (label) -> Class
	 */
	public List getOutcomeTypes() {
		return outcomeTypes;
	}

	public void setOutcomeTypes(List outcomeTypes) {
		this.outcomeTypes = outcomeTypes;
	}

	public void addOutcomeType(ClassDescriptor outcomeType) {
		this.outcomeTypes.add(outcomeType);
	}

}
