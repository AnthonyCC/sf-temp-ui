package com.freshdirect.referral.extole;

public enum EnumRafTransactionType {

	purchase, 
	approve;

	public static EnumRafTransactionType getEnum(String name) {
		return EnumRafTransactionType.valueOf(name);
	}

}
