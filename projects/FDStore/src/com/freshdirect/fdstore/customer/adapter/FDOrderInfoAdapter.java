package com.freshdirect.fdstore.customer.adapter;

import java.util.Date;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.payment.EnumPaymentMethodType;


public class FDOrderInfoAdapter implements FDOrderInfoI {
	
	private static final long serialVersionUID = -4023909427656438190L;

	protected final ErpSaleInfo saleInfo;

	// Set deliveryIssueTypes;
	
	public FDOrderInfoAdapter(ErpSaleInfo saleInfo) {
		this.saleInfo = saleInfo;
	}

	@Override
	public String getErpSalesId() {
		return this.saleInfo.getSaleId();
	}

	@Override
    public EnumSaleStatus getOrderStatus() {
        return this.saleInfo.getStatus();
    }

	@Override
	public Date getRequestedDate() {
		return this.saleInfo.getRequestedDate();
	}

	@Override
	public double getTotal() {
		return this.saleInfo.getAmount();
	}

	@Override
	public EnumTransactionSource getOrderSource() {
		return this.saleInfo.getSource();
	}
    
	@Override
    public boolean isPending() {
        return this.getOrderStatus().isPending();
    }
    
	@Override
    public Date getDeliveryStartTime() {
        return this.saleInfo.getDeliveryStartTime();
    }
    
	@Override
    public Date getDeliveryEndTime() {
        return this.saleInfo.getDeliveryEndTime();
    }

	@Override
	public Date getDeliveryCutoffTime() {
		return this.saleInfo.getDeliveryCutoffTime();
	}

	@Override
	public EnumDeliveryType getDeliveryType() {
		return this.saleInfo.getDeliveryType();
	}
   
	@Override
	public Date getCreateDate() {
		return this.saleInfo.getCreateDate();
	}
    
	@Override
	public String getCreatedBy() {
		return this.saleInfo.getCreatedBy();
	}
	
	@Override
	public EnumTransactionSource getModificationSource() {
		return this.saleInfo.getModificationSource();
	}
    
	@Override
	public Date getModificationDate() {
		return this.saleInfo.getModificationDate();
	}
    
	@Override
	public String getModifiedBy() {
		return this.saleInfo.getModifiedBy();
	}
	
	@Override
	public double getPendingCreditAmount() {
		return this.saleInfo.getPendingCreditAmount();
	}
    
	@Override
	public double getApprovedCreditAmount() {
		return this.saleInfo.getApprovedCreditAmount();
	}
    
	@Deprecated
	@Override
	public EnumSaleStatus getSaleStatus() {
		return this.saleInfo.getStatus();
	}
    
	@Override
	public EnumPaymentMethodType getPaymentMethodType() {
		return this.saleInfo.getPaymentMethodType();
	}
	
	@Override
	public boolean isDlvPassApplied() {
		return ((this.saleInfo.getDlvPassId() != null) ? true : false);
	}
	
	@Override
	public String getDlvPassId(){
		return this.saleInfo.getDlvPassId();
	}

	@Override
	public EnumSaleType getSaleType() {
		return this.saleInfo.getSaleType();
	}

	// can be null
	@Override
	public String getTruckNumber() {
		return this.saleInfo.getTruckNumber();
	}
	
	// can be null
	@Override
	public String getStopSequence() {
		return this.saleInfo.getStopSequence();
	}

	@Override
	public String getStandingOrderId() {
		return this.saleInfo.getStandingOrderId();
	}


	@Override
	public boolean isModifiable() {
		final EnumSaleStatus status = saleInfo.getStatus();
		return (EnumSaleStatus.SUBMITTED.equals(status) ||
				EnumSaleStatus.AUTHORIZED.equals(status) ||
				EnumSaleStatus.AVS_EXCEPTION.equals(status)) &&
				!saleInfo.getSaleType().equals(EnumSaleType.DONATION);
	}

	@Override
	public boolean isShopFromThisOrder() {
		return	!isPending() &&
				!this.getSaleType().equals(EnumSaleType.GIFTCARD) &&
				!this.getSaleType().equals(EnumSaleType.DONATION);
	}
}
