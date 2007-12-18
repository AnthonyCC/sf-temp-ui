/*
 * Created on Apr 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.crm;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.payment.EnumPaymentMethodType;
import java.util.Date;

/**
 * @author jng
 *
 */
public class CrmSettlementProblemReportLine {
	private final String saleId;
	private final String customerName;
	private final double amount;
	private final Date deliveryDate;
	private final Date failureDate;
	private final EnumSaleStatus status;
	private final EnumTransactionType transactionType;
	private final EnumPaymentMethodType paymentMethodType;
	
	public CrmSettlementProblemReportLine (String saleId, String customerName, double amount, Date deliveryDate, Date failureDate, 
											EnumSaleStatus status, EnumTransactionType transactionType, EnumPaymentMethodType paymentMethodType) {
		this.saleId = saleId;
		this.customerName = customerName;
		this.amount = amount;
		this.deliveryDate = deliveryDate;
		this.failureDate = failureDate;
		this.status = status;
		this.transactionType = transactionType;
		this.paymentMethodType = paymentMethodType;
		
	}

	public String getSaleId() {
		return saleId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public double getAmount() {
		return amount;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public Date getFailureDate() {
		return failureDate;
	}

	public EnumSaleStatus getStatus() {		
		return status;
	}

	public EnumPaymentMethodType getPaymentMethodType() {		
		return paymentMethodType;
	}
	
	public EnumTransactionType getTransactionType() {		
		return transactionType;
	}
	
}
