package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.crm.ejb.CrmManagerHome;
import com.freshdirect.crm.ejb.CrmManagerSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.ServiceLocator;


public class ErpCreateCaseCommand {

	private final List caseInfos;
	private final ServiceLocator locator;
	private boolean requiresNewTx = false;

	public ErpCreateCaseCommand(ServiceLocator locator, CrmSystemCaseInfo ci) {
		this.caseInfos = new ArrayList(1);
		this.caseInfos.add(ci);
		this.locator = locator;
	}

	public ErpCreateCaseCommand(ServiceLocator locator, List caseInfos) {
		this.caseInfos = caseInfos;
		this.locator = locator;
	}

	public void execute() {
		try {
			this.createFDCase();
		} catch (CreateException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (FDResourceException e) {
			throw new EJBException(e);
		}
	}

	private void createFDCase() throws CreateException, FDResourceException, RemoteException {
		CrmManagerSB sb = this.getCrmManagerHome().create();
		for (Iterator i = this.caseInfos.iterator(); i.hasNext();) {
			CrmSystemCaseInfo caseInfo = (CrmSystemCaseInfo) i.next();
			if (this.requiresNewTx) {
				sb.createSystemCaseInSingleTx(caseInfo);
			} else {
				sb.createSystemCase(caseInfo);
			}
		}
	}


	public void setRequiresNewTx(boolean requiresNewTx){
		this.requiresNewTx = requiresNewTx;
	}


	private CrmManagerHome getCrmManagerHome() {
		try {
			return (CrmManagerHome) locator.getRemoteHome("freshdirect.crm.Manager", CrmManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
