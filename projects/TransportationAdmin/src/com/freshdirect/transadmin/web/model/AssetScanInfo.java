package com.freshdirect.transadmin.web.model;

public class AssetScanInfo {
	
	private String assetNo;
	private String employeeId;
	private String employeeName;
	private String status;
	private String scanTime;
		
	public AssetScanInfo(String assetNo, String employeeId, String status) {
		super();
		this.assetNo = assetNo;
		this.employeeId = employeeId;
		this.status = status;
	}
	public AssetScanInfo() {
		// TODO Auto-generated constructor stub
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
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}	
	public String getScanTime() {
		return scanTime;
	}
	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetNo == null) ? 0 : assetNo.hashCode());
		result = prime * result
				+ ((employeeId == null) ? 0 : employeeId.hashCode());
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
		AssetScanInfo other = (AssetScanInfo) obj;
		if (assetNo == null) {
			if (other.assetNo != null)
				return false;
		} else if (!assetNo.equals(other.assetNo))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}
	
}
