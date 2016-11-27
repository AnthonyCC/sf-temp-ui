package com.freshdirect.temails;

import java.util.Iterator;
import java.util.List;

/**
 * @author knadeem Date Apr 11, 2005
 */
public abstract class AbstractTEmailStore implements TEmailStoreI {
	
	protected TEmailsConfig findConfig(List configurations, String subsystem) {
		for (Iterator i = configurations.iterator(); i.hasNext();) {
			TEmailsConfig conf = (TEmailsConfig) i.next();
			if (subsystem.equals(conf.getSubsystem())) {
				return conf;
			}
		}
		throw new IllegalArgumentException("Template No configuration for " + subsystem);
	}

}
