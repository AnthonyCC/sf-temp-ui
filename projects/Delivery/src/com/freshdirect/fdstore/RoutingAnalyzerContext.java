package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.routing.model.IPackagingModel;

public class RoutingAnalyzerContext implements Serializable {
	
	List<FDTimeslot> dlvTimeSlots;
	
	IPackagingModel historyPackageInfo;
	
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
