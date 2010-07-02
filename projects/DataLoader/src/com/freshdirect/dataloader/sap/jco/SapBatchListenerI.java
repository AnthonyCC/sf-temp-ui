/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.jco;

import com.freshdirect.dataloader.LoaderException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SapBatchListenerI {

	public void processErpsBatch(String destination, String prefix) throws LoaderException;

}