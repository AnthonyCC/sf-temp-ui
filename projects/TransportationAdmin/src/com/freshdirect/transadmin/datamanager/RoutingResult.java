package com.freshdirect.transadmin.datamanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutingResult implements Serializable {
	
	private String outputFile1;
	
	private String outputFile2;
	
	private String outputFile3;
	
	private List errors;
	
	private String additionalInfo;
	
	private Map routeNoSaveInfos;
	
	private List orders;
	
	private List trucks;
	
	private List regularOrders;
	
	private List depotOrders;
			
	private List depotTruckSchedule;
			
	private Map depotRouteMapping;
		
	public Map getDepotRouteMapping() {
		return depotRouteMapping;
	}

	public void setDepotRouteMapping(Map depotRouteMapping) {
		this.depotRouteMapping = depotRouteMapping;
	}

	public Map getRouteNoSaveInfos() {
		return routeNoSaveInfos;
	}

	public void setRouteNoSaveInfos(Map routeNoSaveInfos) {
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

	public List getDepotOrders() {
		return depotOrders;
	}

	public void setDepotOrders(List depotOrders) {
		this.depotOrders = depotOrders;
	}

	public List getDepotTruckSchedule() {
		return depotTruckSchedule;
	}

	public void setDepotTruckSchedule(List depotTruckSchedule) {
		this.depotTruckSchedule = depotTruckSchedule;
	}

	public List getOrders() {
		return orders;
	}

	public void setOrders(List orders) {
		this.orders = orders;
	}

	public List getRegularOrders() {
		return regularOrders;
	}

	public void setRegularOrders(List regularOrders) {
		this.regularOrders = regularOrders;
	}

	public List getTrucks() {
		return trucks;
	}

	public void setTrucks(List trucks) {
		this.trucks = trucks;
	}
	
}
