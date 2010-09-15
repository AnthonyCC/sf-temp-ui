/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.io.Serializable;

/**
 * Lightweight class representing a SKU code / version pair.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDSku implements Serializable, FDVersion<Integer> {
	private static final long serialVersionUID = 8643436115597269242L;

	/** SKU code */
	private final String skuCode;
	
	/** Business object version */
	private final Integer version;
    
    
    public FDSku(FDSku sku) {
    	this(sku.getSkuCode(), sku.getVersion()); 
    }

	public FDSku(String skuCode, int version) {
		this.skuCode = skuCode;
		this.version = version;
	}
	
	/**
	 * Get SKU code.
	 *
	 * @return SKU code
	 */
	public String getSkuCode() {
		return this.skuCode;
	}

	/**
	 * Get the business object version.
	 *
	 * @return version number
	 */
	public Integer getVersion() {
		return this.version;
	}
	
	public String toString() {
		return "FDSku[" + this.skuCode + ", " + this.version + "]";
	}
	
	public final int hashCode() {
		return this.skuCode.hashCode() ^ this.version;
	}

	public final boolean equals(Object o) {
		if (!(o instanceof FDSku)) return false;
		FDSku sku = (FDSku)o;
		return this.skuCode.equals(sku.skuCode) && (this.version==sku.version);
	}
}