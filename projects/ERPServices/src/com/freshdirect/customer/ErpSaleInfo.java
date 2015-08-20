/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * Lightweight information about a Sale.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpSaleInfo extends BasicSaleInfo {
	private static final long serialVersionUID = 296882849292695124L;

	private final double amount;
	private final double subTotal;
	private final Date requestedDate;
	private final Date createRequestedDate;
	private final EnumTransactionSource source;
	private final Date deliveryStart;
	private final Date deliveryEnd;
	private final Date cutoffTime;
	private final EnumDeliveryType deliveryType;

	private final Date createDate;
	private final String createBy;
	private final EnumTransactionSource modSource;
	private final Date modDate;
	private final String modBy;
	private final double pendingCreditAmount;
	private final double appliedCreditAmount;
	private final String zone;
	private final EnumPaymentMethodType paymentMethodType;
	private final EnumSaleType saleType;
	private final boolean isMakeGood;



	private Set usedPromotionCodes = Collections.EMPTY_SET;
	//DlvPassId will be not null if delivery pass was applied to this order.
	private String dlvPassId;

	
	private String truckNumber;
	private String stopSequence;
	
	private String standingOrderId;
	private boolean soHolidayMovement;
	
	private final EnumEStoreId eStore;
	private final String plantId;
	private final String salesOrg;
	private final String distributionChanel;
	
	public ErpSaleInfo(
		String saleId,
		String  erpCustomerId,
		EnumSaleStatus status,
		double amount,
		double subTotal,
		Date requestedDate,
		Date createRequestedDate,
		EnumTransactionSource createSource,
		Date createDate,
		String createBy,
		EnumTransactionSource modSource,
		Date modDate,
		String modBy,
		Date deliveryStart,
		Date deliveryEnd,
		Date cutoffTime,
		EnumDeliveryType deliveryType,
		double pendingCreditAmount,
		double appliedCreditAmount,		
		String zone,
		EnumPaymentMethodType paymentMethodType,
		String dlvPassId,
		EnumSaleType saleType,
		String truckNumber,
		String stopSequence,
		boolean isMakeGood,
		String standingOrderId,
		boolean soHolidayMovement,
		EnumEStoreId eStore,
		String plantId,
		String salesOrg,
		String distributionChanel
			) {

		super(saleId, erpCustomerId,status);
		this.amount = amount;
		this.subTotal=subTotal;
		this.requestedDate = requestedDate;
		this.createRequestedDate = createRequestedDate;
		this.source = createSource;
		this.deliveryStart = deliveryStart;
		this.deliveryEnd = deliveryEnd;
		this.cutoffTime = cutoffTime;
		this.deliveryType = deliveryType;

		this.createDate = createDate;
		this.createBy = createBy;
		this.modSource = modSource;
		this.modDate = modDate;
		this.modBy = modBy;
		this.pendingCreditAmount = pendingCreditAmount;
		this.appliedCreditAmount = appliedCreditAmount;
		this.zone = zone;
		this.paymentMethodType = paymentMethodType;
		this.dlvPassId = dlvPassId;
		this.saleType=saleType;
		this.truckNumber = truckNumber;
		this.stopSequence = stopSequence;
		this.isMakeGood=isMakeGood;
		
		this.standingOrderId = standingOrderId;
		this.soHolidayMovement = soHolidayMovement;
		this.eStore=eStore;
		this.plantId=plantId;
		this.salesOrg=salesOrg;
		this.distributionChanel=distributionChanel;
	}

	public EnumEStoreId geteStore() {
		return eStore;
	}

	public String getPlantId() {
		return plantId;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public String getDistributionChanel() {
		return distributionChanel;
	}

	public boolean isMakeGood() {
		return isMakeGood;
	}
	public double getAmount() {
		return this.amount;
	}

	public double getSubTotal() {
		return this.subTotal;
	}
	public Date getRequestedDate() {
		return this.requestedDate;
	}
	public Date getCreateRequestedDate() {
		return this.createRequestedDate;
	}

	public EnumTransactionSource getSource() {
		return this.source;
	}

	public Date getDeliveryStartTime() {
		return this.deliveryStart;
	}

	public Date getDeliveryEndTime() {
		return this.deliveryEnd;
	}

	public Date getDeliveryCutoffTime() {
		return this.cutoffTime;
	}

	public EnumDeliveryType getDeliveryType() {
		return this.deliveryType;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreatedBy() {
		return this.createBy;
	}
	
	public EnumTransactionSource getModificationSource() {
		return this.modSource;
	}

	public Date getModificationDate() {
		return this.modDate;
	}
	
	public String getModifiedBy() {
		return this.modBy;
	}

	public double getPendingCreditAmount() {
		return this.pendingCreditAmount;
	}

	public double getApprovedCreditAmount() {
		return this.appliedCreditAmount;
	}

	public Set getUsedPromotionCodes() {
		return usedPromotionCodes;
	}

	public void setUsedPromotionCodes(Set usedPromotionCodes) {
		this.usedPromotionCodes = usedPromotionCodes;
	}

	public boolean isDelivered() {
		return EnumSaleStatus.PAYMENT_PENDING.equals(this.getStatus()) || EnumSaleStatus.SETTLED.equals(this.getStatus());
	}
	
	public boolean isSettled() {
		return EnumSaleStatus.SETTLED.equals(this.getStatus());
	}
	
	public String getZone() {
		return zone;
	}

	public EnumPaymentMethodType getPaymentMethodType() {
		return this.paymentMethodType;
	}

	public String getDlvPassId() {
		return dlvPassId;
	}
	
	public EnumSaleType getSaleType() {
		return saleType;
	}

	public String getTruckNumber() {
		return truckNumber;
	}

	public String getStopSequence() {
		return stopSequence;
	}
	
	public String getStandingOrderId() {
		return standingOrderId;
	}
	
	public boolean isSoHolidayMovement(){
		return soHolidayMovement;
	}

	@Override
	public String toString() {
		return "ErpSaleInfo [amount=" + amount + ", subTotal=" + subTotal
				+ ", requestedDate=" + requestedDate + ", createRequestedDate="
				+ createRequestedDate + ", source=" + source
				+ ", deliveryStart=" + deliveryStart + ", deliveryEnd="
				+ deliveryEnd + ", cutoffTime=" + cutoffTime
				+ ", deliveryType=" + deliveryType + ", createDate="
				+ createDate + ", createBy=" + createBy + ", modSource="
				+ modSource + ", modDate=" + modDate + ", modBy=" + modBy
				+ ", pendingCreditAmount=" + pendingCreditAmount
				+ ", appliedCreditAmount=" + appliedCreditAmount + ", zone="
				+ zone + ", paymentMethodType=" + paymentMethodType
				+ ", saleType=" + saleType + ", isMakeGood=" + isMakeGood
				+ ", usedPromotionCodes=" + usedPromotionCodes + ", dlvPassId="
				+ dlvPassId + ", truckNumber=" + truckNumber
				+ ", stopSequence=" + stopSequence + ", standingOrderId="
				+ standingOrderId + ", soHolidayMovement=" + soHolidayMovement
				+ ", eStore=" + eStore + ", plantId=" + plantId + ", salesOrg="
				+ salesOrg + ", distributionChanel=" + distributionChanel + "]";
	}
	
	
}
