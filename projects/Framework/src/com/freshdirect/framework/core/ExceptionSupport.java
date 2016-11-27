package com.freshdirect.framework.core;

import java.io.*;

/**
 * An abstract exception class that provides support for nested exceptions.
 */

public abstract class ExceptionSupport extends Exception {

	private static final long	serialVersionUID	= -7214832700267315648L;
	
	/** the nested exception */	
	private Exception nestedException = null;

	/**
	 * Default constructor.
	 */	
	public ExceptionSupport() {
	}

	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a custom message
	 */	
	public ExceptionSupport(String message) {
		super(message);
	}

	/**
	 * Creates a new exception, nesting another exception.
	 * 
	 * @param ex the exception to wrap
	 */	
	public ExceptionSupport(Exception ex) {
		this.nestedException = ex;
	}

	/**
	 * Creates a new exception with a custom message and a nested exception.
	 *
	 * @param ex the exception to wrap
	 * @param message a custom message
	 */	
	public ExceptionSupport(Exception ex, String message) {
		this(message);
		this.nestedException = ex;
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public String toString() {
		String output = super.toString();
		if (nestedException!=null) {
			output += "\n" + this.nestedException.toString();
		}
		return output;
	}

}

