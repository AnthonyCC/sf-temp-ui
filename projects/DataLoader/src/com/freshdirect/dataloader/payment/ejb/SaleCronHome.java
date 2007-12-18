package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

public interface SaleCronHome extends EJBHome{
	
	public SaleCronSB create() throws CreateException, RemoteException;
	

}
