/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */
 
package com.freshdirect.fdstore;

import java.util.Map;


/**
 * ErpConfiguredProductFactory
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public class FDConfiguredProductFactory {
	
	private static FDConfiguredProductFactory instance = null;
	
	
	private FDConfiguredProductFactory() {
		super();
	}
	
	public static FDConfiguredProductFactory getInstance() {
		if (instance == null) {
			instance = new FDConfiguredProductFactory();
		}
		return instance;
	}
	
	/**
	 * @return a product with a specific configuration for a specific version of a sku
	 */
	public FDConfiguredProduct getConfiguration(String skuCode, int version, double quantity, String salesUnit, Map options) throws FDSkuNotFoundException, FDResourceException {
		FDSku sku = new FDSku(FDCachedFactory.getProductInfo(skuCode, version));
		FDConfiguredProduct config = new FDConfiguredProduct(sku, quantity, salesUnit, options);
		validateConfiguration(config);
		return config;
	}

	/**
	 * @return a product with a specific configuration for the latest version of a sku
	 */	
	public FDConfiguredProduct getConfiguration(String skuCode, double quantity, String salesUnit, Map options) throws FDSkuNotFoundException, FDResourceException {
		FDSku sku = new FDSku(FDCachedFactory.getProductInfo(skuCode));
		FDConfiguredProduct config = new FDConfiguredProduct(sku, quantity, salesUnit, options);
		validateConfiguration(config);
		return config;
	}
	
	/**
	 * @return a product with a default configuration for a specific version of a sku
	 */
	public FDConfiguredProductFactory getConfiguration(String skuCode, int version) throws FDSkuNotFoundException, FDResourceException {
		//FDSku sku = FDCachedFactory.getProductInfo(skuCode, version);
		throw new RuntimeException("Operation not yet implemented");
	}
	
	/**
	 * return a product with a default configuration for the latest version of a sku
	 */
	public FDConfiguredProductFactory getConfiguration(String skuCode) throws FDSkuNotFoundException, FDResourceException {
		//FDSku sku = FDCachedFactory.getProductInfo(skuCode);
		throw new RuntimeException("Operation not yet implemented");
	}
	
	private void validateConfiguration(FDConfiguredProduct config) {
		//
		// make sure there isn't any funny business going on...
		//
		
	}

}
