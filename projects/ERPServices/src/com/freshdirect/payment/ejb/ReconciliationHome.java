package com.freshdirect.payment.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

public interface ReconciliationHome extends EJBHome {
	
	public ReconciliationSB create() throws CreateException, RemoteException;

}
