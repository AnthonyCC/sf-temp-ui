package com.freshdirect.sap.command;

import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiSendSettlement;
import com.freshdirect.sap.ejb.SapException;

public class SapSendSettlement extends SapCommandSupport {

	private final String fileName;
	private final String fileLocation;

	public SapSendSettlement(String fileName, String fileLocation) {
		this.fileName = fileName;
		this.fileLocation = fileLocation;
	}

	public void execute() throws SapException {
		BapiSendSettlement bapi = BapiFactory.getInstance().getSendSettlementBuilder();
		bapi.setFileLocation(this.fileLocation);
		bapi.setFileName(this.fileName);
		this.invoke(bapi);
	}

}
