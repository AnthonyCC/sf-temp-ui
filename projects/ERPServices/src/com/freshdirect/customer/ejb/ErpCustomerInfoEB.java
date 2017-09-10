package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;

import com.freshdirect.framework.core.EntityBeanRemoteI;
import com.freshdirect.framework.core.ModelI;



/**
 * ErpCustomer remote interface.
 *
 * @version
 * @author     $Author$
 */
public interface ErpCustomerInfoEB extends EntityBeanRemoteI {
	
	@Override
	public ModelI getModel() throws RemoteException;
	
}

