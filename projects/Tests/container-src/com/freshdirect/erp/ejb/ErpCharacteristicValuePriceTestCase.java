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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * Test case for ErpMaterial
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpCharacteristicValuePriceTestCase extends TestCase {

	protected Context ctx;
    protected ErpClassHome classHome;
    protected ErpMaterialHome matlHome;
	protected ErpCharacteristicValuePriceHome cvpHome;
    
    protected static ErpClassEB erpClass;
    protected static ErpMaterialEB erpMatl;
    
    protected int version;
    protected static String sapId;
    protected static String cvId;
    protected static String condType;
    protected static String pricingUnit;
    protected static double price;
    
    public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpCharacteristicValuePriceTestCase("testBasicEJB"));
	}
	
	public ErpCharacteristicValuePriceTestCase(String testName) {
		super(testName);
	}

	protected void setUp() throws NamingException, CreateException, RemoteException {
        
        // home interfaces
		ctx = TestUtil.getInitialContext();
        classHome = (ErpClassHome) ctx.lookup("freshdirect.erp.Class");
		matlHome = (ErpMaterialHome) ctx.lookup("freshdirect.erp.Material");
        cvpHome = (ErpCharacteristicValuePriceHome) ctx.lookup("freshdirect.erp.CharacteristicValuePrice");
        
        // version number to be used for all objects this test creates
        version = 123;
        // other set up for this material
        condType = "PR00";
        pricingUnit = "LB";
        price = 0.50;
        
        // create the class entity
        erpClass = classHome.create(version, ErpClassTestCase.createClassModel("TEST_CLASS_1"));
        
        // create the material entity
        LinkedList classList = new LinkedList();
        classList.add(erpClass.getModel());
        erpMatl = matlHome.create(version, ErpMaterialTestCase.createMaterialModel("0000283746238", classList));
        
         
        
	}
	
	protected void tearDown() throws NamingException, RemoveException, RemoteException {
        
        // get rid of the material entities
        erpMatl.remove();
        erpClass.remove();
		ctx.close();
	}

	public static ErpCharacteristicValuePriceModel createCharacteristicValuePriceModel(String materialId, String charValueId, String sapId) {

		ErpCharacteristicValuePriceModel model = new ErpCharacteristicValuePriceModel(materialId, charValueId, sapId, price, pricingUnit, condType);

		return model;		
	}

	/**
	 * Test basic EJB functionality, eg. create, find, getModel, remove
	 */
	public void testBasicEJB() throws Exception{
	    try{
	    
		// create a dummy cvp sap id to use for this test
		String sapId = "test"+(int)(Math.random()*1000.0);

		// get some info from the material
		ErpMaterialModel matlModel = (ErpMaterialModel) erpMatl.getModel();
		String matlId = matlModel.getPK().getId();
		ErpCharacteristicModel charac = (ErpCharacteristicModel) matlModel.getCharacteristics().get(0);
		ErpCharacteristicValueModel charValue = (ErpCharacteristicValueModel) charac.getCharacteristicValues().get(0);
		String cvId = charValue.getPK().getId();

		// create cvp entity
		ErpCharacteristicValuePriceEB eb = cvpHome.create(version, createCharacteristicValuePriceModel(matlId, cvId, sapId));
		VersionedPrimaryKey pk = (VersionedPrimaryKey) eb.getPrimaryKey();


		// invalidate
		try{
		    eb.invalidate();
		    fail("Could not invalidate entity");
		}catch(EJBException ee){
		}catch(RemoteException re){
		}
		// find entity
		eb = cvpHome.findByPrimaryKey(pk);

		assertEquals("PK version", version, pk.getVersion());

		// getModel
		ErpCharacteristicValuePriceModel model = (ErpCharacteristicValuePriceModel) eb.getModel();

		// test ErpMaterial

		// simple properties
		assertEquals("SAP ID", sapId, model.getSapId());
		assertEquals("Price", price, model.getPrice(), 0.0001);
		assertEquals("Pricing Unit", pricingUnit, model.getPricingUnit());
		assertEquals("Condition Type", condType, model.getConditionType());

		// remove
		eb.remove();
		try{
		    eb = cvpHome.findByPrimaryKey(pk);
		    fail("Removed entity found");
		}catch(ObjectNotFoundException oe){
		}
		
	    }catch(Exception e){
		e.printStackTrace();
		throw e;
	    }
	}

}
