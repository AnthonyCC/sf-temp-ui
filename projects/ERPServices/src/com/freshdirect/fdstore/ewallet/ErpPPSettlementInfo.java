package com.freshdirect.fdstore.ewallet;

import java.io.Serializable;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;

public class ErpPPSettlementInfo extends ErpSettlementInfo {
	
	double amount;
	String txEventCode;
	
	public ErpPPSettlementInfo (String invoiceNumber, ErpAffiliate affiliate) {
		super(invoiceNumber, affiliate);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTxEventCode() {
		return txEventCode;
	}

	public void setTxEventCode(String txEventCode) {
		this.txEventCode = txEventCode;
	}
	
	public String getSettlementType() {
		return "STL";
	}

}
