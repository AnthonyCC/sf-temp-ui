/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiFunctionI {

	public void execute() throws BapiException;

	public BapiInfo[] getInfos();

	public boolean isFinished();

}