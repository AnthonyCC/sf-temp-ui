/*
 * Created on Oct 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.freshdirect.fdstore.customer.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.ServiceLocator;

/**
 * @author ekracoff
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FailedAuthorizationEmailTestCase extends TestCase {

	public FailedAuthorizationEmailTestCase(String testName) {
		super(testName);
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(new FailedAuthorizationEmailTestCase("testAuthorizeSale"));
	}

	public void testAuthorizeSale() throws CreateException, FDResourceException, RemoteException {
		String saleId = "8498489";

		FDCustomerManagerSB sb = this.getFDCustomerManagerHome().create();
		sb.authorizeSale(saleId);

	}

	private FDCustomerManagerHome getFDCustomerManagerHome() {
		try {
			ServiceLocator LOCATOR = new ServiceLocator(ErpServicesProperties.getInitialContext());
			return (FDCustomerManagerHome) LOCATOR.getRemoteHome(
				"freshdirect.fdstore.CustomerManager",
				FDCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
