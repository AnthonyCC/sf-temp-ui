/*
 * 
 * MailerGatewayHome.java
 * Date: Oct 4, 2002 Time: 6:33:43 PM
 */
package com.freshdirect.mail.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface MailerGatewayHome extends EJBHome {
	
	public MailerGatewaySB create() throws CreateException, RemoteException;

}
