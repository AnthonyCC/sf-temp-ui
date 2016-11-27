/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.io.Serializable;

import com.freshdirect.sap.ejb.SapException;

/**
 * Represents a complex request to SAP.
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SapCommandI extends Serializable {

	public void execute() throws SapException;

}
