package com.freshdirect.smartstore.impl;

import java.util.Map;

public interface IConfigurable {
	/**
	 * Method to append configuration entries the implementor may have
	 * 
	 * @param configMap
	 * @return Should return the same given map
	 */
	public Map appendConfiguration(Map configMap);
}
