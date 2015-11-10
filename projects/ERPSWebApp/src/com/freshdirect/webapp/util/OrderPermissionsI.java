/**
 * 
 * OrderPermissionsI.java
 * Created Nov 27, 2002
 */
package com.freshdirect.webapp.util;

import com.freshdirect.customer.EnumSaleStatus;

/**
 *
 *  @author knadeem
 */

public interface OrderPermissionsI {
	
	public boolean allowNewCharges();
	public boolean allowComplaint();
	public boolean allowCancelOrder();
	public boolean allowModifyOrder();
	public boolean allowReturnOrder();
	public boolean hasPaymentException();
	public boolean allowResubmitOrder();
	public boolean isRefusedOrder();
	public boolean isPNAOrder();
	public boolean isAuthFailedOrder();
	public boolean isAddOnOrder();

}
