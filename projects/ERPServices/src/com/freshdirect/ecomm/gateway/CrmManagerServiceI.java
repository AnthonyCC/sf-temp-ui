package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;

public interface CrmManagerServiceI {
	        
	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;

	public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;
                																								
}
