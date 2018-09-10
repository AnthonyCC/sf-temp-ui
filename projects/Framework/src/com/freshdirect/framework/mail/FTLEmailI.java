package com.freshdirect.framework.mail;

import java.io.Serializable;
import java.util.Map;

public interface FTLEmailI extends EmailI {
	
	public Map<String,Serializable> getParameters();
	
}
