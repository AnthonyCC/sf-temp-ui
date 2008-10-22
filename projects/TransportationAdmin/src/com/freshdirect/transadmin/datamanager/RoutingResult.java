package com.freshdirect.transadmin.datamanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoutingResult implements Serializable {
	
	private String outputFile1;
	
	private String outputFile2;
	
	private String outputFile3;
	
	private List errors;
	
	private String additionalInfo;
	
	private List routeNoSaveInfos;	
	
	public List getRouteNoSaveInfos() {
		return routeNoSaveInfos;
	}

	public void setRouteNoSaveInfos(List routeNoSaveInfos) {
		this.routeNoSaveInfos = routeNoSaveInfos;
	}

	public List getErrors() {
		return errors;
	}

	public void setErrors(List errors) {
		this.errors = errors;
	}

	public String getOutputFile1() {
		return outputFile1;
	}

	public void setOutputFile1(String outputFile1) {
		this.outputFile1 = outputFile1;
	}

	public String getOutputFile2() {
		return outputFile2;
	}

	public void setOutputFile2(String outputFile2) {
		this.outputFile2 = outputFile2;
	}

	public String getOutputFile3() {
		return outputFile3;
	}

	public void setOutputFile3(String outputFile3) {
		this.outputFile3 = outputFile3;
	}
	
	public void addError(String error) {
		if(errors == null) {
			errors = new ArrayList();
		}
		errors.add(error);
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	

}
