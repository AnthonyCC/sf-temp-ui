package com.freshdirect.temails;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.rules.ClassDescriptor;

public class TEmailsConfig {

	private String subsystem;

	private String serviceName;

	/** List of ClassDescriptors */
	private List parserTypes = new ArrayList();

	
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

	public List getParserTypes() {
		return parserTypes;
	}

	public void setParserTypes(List formaterTypes) {
		this.parserTypes = parserTypes;
	}

	public void addParserType(TemplateDescriptor conditionType) {
		this.parserTypes.add(conditionType);
	}
	

}
