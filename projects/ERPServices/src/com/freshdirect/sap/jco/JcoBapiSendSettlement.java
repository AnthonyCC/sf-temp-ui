package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSendSettlement;
import com.sap.conn.jco.JCoException;

public class JcoBapiSendSettlement extends JcoBapiFunction implements BapiSendSettlement {

	public JcoBapiSendSettlement() throws JCoException
	{
		super("Z_BAPI_POST_INCOMING_PAYMENT");
	}

	public void setFileName(String fileName) {
		this.function.getImportParameterList().setValue("INPUT_FILE", fileName);
	}

	public void setFileLocation(String fileLocation) {
		this.function.getImportParameterList().setValue("INPUT_FOLDER", fileLocation);
	}

}
