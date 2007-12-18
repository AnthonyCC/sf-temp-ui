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

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ActionResult {

	private HashMap errors = null;
	private HashMap warnings = null;

	public ActionResult() {
		errors = new HashMap();
		warnings = new HashMap();
	}

	public boolean isSuccess() {
		return (0 == errors.size());
	}

	public boolean isFailure() {
		return (0 < errors.size());
	}

	public void addError(ActionError error) {
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
