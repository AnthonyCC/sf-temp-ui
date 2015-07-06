package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.AbstractCoremetricsResponse;

/**
 *	Simple java bean for add to cart response. 
 *	Class structure is representing the JSON structure. 	
 * 
 * @author treer
 */
public class AddToCartResponseData extends AbstractCoremetricsResponse implements Serializable {

	
	private static final long	serialVersionUID	= 8413382103584983050L;

	private List<AddToCartResponseDataItem> atcResult = new ArrayList<AddToCartResponseDataItem>();
	
	private PendingPopupData pendingPopupData;

	// map: couponId -> statusMessage 
	private Map<String,AddToCartCouponResponse> couponStatus = new HashMap<String, AddToCartCouponResponse>();

	// redirect for optional cart-confirm page
	private String redirectUrl;
	
	public List<AddToCartResponseDataItem> getAtcResult() {
		return atcResult;
	}
	public void setAtcResult(List<AddToCartResponseDataItem> items) {
		this.atcResult = items;
	}
	public PendingPopupData getPendingPopupData() {
		return pendingPopupData;
	}
	public void setPendingPopupData(PendingPopupData pendingPopupData) {
		this.pendingPopupData = pendingPopupData;
	}
	
	public Map<String, AddToCartCouponResponse> getCouponStatus() {
		return couponStatus;
	}
	
	public void setCouponStatus( Map<String, AddToCartCouponResponse> couponStatus ) {
		this.couponStatus = couponStatus;
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}
	
	public void setRedirectUrl( String redirectUrl ) {
		this.redirectUrl = redirectUrl;
	}

}
