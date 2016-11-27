package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendHandOff;

public interface IHandOffBatchTrailer extends ITrailerModel, BapiSendHandOff.HandOffTrailerIn {	

	String getBatchId();
	void setBatchId(String batchId);

}
