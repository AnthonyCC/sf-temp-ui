package com.freshdirect.fdstore.customer.adapter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpInvoiceModel;

public class FDInvoiceAdapter implements Serializable {
	
	private ErpInvoiceModel invoice;
	
	public FDInvoiceAdapter(ErpInvoiceModel invoice){
		this.invoice = invoice;
	}
	
	public double getInvoicedTotal(){
		return this.invoice.getAmount();
	}
	
	public double getInvoicedSubTotal(){
        return this.invoice.getSubTotal();
    }
    
    public double getInvoicedTaxValue(){
        return this.invoice.getTax();
    }
    
    public double getInvoicedDepositValue() {
    	return this.invoice.getDepositValue();
    }
    
    public double getActualDiscountValue(){
    	List d = this.invoice.getDiscounts();
		if (d == null || d.isEmpty()) {
			return 0;
		}
		double totalDiscountAmount = 0.0;
		for (Iterator i = d.iterator(); i.hasNext();) {
			ErpDiscountLineModel discountLine = (ErpDiscountLineModel) i.next();
			totalDiscountAmount += discountLine.getDiscount().getAmount();
		}
		return totalDiscountAmount;
    }
    
    /**
     * Retrieves the list of applied credits recorded for a given order.
     * @return read-only Collection of ErpAppliedCreditModel objects.
     */
    public Collection getAppliedCredits() {
        return invoice.getAppliedCredits();
    }

    public double getCustomerCreditsValue(){
        Collection appliedCredits = getAppliedCredits();
        double creditValue = 0;

        for (Iterator i= appliedCredits.iterator(); i.hasNext();) {
            ErpAppliedCreditModel ac = (ErpAppliedCreditModel) i.next();
            creditValue += ac.getAmount();
        }
        return creditValue;
    }
    
    // new Added methods
    public List getInvoiceLines(){
    	return this.invoice.getInvoiceLines();
    }
    
    public ErpInvoiceLineI getInvoiceLine(String lineNumber){
    	return this.invoice.getInvoiceLine(lineNumber);
    }
    
    public List getInvoicedCharges(){
    	return this.invoice.getCharges();
    }
    
    public double getInvoicedDeliveryCharge(){
    	ErpChargeLineModel c = this.getCharge(EnumChargeType.DELIVERY);
    	return c != null ? c.getAmount() : 0.0;
    }
    
	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
		for (Iterator i=this.invoice.getCharges().iterator(); i.hasNext(); ) {
			ErpChargeLineModel curr = (ErpChargeLineModel)i.next();
			if (chargeType.equals( curr.getType() )) {
				return curr;
			}
		}
		return null;
	}

    public List getActualDiscounts(){    	
    	return this.invoice.getDiscounts();
    }
	
}
