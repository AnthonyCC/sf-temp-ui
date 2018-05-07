package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.framework.core.EntityBeanRemoteI;
import com.freshdirect.framework.core.PrimaryKey;
/**
 *@deprecated This class methods are moved to backoffice project.
 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
 */
@Deprecated
public interface CrmCaseEB extends EntityBeanRemoteI {
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
    public void addCaseAction(CrmCaseAction caseAction) throws RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
    public void updateCaseInfo(CrmCaseInfo caseInfo) throws RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
    public void lock(PrimaryKey agentPK) throws RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
    public void unlock() throws RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
    public void setState(CrmCaseState state) throws RemoteException;

}
