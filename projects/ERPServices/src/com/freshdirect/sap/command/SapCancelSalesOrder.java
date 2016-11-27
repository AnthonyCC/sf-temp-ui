/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiSalesOrderCancel;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCancelSalesOrder extends SapCommandSupport implements SapOrderCommand {

	private final String webOrderNumber;

	/** SAP sales document number */
	private final String salesDocumentNumber;

	public SapCancelSalesOrder(String webOrderNumber, String salesDocumentNumber) {
		if (salesDocumentNumber == null) {
			throw new IllegalArgumentException("Sales document number cannot be null");
		}
		this.webOrderNumber = webOrderNumber;
		this.salesDocumentNumber = salesDocumentNumber;
	}

	public String getWebOrderNumber() {
		return this.webOrderNumber;
	}

	public void execute() throws SapException {

		BapiSalesOrderCancel bapi = BapiFactory.getInstance().getSalesOrderCancelBuilder();
		bapi.setSalesDocumentNumber(this.salesDocumentNumber);

		this.invoke(bapi);

	}

}
