package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * @author knadeem
 */
/**
 *@deprecated This class methods are moved to backoffice project.
 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
 */
@Deprecated
public interface CrmAgentEB extends EntityBeanRemoteI {
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public boolean isActive() throws RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public boolean isMasqueradeAllowed() throws RemoteException;
}
