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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.QuickDateFormat;

/**
 * Test case for ErpInventory
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpInventoryTestCase extends TestCase {

	protected Context ctx;
	protected ErpInventoryHome home;
	
    public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpInventoryTestCase("testBasicEJB"));
	}
    
	public ErpInventoryTestCase(String testName) {
		super(testName);
	}

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (ErpInventoryHome) ctx.lookup("freshdirect.erp.Inventory");
	}
	
	protected void tearDown() throws NamingException {
		ctx.close();
	}

	/**
	 * Test basic EJB functionality, eg. create, find, getModel, remove
	 */
	public void testBasicEJB() throws RemoteException, CreateException, RemoveException, FinderException {
		// create model in memory
		List entryList = new LinkedList();
		
		Calendar rollingDate = Calendar.getInstance();
		String[] dates = new String[3];
		
		for (int i=0; i<3; i++) {
			dates[i] = QuickDateFormat.SHORT_DATE_FORMATTER.format(rollingDate);
			ErpInventoryEntryModel invEntry = new ErpInventoryEntryModel(rollingDate.getTime(), 123.123);
			entryList.add(invEntry);
			rollingDate.add(Calendar.DATE, 2);	// skip 2 days
		}
		String sapId = "test"+(int)(Math.random()*1000.0);
		ErpInventoryModel inv = new ErpInventoryModel(sapId, new Date(), entryList);

		// create entity
		ErpInventoryEB invEB = home.create(inv);
        
        // invalidate the entity
        try {
            invEB.invalidate();
            fail("Invalidation did not throw an exception");
        } catch (EJBException ejbe) {
        } catch (RemoteException re) {
        }

		// find entity again
		invEB = home.findByPrimaryKey(new PrimaryKey(sapId));

		// getModel
		inv = (ErpInventoryModel) invEB.getModel();
		
		assertEquals("Material SAP ID", sapId, inv.getSapId());
		entryList = inv.getEntries();
		assertEquals("Number of invability entries", 3, entryList.size() );
		for (int i=0; i<3; i++) {
			ErpInventoryEntryModel entry = (ErpInventoryEntryModel) entryList.get(i);
			assertEquals("Entry date", dates[i], QuickDateFormat.SHORT_DATE_FORMATTER.format( entry.getStartDate() ) );
			assertEquals("Entry quantity", 123.123, entry.getQuantity(), 0.001);
		}

		rollingDate = Calendar.getInstance();
		entryList = new ArrayList();
		for (int i=0; i<3; i++) {
			dates[i] = QuickDateFormat.SHORT_DATE_FORMATTER.format(rollingDate);
			ErpInventoryEntryModel invEntry = new ErpInventoryEntryModel(rollingDate.getTime(), 0.0);
			entryList.add(invEntry);
			rollingDate.add(Calendar.DATE, 2);	// skip 2 days
		}

		assertTrue("setEntries epoch", !invEB.setEntries( new Date(0), entryList ) );
		
		assertTrue("setEntries now", invEB.setEntries( new Date(), entryList ) );

        // invalidate the entity
        try {
            invEB.invalidate();
            fail("Invalidation did not throw an exception");
        } catch (EJBException ejbe) {
        } catch (RemoteException re) {
        }

		// find entity again
		invEB = home.findByPrimaryKey(new PrimaryKey(sapId));
		// getModel
		inv = (ErpInventoryModel) invEB.getModel();

		assertEquals("Material SAP ID", sapId, inv.getSapId());
		entryList = inv.getEntries();
		assertEquals("Number of invability entries", 3, entryList.size() );
		for (int i=0; i<3; i++) {
			ErpInventoryEntryModel entry = (ErpInventoryEntryModel) entryList.get(i);
			assertEquals("Entry date", dates[i], QuickDateFormat.SHORT_DATE_FORMATTER.format( entry.getStartDate() ) );
			assertEquals("Entry quantity", 0.0, entry.getQuantity(), 0.001);
		}


		// remove
		invEB.remove();
		try {
			invEB = home.findByPrimaryKey(new PrimaryKey(sapId));
			fail("Removed entity found");
		} catch (FinderException ex) {}
	}

}
