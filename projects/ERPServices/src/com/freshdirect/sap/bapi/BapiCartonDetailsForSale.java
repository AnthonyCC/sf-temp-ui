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
import java.util.List;

import com.freshdirect.customer.ErpCartonInfo;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiCartonDetailsForSale extends BapiFunctionI {

	void setParameters(String plantCode, Date deliveryDate, String saleId, String saleSapId, String waveNumber);
	
	List<ErpCartonInfo> getCartonInfos();

}
