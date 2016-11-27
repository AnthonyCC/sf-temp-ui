package com.freshdirect.fdstore.ecoupon.model.yt;

import java.io.Serializable;
import java.util.List;



public class YTCouponResponse implements Serializable{

	private List<YTCouponResponseErrorCode> codes;
	private String result;
	/**
	 * @return the errorCodes
	 */
	public List<YTCouponResponseErrorCode> getCodes() {
		return codes;
	}
	/**
	 * @param errorCodes the errorCodes to set
	 */
	public void setCodes(List<YTCouponResponseErrorCode> codes) {
		this.codes = codes;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	
	
}
