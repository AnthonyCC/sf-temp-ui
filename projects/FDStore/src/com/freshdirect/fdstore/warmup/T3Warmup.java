/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.warmup;

import java.util.Hashtable;
import weblogic.common.T3StartupDef;
import weblogic.common.T3ServicesDef;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class T3Warmup extends Warmup implements T3StartupDef {


	public void setServices(T3ServicesDef services) {
	}
	
	public String startup(String name, Hashtable args) throws Exception {
		this.warmup();
		return "Warmup OK";
	}
	
}