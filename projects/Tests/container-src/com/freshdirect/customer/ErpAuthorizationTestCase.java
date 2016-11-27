/*
 * ErpAuthorizationTestCase.java
 *
 * Created on January 10, 2002, 12:35 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpSaleHome;

public class ErpAuthorizationTestCase extends TestCase{
	
	protected String[] sales = {"88512", "88366", "88925"};
	
	protected Context ctx;
	//protected ErpCustomerManagerHome customerHome;
	protected ErpSaleHome saleHome;
	protected ErpCustomerManagerHome managerHome;

	/** Creates new ErpAuthorizationTestCase */
    public ErpAuthorizationTestCase(String testName) {
		super(testName);
    }
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpAuthorizationTestCase("testCCDAuthorization"));
	}
	
	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		//customerHome = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
		//saleHome = (ErpSaleHome)ctx.lookup("freshdirect.erp.Sale");
		managerHome = (ErpCustomerManagerHome)ctx.lookup("freshdirect.erp.CustomerManager");
	}
	
	/** 
	 * This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	
	public void testCCDAuthorization() throws CreateException, RemoveException, FinderException, ErpAuthorizationException, RemoteException{
		ErpCustomerManagerSB sb = this.managerHome.create();
		/*for(int i = 0; i < sales.length; i++){
			System.out.println("Authorizing sale#: "+sales[i]);
			sb.authorizeCreditCard(sales[i]);
		}*/
		sb.authorizeSale("17745");
		//ErpSaleEB eb = saleHome.findByPrimaryKey(new PrimaryKey("11059"));
		//ErpAbstractOrderModel orderModel = eb.getCurrentOrder();
	}
	
}
