package com.freshdirect.mail.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface TEmailerGatewayHome extends EJBHome {
	
	public TMailerGatewaySB create() throws CreateException, RemoteException;

}