package com.freshdirect.delivery.model;

import com.freshdirect.framework.core.ModelSupport;

public class DlvZoneDescriptor extends ModelSupport {
	
	private String zoneCode;
	
	public String getZoneCode() {
		return zoneCode;
	}

	
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	private boolean unattended;
	
	public boolean isUnattended() {
		return unattended;
	}
	
	public void setUnattended(boolean unattended) {
		this.unattended = unattended;
	}
	
	public DlvZoneDescriptor() {
		super();
	}
	
	public DlvZoneDescriptor(String zoneCode, boolean unattended) {
		this();
		this.zoneCode = zoneCode;
		this.unattended = unattended;
	}
}
