/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.webapp;

import java.util.*;

import org.apache.log4j.Category;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ActionResult {
	private static final Category LOGGER = Category.getInstance(ActionResult.class);

	private HashMap errors = null;
	private HashMap warnings = null;

	public ActionResult() {
		errors = new HashMap();
		warnings = new HashMap();
	}

	public boolean isSuccess() {
		boolean success = (0 == errors.size());
		LOGGER.debug("isSuccess: " + success);
		return success;
	}

	public boolean isFailure() {
		boolean failure = (0 < errors.size());
		LOGGER.debug("isFailure: " + failure);
		return failure;
	}

	public void addError(ActionError error) {
		LOGGER.debug("error added: " + error.getType() +", " + error.getDescription());
		errors.put(error.getType(), error);
	}

	/**
	 * Convenience method for conditionally creating and adding an error.
	 * 
	 * @return the condition, for convenience
	 */
	public boolean addError(boolean condition, String type, String message) {
		if (condition) {
			this.addError(new ActionError(type, message));
			return true;
		}
		return false;
	}

	/**
	 * Convenience method for setting the form-level generic error.
	 */
	public void setError(String message) {
		this.addError(new ActionError(message));
	}

	public Collection getErrors() {
		return Collections.unmodifiableCollection(errors.values());
	}

	public ActionError getError(String type) {
		return (ActionError) errors.get(type);
	}

	public boolean hasError(String type) {
		return errors.containsKey(type);
	}
	
	public void addWarning(ActionWarning warning){
		LOGGER.debug("warning added: " + warning.getType() +", " + warning.getDescription());
		warnings.put(warning.getType(), warning);
	}
	
	public boolean addWarning(boolean condition, String type, String message) {
		if(condition){
			this.addWarning(new ActionWarning(type, message));
			return true;
		}
		return false;
	}
	
	public Collection getWarnings(){
		return Collections.unmodifiableCollection(warnings.values());
	}
	
	public ActionWarning getWarning(String type){
		return (ActionWarning) warnings.get(type);
	}
	
	public boolean hasWarning(String type) {
		return warnings.containsKey(type);
	}

	public String toString() {
		return "ActionResult[ERRORS: " + this.errors + "\nWARNINGS: " + this.warnings +"]";
	}
	
}
