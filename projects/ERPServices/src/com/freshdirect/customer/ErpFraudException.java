/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for fraud-related errors: fraud rule violations.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpFraudException extends ExceptionSupport {

	private final EnumFraudReason fraudReason;

	/**
	 * Default constructor.
	 */
	public ErpFraudException(EnumFraudReason fraudReason) {
		super();
		this.fraudReason = fraudReason;
	}

	public EnumFraudReason getFraudReason() {
		return this.fraudReason;
	}

}
