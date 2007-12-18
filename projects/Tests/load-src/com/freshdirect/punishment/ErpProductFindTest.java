/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.punishment;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.extensions.ActiveTestSuite;
import junit.extensions.RepeatedTest;
import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.erp.ejb.ErpProductEB;
import com.freshdirect.erp.ejb.ErpProductHome;
import com.freshdirect.erp.model.ErpProductModel;

/**
 * TestSuite that repeatedly grabs EprProductModels from the ERP layer from multiple threads simultaneously.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpProductFindTest extends TestCase {
    
    public static void main(String[] args) {
        int threads = 12;
        int requestsPerThread = 1000;
        
        ActiveTestSuite suite = new ActiveTestSuite();
        for (int i=0;i<threads;i++) {
            suite.addTest(new RepeatedTest(new ErpProductFindTest("testFindBySku"), requestsPerThread));
        }
        
        junit.textui.TestRunner.run(suite);
    }
    
    public static List skus = new ArrayList();
    protected static ErpProductHome prdHome;
    
    public ErpProductFindTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws java.sql.SQLException, NamingException, RemoteException {
        if (skus.size() == 0) {
            synchronized (skus) {
                if (skus.size() == 0) {
                	skus.addAll( PunishmentUtil.getRandomSKUList() );
			        //
			        // find home
			        //
			        Context ctx = TestUtil.getInitialContext();
			        prdHome = (ErpProductHome) ctx.lookup("freshdirect.erp.Product");
			        ctx.close();
                }
            }
        }
    }
    
    
    public void testFindBySku() throws RemoteException {
        int idx = (int) (skus.size() * Math.random());
        String skuCode = (String) skus.get(idx);
        try {
	        ErpProductEB erpPrdEB = prdHome.findBySkuCode(skuCode);
	        ErpProductModel erpPrdModel = (ErpProductModel) erpPrdEB.getModel();
	        assertEquals("Remote Sku code", skuCode, erpPrdModel.getSkuCode());
	        assertEquals("Sku code", skuCode, erpPrdModel.getSkuCode());
        } catch (FinderException fe) {
			System.out.print("_");
		}
    }
    
    
}