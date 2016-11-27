package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSalesOrderCancel;
import com.sap.conn.jco.JCoException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderCancel extends JcoBapiFunction implements BapiSalesOrderCancel {

	public JcoBapiSalesOrderCancel() throws JCoException
	{
		super("Z_BAPI_SALESORDER_CHANGE");
		this.function.getImportParameterList().getStructure("ORDER_HEADER_INX").setValue("UPDATEFLAG","D");
	}

	public void setSalesDocumentNumber(String salesDocumentNumber) {
		this.function.getImportParameterList().setValue("SALESDOCUMENT", salesDocumentNumber);
	}
}
