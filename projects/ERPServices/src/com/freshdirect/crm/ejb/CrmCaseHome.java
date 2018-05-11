package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
/**
 *@deprecated This class methods are moved to backoffice project.
 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
 */
@Deprecated
public interface CrmCaseHome extends EJBHome {
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public CrmCaseEB create() throws CreateException, RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public CrmCaseEB create(ModelI model) throws CreateException, RemoteException;
	/**
	 *@deprecated This class methods are moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public CrmCaseEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;

}
