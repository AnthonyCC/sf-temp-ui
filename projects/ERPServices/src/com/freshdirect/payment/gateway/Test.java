package com.freshdirect.payment.gateway;

public class Test {

	static enum ProfileRequest {
		CustomerMerchantID,
		CustomerBin,
		CustomerName;
	}
	public static void main(String[] a) {
		
		for(ProfileRequest c : ProfileRequest.values()) {
			System.out.println(c.name()) ;
		}
	}
}
