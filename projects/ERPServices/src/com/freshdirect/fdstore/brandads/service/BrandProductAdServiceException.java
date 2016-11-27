package com.freshdirect.fdstore.brandads.service;

import java.util.List;

import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponseErrorCode;

public class BrandProductAdServiceException extends  Exception {
	
	private String details;
	
	public BrandProductAdServiceException() {
		super();
	}
	
	public BrandProductAdServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BrandProductAdServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BrandProductAdServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public BrandProductAdServiceException(String message,String details) {
		super(message);
		this.details = details;
	}
	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CouponServiceException [message="+this.getMessage()+". details=" + details + "]";
	}
	
}
