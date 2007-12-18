/*
 * 
 * MailerGatewaySB.java
 * Date: Oct 4, 2002 Time: 6:29:23 PM
 */
package com.freshdirect.mail.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.framework.mail.XMLEmailI;

public interface MailerGatewaySB extends EJBObject {
	
	public void enqueueEmail(XMLEmailI email) throws RemoteException;

}
