/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface FDOrderInfoI extends Serializable {

	public String getErpSalesId();

	public EnumSaleStatus getOrderStatus();

	public Date getRequestedDate();

	public double getTotal() throws PricingException;

	public EnumTransactionSource getOrderSource();

	public EnumDeliveryType getDeliveryType();
    
    public boolean isPending();

	public Date getCreateDate();
    
	public String getCreatedBy();
	
	public EnumTransactionSource getModificationSource();
    
	public Date getModificationDate();
    
	public String getModifiedBy();
	
	public double getPendingCreditAmount();
    
	public double getApprovedCreditAmount();
    
	public EnumSaleStatus getSaleStatus();

	public Date getDeliveryStartTime();
    
	public Date getDeliveryEndTime();
    
	public Date getDeliveryCutoffTime();

	public EnumPaymentMethodType getPaymentMethodType();
	
	public boolean isDlvPassApplied(); 
	
	public String getDlvPassId();
}
