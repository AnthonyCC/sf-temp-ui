/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiMaterialAvailability extends JcoBapiFunction implements BapiMaterialAvailability {

	private Date[] commitedDates;
	private double[] commitedQtys;

	public JcoBapiMaterialAvailability() {
		super("Z_BAPI_MATERIAL_AVAILABILITY");
	}

	public void setPlant(String plant) {
		this.function.getImportParameterList().setValue(plant, "PLANT");
	}

	public void setStgeLoc(String stgeLoc) {
		this.function.getImportParameterList().setValue(stgeLoc, "STGE_LOC");
	}

	public void setCustomerNumber(String customerNo) {
		this.function.getImportParameterList().setValue(customerNo, "CUSTOMER");
	}

	public void setMaterialNumber(String materialNo) {
		this.function.getImportParameterList().setValue(materialNo, "MATERIAL");
	}

	public void setSalesUnit(String salesUnit) {
		this.function.getImportParameterList().setValue(salesUnit, "UNIT");
	}

	public void addRequest(Date requestedDate, double quantity) {
		JCO.Table wmdvsx = this.function.getTableParameterList().getTable("WMDVSX");
		wmdvsx.appendRow();
		wmdvsx.setValue(requestedDate, "REQ_DATE");
		wmdvsx.setValue(quantity, "REQ_QTY");
	}

	public void processResponse() throws BapiException {
		super.processResponse();

		JCO.Table wmdvex = function.getTableParameterList().getTable("WMDVEX");

		// fill inventory entries
		this.commitedDates = new Date[wmdvex.getNumRows()];
		this.commitedQtys = new double[commitedDates.length];
		wmdvex.firstRow();
		for (int i = 0; i < commitedDates.length; i++) {
			this.commitedDates[i] = wmdvex.getDate("COM_DATE");
			this.commitedQtys[i] = wmdvex.getDouble("COM_QTY");
			wmdvex.nextRow();
		}
	}

	public int getCommitedLength() {
		return this.commitedDates.length;
	}

	public Date getCommitedDate(int index) {
		return this.commitedDates[index];
	}

	public double getCommitedQty(int index) {
		return this.commitedQtys[index];
	}

}
