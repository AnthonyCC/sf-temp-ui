/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.List;
import java.util.Map;

import com.freshdirect.sap.bapi.BapiCartonInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCartonInfo extends SapCommandSupport {

	private final List orderIdList;
	
	private Map cartonInfoDetails;
	
	public SapCartonInfo(List orderIdList) {
		this.orderIdList = orderIdList;		
	}

	public void execute() throws SapException {

		BapiCartonInfo bapi = BapiFactory.getInstance().getCartonInfoProvider();
		bapi.setOrderIds(orderIdList);

		this.invoke(bapi);
		
		cartonInfoDetails = bapi.getCartonInfos();
	}
	
	public Map getCartonInfos() {
		return cartonInfoDetails;
	}
}
