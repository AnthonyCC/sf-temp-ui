package com.freshdirect.deliverypass;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enum.Enum;

public class EnumDPAutoRenewalType  implements java.io.Serializable {
	
	public static final EnumDPAutoRenewalType YES = new EnumDPAutoRenewalType("Y");
	public static final EnumDPAutoRenewalType NO = new EnumDPAutoRenewalType("N");
	public static final EnumDPAutoRenewalType NONE = new EnumDPAutoRenewalType("");
	private String value=""; 

	private EnumDPAutoRenewalType(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}
}

