package com.freshdirect.rules;

import java.util.Map;

/**
 * @author knadeem Date Apr 13, 2005
 */
public interface RulesPublisherI {
	
	public void publish(Map rules, String subsystem, String targetEnv);

}
