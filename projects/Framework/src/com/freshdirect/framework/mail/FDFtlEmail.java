package com.freshdirect.framework.mail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FDFtlEmail extends EmailSupport implements FTLEmailI {
	
	private Map parameters = new HashMap();
	

	public Map getParameters() {
		return this.parameters;
	}
	
	public void decorateMap(){}

}
