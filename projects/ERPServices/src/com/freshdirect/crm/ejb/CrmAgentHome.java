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

public interface CrmAgentHome extends EJBHome {
	
	public CrmAgentEB create() throws CreateException, RemoteException;

	public CrmAgentEB create(ModelI model) throws CreateException, DuplicateKeyException, RemoteException;
	
	public Collection findAll() throws FinderException, RemoteException;

	public CrmAgentEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;

	public CrmAgentEB findByUserIdAndPassword(String userId, String password) throws ObjectNotFoundException, FinderException, RemoteException;

}
