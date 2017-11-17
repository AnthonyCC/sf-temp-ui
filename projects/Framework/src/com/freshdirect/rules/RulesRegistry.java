package com.freshdirect.rules;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.framework.conf.FDRegistry;

/**
 * @author knadeem Date Apr 4, 2005
 */
public class RulesRegistry {
	
	private static final String CONFIGURATIONS = "com.freshdirect.rules.Configurations";
	private static final String RULES_PUBLISHER = "com.freshdirect.rules.Publisher"; 

	public static RulesEngineI getRulesEngine(String subsystem) {
		RulesConfig rulesConfig = getRulesConfig(subsystem);
		if (rulesConfig != null) { 
			String serviceName = rulesConfig.getServiceName();
			return (RulesEngineI) FDRegistry.getInstance().getService(serviceName, RulesEngineI.class);
		}
		return null;
	}
	
	public static RulesPublisherI getPublisher() {
		return (RulesPublisherI) FDRegistry.getInstance().getService(RULES_PUBLISHER, RulesPublisherI.class);
	}

	public static RulesConfig getRulesConfig(String subsystem) {
		List configs = getRulesConfig();
		for (Iterator i = configs.iterator(); i.hasNext();) {
			RulesConfig config = (RulesConfig) i.next();
			if (config.getSubsystem().equals(subsystem)) {
				return config;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return Set of String
	 */
	public static Set getSubsystems() {
		List configs = getRulesConfig();
		Set s = new HashSet();

		for (Iterator i = configs.iterator(); i.hasNext();) {
			RulesConfig config = (RulesConfig) i.next();
			s.add(config.getSubsystem());
		}

		return s;
	}

	private static List getRulesConfig() {
		return FDRegistry.getInstance().getConfiguration(CONFIGURATIONS);
	}

}
