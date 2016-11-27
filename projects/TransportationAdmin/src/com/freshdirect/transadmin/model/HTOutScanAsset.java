package com.freshdirect.transadmin.model;

import java.util.Date;

public class HTOutScanAsset {
	private String assetIdentifier;
	private Date assetDate;
	
	public String getAssetIdentifier() {
		return assetIdentifier;
	}
	public void setAssetIdentifier(String assetIdentifier) {
		this.assetIdentifier = assetIdentifier;
	}

	public Date getAssetDate() {
		return assetDate;
	}
	public void setAssetDate(Date assetDate) {
		this.assetDate = assetDate;
	}
	public HTOutScanAsset(String assetIdentifier, Date assetDate) {
		super();
		this.assetIdentifier = assetIdentifier;
		this.assetDate = assetDate;
	}

}
