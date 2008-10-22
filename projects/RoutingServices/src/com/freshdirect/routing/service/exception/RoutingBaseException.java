package com.freshdirect.routing.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class RoutingBaseException extends Exception {
	
	/** the nested exception */	
	private Exception nestedException = null;
	
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
		this.nestedException = ex;
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public RoutingBaseException(Exception ex, String message) {
		this(message);
		this.nestedException = ex;
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

	/**
	 * Get the nested exception.
	 *
	 * @return the wrapped exception
	 */	
	public Exception getNestedException() {
		return this.nestedException;
	}

	/** 
	 * Prints the exception's stack trace to standard output
	 */	
	public void printStackTrace() {
		super.printStackTrace();
		if (this.nestedException != null) {
			this.nestedException.printStackTrace();
		}
	}

	/**
	 * Prints the exception's stack trace to a supplied PrintStream.
	 *
	 * @param s the PrintStream to dump the stack trace to
	 */	
	public void printStackTrace( PrintStream s ) {
		super.printStackTrace( s );
		if (this.nestedException != null) {
			this.nestedException.printStackTrace( s );
		}
	}

	/**
	 * Prints the exception's stack trace to a supplied PrintWriter
	 *
	 * @param s the PrintWriter to dump the stack trace to
	 */	
	public void printStackTrace( PrintWriter s ) {
		super.printStackTrace( s );
		if (this.nestedException != null) {
			this.nestedException.printStackTrace( s );
		}
	}

	/**
	 * Get the stack trace as a string.
	 */
	public String getFDStackTrace() {
		StringWriter sw = new StringWriter();
		this.printStackTrace( new PrintWriter(sw) );
		return sw.toString();
	}

	/** returns the String equivalent of the exception
	 * @return the exception as a String
	 */	
	public String toString() {
		String output = super.toString();
		if (nestedException!=null) {
			output += "\n" + this.nestedException.toString();
		}
		return output;
	}
}
