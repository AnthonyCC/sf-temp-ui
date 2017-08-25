package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.freshdirect.framework.core.*;

/**
 * ErpCustomerLite entity home interface.
 *
 * @version
 * @author     $Author$
 */
public interface ErpCustomerInfoHome extends EJBHome {

	public ErpCustomerInfoEB create() throws CreateException, RemoteException;
	
	public ErpCustomerInfoEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;
	
	public ErpCustomerInfoEB findByErpCustomerId(String id) throws FinderException, RemoteException;
	
}

