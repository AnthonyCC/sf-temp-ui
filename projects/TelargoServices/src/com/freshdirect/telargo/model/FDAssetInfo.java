package com.freshdirect.telargo.model;

import java.io.Serializable;

import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetWithStateDto;

public class FDAssetInfo implements Serializable {
	String registrationNo;
	String internalNo;
	String lastStatus;
	String lastLocation;
	
	public FDAssetInfo(AssetWithStateDto assetDTO) {
		this.registrationNo = assetDTO.getRegistrationNumber();
		this.internalNo = assetDTO.getInternalNumber();
		if(assetDTO.getLastState() != null) {
			if(assetDTO.getLastState().getState() != null) {
				this.lastStatus = assetDTO.getLastState().getState().getValue();
			}
			this.lastLocation = assetDTO.getLastState().getLocationRgcDescription();
		}
		
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getInternalNo() {
		return internalNo;
	}

	public void setInternalNo(String internalNo) {
		this.internalNo = internalNo;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	@Override
	public String toString() {
		return "FDAssetInfo [Registration No=" + registrationNo
				+ ",\n Truck No=" + internalNo + ",\n lastStatus="
				+ lastStatus + ",\n lastLocation=" + lastLocation + "]";
	}
	
	
}
