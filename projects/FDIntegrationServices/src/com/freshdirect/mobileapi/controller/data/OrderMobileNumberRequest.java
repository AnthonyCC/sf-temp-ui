package com.freshdirect.mobileapi.controller.data;


import com.freshdirect.mobileapi.controller.data.Message;

public class OrderMobileNumberRequest extends Message{
	
	private String mobile_number;

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

}


