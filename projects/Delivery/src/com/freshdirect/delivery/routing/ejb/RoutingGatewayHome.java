package com.freshdirect.delivery.routing.ejb;


import javax.ejb.*;
import java.rmi.RemoteException;

/**
 *
 *
 * @version $Revision: 1$
 * @author $Author: Sivachandar$
 */
public interface RoutingGatewayHome extends EJBHome {
    
    public RoutingGatewaySB create() throws CreateException, RemoteException;

}
