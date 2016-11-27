package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

public interface CrmCaseHome extends EJBHome {

	public CrmCaseEB create() throws CreateException, RemoteException;

	public CrmCaseEB create(ModelI model) throws CreateException, RemoteException;

	public CrmCaseEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;

}
