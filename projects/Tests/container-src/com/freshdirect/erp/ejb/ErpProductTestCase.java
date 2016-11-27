/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * Test case for ErpProduct
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpProductTestCase extends TestCase {
    
    protected Context ctx;
    protected ErpClassHome classHome;
    protected ErpMaterialHome matlHome;
    protected ErpProductHome prodHome;
    
    protected ErpClassEB erpClass;
    protected ErpMaterialEB erpMatl;
    protected ErpMaterialModel erpMatlModel;
    
    private final static int VERSION = 123;
    private final static double DEFAULT_PRICE = 12.3;
    private final static String DEFAULT_PRICE_UNIT = "EA";
    protected static String prdUnavailStatus = "TEST";
    protected static Date prdUnavailDate = new Date();
    protected static String prdUnavailReason = "Test Reason";
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new ErpProductTestCase("testBasicEJB"));
    }
    
    public ErpProductTestCase(String testName) {
        super(testName);
    }
    
    protected void setUp() throws NamingException, CreateException, RemoteException {
        // home interfaces
        ctx = TestUtil.getInitialContext();
        classHome = (ErpClassHome) ctx.lookup("freshdirect.erp.Class");
        matlHome = (ErpMaterialHome) ctx.lookup("freshdirect.erp.Material");
        prodHome = (ErpProductHome) ctx.lookup("freshdirect.erp.Product");
        
        // create the class entity
        erpClass = classHome.create(VERSION, ErpClassTestCase.createClassModel("TST_CLS_PRD"));
        
        // create the material entity
        LinkedList classList = new LinkedList();
        classList.add(erpClass.getModel());
        erpMatl = matlHome.create(VERSION, ErpMaterialTestCase.createMaterialModel("0000123456789", classList));
        
        erpMatlModel = (ErpMaterialModel)erpMatl.getModel();
    }
    
    protected void tearDown() throws NamingException, RemoveException, RemoteException {
        
        // get rid of the material entities
        erpMatl.remove();
        erpClass.remove();
        ctx.close();
    }
    
    public static ErpProductModel createProductModel(String skuCode, ErpMaterialModel materialModel ) {
        
        ErpProductModel model = new ErpProductModel(
        	skuCode, DEFAULT_PRICE, DEFAULT_PRICE_UNIT,
        	prdUnavailStatus, prdUnavailDate, prdUnavailReason, new Date(),
			materialModel, new VersionedPrimaryKey[0], new VersionedPrimaryKey[0]);
        
        return model;
    }
    
    /**
     * Test basic EJB functionality, eg. create, find, getModel, remove
     */
    public void testBasicEJB() throws RemoteException, CreateException, RemoveException, FinderException {
        
        String skuCode = "test"+(int)(Math.random()*1000.0);
        // get some info from the material
        /*
        ErpMaterialModel matlModel = (ErpMaterialModel) erpMatl.getModel();
        String matlId = matlModel.getPK().getId();
        ErpCharacteristicModel charac = (ErpCharacteristicModel) matlModel.getCharacteristics().get(0);
        ErpCharacteristicValueModel charValue = (ErpCharacteristicValueModel) charac.getCharacteristicValues().get(0);
        String cvId = charValue.getPK().getId();
         */
        
        // create product entity
        ErpProductEB eb = prodHome.create(VERSION, createProductModel(skuCode, erpMatlModel));
        VersionedPrimaryKey pk = (VersionedPrimaryKey) eb.getPrimaryKey();
        
        // invalidate
        try {
            eb.invalidate();
            fail("Couldn't invalidate entity");
        } catch (EJBException ejbe) {
        } catch (RemoteException re) {
        }
        
        // find entity
        eb = prodHome.findByPrimaryKey(pk);
        
        assertEquals("PK version", VERSION, pk.getVersion());
        
        // getModel
        ErpProductModel model = (ErpProductModel) eb.getModel();
        
        // simple properties
        assertEquals("SKU code", skuCode, model.getSkuCode());
        assertEquals("Default Price", DEFAULT_PRICE, model.getDefaultPrice(), 0.001);
        assertEquals("Default Price UOM", DEFAULT_PRICE_UNIT, model.getDefaultPriceUnit());
        assertEquals("Material unavailablility status", prdUnavailStatus, model.getUnavailabilityStatus());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
        assertEquals("Material unavailability date", sdf.format(prdUnavailDate), sdf.format(model.getUnavailabilityDate()));
        assertEquals("Material unavailability description", prdUnavailReason, model.getUnavailabilityReason());
        
        // remove
        eb.remove();
        try {
            eb = prodHome.findByPrimaryKey(pk);
            fail("Removed entity found");
        } catch (FinderException ex) {}
        
    }
    
}
