/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.util.Date;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiMaterialAvailability extends BapiFunctionI {

	public void setPlant(String plant);
	public void setStgeLoc(String stgeLoc);
	public void setCustomerNumber(String customerNo);	
	public void setMaterialNumber(String materialNo);
	public void setSalesUnit(String salesUnit);

	public void addRequest(Date requestedDate, double quantity);

	public int getCommitedLength();
	public Date getCommitedDate(int index);
	public double getCommitedQty(int index);

}
