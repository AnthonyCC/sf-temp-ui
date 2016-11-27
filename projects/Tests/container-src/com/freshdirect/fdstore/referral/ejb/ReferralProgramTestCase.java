package com.freshdirect.fdstore.referral.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.referral.EnumReferralProgramStatus;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralCampaign;
import com.freshdirect.fdstore.referral.ReferralChannel;
import com.freshdirect.fdstore.referral.ReferralHistory;
import com.freshdirect.fdstore.referral.ReferralObjective;
import com.freshdirect.fdstore.referral.ReferralPartner;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.fdstore.referral.ReferralProgramManager;
import com.freshdirect.framework.core.PrimaryKey;

import junit.framework.TestCase;

public class ReferralProgramTestCase extends DbTestCaseSupport {

	/*
	 * Test method for 'com.freshdirect.fdstore.referral.FDReferralManager.loadAllReferralPrograms()'
	 */
	
	public ReferralProgramTestCase(String name) {
		super(name);
	}
	
	
	public void testReferralProgramManager() throws FDException
	{
		ReferralProgramManager manager=ReferralProgramManager.getInstance();
		boolean value=manager.isInternalReferralInvitee("4");		
		assertTrue((value==true));
	}
	
	
	public void  testCreateReferralHistory() throws FDResourceException {
	      SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
		  ReferralHistory history=new ReferralHistory(new PrimaryKey("1"));
		  history.setDateCreated(new Date());
		  history.setFdUserId("44224");
		  history.setReferralProgramId("2");
		  history.setRefTrkKeyDtls("ajhhadb");
		  ReferralHistory history1=FDReferralManager.createReferralHistory(history);
		  assertNotNull(history1.getPK().getId());
	}
	
	
	
      public void testRemoveReferralProgram() throws FDResourceException, RemoteException
      {
           FDReferralManager.removeReferralProgram(new String[]{"516065321"});    	      	  
      }    

	
	/*
	 * Test method for 'com.freshdirect.fdstore.referral.FDReferralManager.createReferralProgram(ReferralProgram)'
	 */
	public void testCreateReferralProgram() throws FDResourceException, ParseException {
		
	     SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
		 		 
		 ReferralChannel channel=new ReferralChannel(null,"TESTEMAIL");		 
		 channel.setType("TESTINTERNAL");
		 channel.setDescription("used for all communications to FreshDirect Customers");
		 
		 ReferralPartner partner=new ReferralPartner(null,"TESTPARTNER1");		 		 
		 partner.setDescription("testing the partner creation");		 		
		
		 ReferralObjective objective=new ReferralObjective(new PrimaryKey("1"),"TESTOBJ");		 
		 objective.setDescription("TEST OBJECTIVE");		 
		 ReferralCampaign campaign=new ReferralCampaign(null,"EMAILCAMP",objective);		 		 
		 campaign.setDescription("TESTING CAMPAIGN ");	
		 
		 ReferralProgram program=new ReferralProgram(null,"TESTPROGRAM",channel,campaign,partner);		 		 
		 program.setDescription("TESTING CAMPAIGN ");	
		 program.setCreativeDesc("TEST DESC");
		 program.setExpDate(format.parse("09/09/2006"));
		 program.setStartDate(format.parse("09/09/2006"));
		 program.setStatus(EnumReferralProgramStatus.ACTIVE);
		 ReferralProgram program2=FDReferralManager.createReferralProgram(program);
		 System.out.println("program2.getPK().getId()"+program2.getPK().getId());
		 assertNotNull(program2.getPK().getId());   

	}

	
	
	/*
	 * Test method for 'com.freshdirect.fdstore.referral.FDReferralManager.loadReferralFromPK(String)'
	 */
	public void testLoadReferralFromPK() throws FDResourceException {
		  ReferralProgram program=FDReferralManager.loadReferralProgramFromPK("1");
		  System.out.println(program);
		  assertNotNull(program); 
	}

	
	/*
	 * Test method for 'com.freshdirect.fdstore.referral.FDReferralManager.loadLastestActiveReferralProgram()'
	 */
	public void testLoadLastestActiveReferralProgram() throws FDResourceException {
		  ReferralProgram program=FDReferralManager.loadLastestActiveReferralProgram();
		  System.out.println(program);
		  assertNotNull(program);

	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
//			"CUST.CUSTOMERINFO",
//			"CUST.FDCUSTOMER",
//			"CUST.REF_PROGRAM",
//			"CUST.REF_PROG_INVITATION",
//			"CUST.REF_OBJECTIVE",
//			"CUST.REF_CHANNEL",
//			"CUST.REF_CAMPAIGN",
//			"CUST.REF_HISTORY"
			};
	}

}
