package com.freshdirect.dashboard.exception;


public class FDBaseException extends RuntimeExceptionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -183255576543206204L;
	protected String issueId;
  	
	/**
	 * Default constructor.
	 */	
	public FDBaseException() {
	}

	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a custom message
	 */	
	public FDBaseException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception, nesting another exception.
	 * 
	 * @param ex the exception to wrap
	 */	
	public FDBaseException(Exception ex) {
		super(ex);
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public FDBaseException(Exception ex, String message) {
		super(ex, message);
	}
	
	public FDBaseException(String msg, Exception e, String id) {
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
