/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiCartonInfo extends BapiFunctionI {

	public void setOrderIds(List orderIdList);
	
	public Map getCartonInfos();

}
