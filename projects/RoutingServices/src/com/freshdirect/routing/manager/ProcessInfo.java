package com.freshdirect.routing.manager;

import java.io.Serializable;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;

public class ProcessInfo implements Serializable {
	
	private EnumProcessInfoType processInfoType;
	private EnumProcessType processType;
	private String orderId;
	private String locationId;
	private String zoneCode;
	private String additionalInfo;
				
	public EnumProcessInfoType getProcessInfoType() {
		return processInfoType;
	}
	public void setProcessInfoType(EnumProcessInfoType processInfoType) {
		this.processInfoType = processInfoType;
	}
	public EnumProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(EnumProcessType processType) {
		this.processType = processType;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getProcessInfoMsg() {
		String msg = "";
		if(processInfoType != null) {
			msg = processInfoType.getName();
		}
		return msg;
	}
		
	public String getProcessTypeMsg() {
		String msg = "";
		if(processType != null) {
			msg = processType.getProcessError();
		}
		return msg;
	}
	
	public void setProcessInfoMsg(String value) {		
	}
		
	public void setProcessTypeMsg(String value) {		
	}
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	

}
