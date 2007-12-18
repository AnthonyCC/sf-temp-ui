/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import junit.framework.TestCase;

/**
 * Test case for FDFactory
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDFactoryTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new FDFactoryTestCase("testBasic"));
	}
    
	public FDFactoryTestCase(String testName) {
		super(testName);
	}

	/**
	 * Test basic functionality
	 */
	public void testBasic() throws FDResourceException, FDSkuNotFoundException {
		debugStuff( "FRU0004980" );
		debugStuff( "DEL0008275" );
		debugStuff( "SEA0007015" );
		debugStuff( "DEL0008049" );
		debugStuff( "DEL0008178" );
		debugStuff( "FRU0005121" );
		debugStuff( "FRU0005122" );
		debugMoreStuff( "MEA0004667" );
	}

	protected void debugStuff(String sku) throws FDResourceException, FDSkuNotFoundException {
		FDProductInfo productInfo = FDFactory.getProductInfo(sku);
		System.out.println(productInfo);

		FDProduct product = FDFactory.getProduct(sku, productInfo.getVersion());
		System.out.println(product);
		System.out.println(product.getPricing());
	}

	protected void debugMoreStuff(String sku) throws FDResourceException, FDSkuNotFoundException {
		FDProductInfo productInfo = FDFactory.getProductInfo(sku);
		System.out.println(productInfo);

		FDProduct product = FDFactory.getProduct(sku, productInfo.getVersion());
		System.out.println(product);
		System.out.println(product.getPricing());

		FDVariation[] vars = product.getVariations();
		for (int i=0; i<vars.length; i++) {
			FDVariationOption[] varopts = vars[i].getVariationOptions();
			System.out.println("Variation "+vars[i].getName());

			for (int j=0; j<varopts.length; j++) {
				FDVariationOption vo = varopts[j];
				System.out.println(" Variation Option "+vo);
				System.out.println("     isLabelValue "+vo.isLabelValue());
				System.out.println("       isSelected "+vo.isSelected());
			}
		}
	}

}
