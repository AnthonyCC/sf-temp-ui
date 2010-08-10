package com.freshdirect.transadmin.web.model;

import java.text.ParseException;

import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.transadmin.util.TransStringUtil;

public class HandOffBatchDepotScheduleInfo implements java.io.Serializable {
	
	private final IHandOffBatchDepotSchedule depotSchedule;
	
	public HandOffBatchDepotScheduleInfo(IHandOffBatchDepotSchedule depotSchedule) {
		super();
		this.depotSchedule = depotSchedule;
	}
	
	public String getArea() {
		return depotSchedule.getArea();
	}
	
	public String getDepotArrivalTimeInfo() {
		try {
			return TransStringUtil.getServerTime(depotSchedule.getDepotArrivalTime());
		} catch (ParseException e) {
			return null;
		}
	}
	
	public String getTruckDepartureTimeInfo() {
		try {
			return TransStringUtil.getServerTime(depotSchedule.getTruckDepartureTime());
		} catch (ParseException e) {
			return null;
		}
	}
	
	public String getDelete() {
		return "";
	}
}
