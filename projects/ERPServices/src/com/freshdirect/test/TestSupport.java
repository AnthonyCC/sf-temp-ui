package com.freshdirect.test;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.test.ejb.TestSupportHome;
import com.freshdirect.test.ejb.TestSupportSB;



/**
 * Test supporter class
 * 
 * @author segabor
 *
 */
public class TestSupport {
	private static TestSupport sharedInstance = null;
	private ServiceLocator serviceLocator = null;

	private TestSupport () throws NamingException {
		serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
	}

	private TestSupportHome getTestSupportHome() {
		try {
			return (TestSupportHome) serviceLocator.getRemoteHome(
				"freshdirect.test.TestSupport", TestSupportHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}


	/**
	 * Get shared instance.
	 * @return instance
	 */
	synchronized public static TestSupport getInstance() {
		if (sharedInstance == null) {
			try {
				sharedInstance = new TestSupport();
			} catch (NamingException e) {
				throw new FDRuntimeException(e,"Could not create test support shared instance");
			}
		}
		return sharedInstance;
	}
	
	
	/**
	 * Dummy method. Ignore.
	 */
	public void ping() {
		try {
			TestSupportSB bean = this.getTestSupportHome().create();
			bean.ping();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	



	public List<Long> getDYFEligibleCustomerIDs() {
		try {
			TestSupportSB bean = this.getTestSupportHome().create();
			
			return bean.getDYFEligibleCustomerIDs();
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		}
	}


	public List<Long> getErpCustomerIDs() {
		try {
			TestSupportSB bean = this.getTestSupportHome().create();
			
			return bean.getErpCustomerIds();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
		
	}

	public String getFDCustomerIDForErpId(String erp_id) {
		try {
			TestSupportSB bean = this.getTestSupportHome().create();
			
			return bean.getFDCustomerIDForErpId(erp_id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getErpIDForUserID(String user_id) {
		try {
			TestSupportSB bean = this.getTestSupportHome().create();
			
			return bean.getErpIDForUserID(user_id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
