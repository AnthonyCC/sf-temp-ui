/*
 * Created on Jul 10, 2003
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiPostReturnI;
import com.sap.mw.jco.JCO;

/**
 * @author knadeem
 */
public class JcoBapiPostReturn extends JcoBapiFunction implements BapiPostReturnI {
		
	private JCO.Table returnValuesIn;	
	
	public JcoBapiPostReturn() {
		//super("ZBAPI_POST_RETURNS_REDELIVERY");
		super("Z_BAPI_POST_RETURNS_REDELIVERY");
		this.returnValuesIn = this.function.getTableParameterList().getTable("RETURN_VALUES");
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.function.getImportParameterList().setValue(invoiceNumber, "ACCT_DOC");
	}

	public void addAffiliateCharges(AffiliateCharges affiliateCharges) {
		
		String aff = affiliateCharges.getAffiliateCode();
		
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(aff,	"NAME");
		this.returnValuesIn.setValue("PR00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(affiliateCharges.getSubtotal(), "AMOUNT");

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(aff,	"NAME");
		this.returnValuesIn.setValue("ZT00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(affiliateCharges.getTax(), "AMOUNT");

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(aff,	"NAME");
		this.returnValuesIn.setValue("ZD00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(affiliateCharges.getDeposit(), "AMOUNT");

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(aff,	"NAME");
		this.returnValuesIn.setValue("ZR00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(affiliateCharges.getRestockingFee(), "AMOUNT");

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(aff,	"NAME");
		this.returnValuesIn.setValue("ZC00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(affiliateCharges.getAppliedCredit(), "AMOUNT");

	}

	public void addPromotionAmount(SapPromotion sapPromo) {
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(sapPromo.getSapPromoCode(),	"NAME");
		this.returnValuesIn.setValue("ZP00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(sapPromo.getAmount(), "AMOUNT");
	}

	public void setDeliveryCharge(double dlvCharge) {
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(" ",	"NAME");
		this.returnValuesIn.setValue("ZF00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(dlvCharge, "AMOUNT");
	}

	public void setPhoneCharge(double phoneCharge) {
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue(" ",	"NAME");
		this.returnValuesIn.setValue("ZH00", "AMOUNT_TYPE");
		this.returnValuesIn.setValue(phoneCharge, "AMOUNT");
	}

	/*****************
	public void addAffiliateCharges(AffiliateCharges affiliateCharges) {
		ParameterList p = this.function.getImportParameterList();
		String aff = affiliateCharges.getAffiliateCode();
		p.setValue(affiliateCharges.getSubtotal(), aff + "_ORDERLINE_AMOUNT");	
		p.setValue(affiliateCharges.getTax(), aff+"_TAX_AMOUNT");
		p.setValue(affiliateCharges.getDeposit(), aff+"_BOTTLE_DEPOSIT_AMOUNT");
		p.setValue(affiliateCharges.getRestockingFee(), aff+"_RESTOCKING_FEE");
		p.setValue(affiliateCharges.getAppliedCredit(), "CREDIT_MEMOS_"+aff);

	}

	public void setPromotionAmount(double promotionAmount) {
		this.function.getImportParameterList().setValue(promotionAmount, "SIGNUP_PROMOTION_AMOUNT");
	}

	public void setDeliveryCharge(double dlvCharge) {
		this.function.getImportParameterList().setValue(dlvCharge, "DELIVERY_CHARGE");
	}

	public void setPhoneCharge(double phoneCharge) {
		this.function.getImportParameterList().setValue(phoneCharge, "PHONE_CHARGE");
	}
	******************************/
	
}
