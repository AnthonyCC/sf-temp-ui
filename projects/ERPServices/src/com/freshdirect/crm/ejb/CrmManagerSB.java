package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;

public interface CrmManagerSB extends EJBObject {

	public void createAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, ErpDuplicateUserIdException, RemoteException;

	public void updateAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, RemoteException;

	public CrmAgentModel getAgentByPk(String agentPk) throws FDResourceException, FinderException, RemoteException;

	public CrmAgentList getAllAgents() throws FDResourceException, RemoteException;

	public List findCases(CrmCaseTemplate template) throws FDResourceException, RemoteException;

	public CrmCaseModel getCaseByPk(String casePk) throws FDResourceException, RemoteException;

	public CrmAgentModel loginAgent(String username, String password) throws FDResourceException, CrmAuthenticationException, RemoteException;

	public boolean lockCase(PrimaryKey agentPK, PrimaryKey casePK) throws FDResourceException, RemoteException;
	
	public void unlockCase(PrimaryKey casePK) throws FDResourceException, RemoteException;
    
	public PrimaryKey createCase(CrmCaseModel caseModel) throws FDResourceException, RemoteException;
    
	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;

	public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;
    
	public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction action, PrimaryKey agentPk) throws FDResourceException, CrmAuthorizationException, RemoteException;
    
	public List getQueueOverview() throws FDResourceException, RemoteException;

	public List getCSROverview() throws FDResourceException, RemoteException;
    
    public List getOperations() throws FDResourceException, RemoteException;
    
	public void downloadCases(PrimaryKey agentPK, String queue, String subject, int numberToDownload) throws FDResourceException, RemoteException;
	
	public CrmStatus getSessionStatus(PrimaryKey agentPK) throws FDResourceException, RemoteException;
	
	public void saveSessionStatus(CrmStatus status) throws FDResourceException, RemoteException;
	
	public PrimaryKey createLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException,RemoteException;
	
	public void updateLateIssue(CrmLateIssueModel lateIssue) throws FinderException,FDResourceException,RemoteException;
	
	public CrmLateIssueModel getLateIssueById(String id) throws FinderException, FDResourceException,RemoteException;

	public Collection getLateIssuesByRouteAndDate(String route,Date date) throws  FDResourceException,RemoteException;
	
	public Collection getLateIssuesByDate(Date date) throws FDResourceException, RemoteException;

	public List getTruckNumbersForDate(Date date)throws FDResourceException,RemoteException;

	public CrmLateIssueModel getRecentLateIssueForOrder(String orderId)throws FDResourceException,RemoteException;

	public CrmCustomerHeaderInfo getCustomerHeaderInfo(String customerId) throws FDResourceException, RemoteException;

	public void incrDeliveryCount(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			int delta, 
			String note, 
			String reasonCode, 
			String saleId)  throws FDResourceException, CrmAuthorizationException, RemoteException;
	

	public void incrExpirationPeriod(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			int noOfDays, 
			String note, 
			String reasonCode, 
			String saleId)  throws FDResourceException, CrmAuthorizationException, RemoteException;
	
	
	public void cancelDeliveryPass(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException, RemoteException;
	
	public void reactivateDeliveryPass(DeliveryPassModel model) throws FDResourceException, RemoteException;
	
	public String lookupAccount(String accountNum) throws FDResourceException, RemoteException;
	
	public List lookupOrders(String accountNum) throws FDResourceException, RemoteException;
	
	public void logViewAccount(CrmAgentModel agent, String customerID) throws FDResourceException, RemoteException;
}

