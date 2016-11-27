package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.framework.core.EntityBeanRemoteI;
import com.freshdirect.framework.core.PrimaryKey;

public interface CrmCaseEB extends EntityBeanRemoteI {
    
    public void addCaseAction(CrmCaseAction caseAction) throws RemoteException;
    
    public void updateCaseInfo(CrmCaseInfo caseInfo) throws RemoteException;
    
    public void lock(PrimaryKey agentPK) throws RemoteException;
    
    public void unlock() throws RemoteException;
    
    public void setState(CrmCaseState state) throws RemoteException;

}
