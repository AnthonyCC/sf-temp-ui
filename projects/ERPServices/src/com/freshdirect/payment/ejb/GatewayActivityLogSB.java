/*
 * 
 * GatewayActivityLogSB.java
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author tbalumuri
 */
import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Response;

public interface GatewayActivityLogSB extends EJBObject {
	
	public void logActivity(GatewayType gatewayType,Response response) throws RemoteException;
}
