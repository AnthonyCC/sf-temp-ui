package com.freshdirect.transadmin.web;

import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class BaseRoutingFormController extends BaseFormController  {

	protected String getOutputFilePath(String filePath) {
//		System.out.println("TransportationAdminProperties.getDownloadProviderUrl() >"+TransportationAdminProperties.getDownloadProviderUrl());
		return TransportationAdminProperties.getDownloadProviderUrl()+"?filePath="+filePath;
	}
}
