package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.TestUtil;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvPlan;
import com.freshdirect.delivery.planning.DlvShiftModel;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.delivery.admin.ejb.DlvAdminManagerHome;
import com.freshdirect.delivery.admin.ejb.DlvAdminManagerSB;

public class DlvAdminManagerTestCase extends TestCase{
	
	protected Context ctx;
	protected DlvAdminManagerHome home;
	
	
	/** 
	 * main method of this class
	 * @param args command line arguments
	 */
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new DlvManagerTestCase("testDlvAdminManager"));
	}

	public DlvAdminManagerTestCase(String testName) {
		super(testName);
		
	}
	
	/** 
	 * This method sets up all the necessay resources nedded
	 * by the test to run
	 * @throws NamingException throws NamingException if unable to get context
	 */
	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (DlvAdminManagerHome) ctx.lookup("freshdirect.delivery.AdminManager");
	}
	
	/** 
	 * This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	

	public void testDlvAdminManagerPlanMethod() throws RemoteException, CreateException, DlvResourceException, ReservationException, InvalidAddressException {
		
		// create light weight plans with regionId 001, which is created for unit test in Region table
		// this is for testGetPlansForRegion()
		DlvPlanModel plan1 = new DlvPlanModel();
		DlvPlanModel plan2 = new DlvPlanModel();
		plan1.setName("Unit Test Plan Name1");
		plan1.setRegionId("001");
		plan2.setName("Unit Test Plan Name2");
		plan2.setRegionId("001");
		
		// create plan with list of shifts, which has list of timeslots
		DlvPlanModel plan = new DlvPlanModel();
		List shiftList = new ArrayList();
		DlvShiftModel shift1 = new DlvShiftModel();
		DlvShiftModel shift2 = new DlvShiftModel();
		DlvShiftTimeslotModel timeslot1 = new DlvShiftTimeslotModel();
		DlvShiftTimeslotModel timeslot2 = new DlvShiftTimeslotModel();
		List timeslotList = new ArrayList();
		
		
		timeslot1.setStartTime(new TimeOfDay("08:00 AM"));
		timeslot1.setEndTime(new TimeOfDay("10:00 AM"));
		timeslot1.setCutoffTime(new TimeOfDay("09:00 PM"));
		timeslot1.setStatus(EnumTimeslotStatus.STANDARD);
		timeslot1.setTrafficFactor(0.0);

		timeslot2.setStartTime(new TimeOfDay("10:00 AM"));
		timeslot2.setEndTime(new TimeOfDay("12:00 PM"));
		timeslot2.setCutoffTime(new TimeOfDay("09:00 PM"));
		timeslot2.setStatus(EnumTimeslotStatus.STANDARD);
		timeslot2.setTrafficFactor(0.0);
		
		timeslotList.add(timeslot1);
		timeslotList.add(timeslot2);
		
		shift1.setName("Unit test Monday morning shift");
		shift1.setStart(new TimeOfDay("08:00 AM"));
		shift1.setEnd(new TimeOfDay("01:00 PM"));
		shift1.setDayOfWeek(EnumDayCode.MONDAY);
		shift1.setTimeslots(timeslotList);

		shift2.setName("Unit test Monday afternoon shift");
		shift2.setStart(new TimeOfDay("01:00 PM"));
		shift2.setEnd(new TimeOfDay("06:00 PM"));
		shift2.setDayOfWeek(EnumDayCode.MONDAY);
		
		shiftList.add(shift1);
		shiftList.add(shift2);
				
		plan.setRegionId("146");
		plan.setName("Unit Test Plan Name");
		plan.setShifts(shiftList);
		
		String planId = testAddPlan_GetPlan(plan);
		testUpdatePlan(planId);
		testDeletePlan(planId);
				
		testGetPlansForRegion(plan1, plan2);	
		
	}
	
	private String testAddPlan_GetPlan(DlvPlanModel plan) throws DlvResourceException,RemoteException, CreateException {
		
		DlvAdminManagerSB sb = home.create();
		String generatedId = sb.addPlan(plan,true);
		plan.setId(generatedId);
		DlvPlanModel savedPlan = sb.getPlan(generatedId);
//		 NOTE: the order in the retured list is not guaranteed
		DlvShiftModel shift1 = (DlvShiftModel)savedPlan.getShifts().get(0);
		DlvShiftModel shift2 = (DlvShiftModel)savedPlan.getShifts().get(1);
		DlvShiftTimeslotModel timeslot1 = (DlvShiftTimeslotModel)shift1.getTimeslots().get(0);
		DlvShiftTimeslotModel timeslot2 = (DlvShiftTimeslotModel)shift1.getTimeslots().get(0);
		
		assertEquals(plan, savedPlan);
		assertEquals("146", savedPlan.getRegionId());
		assertEquals("Unit Test Plan Name", savedPlan.getName());
		assertEquals(generatedId, savedPlan.getId());
		assertEquals(2, savedPlan.getShifts().size());
		assertEquals("Unit test Monday morning shift", shift1.getName());
		assertEquals(new TimeOfDay("08:00 AM"), shift1.getStart());
		assertEquals(new TimeOfDay("01:00 PM"), shift1.getEnd());
		assertEquals(EnumDayCode.MONDAY, shift1.getDayOfWeek());
		assertEquals(2, shift1.getTimeslots().size());
		assertEquals(new TimeOfDay("08:00 AM"), timeslot1.getStartTime());
		assertEquals(new TimeOfDay("10:00 AM"), timeslot1.getEndTime());
		assertEquals(new TimeOfDay("09:00 PM"), timeslot1.getCutoffTime());
		assertEquals(EnumTimeslotStatus.STANDARD,timeslot1.getStatus());
		assertEquals(0.0, timeslot1.getTrafficFactor(), 0.001);
		
		
		return generatedId;
	}

	private void testUpdatePlan(String planId) throws DlvResourceException,RemoteException, CreateException {
		
		DlvAdminManagerSB sb = home.create();
		
		DlvPlanModel plan = sb.getPlan(planId);
		
		plan.setName("Unit test plan name updated");
		DlvShiftModel shift1 = (DlvShiftModel)plan.getShifts().get(0);
		DlvShiftTimeslotModel timeslot1 = (DlvShiftTimeslotModel)shift1.getTimeslots().get(0);		
		shift1.setName("Unit test Monday morning shift updated");
		timeslot1.setCutoffTime(new TimeOfDay("11:00 PM"));
		
		sb.updatePlan(plan);
		
		DlvPlanModel updatedPlan = sb.getPlan(planId);
		DlvShiftModel updatedShift1 = (DlvShiftModel)updatedPlan.getShifts().get(0);
		DlvShiftTimeslotModel updatedTimeslot1 = (DlvShiftTimeslotModel)updatedShift1.getTimeslots().get(0);

		assertEquals(plan, updatedPlan);
		assertEquals("Unit test plan name updated", updatedPlan.getName());
		assertEquals("Unit test Monday morning shift updated", updatedShift1.getName());
		assertEquals(new TimeOfDay("11:00 PM"), updatedTimeslot1.getCutoffTime());
	}
	
	private void testDeletePlan(String planId) throws DlvResourceException,RemoteException, CreateException {
		boolean		gotException;
		DlvAdminManagerSB sb = home.create();
		
		sb.deletePlan(planId);
		
		gotException = false;
		try {
			sb.getPlan(planId);
		} catch (DlvResourceException e) {
			gotException = true;
		}
		assertTrue(gotException);	
	}
	
	private void testGetPlansForRegion(DlvPlanModel plan1,DlvPlanModel plan2) throws DlvResourceException,RemoteException, CreateException {
		
		DlvAdminManagerSB sb = home.create();
		
		String id1 = sb.addPlan(plan1,true);
		String id2 = sb.addPlan(plan2,true);

		List planList = sb.getPlansForRegion("001");
		
		DlvPlan p1 = (DlvPlan)planList.get(0);
		DlvPlan p2 = (DlvPlan)planList.get(1);
		sb.deletePlan(id1);
		sb.deletePlan(id2);
		
		assertEquals(2,planList.size());
		assertEquals("Unit Test Plan Name1",p1.getName());
		assertEquals("Unit Test Plan Name2",p2.getName());
		
	}	
}
