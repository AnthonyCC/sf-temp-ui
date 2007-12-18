/*
 * Created on Jul 10, 2003
 */
package com.freshdirect.sap.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiPostReturnI;
import com.freshdirect.sap.ejb.SapException;

/**
 * @author knadeem
 */
public class SapPostReturnCommand extends SapCommandSupport {

	private static final String FAKE_SAP_PROMO_CODE = "FAKE_SAP_PROMO_CODE";

	private final String invoiceNumber;
	private final List affiliateCharges = new ArrayList();
	private final List sapPromotions = new ArrayList();
	private double deliveryCharge;
	private double phoneCharge;

	public SapPostReturnCommand(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void execute() throws SapException {
		BapiPostReturnI bapi = BapiFactory.getInstance().getPostReturnBuilder();

		bapi.setInvoiceNumber(this.invoiceNumber);
		for (Iterator i = sapPromotions.iterator(); i.hasNext();) {			
			bapi.addPromotionAmount((SapPromotion) i.next());
		}
		bapi.setDeliveryCharge(this.deliveryCharge);
		bapi.setPhoneCharge(this.phoneCharge);

		for (Iterator i = affiliateCharges.iterator(); i.hasNext();) {
			bapi.addAffiliateCharges((AffiliateCharges) i.next());
		}

		this.invoke(bapi);
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setDeliveryCharge(double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public void setPhoneCharge(double phoneCharge) {
		this.phoneCharge = phoneCharge;
	}

	public void setPromotionAmount(double promotionAmount) {		
		this.addPromotion(FAKE_SAP_PROMO_CODE, promotionAmount);
	}

	public void addPromotion(String sapPromoCode, double promotionAmount) {		
		this.sapPromotions.add(new SapPromotion(sapPromoCode, promotionAmount));
	}

	public void addCharges(
		ErpAffiliate affiliate,
		double subtotal,
		double tax,
		double deposit,
		double restockingFee,
		double appliedCredit) {
		this.affiliateCharges.add(new AffiliateCharges(affiliate, subtotal, tax, deposit, restockingFee, appliedCredit));
	}

	private static class AffiliateCharges implements BapiPostReturnI.AffiliateCharges {

		private final ErpAffiliate affiliate;
		private final double subtotal;
		private final double tax;
		private final double deposit;
		private final double restockingFee;
		private final double appliedCredit;

		public AffiliateCharges(
			ErpAffiliate affiliate,
			double subtotal,
			double tax,
			double deposit,
			double restockingFee,
			double appliedCredit) {
			this.affiliate = affiliate;
			this.subtotal = subtotal;
			this.tax = tax;
			this.deposit = deposit;
			this.restockingFee = restockingFee;
			this.appliedCredit = appliedCredit;
		}

		public String getAffiliateCode() {
			return this.affiliate.getCode();
		}

		public double getSubtotal() {
			return this.subtotal;
		}

		public double getTax() {
			return this.tax;
		}

		public double getDeposit() {
			return this.deposit;
		}

		public double getAppliedCredit() {
			return this.appliedCredit;
		}

		public double getRestockingFee() {
			return this.restockingFee;
		}

		public String toString() {
			return "[AffiliateCharges affiliate: "
				+ affiliate
				+ " subtotal: "
				+ subtotal
				+ " tax: "
				+ tax
				+ " deposit: "
				+ deposit
				+ " restockingFee: "
				+ restockingFee
				+ " appliedCredit: "
				+ appliedCredit
				+ "]";
		}

	}
	
	private static class SapPromotion implements BapiPostReturnI.SapPromotion {

		private final String sapPromoCode;
		private final double amount;

		public SapPromotion(
			String sapPromoCode,
			double amount) {
			this.sapPromoCode = sapPromoCode;
			this.amount = amount;
		}

		public String getSapPromoCode() {
			return this.sapPromoCode;
		}

		public double getAmount() {
			return this.amount;
		}

		public String toString() {
			return "[SapPromotion SapPromoCode: "
				+ sapPromoCode
				+ " amount: "
				+ amount
				+ "]";
		}
		
	}
	
}
