package com.freshdirect.routing.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

public class RoutingBaseException extends RuntimeExceptionSupport {
	
	protected String issueId;
  	
	/**
	 * Default constructor.
	 */	
	public RoutingBaseException() {
	}

	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a custom message
	 */	
	public RoutingBaseException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception, nesting another exception.
	 * 
	 * @param ex the exception to wrap
	 */	
	public RoutingBaseException(Exception ex) {
		super(ex);
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public RoutingBaseException(Exception ex, String message) {
		super(ex, message);
	}
	
	public RoutingBaseException(String msg, Exception e, String id) {
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
