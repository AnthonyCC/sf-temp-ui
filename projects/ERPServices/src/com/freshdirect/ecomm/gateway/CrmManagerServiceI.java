package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.FinderException;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerCreditModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;

public interface CrmManagerServiceI {
	        
	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;

	public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo) throws FDResourceException, RemoteException;
            
    public List<CrmCaseOperation> getOperations() throws FDResourceException, RemoteException;
    			
	public PrimaryKey createLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException,RemoteException;
	
	public void updateLateIssue(CrmLateIssueModel lateIssue) throws FinderException,FDResourceException,RemoteException;
	
	public CrmLateIssueModel getLateIssueById(String id) throws FinderException, FDResourceException,RemoteException;

	public Collection<CrmLateIssueModel> getLateIssuesByRouteAndDate(String route,Date date) throws  FDResourceException,RemoteException;
	
	public Collection<CrmLateIssueModel> getLateIssuesByDate(Date date) throws FDResourceException, RemoteException;

	public List<ErpTruckInfo> getTruckNumbersForDate(Date date)throws FDResourceException,RemoteException;

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
	
	public List<String> lookupOrders(String accountNum) throws FDResourceException, RemoteException;
	
	public void logViewAccount(CrmAgentModel agent, String customerID) throws FDResourceException, RemoteException;
	
	public void logViewAccount(CrmAgentModel agent, String customerID,EnumAccountActivityType activityType,String maskedAcctNumber)throws FDResourceException, RemoteException;
	
	public ErpCannedText createCannedText(ErpCannedText cannedText) throws FDResourceException, RemoteException;

	public void updateCannedText(ErpCannedText cannedText, String id) throws FDResourceException, RemoteException;

	public void deleteCannedText(String id) throws FDResourceException, RemoteException;
	
	public ErpCannedText getCannedTextById(String id) throws FDResourceException, RemoteException;

	public Collection<ErpCannedText> getAllCannedTextInCategory(EnumCannedTextCategory category) throws FDResourceException, RemoteException;

	public Collection<ErpCannedText> getAllCannedText() throws FDResourceException, RemoteException;

	public Map<String,Set<String>> getComplaintDeliveryIssueTypes(String erpCustomerId) throws FDResourceException, RemoteException;
	
	public String getLastDeliveredOrder(String erpCustomerId) throws FDResourceException, RemoteException;
	
	public CrmAgentModel getAgentByLdapId(String agentLdapId) throws FDResourceException, CrmAuthenticationException,RemoteException;
	
	public List<CrmAuthInfo> getAuthorizations(CrmAgentRole role,CrmAuthSearchCriteria filter)throws FDResourceException, RemoteException;
	
	public CustomerCreditModel getOrderForLateCredit(String saleId, String autoId) throws FDResourceException, RemoteException;
	
	public boolean isCaseCreatedForOrderLateDelivery(String saleId) throws FDResourceException, RemoteException;
	
	public boolean isOrderCreditedForLateDelivery(String saleId) throws FDResourceException, RemoteException;
	
	public DeliveryPassModel getDeliveryPassInfoById(String dlvPassId) throws FDResourceException, RemoteException;
	
	public void updateAutoLateCredit(String autoId, String orderId) throws FDResourceException, RemoteException;
	
	public DeliveryPassModel getActiveDP(String custId) throws FDResourceException, RemoteException;
	
	public void updateLateCreditsRejected(String autoId, String agent) throws FDResourceException, RemoteException;
	
	public boolean isDlvPassAlreadyExtended(String orderId, String customerId) throws FDResourceException, RemoteException;
	
	public String getAllowedUsers() throws FDResourceException, RemoteException;

	public boolean isCRMRestrictionEnabled() throws FDResourceException, RemoteException;
}
