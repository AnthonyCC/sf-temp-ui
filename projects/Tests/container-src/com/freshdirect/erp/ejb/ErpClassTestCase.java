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
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * Test case for ErpClass
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpClassTestCase extends TestCase {

	protected Context ctx;
	protected ErpClassHome home;
	
    public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpClassTestCase("testBasicEJB"));
	}
    
	public ErpClassTestCase(String testName) {
		super(testName);
	}

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (ErpClassHome) ctx.lookup("freshdirect.erp.Class");
	}
	
	protected void tearDown() throws NamingException {
		ctx.close();
	}

	public static ErpClassModel createClassModel(String sapId) {
		List charValueList1 = new LinkedList();
		charValueList1.add( new ErpCharacteristicValueModel("TST_CV_1", "Test CharValue One") );
		charValueList1.add( new ErpCharacteristicValueModel("TST_CV_2", "Test CharValue Two") );
        
        List charValueList2 = new LinkedList();
		charValueList2.add( new ErpCharacteristicValueModel("TST_CV_3", "Test CharValue Three") );
		charValueList2.add( new ErpCharacteristicValueModel("TST_CV_4", "Test CharValue Four") );
        charValueList2.add( new ErpCharacteristicValueModel("TST_CV_5", "Test CharValue Five") );

		List charList = new LinkedList();
		charList.add( new ErpCharacteristicModel("TST_CHR_A", charValueList1) );
		charList.add( new ErpCharacteristicModel("TST_CHR_B", charValueList2) );

		ErpClassModel model = new ErpClassModel(sapId, charList);

		return model;		
	}

	/**
	 * Test basic EJB functionality, eg. create, find, getModel, remove
	 */
	public void testBasicEJB() throws RemoteException, CreateException, RemoveException, FinderException {
		// create model in memory
		String sapId = "test"+(int)(Math.random()*1000.0);

		// create entity
		ErpClassEB eb = home.create(123, createClassModel(sapId) );
        
		VersionedPrimaryKey pk = (VersionedPrimaryKey) eb.getPrimaryKey();
		assertEquals("PK version", 123, pk.getVersion());
        
        // invalidate
        try {
            eb.invalidate();
            fail("Could not invalidate entity instance");
        } catch (EJBException ejbe) {
        } catch (RemoteException re) {
        }

		// find entity
		eb = home.findByPrimaryKey(pk);

		// getModel
		ErpClassModel model = (ErpClassModel) eb.getModel();
		
		// test ErpClass
		assertEquals("SAP ID", sapId, model.getSapId());
		assertEquals("Number of characteristics", 2, model.numberOfCharacteristics());
	
		List charList = model.getCharacteristics();
		assertEquals("Characteristics size", 2, charList.size());

		// test characteristics
		ErpCharacteristicModel charModel = (ErpCharacteristicModel) model.getCharacteristic("TST_CHR_A");
		assertEquals("Characteristic Name 1", "TST_CHR_A", charModel.getName() );
		
		charModel = (ErpCharacteristicModel) model.getCharacteristic("TST_CHR_B");
		assertEquals("Characteristic Name 2", "TST_CHR_B", charModel.getName() );
		assertEquals("Number of Char Values", 3, charModel.numberOfCharacteristicValues() );

		// test characteristic values
		List charValueList = charModel.getCharacteristicValues();
		assertEquals("Characteristic values size", 3, charValueList.size());
		
		ErpCharacteristicValueModel cvModel = (ErpCharacteristicValueModel) charModel.getCharacteristicValue("TST_CV_3");
		assertEquals("Char Value 3 name", "TST_CV_3", cvModel.getName() );
		assertEquals("Char Value 3 description", "Test CharValue Three", cvModel.getDescription() );

		cvModel = (ErpCharacteristicValueModel) charModel.getCharacteristicValue("TST_CV_4");
		assertEquals("Char Value 4 name", "TST_CV_4", cvModel.getName() );
		assertEquals("Char Value 4 description", "Test CharValue Four", cvModel.getDescription() );
		
		// remove
		eb.remove();
		try {
			eb = home.findByPrimaryKey(pk);
			fail("Removed entity found");
		} catch (FinderException ex) {}
	}

}
