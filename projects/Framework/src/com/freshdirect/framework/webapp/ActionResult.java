package com.freshdirect.framework.webapp;

import java.util.*;

import org.apache.log4j.Logger;

public class ActionResult {
	
	private static final Logger LOGGER = Logger.getLogger( ActionResult.class );

	private HashMap<String, ActionError>	errors		= null;
	private HashMap<String, ActionWarning>	warnings	= null;

	public ActionResult() {
		errors = new HashMap<String, ActionError>();
		warnings = new HashMap<String, ActionWarning>();
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

	public Collection<ActionError> getErrors() {
		return Collections.unmodifiableCollection(errors.values());
	}
	
	public ActionError getFirstError() {
		for ( ActionError err : errors.values() ) {
			return err;
		}
		return null;
	}

	public ActionError getError(String type) {
		return errors.get(type);
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
	
	public Collection<ActionWarning> getWarnings(){
		return Collections.unmodifiableCollection(warnings.values());
	}
	
	public ActionWarning getWarning(String type){
		return warnings.get(type);
	}
	
	public boolean hasWarning(String type) {
		return warnings.containsKey(type);
	}

	public String toString() {
		return "ActionResult[ERRORS: " + this.errors + "][WARNINGS: " + this.warnings +"]";
	}
	
}
