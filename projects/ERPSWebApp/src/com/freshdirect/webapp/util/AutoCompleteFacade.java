package com.freshdirect.webapp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.cms.application.CmsManager;
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

	private List terms; 
	
	public AutoCompleteFacade() {
		terms = new ArrayList();
		terms.add("milk");
		terms.add("banana");
		terms.add("apple");
		terms.add("pie");
		terms.add("foo");
		terms.add("bar");
		
		Collections.sort(terms);
	}
		
	public List getTerms(String prefix) {
	    LOGGER.info("autocomplete for "+prefix);
	    return ContentSearch.getInstance().getAutocompletions(prefix);
	}

	public static AutoCompleteFacade create() {		
		return new AutoCompleteFacade();
	}
	
}
