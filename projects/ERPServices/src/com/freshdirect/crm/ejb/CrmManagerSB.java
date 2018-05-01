package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.customer.CustomerCreditModel;
import com.freshdirect.framework.core.PrimaryKey;
/**
 *@deprecated This method is moved to backoffice project.
 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
 */
@Deprecated 
public interface CrmManagerSB extends EJBObject {
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public PrimaryKey createAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, ErpDuplicateUserIdException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public void updateAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentModel getAgentByPk(String agentPk) throws FDResourceException, FinderException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentList getAllAgents() throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public List<CrmCaseModel> findCases(CrmCaseTemplate template) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmCaseModel getCaseByPk(String casePk) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public CrmAgentModel loginAgent(String username, String password) throws FDResourceException, CrmAuthenticationException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public boolean lockCase(PrimaryKey agentPK, PrimaryKey casePK) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public void unlockCase(PrimaryKey casePK) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public boolean closeAutoCase(PrimaryKey casePK) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public PrimaryKey createCase(CrmCaseModel caseModel) throws FDResourceException, RemoteException,CrmAuthorizationException;
    // need to check whether it needs to be migrated to SF 2.0
	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;
    // need to check whether it needs to be migrated to SF 2.0
	public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction action, PrimaryKey agentPk) throws FDResourceException, CrmAuthorizationException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public List<CrmQueueInfo> getQueueOverview() throws FDResourceException, RemoteException;
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated 
	public List<CrmAgentInfo> getCSROverview() throws FDResourceException, RemoteException;
    // need to check whether it needs to be migrated to SF 2.0
    public List<CrmCaseOperation> getOperations() throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void downloadCases(PrimaryKey agentPK, String queue, String subject, int numberToDownload) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CrmStatus getSessionStatus(PrimaryKey agentPK) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void saveSessionStatus(CrmStatus status) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public PrimaryKey createLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void updateLateIssue(CrmLateIssueModel lateIssue) throws FinderException,FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CrmLateIssueModel getLateIssueById(String id) throws FinderException, FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public Collection<CrmLateIssueModel> getLateIssuesByRouteAndDate(String route,Date date) throws  FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public Collection<CrmLateIssueModel> getLateIssuesByDate(Date date) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public List<ErpTruckInfo> getTruckNumbersForDate(Date date)throws FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CrmLateIssueModel getRecentLateIssueForOrder(String orderId)throws FDResourceException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CrmCustomerHeaderInfo getCustomerHeaderInfo(String customerId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void incrDeliveryCount(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			int delta, 
			String note, 
			String reasonCode, 
			String saleId)  throws FDResourceException, CrmAuthorizationException, RemoteException;
    
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void incrExpirationPeriod(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			int noOfDays, 
			String note, 
			String reasonCode, 
			String saleId)  throws FDResourceException, CrmAuthorizationException, RemoteException;
	
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void cancelDeliveryPass(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException, RemoteException;
    
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void reactivateDeliveryPass(DeliveryPassModel model) throws FDResourceException, RemoteException;
	
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public String lookupAccount(String accountNum) throws FDResourceException, RemoteException;
	
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public List<String> lookupOrders(String accountNum) throws FDResourceException, RemoteException;
	
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void logViewAccount(CrmAgentModel agent, String customerID) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void logViewAccount(CrmAgentModel agent, String customerID,EnumAccountActivityType activityType,String maskedAcctNumber)throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public ErpCannedText createCannedText(ErpCannedText cannedText) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void updateCannedText(ErpCannedText cannedText, String id) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void deleteCannedText(String id) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public ErpCannedText getCannedTextById(String id) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public Collection<ErpCannedText> getAllCannedTextInCategory(EnumCannedTextCategory category) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public Collection<ErpCannedText> getAllCannedText() throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public Map<String,Set<String>> getComplaintDeliveryIssueTypes(String erpCustomerId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public String getLastDeliveredOrder(String erpCustomerId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CrmAgentModel getAgentByLdapId(String agentLdapId) throws FDResourceException, CrmAuthenticationException,RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public List<CrmAuthInfo> getAuthorizations(CrmAgentRole role,CrmAuthSearchCriteria filter)throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public CustomerCreditModel getOrderForLateCredit(String saleId, String autoId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public boolean isCaseCreatedForOrderLateDelivery(String saleId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public boolean isOrderCreditedForLateDelivery(String saleId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public DeliveryPassModel getDeliveryPassInfoById(String dlvPassId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated 
	public void updateAutoLateCredit(String autoId, String orderId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated
	public DeliveryPassModel getActiveDP(String custId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated
	public void updateLateCreditsRejected(String autoId, String agent) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated
	public boolean isDlvPassAlreadyExtended(String orderId, String customerId) throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated
	public String getAllowedUsers() throws FDResourceException, RemoteException;
    /**
     *@deprecated This method is moved to backoffice project.
     * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
    @Deprecated
	public boolean isCRMRestrictionEnabled() throws FDResourceException, RemoteException;
}
