package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSendSettlement;

public class JcoBapiSendSettlement extends JcoBapiFunction implements BapiSendSettlement {

	public JcoBapiSendSettlement() {
		super("Z_BAPI_POST_INCOMING_PAYMENT");
	}

	public void setFileName(String fileName) {
		this.function.getImportParameterList().setValue(fileName, "INPUT_FILE");
	}

	public void setFileLocation(String fileLocation) {
		this.function.getImportParameterList().setValue(fileLocation, "INPUT_FOLDER");
	}

}
