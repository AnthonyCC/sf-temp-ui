package com.freshdirect.framework.mail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class FDFtlEmail extends EmailSupport implements FTLEmailI {
	
	private static final long	serialVersionUID	= 1742578804817723232L;
	
	private Map<String,Serializable> parameters = new HashMap<String,Serializable>();	

	public Map<String,Serializable> getParameters() {
		return this.parameters;
	}
	
	public void decorateMap() {}

}
