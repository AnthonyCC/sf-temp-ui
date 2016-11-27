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
import java.util.Collection;
import java.util.List;

import junit.extensions.ActiveTestSuite;
import junit.extensions.RepeatedTest;
import junit.framework.TestCase;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.load.LoadUtil;

/**
 * TestSuite that repeatedly grabs 50 SKUs using FDFactory.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDFactoryProductInfosTest extends TestCase {
    
    private final static int GRAB_SIZE=50;
    
    public static void main(String[] args) {
        int threads = 10;
        int requestsPerThread = 20;
        
        ActiveTestSuite suite = new ActiveTestSuite();
        for (int i=0;i<threads;i++) {
            suite.addTest(new RepeatedTest(new FDFactoryProductInfosTest("testGetProductInfos"), requestsPerThread));
        }
        
        junit.textui.TestRunner.run(suite);
    }

	private List skus = new ArrayList();
    
    public FDFactoryProductInfosTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws java.sql.SQLException {
        if (skus.size() == 0) {
            synchronized (skus) {
                if (skus.size() == 0) {
                	skus.addAll( LoadUtil.getRandomSKUList() );
                }
            }
        }
    }
    
    public void testGetProductInfos() throws FDResourceException {
        int idx = (int) ((skus.size()-GRAB_SIZE) * Math.random());
        String[] skuCodes = (String[]) skus.subList(idx, idx+GRAB_SIZE).toArray(new String[0]);
		Collection fdInfos = FDCachedFactory.getProductInfos( skuCodes );
    }
    
}