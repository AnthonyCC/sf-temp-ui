package com.freshdirect.giftcard;

import java.io.Serializable;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;

public class ErpGCSettlementInfo extends ErpSettlementInfo{
	
	double amount;
	boolean postAuthFailed;
	
	public ErpGCSettlementInfo (String invoiceNumber, ErpAffiliate affiliate) {
		super(invoiceNumber, affiliate);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isPostAuthFailed() {
		return postAuthFailed;
	}

	public void setPostAuthFailed(boolean postAuthFailed) {
		this.postAuthFailed = postAuthFailed;
	}
	

}
