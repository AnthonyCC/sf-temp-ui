/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer.adapter;

import com.freshdirect.fdstore.customer.*;
import com.freshdirect.customer.*;
import com.freshdirect.payment.EnumPaymentMethodType;

import java.util.Date;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDOrderInfoAdapter implements FDOrderInfoI {
	private static final long serialVersionUID = -4023909427656438190L;

	protected final ErpSaleInfo saleInfo;

	public FDOrderInfoAdapter(ErpSaleInfo saleInfo) {
		this.saleInfo = saleInfo;
	}

	public String getErpSalesId() {
		return this.saleInfo.getSaleId();
	}

    public EnumSaleStatus getOrderStatus() {
        return saleInfo.getStatus();
    }

	public Date getRequestedDate() {
		return this.saleInfo.getRequestedDate();
	}

	public double getTotal() {
		return this.saleInfo.getAmount();
	}

	public EnumTransactionSource getOrderSource() {
		return this.saleInfo.getSource();
	}
    
    public boolean isPending() {
        return this.getOrderStatus().isPending();
    }
    
    public Date getDeliveryStartTime() {
        return this.saleInfo.getDeliveryStartTime();
    }
    
    public Date getDeliveryEndTime() {
        return this.saleInfo.getDeliveryEndTime();
    }

	public Date getDeliveryCutoffTime() {
		return this.saleInfo.getDeliveryCutoffTime();
	}

	public EnumDeliveryType getDeliveryType() {
		return this.saleInfo.getDeliveryType();
	}

   
	public Date getCreateDate() {
		return this.saleInfo.getCreateDate();
	}
    
	public String getCreatedBy() {
		return this.saleInfo.getCreatedBy();
	}
	
	public EnumTransactionSource getModificationSource() {
		return this.saleInfo.getModificationSource();
	}
    
	public Date getModificationDate() {
		return this.saleInfo.getModificationDate();
	}
    
	public String getModifiedBy() {
		return this.saleInfo.getModifiedBy();
	}
	
	public double getPendingCreditAmount() {
		return this.saleInfo.getPendingCreditAmount();
	}
    
	public double getApprovedCreditAmount() {
		return this.saleInfo.getApprovedCreditAmount();
	}
    
	public EnumSaleStatus getSaleStatus() {
		return this.saleInfo.getStatus();
	}
    
	public EnumPaymentMethodType getPaymentMethodType() {
		return this.saleInfo.getPaymentMethodType();
	}
	
	public boolean isDlvPassApplied() {
		return ((this.saleInfo.getDlvPassId() != null) ? true : false);
	}
	
	public String getDlvPassId(){
		return this.saleInfo.getDlvPassId();
	}

	public EnumSaleType getSaleType() {
		return this.saleInfo.getSaleType();
	}

	// can be null
	public String getStopSequence() {
		return this.saleInfo.getTruckNumber();
	}

	// can be null
	public String getTruckNumber() {
		return this.saleInfo.getStopSequence();
	}
}
