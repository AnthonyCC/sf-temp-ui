/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.sap.bapi.BapiCartonDetailsForSale;
import com.freshdirect.sap.bapi.BapiCartonInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCartonInfoForSale extends SapCommandSupport {

	private String plantCode;
	private Date deliveryDate;
	private String waveNumber;
	private String saleId;
	private String sapSaleId;
	
	
	private List<ErpCartonInfo> cartonInfoDetails;
	
	public SapCartonInfoForSale(String plantCode, Date deliveryDate, String saleId, String saleSapId, String waveNumber) {
		this.saleId = saleId;
		this.sapSaleId = saleSapId;
		this.plantCode = plantCode;
		this.waveNumber = waveNumber;
		this.deliveryDate = deliveryDate;
	}

	public void execute() throws SapException {

		BapiCartonDetailsForSale bapi = BapiFactory.getInstance().getBapiCartonDetailsForSale();
		bapi.setParameters(plantCode, deliveryDate, saleId, sapSaleId, waveNumber);

		this.invoke(bapi);
		
		cartonInfoDetails = bapi.getCartonInfos();
	}
	
	public List<ErpCartonInfo> getCartonInfos() {
		return cartonInfoDetails;
	}
}
