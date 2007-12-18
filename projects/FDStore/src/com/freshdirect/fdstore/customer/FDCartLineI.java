/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.customer;

import java.util.List;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpReturnLineI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.event.EnumEventSource;


public interface FDCartLineI extends FDProductSelectionI {

	public List buildErpOrderLines(int baseLineNumber) throws FDResourceException, FDInvalidConfigurationException;

	public FDCartLineI createCopy();
		
	public int getErpOrderLineSize();

	public int getRandomId();

	public Discount getDiscount();
	
	/**
	 * Get configured price for this orderline (without promotion).
	 *
	 * @return price in USD
	 */
	public double getPrice();

	/**
	 * Get the value of the promotion applied to this orderline.
	 *
	 * @return promotion value in USD
	 */
	public double getPromotionValue();
	
	/**
	 * Get the tax rate as percentage. 
	 *
	 * @return tax rate as percentage (0.0825 means 8.25%).
	 */
	public double getTaxRate();
	public void setTaxRate(double taxRate);

    /**
	 * Get the value of the tax charged on this orderline.
	 *
	 * @return tax value in USD
	 */
    public double getTaxValue();

	public boolean isSample();
    
	public boolean isEstimatedPrice();
	public boolean hasTax();
	public boolean hasScaledPricing();
	
	public boolean hasDepositValue();
	public double getDepositValue();
	public void setDepositValue(double depositRate);
	
	public boolean hasInvoiceLine();
	public ErpInvoiceLineI getInvoiceLine();

	public boolean hasReturnLine();
	public ErpReturnLineI getReturnLine();
	
	
	public String getOrderedQuantity();
	public String getDeliveredQuantity();
	public String getReturnedQuantity();
	public String getUnitsOfMeasure();
	public String getReturnDisplayQuantity();
	public boolean hasRestockingFee();
	
	public Set getApplicableRestrictions();
	
	public String getOrderLineId();
	public String getOrderLineNumber();
	
	public String getMaterialNumber();
	
	public String getCartlineId();
	
	/**
	 *  Set the source of the event.
	 *  
	 *  @param source the part of the site this event was generated from.
	 */
	public void setSource(EnumEventSource source);
	
	/**
	 *  Get the source of the event.
	 *  
	 *  @return the part of the site this event was generated from.
	 */
	public EnumEventSource getSource();

	/**
	 *  Get the Advance Order flag setting.
	 *  
	 *  @return Advance Order flag setting.
	 */

	public boolean hasAdvanceOrderFlag();

}
