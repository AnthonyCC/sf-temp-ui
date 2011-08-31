package com.freshdirect.webapp.util;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author greg
 *
 * Smart Search AutoComplete facade class
 */
public class AutoCompleteFacade implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	private final static Category LOGGER = LoggerFactory.getInstance(AutoCompleteFacade.class);

	public AutoCompleteFacade() {
	}
		
	public List<String> getTerms(String prefix) {
	    LOGGER.info("autocomplete for "+prefix);
	    return ContentSearch.getInstance().getProductAutocompletions(prefix, 20);
	}

	public static AutoCompleteFacade create() {		
		return new AutoCompleteFacade();
	}
	
}
