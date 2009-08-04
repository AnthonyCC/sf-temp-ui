package com.freshdirect.transadmin.web.model;

import com.freshdirect.transadmin.datamanager.model.IRoutingOutputInfo;

public class RoutingMergeCommand extends BaseRoutingOutCommand implements IRoutingOutputInfo {
	
	private byte[] truckFile;
	
	private byte[] orderFile;

	public byte[] getOrderFile() {
		return orderFile;
	}

	public void setOrderFile(byte[] orderFile) {
		this.orderFile = orderFile;
	}

	public byte[] getTruckFile() {
		return truckFile;
	}

	public void setTruckFile(byte[] truckFile) {
		this.truckFile = truckFile;
	}
		
}

