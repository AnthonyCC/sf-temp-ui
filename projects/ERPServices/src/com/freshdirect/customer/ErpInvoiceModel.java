/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.framework.util.MathUtil;

/**
 * ErpInvoice interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInvoiceModel extends ErpAbstractInvoiceModel {
	
	private List appliedCredits = new ArrayList();
	private List discounts = new ArrayList();
	private List appliedGiftCards = new ArrayList();
	
    public ErpInvoiceModel() {
		super(EnumTransactionType.INVOICE);
    }
    
    public ErpInvoiceModel(EnumTransactionType transType){
    	super(transType);
    }

	
	public double getDiscountAmount(){
		double totalDiscountAmount = 0.0;
		if (this.discounts != null || this.discounts.size() > 0) {
			for (Iterator iter = this.discounts.iterator(); iter.hasNext();) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}
			return totalDiscountAmount;
		}
		return totalDiscountAmount; 
	}
	
    public List getAppliedCredits() { 
    	return appliedCredits; 
    }
	public void setAppliedCredits(List appliedCredits){ 
		this.appliedCredits = appliedCredits; 
	}
	public void addAppliedCredit(ErpAppliedCreditModel credit){
		this.appliedCredits.add(credit);
	}
	
	public double getAppliedCreditAmount(){
		if(this.appliedCredits == null || this.appliedCredits.size() <= 0){
			return 0;
		}
		double amt = 0;
		for(Iterator i = this.appliedCredits.iterator(); i.hasNext(); ){
			ErpAppliedCreditModel credit = (ErpAppliedCreditModel) i.next();
			amt += credit.getAmount();
		}
		return amt;
	}
	
	public double getAppliedCreditAmount(ErpAffiliate affiliate){
		if(this.appliedCredits == null || this.appliedCredits.size() <= 0){
			return 0;
		}
		double amt = 0;
		for(Iterator i = this.appliedCredits.iterator(); i.hasNext(); ){
			ErpAppliedCreditModel credit = (ErpAppliedCreditModel) i.next();
			if(affiliate.equals(credit.getAffiliate())){
				amt += credit.getAmount();
			}
		}
		return amt;
	}
    
    
    public boolean equals(Object obj){
    	if(!(obj instanceof ErpInvoiceModel)){
    		return false;
    	}
    	ErpInvoiceModel invoice = (ErpInvoiceModel)obj;
    	
    	List thisLines = this.getInvoiceLines(); 
    	Collections.sort(thisLines, INVOICELINE_COMPARATOR);
    	List testLines = invoice.getInvoiceLines();
    	Collections.sort(testLines, INVOICELINE_COMPARATOR);
    	
    	if(thisLines.size() != testLines.size()){
    		return false;
    	}
    	
    	ErpInvoiceLineModel thisLine = null;
		ErpInvoiceLineModel testLine = null;
		for (int i = 0, size = thisLines.size(); i < size; i++) {
			thisLine = (ErpInvoiceLineModel) thisLines.get(i);
			testLine = (ErpInvoiceLineModel) testLines.get(i);
			if (!(thisLine.getMaterialNumber().equals(testLine.getMaterialNumber()) &&
				MathUtil.roundDecimal(thisLine	.getQuantity()) == MathUtil.roundDecimal(testLine.getQuantity()))) {
				return false;
			}
		}
    	
    	return true;
    }
    
    public List getDiscounts() { return this.discounts; }
    public void setDiscounts(List l) { this.discounts = l; }
    public void addDiscount(ErpDiscountLineModel discount) { this.discounts.add(discount); }

	public ErpDiscountLineModel getDiscount(EnumDiscountType discountType) {
        for (Iterator i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = (ErpDiscountLineModel)i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List getDiscounts(EnumDiscountType discountType) {
		List discountList = new ArrayList();
        for (Iterator i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = (ErpDiscountLineModel)i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
        		discountList.add( curr );
        	}
        }
		return discountList;
	}

	private static final Comparator INVOICELINE_COMPARATOR = new Comparator(){
				public int compare(Object o1, Object o2) {
					ErpInvoiceLineModel line1 = (ErpInvoiceLineModel) o1;
					ErpInvoiceLineModel line2 = (ErpInvoiceLineModel) o2;
					return line1.getOrderLineNumber().compareTo(line2.getOrderLineNumber());
				}
			};

	public List getAppliedGiftCards() {
		return appliedGiftCards;
	}

	public void setAppliedGiftCards(List appliedGiftCards) {
		this.appliedGiftCards = appliedGiftCards;
	} 
}

