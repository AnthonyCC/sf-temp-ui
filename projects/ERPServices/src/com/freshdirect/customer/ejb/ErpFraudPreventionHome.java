package com.freshdirect.customer.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;


public interface ErpFraudPreventionHome extends EJBHome {

    public ErpFraudPreventionSB create() throws CreateException, RemoteException;

}

