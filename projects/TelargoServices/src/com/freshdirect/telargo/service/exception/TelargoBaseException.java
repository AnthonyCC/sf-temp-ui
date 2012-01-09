package com.freshdirect.telargo.service.exception;

public class TelargoBaseException extends RuntimeExceptionSupport {
	
	protected String issueId;
  	
	/**
	 * Default constructor.
	 */	
	public TelargoBaseException() {
	}

	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a custom message
	 */	
	public TelargoBaseException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception, nesting another exception.
	 * 
	 * @param ex the exception to wrap
	 */	
	public TelargoBaseException(Exception ex) {
		super(ex);
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public TelargoBaseException(Exception ex, String message) {
		super(ex, message);
	}
	
	public TelargoBaseException(String msg, Exception e, String id) {
	      this(e, msg);	      
	      this.issueId = id;
	   }
	   
	   public String getIssue() {
	      return this.issueId;
	   }
	   
	   public String getIssueMessage() {
		   return Issue.getMessage(issueId);
	   }

}
