package com.freshdirect.fdstore.brandads.model;


import java.io.Serializable;
import java.util.List;


public class HLBrandProductAdResponse implements Serializable {

	private static final long serialVersionUID = -6321099101771809278L;
	
	public String PageBeacon;
	
	public String updatePdpPageBeacon;
	
	public List<HLBrandProductAdInfo> SearchProductAd;
	
	public List<HLBrandProductAdInfo> ProductAd;
	

	public List<HLBrandProductAdInfo> getSearchProductAd() {
		return SearchProductAd;
	}

	public void setSearchProductAd(List<HLBrandProductAdInfo> searchProductAd) {
		SearchProductAd = searchProductAd;
	}

	public String getPageBeacon() {
		return PageBeacon;
	}

	public void setPageBeacon(String pageBeacon) {
		PageBeacon = pageBeacon;
	}
	
	public List<HLBrandProductAdInfo> getProductAd() {
		return ProductAd;
	}

	public void setProductAd(List<HLBrandProductAdInfo> productAd) {
		ProductAd = productAd;
	}

	public String getUpdatePdpPageBeacon() {
		return updatePdpPageBeacon;
	}

	public void setUpdatePdpPageBeacon(String updatePdpPageBeacon) {
		this.updatePdpPageBeacon = updatePdpPageBeacon;
	}
	
}
