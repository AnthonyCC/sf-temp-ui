package com.freshdirect.fdstore.rules;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.customer.FDUserI;

public interface FDRuleContextI {
	
	public String getCounty();
	public EnumServiceType getServiceType();
	public boolean isChefsTable();
	public boolean isVip();
	public double getOrderTotal();
	
	public boolean hasProfileAttribute(String attributeName, String attributeValue);
	public String getDepotCode ();
	
	public FDUserI getUser();
	
}
