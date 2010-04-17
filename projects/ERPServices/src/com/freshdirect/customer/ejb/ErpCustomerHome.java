package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.freshdirect.framework.core.*;

/**
 * ErpCustomer entity home interface.
 *
 * @version
 * @author     $Author$
 */
public interface ErpCustomerHome extends EJBHome {

	public ErpCustomerEB create() throws CreateException, RemoteException;

	public ErpCustomerEB create(ModelI model) throws CreateException, RemoteException;

	public ErpCustomerEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;

	public ErpCustomerEB findByUserId(String userId) throws FinderException, RemoteException;

	public ErpCustomerEB findByUserIdAndPasswordHash(String userId, String passwordHash) throws FinderException, RemoteException;

}

