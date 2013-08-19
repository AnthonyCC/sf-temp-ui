/*
 * 
 * GatewayActivityLogHome.java
 * 
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author tbalumuri
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface GatewayActivityLogHome extends EJBHome{
	
	public GatewayActivityLogSB create() throws CreateException, RemoteException;

}
