package com.freshdirect.rules;

import java.util.Iterator;
import java.util.List;

/**
 * @author knadeem Date Apr 11, 2005
 */
public abstract class AbstractRuleStore implements RulesStoreI {
	
	protected RulesConfig findConfig(List configurations, String subsystem) {
		for (Iterator i = configurations.iterator(); i.hasNext();) {
			RulesConfig conf = (RulesConfig) i.next();
			if (subsystem.equals(conf.getSubsystem())) {
				return conf;
			}
		}
		throw new IllegalArgumentException("No configuration for " + subsystem);
	}

}
