package com.freshdirect.temails;

import java.util.Map;

/**
 * @author 
 */
public interface TEmailPublisherI {
	
	public void publish(Map rules, String subsystem, String targetEnv);

}
