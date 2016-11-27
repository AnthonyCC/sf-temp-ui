package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;

public class ErpSettlementInfo implements Serializable {
	
	private final String invoiceNumber;
	private final ErpAffiliate affiliate;
	private String id;
	private int txCount;
	private boolean splitTransaction;
	private boolean chargeSettlement;
	private boolean settlementFailedAfterSettled;
	private EnumCardType cardType;
	
	public ErpSettlementInfo (String invoiceNumber, ErpAffiliate affiliate) {
		this.invoiceNumber = invoiceNumber;
		this.affiliate = affiliate;
	}
	
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	public ErpAffiliate getAffiliate(){
		return this.affiliate;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setTransactionCount(int txCount) {
		this.txCount = txCount;
	}
	
	public int getTransactionCount(){
		return this.txCount;
	}
	
	public boolean hasSplitTransaction(){
		return this.splitTransaction;
	}
	
	public void setSplitTransaction(boolean splitTransaction) {
		this.splitTransaction = splitTransaction;
	}
	
	public boolean isChargeSettlement() {
		return this.chargeSettlement;
	}
	
	public void setChargeSettlement(boolean chargeSettlement) {
		this.chargeSettlement = chargeSettlement;
	}
	
	public boolean isSettlementFailedAfterSettled(){
		return this.settlementFailedAfterSettled;
	}
	
	public void setsettlementFailedAfterSettled(boolean settlementFailedAfterSettled) {
		this.settlementFailedAfterSettled = settlementFailedAfterSettled;
	}
	
	public EnumCardType getCardType(){
		return this.cardType;
	}
	
	public void setCardType(EnumCardType cardType) {
		this.cardType = cardType;
	}

}
