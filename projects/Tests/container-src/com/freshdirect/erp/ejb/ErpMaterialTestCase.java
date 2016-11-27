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
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * Test case for ErpMaterial
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpMaterialTestCase extends TestCase {
    
    protected Context ctx;
    protected ErpMaterialHome matlHome;
    protected ErpClassHome classHome;
    
    protected static ErpClassEB class1;
    protected static ErpClassEB class2;
    
    protected int version;
    protected static String matlBaseUnit = "LB";
    protected static String matlDescription = "Test Material Description";
	protected static EnumATPRule atpRule = EnumATPRule.JIT;
	protected static int leadTime = 3;
	protected static String upc="1234567890123";
    protected static String matlQuantChar = "X_TEST_QTY";
    protected static String matlSalesUnitChar = "SS1";
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new ErpMaterialTestCase("testBasicEJB"));
    }
    
    public ErpMaterialTestCase(String testName) {
        super(testName);
    }
    
    protected void setUp() throws NamingException, CreateException, RemoteException {
        
        // home interfaces
        ctx = TestUtil.getInitialContext();
        matlHome = (ErpMaterialHome) ctx.lookup("freshdirect.erp.Material");
        classHome = (ErpClassHome) ctx.lookup("freshdirect.erp.Class");
        
        // version number to be used for all objects this test creates
        version = 123;
        
        // create the class entities for this material first
        class1 = classHome.create(version, ErpClassTestCase.createClassModel("TEST_CLASS_1"));
        class2 = classHome.create(version, ErpClassTestCase.createClassModel("TEST_CLASS_2"));
        
    }
    
    protected void tearDown() {
        try {
            // get rid of the class entities
            class1.remove();
            class2.remove();
            ctx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static ErpMaterialModel createMaterialModel(String sapId) throws RemoteException {
        
        List matlPriceList = new LinkedList();
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 15.99, "EA",  0.0, "XXX") );
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 14.99, "EA",  5.0, "XXX") );
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 13.99, "EA", 10.0, "XXX") );
        
        List salesUnitList = new LinkedList();
        salesUnitList.add( new ErpSalesUnitModel("SS1", "EA", 1, 1, "Test Sales Unit 1") );
        salesUnitList.add( new ErpSalesUnitModel("SS4", "EA", 1, 1, "Test Sales Unit 2") );
        
        List classList = new LinkedList();
        classList.add(class1.getModel());
        classList.add(class2.getModel());
  
        ErpMaterialModel model = new ErpMaterialModel(
	        sapId, matlBaseUnit, matlDescription,
	        atpRule, leadTime, upc,
        	matlQuantChar, matlSalesUnitChar,
        	EnumAlcoholicContent.NONE, false, false, false, 
        	matlPriceList, salesUnitList, classList);
        
        return model;
    }
    
    public static ErpMaterialModel createMaterialModel(String sapId, List classList) throws RemoteException {
        
        List matlPriceList = new LinkedList();
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 15.99, "EA",  0.0, "XXX") );
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 14.99, "EA",  5.0, "XXX") );
        matlPriceList.add( new ErpMaterialPriceModel(sapId, 13.99, "EA", 10.0, "XXX") );
        
        List salesUnitList = new LinkedList();
        salesUnitList.add( new ErpSalesUnitModel("SS1", "EA", 1, 1, "Test Sales Unit 1") );
        salesUnitList.add( new ErpSalesUnitModel("SS4", "EA", 1, 1, "Test Sales Unit 2") );
        
        ErpMaterialModel model = new ErpMaterialModel(
	        sapId, matlBaseUnit, matlDescription,
	        atpRule, leadTime, upc,
	        matlQuantChar, matlSalesUnitChar,
			EnumAlcoholicContent.NONE, false, false, false,
	        matlPriceList, salesUnitList, classList);
        
        return model;
    }
    
    /**
     * Test basic EJB functionality, eg. create, find, getModel, remove
     */
    public void testBasicEJB() throws RemoteException, EJBException, CreateException, RemoveException, FinderException {
        
        // create a dummy material number to use for this test
        String sapId = "test"+(int)(Math.random()*1000.0);
        
        // create material entity
        ErpMaterialEB eb = matlHome.create(123, createMaterialModel(sapId));
        
        VersionedPrimaryKey pk = (VersionedPrimaryKey) eb.getPrimaryKey();
        assertEquals("PK version", version, pk.getVersion());
        
        // invalidate
        try {
            eb.invalidate();
            System.out.println("Hah!!!");
            fail("Could not invalidate bean");
        } catch (EJBException ejbe) {
        } catch (RemoteException re) {
        }
        
        
        // find entity
        eb = matlHome.findByPrimaryKey(pk);
        
        // getModel
        ErpMaterialModel model = (ErpMaterialModel) eb.getModel();
        
        // test ErpMaterial
        
        // simple properties
        assertEquals("SAP ID", sapId, model.getSapId());
        assertEquals("Material base unit", matlBaseUnit, model.getBaseUnit());
        assertEquals("Material description", matlDescription, model.getDescription());
        
        assertEquals("Material Lead time", leadTime, model.getLeadTime());
        assertTrue("ATP rule", atpRule.equals(model.getATPRule()) );
        assertEquals("UPC code", upc, model.getUPC());
        
        assertEquals("Material quantity characteristic", matlQuantChar, model.getQuantityCharacteristic());
        assertEquals("Material sales unit characteristic", matlSalesUnitChar, model.getSalesUnitCharacteristic());
        
        // composite properties
        assertEquals("Number of prices", 3, model.numberOfPrices());
        assertEquals("Number of sales units", 2, model.numberOfSalesUnits());
        assertEquals("Number of classes", 2, model.numberOfClasses());
        
        // remove
        eb.remove();
        try {
            eb = matlHome.findByPrimaryKey(pk);
            fail("Removed entity found");
        } catch (FinderException ex) {}
        
    }
    
}
