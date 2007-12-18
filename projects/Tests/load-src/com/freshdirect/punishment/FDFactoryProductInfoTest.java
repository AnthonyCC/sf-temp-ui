/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.punishment;

import java.util.ArrayList;
import java.util.List;

import junit.extensions.ActiveTestSuite;
import junit.extensions.RepeatedTest;
import junit.framework.TestCase;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

/**
 * TestSuite that repeatedly grabs SKUs using FDFactory.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDFactoryProductInfoTest extends TestCase {
    
    public static void main(String[] args) {
        int threads = 10;
        int requestsPerThread = 1000;
        
        ActiveTestSuite suite = new ActiveTestSuite();
        for (int i=0;i<threads;i++) {
            suite.addTest(new RepeatedTest(new FDFactoryProductInfoTest("testGetProductInfo"), requestsPerThread));
        }
        
        junit.textui.TestRunner.run(suite);
    }

	private List skus = new ArrayList();
    
    public FDFactoryProductInfoTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws java.sql.SQLException {
        if (skus.size() == 0) {
            synchronized (skus) {
                if (skus.size() == 0) {
                	skus.addAll( PunishmentUtil.getRandomSKUList() );
                }
            }
        }
    }
    
    public void testGetProductInfo() throws FDResourceException {
    	try {
	        int idx = (int) (skus.size() * Math.random());
	        String skuCode = (String) skus.get(idx);
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
	        assertEquals("Sku codes", skuCode, productInfo.getSkuCode());
			//FDProduct product = FDFactory.getProduct(skuCode, productInfo.getVersion());
		} catch (FDSkuNotFoundException ex) {
			System.out.print("_");
		}
    }
    
    
}