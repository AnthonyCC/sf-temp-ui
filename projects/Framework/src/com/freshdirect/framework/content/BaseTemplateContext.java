package com.freshdirect.framework.content;

import java.util.Collections;
import java.util.Map;


/**
 * 
 * @author ksriram
 *
 */
public class BaseTemplateContext {
	
	private final Map parameters;
	
	/**
	 * Create a template context with no rendering parameters.
	 */
	public BaseTemplateContext() {
		this(Collections.EMPTY_MAP);
	}
	
	/**
	 * Create a template context with additional rendering parameters.
	 * 
	 * @param parameters map of optional rendering parameters (never null)
	 */
	public BaseTemplateContext(Map parameters) {
		this.parameters = Collections.unmodifiableMap(parameters);
	}
	

	
	/**
	 * Get optional rendering parameters.
	 * 
	 * @return parameter map (never null)
	 */
	public Map getParameters() {
		return this.parameters;
	}

}
