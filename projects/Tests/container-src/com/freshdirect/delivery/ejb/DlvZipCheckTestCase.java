package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.TestUtil;
import com.freshdirect.delivery.DlvServiceSelectionResult;

import junit.framework.TestCase;

public class DlvZipCheckTestCase extends TestCase {
	
	protected Context ctx;
	protected DlvManagerHome home;
	private String[] zipcodes = { "10044", "10038", "10014", "11201", "11418", "10016", "10029", "10013", "10282" };
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new DlvZipCheckTestCase("testZipCheck"));
	}

	public DlvZipCheckTestCase(String testName) {
		super(testName);
	
	}
	
	public void testZipCheck() throws RemoteException, CreateException{
		DlvManagerSB sb = home.create();
		for(int i = 0; i < zipcodes.length; i++){
			String zip = zipcodes[i];
			DlvServiceSelectionResult result = sb.checkZipCode(zip);
		}
	}

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
	}

	protected void tearDown() throws NamingException {
		ctx.close();
	}

}
