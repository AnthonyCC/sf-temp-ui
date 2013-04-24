package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.Date;

public class AssetActivity implements Serializable {

	private String id;
	private String assetNo;
	private String employeeId;
	private String status;
	private String scannedBy;
	private Date scannedTime;
	
	private Date deliveryDate;
		
	public AssetActivity(String assetNo, String employeeId, String status) {
		super();
		this.assetNo = assetNo;
		this.employeeId = employeeId;
		this.status = status;
	}
	public AssetActivity() {
		// TODO Auto-generated constructor stub
	}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssetNo() {
		return assetNo;
	}
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScannedBy() {
		return scannedBy;
	}
	public void setScannedBy(String scannedBy) {
		this.scannedBy = scannedBy;
	}
	public Date getScannedTime() {
		return scannedTime;
	}
	public void setScannedTime(Date scannedTime) {
		this.scannedTime = scannedTime;
	}	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetActivity other = (AssetActivity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
