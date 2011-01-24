package com.freshdirect.crm;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.ejb.CrmManagerHome;
import com.freshdirect.crm.ejb.CrmManagerSB;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.EnumComplaintDlvIssueType;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.ExpiringReference;


/**
 * @author knadeem
 */
public class CrmManager {

	private final ServiceLocator serviceLocator;
	private static CrmManager manager = null;
	private final CrmOperationCollection operations;

	private final Map<String, CrmAgentModel> agentCache = new HashMap<String, CrmAgentModel>();
	private CrmAgentList agentListCache = null;

	private final ExpiringReference<List<CrmQueueInfo>> queueOverviewCache = new ExpiringReference<List<CrmQueueInfo>>(5 * 60 * 1000) {

		protected List<CrmQueueInfo> load() {
			try {
				return getCrmManagerSB().getQueueOverview();
			} catch (RemoteException ex) {
				throw new FDRuntimeException(ex);
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}

	};

	private CrmManager() throws NamingException {
		this.serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
		this.operations = new CrmOperationCollection();
	}

	public static CrmManager getInstance() throws FDResourceException {
		if (manager == null) {
			try {
				manager = new CrmManager();
				CrmManagerHome home =
					(CrmManagerHome) manager.serviceLocator.getRemoteHome("freshdirect.crm.Manager");
				CrmManagerSB sb = home.create();
				manager.setOperations(sb.getOperations());
			} catch (NamingException e) {
				throw new FDResourceException(e);
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create CrmManagerSB");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
			}
		}
		return manager;
	}

	public void createAgent(CrmAgentModel agent, PrimaryKey userPk)
		throws FDResourceException, CrmAuthorizationException, ErpDuplicateUserIdException {
		try {
			this.getCrmManagerSB().createAgent(agent, userPk);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public void updateAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException {
		try {
			this.getCrmManagerSB().updateAgent(agent, userPk);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public CrmAgentModel getAgentByPk(String agentPk) throws FDResourceException {
		try {
			return this.getCrmManagerSB().getAgentByPk(agentPk);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		} catch (FinderException e) {
			throw new FDResourceException(e, "Cannot find agent for PK: " + agentPk);
		}
	}

	public String getAgentUserId(String agentPk) throws FDResourceException {
		return getCachedAgent(agentPk).getUserId();
	}

	private synchronized CrmAgentModel getCachedAgent(String agentPk) throws FDResourceException {
		CrmAgentModel agent = agentCache.get(agentPk);
		if (agent == null) {
			agent = this.getAgentByPk(agentPk);
			agentCache.put(agentPk, agent);
		}
		return agent;
	}

	public synchronized CrmAgentList getAllAgents(boolean useCache) throws FDResourceException {
		try {
			if (useCache && agentListCache != null) {
				return agentListCache;
			}
			CrmAgentList l = this.getCrmManagerSB().getAllAgents();
			this.agentListCache = l;
			return l;

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public Map<CrmAgentRole, List<String>> getAllAgentsFromLDAP(boolean useCache) throws FDResourceException {
		if (useCache){
			return CrmLdapAccessor.getLdapGroupMembersMap();
		}
		CrmLdapAccessor.init();
		return CrmLdapAccessor.getLdapGroupMembersMap();
	}
	
	public boolean isAgentValid(String agentId) throws FDResourceException{
		boolean isValid = false;
		Map<CrmAgentRole, List<String>> rolesAndAgents = getAllAgentsFromLDAP(true);
		if(null !=rolesAndAgents && !rolesAndAgents.isEmpty()){
			for (Iterator iterator = rolesAndAgents.keySet().iterator(); iterator.hasNext();) {
				CrmAgentRole agentRole = (CrmAgentRole) iterator.next();
				List<String> agents = rolesAndAgents.get(agentRole);
				if( null != agents && agents.contains(agentId)){
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}

	public List<CrmCaseModel> findCases(CrmCaseTemplate template) throws FDResourceException {
		try {
			return this.getCrmManagerSB().findCases(template);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}

	public List<CrmQueueInfo> getQueueOverview() throws FDResourceException {
		return queueOverviewCache.get();
	}

	public List<CrmAgentInfo> getCSROverview() throws FDResourceException {
		try {
			return this.getCrmManagerSB().getCSROverview();
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}

	public CrmAgentInfo getCSROverview(PrimaryKey agentPK) throws FDResourceException {
		// !!! could be optimized
		List<CrmAgentInfo> infos = this.getCSROverview();
		for ( CrmAgentInfo info : infos ) {
			if (info.getAgentPK().equals(agentPK)) {
				return info;
			}
		}
		throw new FDResourceException("No info for agent " + agentPK);
	}

	public CrmCaseModel getCaseByPk(String casePk) throws FDResourceException {
		try {
			return this.getCrmManagerSB().getCaseByPk(casePk);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public CrmAgentModel loginAgent(String username, String password) throws CrmAuthenticationException, FDResourceException {
		try {
			return this.getCrmManagerSB().loginAgent(username, password);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public PrimaryKey createCase(CrmCaseModel caseModel) throws FDResourceException {
		try {
			return this.getCrmManagerSB().createCase(caseModel);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException {
		try {
			return this.getCrmManagerSB().createSystemCase(caseInfo);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction caseAction, PrimaryKey agentPk)
		throws FDResourceException, CrmAuthorizationException {
		try {
			this.getCrmManagerSB().updateCase(caseInfo, caseAction, agentPk);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction caseAction, CrmCurrentAgent agent)
		throws FDResourceException, CrmAuthorizationException {
		try {
			this.getCrmManagerSB().updateCase(caseInfo, caseAction, agent);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	public boolean lockCase(PrimaryKey agentPK, PrimaryKey casePK) throws FDResourceException {
		try {
			return this.getCrmManagerSB().lockCase(agentPK, casePK);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public boolean lockCase(String agentId, PrimaryKey casePK) throws FDResourceException {
		try {
			return this.getCrmManagerSB().lockCase(agentId, casePK);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public void unlockCase(PrimaryKey casePK) throws FDResourceException {
		try {
			this.getCrmManagerSB().unlockCase(casePK);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public boolean closeAutoCase(PrimaryKey casePK) throws FDResourceException {
		try {
			return this.getCrmManagerSB().closeAutoCase(casePK);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}


	public void downloadCases(PrimaryKey agentPK, String queue, String subject, int numberToDownload) throws FDResourceException {
		try {
			this.getCrmManagerSB().downloadCases(agentPK, queue, subject, numberToDownload);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	private void setOperations(List<CrmCaseOperation> ops) {
		this.operations.setOperations(ops);
	}

	public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject) {
		return this.operations.getOperations(role, subject);
	}

	public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState) {
		return this.operations.getOperations(role, subject, startState);
	}

	public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState, CrmCaseActionType type) {
		return this.operations.getOperations(role, subject, startState, type);
	}
	
	public void saveSessionStatus(CrmStatus status) throws FDResourceException{
		try {
			this.getCrmManagerSB().saveSessionStatus(status);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public CrmStatus getSessionStatus(PrimaryKey agentPK) throws FDResourceException{
		try {
			return this.getCrmManagerSB().getSessionStatus(agentPK);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public CrmStatus getSessionStatus(String agentId) throws FDResourceException{
		try {
			return this.getCrmManagerSB().getSessionStatus(agentId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

	public PrimaryKey createLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException, RemoteException {
		try {
		   return this.getCrmManagerSB().createLateIssue(lateIssue);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmLateIssuerSB");
		}
	}

	public void updateLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException,FinderException, RemoteException {
		try {
		   this.getCrmManagerSB().updateLateIssue(lateIssue);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmLateIssuerSB");
		}
	}
	
	public CrmLateIssueModel getLateIssueById(String id) throws FDResourceException, RemoteException, FinderException {
		try {
			   return this.getCrmManagerSB().getLateIssueById(id);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Exception getting Late Issue by ID");
			}
	}
	
	public Collection<CrmLateIssueModel> getLateIssuesByDate(Date date) throws FDResourceException   {
		try {
			   return this.getCrmManagerSB().getLateIssuesByDate(date);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Excepption getting late issue by Date");
			}
	}
	
	public Collection<CrmLateIssueModel> getLateIssuesByRouteAndDate(String route, Date date) throws FDResourceException   {
		try {
			   return this.getCrmManagerSB().getLateIssuesByRouteAndDate(route,date);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Exception getting late issue by date & route");
			}
	}
	
	public List<ErpTruckInfo> getTruckNumbersForDate(Date dlvDate) throws FDResourceException {
		try {
			   return this.getCrmManagerSB().getTruckNumbersForDate(dlvDate);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Exception getting late issue by date & route");
			}
		
	}
	
	public CrmLateIssueModel getRecentLateIssueForOrder(String orderId) throws FDResourceException {
			try {
			return orderId==null ? null 
				: this.getCrmManagerSB().getRecentLateIssueForOrder(orderId);
		}catch (RemoteException e) {
			throw new FDResourceException(e, "Exception getting recentlate issue for order id: "+orderId);
		}
	}

	public CrmCustomerHeaderInfo getCustomerHeaderInfo(String customerId) throws FDResourceException {
		try{
			return this.getCrmManagerSB().getCustomerHeaderInfo(customerId);
		}catch (RemoteException e) {
			e.printStackTrace();
			throw new FDResourceException(e, "Exception getting customer Header info for customer id: "+customerId);
		}
	}

	public void incrDeliveryCount(DeliveryPassModel model, 
								CrmAgentModel agentModel, 
								int delta, 
								String note, 
								String reasonCode, 
								String saleId) throws FDResourceException, CrmAuthorizationException {
		try {
			
			this.getCrmManagerSB().incrDeliveryCount(model, agentModel, delta, note, reasonCode, saleId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while incrementing delivery count.");
		}
	}
	
	public void incrDeliveryCount(DeliveryPassModel model, 
			String agentId,
			int delta, 
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException, CrmAuthorizationException {
		try {
		
			this.getCrmManagerSB().incrDeliveryCount(model, agentId, delta, note, reasonCode, saleId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while incrementing delivery count.");
		}
	}
	
	public void incrExpirationPeriod(DeliveryPassModel model, 
									CrmAgentModel agentModel, 
									int noOfDays, 
									String note, 
									String reasonCode, 
									String saleId) throws FDResourceException, CrmAuthorizationException{
		try {
			this.getCrmManagerSB().incrExpirationPeriod(model, agentModel, noOfDays, note, reasonCode, saleId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while incrementing expiration period.");
		}
	}	
	
	public void incrExpirationPeriod(DeliveryPassModel model, 
			String agentId,
			int noOfDays, 
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException, CrmAuthorizationException{
			try {
				this.getCrmManagerSB().incrExpirationPeriod(model, agentId, noOfDays, note, reasonCode, saleId);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Error in CrmManagerSB while incrementing expiration period.");
			}
	}	
	
	public void cancelDeliveryPass(DeliveryPassModel model,
								CrmAgentModel agentModel,
								String note, 
								String reasonCode, 
								String saleId) throws FDResourceException{
		try {
			this.getCrmManagerSB().cancelDeliveryPass(model, agentModel, note, reasonCode, saleId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while cancelling delivery pass.");
		}
	}	

	public void cancelDeliveryPass(DeliveryPassModel model,
			String agentId,
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException{
		try {
			this.getCrmManagerSB().cancelDeliveryPass(model, agentId, note, reasonCode, saleId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while cancelling delivery pass.");
		}
	}	
	
	public void reactivateDeliveryPass(DeliveryPassModel model) throws FDResourceException{
		try {
			this.getCrmManagerSB().reactivateDeliveryPass(model);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while cancelling delivery pass.");
		}
	}

	public String lookupAccount(String accountNum) throws FDResourceException {
		try {
			return this.getCrmManagerSB().lookupAccount(accountNum);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while performing Account Lookup.");
		}
	}
	
	public List<String> lookupOrders(String accountNum) throws FDResourceException {
		try {
			return this.getCrmManagerSB().lookupOrders(accountNum);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while performing Order Lookup.");
		}
	}
	
	private CrmManagerSB getCrmManagerSB() throws FDResourceException {
		try {
			CrmManagerHome home = (CrmManagerHome) serviceLocator.getRemoteHome("freshdirect.crm.Manager");
			return home.create();
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} catch (CreateException e) {
			throw new FDResourceException(e, "Cannot create CrmManagerSB");
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public void logViewAccount(CrmAgentModel agent, String customerID)throws FDResourceException {
		try {
			this.getCrmManagerSB().logViewAccount(agent, customerID);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while log view account activity.");
		}
	}
	
	public void logViewAccount(String agentId, String customerID)throws FDResourceException {
		try {
			this.getCrmManagerSB().logViewAccount(agentId, customerID);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while log view account activity.");
		}
	}
	
	/*public void logViewAccount(CrmAgentModel agent, String customerID,EnumAccountActivityType activityType, String maskedAcctNumber)throws FDResourceException {
		try {
			this.getCrmManagerSB().logViewAccount(agent, customerID, activityType, maskedAcctNumber);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while log view account activity.");
		}
	}*/
	
	public void logViewAccount(String agentId, String customerID,EnumAccountActivityType activityType, String maskedAcctNumber)throws FDResourceException {
		try {
			this.getCrmManagerSB().logViewAccount(agentId, customerID, activityType, maskedAcctNumber);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while log view account activity.");
		}
	}
	
	public ErpCannedText createCannedText(ErpCannedText cannedText) throws FDResourceException {
		try {
			return this.getCrmManagerSB().createCannedText(cannedText);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while creating canned text.");
		}		
	}

	public void updateCannedText(ErpCannedText cannedText, String id) throws FDResourceException {
		try {
			this.getCrmManagerSB().updateCannedText(cannedText, id);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while updating canned text.");
		}		
	}
	
	public void deleteCannedText(String id) throws FDResourceException {
		try {
			this.getCrmManagerSB().deleteCannedText(id);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while deleting canned text.");
		}		
	}
	
	public ErpCannedText getCannedTextById(String id) throws FDResourceException {
		try {
			return this.getCrmManagerSB().getCannedTextById(id);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while getting canned text.");
		}		
	}

	public Collection<ErpCannedText> getAllCannedTextInCategory(EnumCannedTextCategory category) throws FDResourceException {
		try {
			return this.getCrmManagerSB().getAllCannedTextInCategory(category);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while getting all canned text in category.");
		}		
	}

	public Collection<ErpCannedText> getAllCannedText() throws FDResourceException {
		try {
			return this.getCrmManagerSB().getAllCannedText();
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while getting all canned text.");
		}		
	}

	public Map<String,Set<EnumComplaintDlvIssueType>> getDeliveryIssueTypes(String customerId) throws FDResourceException {
		try {
			Map<String,Set<String>> m = this.getCrmManagerSB().getComplaintDeliveryIssueTypes(customerId);
			Map<String,Set<EnumComplaintDlvIssueType>> t_m = new HashMap<String,Set<EnumComplaintDlvIssueType>>();

			// transform 
			for ( Map.Entry<String,Set<String>> e : m.entrySet() ) {
				Set<EnumComplaintDlvIssueType> t_s = new HashSet<EnumComplaintDlvIssueType>();
				for ( String s : e.getValue() ) {
					t_s.add( EnumComplaintDlvIssueType.getEnum( s )  );
				}				
				t_m.put(e.getKey(), t_s);
			}
			return t_m;
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while getting delivery issue types.");
		}
	}

	
	public String getLastDeliveredOrder(String customerId) throws FDResourceException {
		try {
			return this.getCrmManagerSB().getLastDeliveredOrder(customerId);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error in CrmManagerSB while getting first delivered sale.");
		}
	}
}
