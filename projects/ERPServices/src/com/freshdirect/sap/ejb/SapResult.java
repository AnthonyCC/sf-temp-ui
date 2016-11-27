/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import java.io.Serializable;

import com.freshdirect.sap.command.SapCommandI;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapResult implements Serializable {

	private final SapCommandI command;
	private final Exception exception;

	public SapResult(SapCommandI command) {
		this(command, null);
	}

	public SapResult(SapCommandI command, Exception exception) {
		this.command = command;
		this.exception = exception;
	}

	public boolean isSuccessful() {
		return this.exception == null;
	}

	public SapCommandI getCommand() {
		return this.command;
	}

	public Exception getException() {
		return this.exception;
	}

}