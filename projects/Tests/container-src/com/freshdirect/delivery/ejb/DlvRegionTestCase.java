/*
 * DlvRegionTestCase.java
 *
 * Created on August 28, 2001, 6:54 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version
 */
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.framework.core.FatPrimaryKey;

public class DlvRegionTestCase extends TestCase{
	
	protected Context ctx;
	
	/** Home interface for the DlvSpecialDateEntity bean */
	protected DlvRegionHome home;
	
	/** main method of this class
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new DlvRegionTestCase("testDlvRegionEB"));
	}
	
	/** Creates new DlvRegionTestCase */
	public DlvRegionTestCase(String testName) {
		super(testName);
	}
	
	/** This method sets up all the necessay resources nedded
	 * by the test to run
	 * @throws NamingException throws NamingException if unable to get context
	 */
	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (DlvRegionHome) ctx.lookup("freshdirect.delivery.Region");
	}
	
	/** This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	
	public void testDlvRegionEB() throws RemoteException, CreateException, RemoveException, FinderException {
		//DlvRegionModel model = new DlvRegionModel();
		//model.setName("URBAN");
		//DlvRegionEB eb = home.create(model);
		Calendar effectiveDate = new GregorianCalendar();
		effectiveDate.set(Calendar.MONTH, 2);
		
		DlvRegionEB eb = home.findByPrimaryKey(new FatPrimaryKey("146"));
		DlvRegionModel regionModel = (DlvRegionModel)eb.getModel(effectiveDate.getTime());
		List zones = regionModel.getZones();
		for(Iterator i = zones.iterator(); i.hasNext(); ){
			DlvZoneModel zone = (DlvZoneModel)i.next();
			System.out.println(zone.getName());
		}
	}
	
}
