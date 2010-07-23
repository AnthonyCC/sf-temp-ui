package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * @author knadeem
 */

public interface CrmAgentEB extends EntityBeanRemoteI {
	
	public boolean isActive() throws RemoteException;
	public boolean isMasqueradeAllowed() throws RemoteException;
}
