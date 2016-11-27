package com.freshdirect.temails;

import java.util.Collection;
import java.util.Map;

/**
 * Service interface for rules engine.
 * Will have one instance per subsystem. 
 */
public interface TEmailEngineI extends TEmailStoreI {

	/**
	 * 
	 * @param context domain-specific context
	 * @return Map of String ruleId -> Object outcome (for rules that have fired).
	 */
	public Object formatTemplates(Object context,String templateId);

	

}
