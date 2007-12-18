/*
 * $Workfile: PaymentMethodI.java$
 *
 * $Date: 12/14/2001 8:56:42 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.customer;

import java.io.Serializable;

import java.util.Date;

import com.freshdirect.payment.EnumBankAccountType;

/**
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public interface PaymentMethodI extends Serializable {

	public EnumCardType getType();
	public String getNumber();
	public Date getExpiration();
	public String getName();
	public String getAbaRouteNumber();
	public String getBankName();
	public EnumBankAccountType getBankAccountType();

}

