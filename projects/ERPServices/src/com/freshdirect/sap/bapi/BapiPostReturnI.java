/*
 * Created on Jul 10, 2003
 */
package com.freshdirect.sap.bapi;

import java.io.Serializable;

/**
 * @author knadeem
 */
public interface BapiPostReturnI extends BapiFunctionI {
	
	public static interface AffiliateCharges extends Serializable{
		public String getAffiliateCode();
		public double getSubtotal();
		public double getTax();
		public double getDeposit();
		public double getAppliedCredit();
		public double getRestockingFee();
	}
	
	public static interface SapPromotion extends Serializable{
		public String getSapPromoCode();
		public double getAmount();
	}

	public void setInvoiceNumber(String invoiceNumber);
	public void addAffiliateCharges(AffiliateCharges affiliateCharges);
	public void addPromotionAmount(SapPromotion sapPromotion);
	public void setDeliveryCharge(double dlvCharge);
	public void setPhoneCharge(double phoneCharge);

}
