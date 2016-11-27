package com.freshdirect.fdstore.ecoupon.service;

import java.util.List;

import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponseErrorCode;

public class CouponServiceException extends  Exception {
	
	private String details;
	
	public CouponServiceException() {
		super();
	}
	
	public CouponServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CouponServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CouponServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CouponServiceException(String message,String details) {
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
