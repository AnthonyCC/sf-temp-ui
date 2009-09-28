package com.freshdirect.giftcard;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.framework.core.ModelSupport;

public class ErpAppliedGiftCardModel extends ModelSupport {
		private String certificateNum;
		private double amount;
		private ErpAffiliate affiliate;
		private String accountNumber;
		
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		public ErpAffiliate getAffiliate() {
			return affiliate;
		}
		public void setAffiliate(ErpAffiliate affiliate) {
			this.affiliate = affiliate;
		}
		public String getCertificateNum() {
			return certificateNum;
		}
		public void setCertificateNum(String certificateNum) {
			this.certificateNum = certificateNum;
		}
		public String getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}
}
