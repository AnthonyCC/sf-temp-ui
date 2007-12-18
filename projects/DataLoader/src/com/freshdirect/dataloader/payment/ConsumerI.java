/*
 * ConsumerI.java
 *
 * Created on December 26, 2001, 8:35 PM
 */

package com.freshdirect.dataloader.payment;

import com.freshdirect.customer.ErpShippingInfo;

/**
 *
 * @author  knadeem
 * @version 
 */
public interface ConsumerI {
	
	public void consume(Object o, String webOrderNum, String billingType, ErpShippingInfo shippingInfo);

}

