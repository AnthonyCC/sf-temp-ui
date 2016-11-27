package com.freshdirect.transadmin.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.freshdirect.framework.core.RuntimeExceptionSupport;
import com.freshdirect.routing.service.exception.Issue;

public class TransAdminBaseException extends RuntimeExceptionSupport {
	
	protected String issueId;
  	
	/**
	 * Default constructor.
	 */	
	public TransAdminBaseException() {
	}

	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a custom message
	 */	
	public TransAdminBaseException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception, nesting another exception.
	 * 
	 * @param ex the exception to wrap
	 */	
	public TransAdminBaseException(Exception ex) {
		super(ex);
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public TransAdminBaseException(Exception ex, String message) {
		super(ex, message);
	}
	
	public TransAdminBaseException(String msg, Exception e, String id) {
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
