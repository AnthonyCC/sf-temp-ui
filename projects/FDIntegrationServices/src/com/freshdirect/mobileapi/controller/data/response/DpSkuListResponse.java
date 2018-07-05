package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductSearchResult;

public class DpSkuListResponse extends Message {

	private List<String> dpskulist;
	
	public List<String> getDpskulist() {
		return dpskulist;
	}

	public void setDpskulist(List<String> dpskulist) {
		this.dpskulist = dpskulist;
	}
	
	private List<ProductSearchResult> dpproductList;
	
	public List<ProductSearchResult> getDpProductlist() {
		return dpproductList;
	}

	public void setDpProductlist(List<ProductSearchResult> dpproductList) {
		this.dpproductList = dpproductList;
	}
	
}
