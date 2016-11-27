package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface PostSettlementNotifyHome extends EJBHome {

	PostSettlementNotifySB create() throws CreateException, RemoteException;

}
