package com.freshdirect.framework.mail;

import java.util.HashMap;
import java.util.Map;


public class FDFtlEmail extends EmailSupport implements FTLEmailI {
	
	private static final long	serialVersionUID	= 1742578804817723232L;
	
	private Map<String,Object> parameters = new HashMap<String,Object>();	

	public Map<String,Object> getParameters() {
		return this.parameters;
	}
	
	public void decorateMap() {}

}
