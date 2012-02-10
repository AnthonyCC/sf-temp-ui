package com.freshdirect.delivery.model;

import java.io.Serializable;

public class AirclicNextTelAsset implements Serializable {
	
	private String assetNo;
	private String nextTelNo;
	private String assetSatus;
	
	public AirclicNextTelAsset(String assetNo, String nextTelNo,
			String assetSatus) {
		super();
		this.assetNo = assetNo;
		this.nextTelNo = nextTelNo;
		this.assetSatus = assetSatus;
	}
	public AirclicNextTelAsset() {
		// TODO Auto-generated constructor stub
	}
	public String getAssetNo() {
		return assetNo;
	}	
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}
	public String getNextTelNo() {
		return nextTelNo;
	}
	public void setNextTelNo(String nextTelNo) {
		this.nextTelNo = nextTelNo;
	}
	public String getAssetSatus() {
		return assetSatus;
	}
	public void setAssetSatus(String assetSatus) {
		this.assetSatus = assetSatus;
	}
}
