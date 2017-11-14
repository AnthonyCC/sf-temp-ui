package com.freshdirect.temails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.properties.FDStorePropertyResolver;

/**
 * @author knadeem Date Apr 4, 2005
 */
public class TEmailsRegistry {

	public static TEmailEngineI getTEmailsEngine(String subsystem) {
        TEmailConfiguration rulesConfig = getTEmailsConfig(subsystem);
		if (rulesConfig != null) { 
            TEmailEngineImpl engine = new TEmailEngineImpl(
                    new TEmailStoreCache(new XMLTEmailStore(rulesConfig.xmlFile(), rulesConfig.getConfig().getSubsystem(), rulesConfig.getConfig())));
            return engine;
		}
		return null;
	}
	
	public static TEmailPublisherI getPublisher() {
        String basePath = null;
        String script = null;
        try {
            basePath = FDStorePropertyResolver.getPropertyValue("com.freshdirect.temails.publish.dir");
            script = FDStorePropertyResolver.getPropertyValue("com.freshdirect.temails.publish.script");
        } catch (IOException e) {
        }

        return new TEmailsPublisher(basePath, script);
	}

    public static TEmailConfiguration getTEmailsConfig(String subsystem) {
        List<TEmailConfiguration> configs = getTEmailsConfig();
        for (TEmailConfiguration config : configs) {
            if (config.getConfig().getSubsystem().equals(subsystem)) {
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

    private static List<TEmailConfiguration> getTEmailsConfig() {
        List<TEmailConfiguration> configs = new ArrayList<TEmailConfiguration>();
        TEmailConfiguration configuration = new TEmailConfiguration();
        configs.add(configuration);
        return configs;
	}

}
