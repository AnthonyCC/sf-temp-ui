/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSalesOrderCancel;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderCancel extends JcoBapiFunction implements BapiSalesOrderCancel {

	public JcoBapiSalesOrderCancel() {
		super("Z_BAPI_SALESORDER_CHANGE");
		this.function.getImportParameterList().getStructure("ORDER_HEADER_INX").setValue("D", "UPDATEFLAG");
	}

	public void setSalesDocumentNumber(String salesDocumentNumber) {
		this.function.getImportParameterList().setValue(salesDocumentNumber, "SALESDOCUMENT");
	}
}
