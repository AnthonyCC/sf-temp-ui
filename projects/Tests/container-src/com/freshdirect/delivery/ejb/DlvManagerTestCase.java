/*
 * DlvManagerTestCase.java
 *
 * Created on August 28, 2001, 11:49 AM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version
 */
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.model.DlvTimeslotModel;


public class DlvManagerTestCase extends TestCase{
	
	protected Context ctx;
	
	/** 
	 * Home interface for the DlvSpecialDateEntity bean 
	 */
	protected DlvManagerHome home;
	
	/** 
	 * main method of this class
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new DlvManagerTestCase("testDlvManagerSB"));
	}
	/** Creates new DlvManagerTestCase */
	public DlvManagerTestCase(String testName) {
		super(testName);
		
	}
	
	/** 
	 * This method sets up all the necessay resources nedded
	 * by the test to run
	 * @throws NamingException throws NamingException if unable to get context
	 */
	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
	}
	
	/** 
	 * This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	
	public void testDlvManagerSB() throws RemoteException, CreateException, DlvResourceException, ReservationException, InvalidAddressException {
		Calendar cal = new GregorianCalendar();
		Calendar currentCal = new GregorianCalendar();
		Calendar begCal = null;
		Calendar endCal = null;
		
		//cal.add(Calendar.DATE, 1);
		begCal = cal;
		begCal.set(Calendar.HOUR_OF_DAY,0);
		begCal.set(Calendar.MINUTE,1);
		Calendar cal1 = new GregorianCalendar();
		endCal = cal1;
		endCal.add(Calendar.DATE, 7);
		endCal.set(Calendar.HOUR,12);
		endCal.set(Calendar.MINUTE,00);
		endCal.set(Calendar.AM_PM, Calendar.AM);
		
		//testAddressTimeslots(begCal, endCal, currentCal);
		
		testDepotTimeslots(begCal, endCal, currentCal);
		
	}
	
	private void testAddressTimeslots(Calendar begCal, Calendar endCal, Calendar currentCal) throws RemoteException, CreateException, InvalidAddressException {
		
		DlvManagerSB sb = home.create();
		Timestamp begTime = new Timestamp(begCal.getTime().getTime());
		Timestamp endTime = new Timestamp(endCal.getTime().getTime());  
		
		AddressModel address = new AddressModel();
		address.setAddress1("900 Main St");
		//address.setApartmentSuite("3C");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10044");
		
		DlvZoneInfoModel info = sb.getZoneInfo(address, new java.util.Date());
		System.out.println("\nZone Id: "+info.getZoneId()); 
		
		List timeslots = sb.getTimeslotForDateRangeAndZone(begTime, endTime, address);
		displayTimeslots(timeslots);
	}
	
	private void testDepotTimeslots(Calendar begCal, Calendar endCal, Calendar currentCal) throws RemoteException, CreateException, DlvResourceException {
		DlvManagerSB sb = home.create();
		List timeslots = sb.getTimeslotsForDepot(begCal.getTime(), endCal.getTime(), "173168", "912");
		displayTimeslots(timeslots);
		
	}
	
	private void displayTimeslots(List timeslots) {
		System.out.println(">>>>>>>>>:"+timeslots.size());
		for(Iterator i = timeslots.iterator(); i.hasNext();){
			DlvTimeslotModel model = (DlvTimeslotModel)i.next();
			System.out.println(model.getStartTimeAsDate());
			System.out.println(model.getEndTimeAsDate());
			System.out.println(model.getCutoffTimeAsDate());
			System.out.println(model.getStatus());
			System.out.println(model.getZoneId());
			System.out.println(model.getCapacity());
			System.out.println("");
		}
	}
	
}
