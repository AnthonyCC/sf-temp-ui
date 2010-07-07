package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;

public class RoutingAnalyzerContext implements Serializable {
	
	private List<FDTimeslot> dlvTimeSlots;
	
	private IPackagingModel historyPackageInfo;
	
	private Map<String, IServiceTimeTypeModel>	serviceTimeTypes;
		
	public Map<String, IServiceTimeTypeModel> getServiceTimeTypes() {
		return serviceTimeTypes;
	}

	public void setServiceTimeTypes(
			Map<String, IServiceTimeTypeModel> serviceTimeTypes) {
		this.serviceTimeTypes = serviceTimeTypes;
	}

	public IPackagingModel getHistoryPackageInfo() {
		return historyPackageInfo;
	}

	public void setHistoryPackageInfo(IPackagingModel historyPackageInfo) {
		this.historyPackageInfo = historyPackageInfo;
	}

	public List<FDTimeslot> getDlvTimeSlots() {
		return dlvTimeSlots;
	}

	public void setDlvTimeSlots(List<FDTimeslot> dlvTimeSlots) {
		this.dlvTimeSlots = dlvTimeSlots;
	}
	
	
}
