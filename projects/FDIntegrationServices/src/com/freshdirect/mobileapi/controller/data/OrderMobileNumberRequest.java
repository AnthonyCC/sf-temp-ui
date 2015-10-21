package com.freshdirect.mobileapi.controller.data;


import com.freshdirect.mobileapi.controller.data.Message;

public class OrderMobileNumberRequest extends Message{
	
	private String mobile_number;
	private String marketing_sms;
	public String getMarketing_sms() {
		return marketing_sms;
	}

	public void setMarketing_sms(String marketing_sms) {
		this.marketing_sms = marketing_sms;
	}

	public String getNon_marketing_sms() {
		return non_marketing_sms;
	}

	public void setNon_marketing_sms(String non_marketing_sms) {
		this.non_marketing_sms = non_marketing_sms;
	}

	private String non_marketing_sms;
	

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

}


