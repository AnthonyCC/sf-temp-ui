/*
 * $Workfile: PricingConfiguration.java$
 *
 * $Date: 8/21/2001 7:52:14 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.Map;

/**
 * Contains the relevant configuration information that is needed to make a product selection.
 *
 * @version $Revision:$
 * @author $Author:$
 */
public class FDConfiguredProduct implements FDConfigurableI {

	private final FDSku sku;

	private final FDConfigurableI configuration;

	/**
	 *
	 * @param quantity Purchased quantity, in sales unit
	 * @param salesUnit Selected sales unit
	 * @param options Map of selected options (characteristic name / char.value name pairs)
	 */
	public FDConfiguredProduct(FDSku sku, double quantity, String salesUnit, Map options) {
		this(sku, new FDConfiguration(quantity, salesUnit, options));
	}

	public FDConfiguredProduct(FDSku sku, FDConfigurableI configuration) {
		this.sku = sku;
		this.configuration = configuration;
	}

	public FDSku getSku() {
		return this.sku;
	}

	public FDConfigurableI getConfiguration() {
		return this.configuration;
	}

	public double getQuantity() {
		return this.configuration.getQuantity();
	}

	public String getSalesUnit() {
		return this.configuration.getSalesUnit();
	}

	public Map getOptions() {
		return this.configuration.getOptions();
	}

	public String toString() {
		return "FDConfiguredProduct[" + sku + ", " + configuration + "]";
	}

	public int hashCode() {
		return this.sku.hashCode() ^ this.configuration.hashCode();
	}
	public boolean equals(Object o){
		if (o==this) {
			return true;
		}
		if (o instanceof FDConfiguredProduct) {
			FDConfiguredProduct cp = (FDConfiguredProduct)o;
			return this.sku.equals(cp.sku) && this.configuration.equals(cp.configuration);
		}
		return false;
	}

}