package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
/**
 *@deprecated This class methods are moved to backoffice project.
 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
 */
@Deprecated
public interface CrmAgentHome extends EJBHome {
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentEB create() throws CreateException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentEB create(ModelI model) throws CreateException, DuplicateKeyException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public Collection<CrmAgentEB> findAll() throws FinderException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentEB findByUserIdAndPassword(String userId, String password) throws ObjectNotFoundException, FinderException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentEB findAgentByLdapId(String ldapId) throws FinderException, RemoteException;

}
