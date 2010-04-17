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
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;

/**
 * ErpInvoice interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInvoiceModel extends ErpAbstractInvoiceModel {
	
	private static final long	serialVersionUID	= -2686808883359684082L;
	
	private List<ErpAppliedCreditModel> appliedCredits = new ArrayList<ErpAppliedCreditModel>();
	private List<ErpDiscountLineModel> discounts = new ArrayList<ErpDiscountLineModel>();
	private List<ErpAppliedGiftCardModel> appliedGiftCards = new ArrayList<ErpAppliedGiftCardModel>();
	
    public ErpInvoiceModel() {
		super(EnumTransactionType.INVOICE);
    }
    
    public ErpInvoiceModel(EnumTransactionType transType){
    	super(transType);
    }

	
	public double getDiscountAmount(){
		double totalDiscountAmount = 0.0;
		if (this.discounts != null || this.discounts.size() > 0) {
			for (Iterator<ErpDiscountLineModel> iter = this.discounts.iterator(); iter.hasNext();) {
				ErpDiscountLineModel discountLine = iter.next();
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}
			return totalDiscountAmount;
		}
		return totalDiscountAmount; 
	}
	
    public List<ErpAppliedCreditModel> getAppliedCredits() { 
    	return appliedCredits; 
    }
	public void setAppliedCredits(List<ErpAppliedCreditModel> appliedCredits){ 
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
		for(Iterator<ErpAppliedCreditModel> i = this.appliedCredits.iterator(); i.hasNext(); ){
			ErpAppliedCreditModel credit = i.next();
			amt += credit.getAmount();
		}
		return amt;
	}
	
	public double getAppliedCreditAmount(ErpAffiliate affiliate){
		if(this.appliedCredits == null || this.appliedCredits.size() <= 0){
			return 0;
		}
		double amt = 0;
		for(Iterator<ErpAppliedCreditModel> i = this.appliedCredits.iterator(); i.hasNext(); ){
			ErpAppliedCreditModel credit = i.next();
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
    	
    	List<ErpInvoiceLineModel> thisLines = getInvoiceLines(); 
    	Collections.sort(thisLines, INVOICELINE_COMPARATOR);
    	List<ErpInvoiceLineModel> testLines = invoice.getInvoiceLines();
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
    
    public List<ErpDiscountLineModel> getDiscounts() { return this.discounts; }
    public void setDiscounts(List<ErpDiscountLineModel> l) { this.discounts = l; }
    public void addDiscount(ErpDiscountLineModel discount) { this.discounts.add(discount); }

	public ErpDiscountLineModel getDiscount(EnumDiscountType discountType) {
        for (Iterator<ErpDiscountLineModel> i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List<ErpDiscountLineModel> getDiscounts(EnumDiscountType discountType) {
		List<ErpDiscountLineModel> discountList = new ArrayList<ErpDiscountLineModel>();
        for (Iterator<ErpDiscountLineModel> i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
        		discountList.add( curr );
        	}
        }
		return discountList;
	}

	private static final Comparator<ErpInvoiceLineI> INVOICELINE_COMPARATOR = new Comparator<ErpInvoiceLineI>(){
				public int compare( ErpInvoiceLineI line1, ErpInvoiceLineI line2 ) {
					return line1.getOrderLineNumber().compareTo(line2.getOrderLineNumber());
				}
			};

	public List<ErpAppliedGiftCardModel> getAppliedGiftCards() {
		return appliedGiftCards;
	}

	public void setAppliedGiftCards(List<ErpAppliedGiftCardModel> appliedGiftCards) {
		this.appliedGiftCards = appliedGiftCards;
	} 
}

