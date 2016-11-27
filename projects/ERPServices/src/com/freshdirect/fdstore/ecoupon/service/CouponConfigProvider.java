package com.freshdirect.fdstore.ecoupon.service;

import java.io.Serializable;

import com.freshdirect.FDCouponProperties;

public class CouponConfigProvider implements Serializable {

	public static String getSiteId() {
		return FDCouponProperties.getYTSiteId();
	}

	public static String getRetailerName() {
		return FDCouponProperties.getYTRetailerName();
	}

	public static String getRetailerId() {
		return FDCouponProperties.getYTRetailerId();
	}

	public static String getSignature() {
		return FDCouponProperties.getYTSignature();
	}
	
	public static String getUrl() {
		return FDCouponProperties.getYTProviderUrl();
	}
	
	public static String getVersion() {
		return FDCouponProperties.getYTProviderVersion();
	}
	
	public static String getYTCouponsMetaURL() {
		return FDCouponProperties.getYTCouponsMetaURL();
	}

	public static String getYTCustomerCouponsURL() {
		return FDCouponProperties.getYTCustomerCouponsURL();
	}

	public static String getYTClipCouponsURL() {
		return FDCouponProperties.getYTClipCouponsURL();
	}

	public static String getYTPreviewCartCouponsURL() {
		return FDCouponProperties.getYTPreviewCartCouponsURL();
	}

	public static String getYTPreviewModifyCartCouponsURL() {
		return FDCouponProperties.getYTPreviewModifyCartCouponsURL();
	}
	
	public static String getYTSubmitCreateOrderURL() {
		return FDCouponProperties.getYTSubmitCreateOrderURL();
	}

	public static String getYTSubmitModifyOrderURL() {
		return FDCouponProperties.getYTSubmitModifyOrderURL();
	}
	
	public static String getYTCancelOrderURL() {
		return FDCouponProperties.getYTCancelOrderURL();
	}

	public static String getYTConfirmOrderURL() {
		return FDCouponProperties.getYTConfirmOrderURL();
	}
	
	public static Integer getYTConnectionTimeoutPeriod() {
		return FDCouponProperties.getYTConnectionTimeoutPeriod();
	}

	public static Integer getYTReadTimeoutPeriod() {
		return FDCouponProperties.getYTReadTimeoutPeriod();
	}
}
