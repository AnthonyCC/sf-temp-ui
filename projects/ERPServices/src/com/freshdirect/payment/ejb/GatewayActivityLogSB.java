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
/**
 *@deprecated Please use the GatewayActivityLogController and GatewayActivityLogServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface GatewayActivityLogSB extends EJBObject {
	@Deprecated
	public void logActivity(GatewayType gatewayType,Response response) throws RemoteException;
}
