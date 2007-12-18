/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap;

import java.io.Serializable;

import com.freshdirect.common.address.*;
import com.freshdirect.common.customer.PaymentMethodI;

/**
 * Representation of a customer.
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SapCustomerI extends Serializable {

	public BasicContactAddressI getShipToAddress();
	public BasicContactAddressI getBillToAddress();
	public PaymentMethodI getPaymentMethod();
	
	public BasicContactAddressI getAlternateAddress();

	public String getSapCustomerNumber();

}

