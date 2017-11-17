package com.freshdirect.temails;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.framework.conf.FDRegistry;

/**
 * @author knadeem Date Apr 4, 2005
 */
public class TEmailsRegistry {
	
	private static final String CONFIGURATIONS = "com.freshdirect.temails.Configurations";
	private static final String TEMAILS_PUBLISHER = "com.freshdirect.temails.Publisher"; 

	public static TEmailEngineI getTEmailsEngine(String subsystem) {
		TEmailsConfig rulesConfig = getTEmailsConfig(subsystem);
		if (rulesConfig != null) { 
			String serviceName = rulesConfig.getServiceName();
			return (TEmailEngineI) FDRegistry.getInstance().getService(serviceName, TEmailEngineI.class);
		}
		return null;
	}
	
	public static TEmailPublisherI getPublisher() {
		return (TEmailPublisherI) FDRegistry.getInstance().getService(TEMAILS_PUBLISHER, TEmailPublisherI.class);
	}

	public static TEmailsConfig getTEmailsConfig(String subsystem) {
		List configs = getTEmailsConfig();
		for (Iterator i = configs.iterator(); i.hasNext();) {
			TEmailsConfig config = (TEmailsConfig) i.next();
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
		List configs = getTEmailsConfig();
		Set s = new HashSet();

		for (Iterator i = configs.iterator(); i.hasNext();) {
			TEmailsConfig config = (TEmailsConfig) i.next();
			s.add(config.getSubsystem());
		}

		return s;
	}

	private static List getTEmailsConfig() {
		return FDRegistry.getInstance().getConfiguration(CONFIGURATIONS);
	}

}
