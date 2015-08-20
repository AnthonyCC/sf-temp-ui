/*
 * Created on Jul 10, 2003
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiPostReturnI;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 * @author knadeem
 */
public class JcoBapiPostReturn extends JcoBapiFunction implements BapiPostReturnI {
		
	private JCoTable returnValuesIn;
	private JCoTable discountDetailsIn;	
	
	public JcoBapiPostReturn() throws JCoException {
		
		super("Z_BAPI_POST_RETURNS_REDELIVERY");
		
		this.returnValuesIn = this.function.getTableParameterList().getTable("RETURN_VALUES");
		this.discountDetailsIn = this.function.getTableParameterList().getTable("DISC_DETAILS");
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.function.getImportParameterList().setValue("ACCT_DOC", invoiceNumber);
	}

	public void addAffiliateCharges(AffiliateCharges affiliateCharges) {
		
		String aff = affiliateCharges.getAffiliateCode();
		
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", aff);
		this.returnValuesIn.setValue("AMOUNT_TYPE", "PR00");
		this.returnValuesIn.setValue("AMOUNT", affiliateCharges.getSubtotal());

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", aff);
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZT00");
		this.returnValuesIn.setValue("AMOUNT", affiliateCharges.getTax());

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", aff);
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZD00");
		this.returnValuesIn.setValue("AMOUNT", affiliateCharges.getDeposit());

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", aff);
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZR00");
		this.returnValuesIn.setValue("AMOUNT", affiliateCharges.getRestockingFee());

		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", aff);
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZC00");
		this.returnValuesIn.setValue("AMOUNT", affiliateCharges.getAppliedCredit());

	}

	public void addPromotionAmount(SapPromotion sapPromo)
	{
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", sapPromo.getSapPromoCode());
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZP00");
		this.returnValuesIn.setValue("AMOUNT", sapPromo.getAmount());
	}

	public void setDeliveryCharge(double dlvCharge)
	{
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", " ");
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZF00");
		this.returnValuesIn.setValue("AMOUNT", dlvCharge);
	}

	public void setPhoneCharge(double phoneCharge)
	{
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", " ");
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZH00");
		this.returnValuesIn.setValue("AMOUNT", phoneCharge);
	}
	
	public void setTipAmount(double amount)
	{
		this.returnValuesIn.appendRow();
		this.returnValuesIn.setValue("NAME", " ");
		this.returnValuesIn.setValue("AMOUNT_TYPE", "ZH00");//::FDX::
		this.returnValuesIn.setValue("AMOUNT", amount);
	}

	
	public void addLineDiscounts(InvoiceLineDiscount lineDiscount)
	{
		this.discountDetailsIn.appendRow();
		this.discountDetailsIn.setValue("TYPE", lineDiscount.getDiscountType());
		this.discountDetailsIn.setValue("LINE_ITEM", lineDiscount.getInvoiceLineNumber());
		this.discountDetailsIn.setValue("COUPON_ID", lineDiscount.getDiscountCode());		
	}

	

	/*****************
	public void addAffiliateCharges(AffiliateCharges affiliateCharges) {
		ParameterList p = this.function.getImportParameterList();
		String aff = affiliateCharges.getAffiliateCode();
		p.setValue(aff + "_ORDERLINE_AMOUNT", affiliateCharges.getSubtotal());	
		p.setValue(aff+"_TAX_AMOUNT", affiliateCharges.getTax());
		p.setValue(aff+"_BOTTLE_DEPOSIT_AMOUNT", affiliateCharges.getDeposit());
		p.setValue(aff+"_RESTOCKING_FEE", affiliateCharges.getRestockingFee());
		p.setValue("CREDIT_MEMOS_"+aff, affiliateCharges.getAppliedCredit());

	}

	public void setPromotionAmount(double promotionAmount) {
		this.function.getImportParameterList().setValue("SIGNUP_PROMOTION_AMOUNT", promotionAmount);
	}

	public void setDeliveryCharge(double dlvCharge) {
		this.function.getImportParameterList().setValue("DELIVERY_CHARGE", dlvCharge);
	}

	public void setPhoneCharge(double phoneCharge) {
		this.function.getImportParameterList().setValue("PHONE_CHARGE", phoneCharge);
	}
	******************************/
	
}
