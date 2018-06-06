/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RegistrationResult implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1793702353434813275L;
	private final FDIdentity identity;
	@JsonProperty("foundFraud")
	private final boolean foundFraud;

	public RegistrationResult(FDIdentity id) {
		this(id, false);
	}
	
	public RegistrationResult(@JsonProperty("identity") FDIdentity id, @JsonProperty("foundFraud") boolean b) {
		super();
		this.identity = id;
		this.foundFraud = b;
	}

	public FDIdentity getIdentity() {
		return this.identity;
	}

	public boolean hasPossibleFraud() {
		return this.foundFraud;
	}

}