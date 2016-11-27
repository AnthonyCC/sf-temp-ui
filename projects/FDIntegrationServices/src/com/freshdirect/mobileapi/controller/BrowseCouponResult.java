package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductCouponResult;

public class BrowseCouponResult extends Message{
	
    private List<ProductCouponResult> productCouponResults = new ArrayList<ProductCouponResult>();

    public List<ProductCouponResult> getProductCouponResults() {
		return productCouponResults;
	}

	public void setProductCouponResults(
			List<ProductCouponResult> productCouponResults) {
		this.productCouponResults = productCouponResults;
	}

}
