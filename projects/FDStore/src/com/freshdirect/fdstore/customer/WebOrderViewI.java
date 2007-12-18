/*
 * Created on May 3, 2003
 */
package com.freshdirect.fdstore.customer;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;

/**
 * @author knadeem
 */
public interface WebOrderViewI {
	
	public ErpAffiliate getAffiliate();
	
	public List getOrderLines();
	public List getSampleLines();

	public boolean isEstimatedPrice();
	
	public boolean isDisplayDepartment();
	public double getTax();
	public double getDepositValue();
	public double getSubtotal();
	public String getDescription();

}
