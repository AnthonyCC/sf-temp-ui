/*
 * $Workfile:FDCustomerManagerSB.java$
 *
 * $Date:8/27/2003 1:44:23 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.EnumIPhoneCaptureType;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDCustomerRequest;
import com.freshdirect.fdstore.customer.FDCustomerSearchCriteria;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderSearchCriteria;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.FTLEmailI;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;

/**
 *
 *
 * @version $Revision:58$
 * @author $Author:Kashif Nadeem$
 */
public interface FDCustomerManagerSB extends EJBObject {
    
    
    /**
     * Register and log in a new customer.
     *
     * @param ErpCustomerModel erpCustomer
     * @param FDCustomerModel fdCustomer
     * @param String cookie
     *
     * @return the resulting FDIdentity
     * @throws FDResourceException if an error occured using remote resources
     * @throws ErpDuplicateUserIdException if user enters an email address already in the system
     */
	public RegistrationResult register(
		FDActionInfo info,
		ErpCustomerModel erpCustomer,
		FDCustomerModel fdCustomer,
		String cookie,
		boolean pickupOnly, 
		boolean eligibleForPromotion,
		FDSurveyResponse survey, EnumServiceType serviceType)
		throws FDResourceException, ErpDuplicateUserIdException, RemoteException;
	
	public RegistrationResult register(
			FDActionInfo info,
			ErpCustomerModel erpCustomer,
			FDCustomerModel fdCustomer,
			String cookie,
			boolean pickupOnly, 
			boolean eligibleForPromotion,
			FDSurveyResponse survey, EnumServiceType serviceType, boolean isGiftCardBuyer)
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException; 
    
    public FDUser createNewUser(String zipCode, EnumServiceType serviceType) throws FDResourceException, RemoteException;
    
    public FDUser createNewUser(AddressModel address, EnumServiceType serviceType) throws FDResourceException, RemoteException;
    
    public FDUser createNewDepotUser(String depotCode, EnumServiceType serviceType) throws FDResourceException, RemoteException;
    
    public FDUser recognize(FDIdentity identity) throws FDAuthenticationException, FDResourceException, RemoteException;
    
    public FDUser recognize(String cookie) throws FDAuthenticationException, FDResourceException, RemoteException;
    
    public FDUser recognizeByEmail(String email) throws FDAuthenticationException, FDResourceException, RemoteException;
    
    public ErpAddressModel assumeDeliveryAddress(FDIdentity identity, String lastOrderId) throws FDResourceException, RemoteException;
    
    /**
     * Authenticate and log in a customer.
     *
     * @param userId
     * @param password
     *
     * @return user identity reference
     *
     * @throws FDAuthenticationException if the userId/password was not found
     * @throws FDResourceException if an error occured using remote resources
     */
    public FDIdentity login(String userId, String password) throws FDAuthenticationException, FDResourceException, RemoteException;
    
    public FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException;
    
    /**
     * Get all the payment methods of the customer.
     *
     * @param identity the customer's identity reference
     *
     * @return collection of ErpPaymentMethodModel objects
     * @throws FDResourceException if an error occured using remote resources
     */
    public Collection getPaymentMethods(FDIdentity identity) throws FDResourceException, RemoteException;
    
    
    /**
     * Add a payment method for the customer.
     *
     * @param identity the customer's identity reference
     * @param paymentMethod ErpPaymentMethodI to add
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public void addPaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod) throws FDResourceException, RemoteException, ErpDuplicatePaymentMethodException, ErpPaymentMethodException;
   
    public void setDefaultPaymentMethod(FDActionInfo info, PrimaryKey paymentMethodPK) throws FDResourceException,RemoteException;
    
    /****
     * return the Pk Id that is stored in the defaultPaymentMethodPK field
     *@ param Identity the customers Identity
     *
     *Throws FDresourceException
     */
    public String getDefaultPaymentMethodPK(FDIdentity identity) throws FDResourceException,RemoteException;

    /**
     * update a payment method for the customer
     *
     * @param identity the customer's identity reference
     * @param paymentMethod ErpPaymentMethodI to update
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public void updatePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod) throws FDResourceException, RemoteException, ErpDuplicatePaymentMethodException, ErpPaymentMethodException;
    
    
    /**
     * remove a payment method for the customer
     *
     * @param identity the customer's identity reference
     * @param pk PrimaryKey of the paymentMethod to remove
     *
     * throws FDResourceException if an error occured using remote resources
     */
    public void removePaymentMethod(FDActionInfo info, PrimaryKey pk) throws FDResourceException, RemoteException;

	public boolean checkBillToAddressFraud(FDActionInfo info, ErpPaymentMethodI paymentMethod) throws FDResourceException, RemoteException;
    
    /**
     * update customer info
     *
     * @param identity the customer's identity reference
     * @param customerInfo ErpCustomerInfoModel of the updated customer info
     *
     * throws FDResourceException if an error occured using remote resources
     */
    public boolean updateCustomerInfo(FDActionInfo info, ErpCustomerInfoModel customerInfo) throws FDResourceException, RemoteException;
    
    public void updateUserId(FDActionInfo info, String userId) throws FDResourceException, ErpDuplicateUserIdException, RemoteException;
    
    public void updatePasswordHint(FDIdentity identity, String passwordHint) throws FDResourceException, RemoteException;
    
    /**
     * Get the customer's every ship to address.
     *
     * @param identity the customer's identity reference
     *
     * @return collection of ErpAddresModel objects
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public Collection getShipToAddresses(FDIdentity identity) throws FDResourceException, RemoteException;
    
    
    /**
     * Add a ship to address for the customer.
     *
     * @param identity the customer's identity reference
     * @param address ErpAddressModel to add
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public boolean addShipToAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address) throws FDResourceException, ErpDuplicateAddressException, RemoteException;
    
    /**
     *	update a ship to address for the customer
     *
     * @param identity the customer's identity reference
     * @param address ErpAddressModel to update
     *
     * @throws FDResourceException if an error occured usong remote resources
     */
    public boolean updateShipToAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address) throws FDResourceException, ErpDuplicateAddressException, RemoteException;
    
    /**
     * remove a ship to address for the customer
     *
     * @param identity the customer's identity reference
     * @param pk PrimayKey of the address to remove
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public void removeShipToAddress(FDActionInfo info, PrimaryKey pk) throws FDResourceException, RemoteException;
    
    /***
     *Set the default Ship To address PK on the customer
     *
     *@Param identity the customer's Identity
     *@param PK Primary Key of the Ship To Address
     **/
    public void setDefaultShipToAddressPK(FDIdentity identity, String shipToAddressPK) throws FDResourceException,RemoteException;
    
    /***
     *Get the default Ship To address PK on the customer
     *
     *@Param identity the customer's Identity
     **/
    public String getDefaultShipToAddressPK(FDIdentity identity) throws FDResourceException,RemoteException;
    
    
    /**
     * Store the user.
     *
     * @param user the customer's user object
     *
     * @throws FDResourceException if an error occured using remote resources
     */
    public void storeUser(FDUser user) throws FDResourceException, RemoteException;
    
    // [APPREQ-369] store Cohort ID that belongs to user
    public void storeCohortName(FDUser user) throws FDResourceException, RemoteException;
    
    public void storeSavedRecipients(FDUser user, List recipientList) throws FDResourceException, RemoteException;
    
    public void storeSavedRecipient(FDUser user, SavedRecipientModel srm) throws FDResourceException, RemoteException;
    
    public void updateSavedRecipient(FDUser user, SavedRecipientModel srm) throws FDResourceException, RemoteException;
    
    public void deleteSavedRecipients(FDUser user) throws FDResourceException, RemoteException;
    
    public void deleteSavedRecipient(String savedRecipientId) throws FDResourceException, RemoteException;
    
    public List loadSavedRecipients(FDUser user) throws FDResourceException, RemoteException;
    
    public List getOrdersByTruck(String truckNumber, Date dlvDate) throws FDResourceException, RemoteException; 

	public FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException, RemoteException;
    
    public FDOrderI getOrder(String saleId) throws FDResourceException, RemoteException;
    
    public ErpSaleModel getErpSaleModel(String saleId) throws FDResourceException, RemoteException;
    
    /**
     * Get lightweight info about a customer's orders.
     *
     * @param identity the customer's identity reference
     */
    public ErpOrderHistory getOrderHistoryInfo(FDIdentity identity) throws FDResourceException, RemoteException;
    
    
    /**
     * Get lightweight info about a customer's used promotions.
     *
     * @param identity the customer's identity reference
     */
    public ErpPromotionHistory getPromoHistoryInfo(FDIdentity identity) throws FDResourceException, RemoteException;
    
    /**
     * Place an order (send msg to SAP, persist order).
     *
     * @param identity the customer's identity reference
     * @return String sale id
     * @throws FDResourceException if an error occured while accessing remote resources
     */
	public String placeOrder(
		FDIdentity identity,
		ErpCreateOrderModel createOrder,
		Set usedPromotionCodes,
		String rsvId,
		boolean sendEmail,
		CustomerRatingI cra,
		CrmAgentRole agentRole,
		EnumDlvPassStatus status,
		boolean pr1)
		throws FDResourceException,
		ErpFraudException,
		ErpAuthorizationException,
		ErpAddressVerificationException,
		ReservationException,
		DeliveryPassException,
		FDPaymentInadequateException,
		ErpTransactionException,
		RemoteException;

	/**
     * Cancel an order (cancel & send msg to SAP).
     *
     * @param identity the customer's identity reference
     * @throws FDResourceException if an error occured while accessing remote resources
     */
    public FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail) throws FDResourceException, ErpTransactionException, DeliveryPassException, RemoteException;
    
    /**
     * Modify an order (modify & send msg to SAP).
     *
     * @param identity the customer's identity reference
     * @throws FDResourceException if an error occured while accessing remote resources
     */
    public void modifyOrder(
		FDIdentity identity,
		String saleId,
		ErpModifyOrderModel order,
		Set usedPromotionCodes,
		String oldReservationId,
		boolean sendEmail,
		CustomerRatingI cra,
		CrmAgentRole agentRole,
		EnumDlvPassStatus status,
		boolean pr1)
		throws FDResourceException,
		ErpFraudException,
		ErpAuthorizationException,
		ErpTransactionException,
		DeliveryPassException,
		FDPaymentInadequateException,
		ErpAddressVerificationException,
		RemoteException;

    /**
     * Modify an auto renew order (modify, don't send msg to SAP).
     *
     * @param identity the customer's identity reference
     * @throws FDResourceException if an error occured while accessing remote resources
     */
    public void modifyAutoRenewOrder(
		FDIdentity identity,
		String saleId,
		ErpModifyOrderModel order,
		Set usedPromotionCodes,
		String oldReservationId,
		boolean sendEmail,
		CustomerRatingI cra,
		CrmAgentRole agentRole,
		EnumDlvPassStatus status)
		throws FDResourceException,
		ErpFraudException,
		ErpAuthorizationException,
		ErpTransactionException,
		DeliveryPassException,
		FDPaymentInadequateException,
		RemoteException;

    /**
     * Charge an order (modify & send msg to SAP).
     *
     * @param identity the customer's identity reference
     * @throws FDResourceException if an error occured while accessing remote resources
     */
    public void chargeOrder(
		FDIdentity identity,
		String saleId,
		ErpPaymentMethodI paymentMethod,
		boolean sendEmail,
		CustomerRatingI cra,
		CrmAgentModel agent,
		double additionalCharge)
		throws FDResourceException,
		ErpFraudException,
		ErpAuthorizationException,
		ErpTransactionException,
		FDPaymentInadequateException,
		ErpAddressVerificationException,
		RemoteException;

    /**
     * Adds a complaint to the user's list of complaints and begins the associated credit issuing process
     *
     * @param ErpComplaintModel represents the complaint
     * @param String the PK of the sale to which the complaint is to be added
     * @throws FDResourceException if an error occured while accessing remote resources
     * @throws ErpComplaintException if order was not in proper state to accept complaints
     */
    public void addComplaint(ErpComplaintModel complaint, String saleId,String erpCustomerId,String fdCustomerId ) throws FDResourceException, ErpComplaintException, RemoteException;
    
    public void approveComplaint(String complaintId, boolean isApproved, String csrId, boolean sendMail) throws FDResourceException, ErpComplaintException, RemoteException;
    
    public List getComplaintsForAutoApproval() throws FDResourceException, ErpComplaintException, RemoteException;
    
    /**
     * Check availability of an order.
     *
     * @return Map of order line number / FDAvailabilityI objects
     */
    public Map checkAvailability(FDIdentity identity, ErpCreateOrderModel createOrder, long timeout) throws FDResourceException, RemoteException;
    
    /**
     * CallCenter method for locating all customers meeting the given criteria
     *
     * @param custNumber
     * @param firstName
     * @param middleName
     * @param lastName
     * @param email
     * @param phone
     * @return Collection of CustomerSearchResult objects
     * @throws FDResourceException if an error occured while accessing remote resources
     */
    public List locateCustomers(FDCustomerSearchCriteria criteria) throws FDResourceException, RemoteException;
    
    public List locateOrders(FDOrderSearchCriteria criteria) throws FDResourceException, RemoteException;
    
    public void setActive(FDActionInfo info, boolean active) throws RemoteException, FDResourceException;
    
    public void doEmail(XMLEmailI email) throws RemoteException, FDResourceException;
    
    public boolean sendPasswordEmail(String emailAddress, boolean tAltEmail) throws RemoteException, FDResourceException, PasswordNotExpiredException ;
    
    public boolean isCorrectPasswordHint(String emailAddress, String hint) throws FDResourceException, ErpFraudException, RemoteException;
    
    public boolean isPasswordRequestExpired(String emailAddress, String passReq) throws FDResourceException, RemoteException;
    
    public void changePassword(FDActionInfo info, String emailAddress, String password) throws FDResourceException, RemoteException;
    
    public void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info) throws RemoteException, FDResourceException;
    
    public void removeProfileAttribute(FDIdentity identity, String key, FDActionInfo info) throws RemoteException, FDResourceException;

    public void setSignupPromotionEligibility(FDActionInfo info, boolean eligible) throws FDResourceException, RemoteException;
    
    public String getDefaultDepotLocationPK(FDIdentity identity) throws FDResourceException, RemoteException;
    
    public void setDefaultDepotLocationPK(FDIdentity identity, String locationId) throws FDResourceException, RemoteException;
    
    public String getDepotCode(FDIdentity identity) throws FDResourceException, RemoteException;
    
    public void setDepotCode(FDIdentity identity, String depotCode) throws FDResourceException, RemoteException;
    
    public String generatePasswordRequest(PrimaryKey fdCustomerPk, java.util.Date expiration) throws FDResourceException, RemoteException;
        
	public Map getProductPopularity() throws FDResourceException, RemoteException;
        
//	public List loadPromotions() throws FDResourceException, RemoteException;

	public List getReminderListForToday() throws FDResourceException, RemoteException;

	public void sendReminderEmail(PrimaryKey custPk) throws FDResourceException, RemoteException;
	
	public void authorizeSale(String salesId)throws FDResourceException, RemoteException;
	
	public void createCase(CrmSystemCaseInfo caseInfo) throws RemoteException, FDResourceException;

	public FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity) throws FDResourceException, RemoteException;
	
	public FDReservation makeReservation(FDIdentity identity, FDTimeslot timeslot, EnumReservationType rsvType, String addressId, FDActionInfo aInfo, boolean chefsTable) throws FDResourceException, ReservationException, RemoteException;
	
	public void updateWeeklyReservation(FDIdentity identity, FDTimeslot timeslot, String addressId, FDActionInfo aInfo) throws FDResourceException, RemoteException;
	
	public FDReservation changeReservation(FDIdentity identity, FDReservation oldReservation, FDTimeslot timeslot, EnumReservationType rsvType, String addressId, FDActionInfo aInfo) throws FDResourceException, ReservationException, RemoteException;
	
	public void cancelReservation (FDIdentity identity, FDReservation reservation, EnumReservationType type, FDActionInfo actionInfo) throws FDResourceException, RemoteException;
	
	public List getRecurringReservationList() throws FDResourceException, RemoteException;
	
	public List getRecurringReservationList(int day_of_week) throws FDResourceException, RemoteException;

	public void storeCustomerRequest(FDCustomerRequest cr) throws FDResourceException, RemoteException;
	
	public boolean isECheckRestricted(FDIdentity identity)  throws FDResourceException, RemoteException;
	
	public boolean isReferrerRestricted(FDIdentity identity)  throws FDResourceException, RemoteException;
	
	public String getNextId(String schema, String sequence) throws FDResourceException, RemoteException;
	
	public Map loadProfileAttributeNames() throws FDResourceException, RemoteException;
	
	public List loadProfileAttributeNameCategories()	throws FDResourceException, RemoteException;
		
	public void setAlert(FDActionInfo info, ErpCustomerAlertModel customerAlert, boolean isOnAlert) throws RemoteException;
	
	public boolean isOnAlert(PrimaryKey pk, String alertType) throws RemoteException;
	
	public boolean isOnAlert(PrimaryKey pk)  throws RemoteException;
		
	public boolean isCustomerActive(PrimaryKey pk) throws RemoteException;
	
	public List getAlerts(PrimaryKey pk)  throws RemoteException;
	
	public List loadRewriteRules() throws FDResourceException, RemoteException;
	
	public List getDeliveryPasses(FDIdentity identity) throws RemoteException;
	
	public Map getDlvPassesUsageInfo(FDIdentity identity) throws RemoteException;
	
	public List getDeliveryPassesByStatus(FDIdentity identity, EnumDlvPassStatus status) throws RemoteException;
	
	public ErpOrderHistory getOrdersByDlvPassId(FDIdentity identity, String dlvPassId) throws RemoteException;
	
	public FDUserDlvPassInfo getDeliveryPassInfo(FDUserI user) throws FDResourceException, RemoteException;
	
	public List getRecentOrdersByDlvPassId(FDIdentity identity, String dlvPassId, int noOfDaysOld) throws FDResourceException, RemoteException;
	
	public Map cancelOrders(FDActionInfo actionInfo,  List customerOrders, boolean sendEmail) throws RemoteException;
	
	public void storeRetentionSurvey(FDIdentity fdIdentity, String profileAttr
			, String profileValue, CrmSystemCaseInfo caseInfo) throws RemoteException, FDResourceException;
	public boolean hasPurchasedPass(String customerPK) throws RemoteException, FDResourceException ;
	
	public int getValidOrderCount(FDIdentity identity) throws RemoteException, FDResourceException;
	
	public String getLastOrderID(FDIdentity identity) throws RemoteException, FDResourceException;
	
	public String hasAutoRenewDP(String customerPK) throws FDResourceException, RemoteException;
	
	public void setHasAutoRenewDP(String customerPK, EnumTransactionSource source, String initiator,boolean autoRenew)throws FDResourceException, RemoteException;
	
	public FDOrderI getLastNonCOSOrderUsingCC(String customerID,EnumSaleType saleType, EnumSaleStatus saleStatus) throws FDResourceException, RemoteException,ErpSaleNotFoundException;
	
	/**
     * Place an order (send msg to SAP, persist order).

     *
     * @param identity the customer's identity reference
     * @return String sale id
     * @throws FDResourceException if an error occured while accessing remote resources
     */
	public String placeSubscriptionOrder( FDIdentity identity,
		                      ErpCreateOrderModel createOrder,
		                      Set usedPromotionCodes,
		                      String rsvId,
		                      boolean sendEmail,
		                      CustomerRatingI cra,
		                      CrmAgentRole agentRole,
		                      EnumDlvPassStatus status
		                    ) throws FDResourceException,
		                             ErpFraudException,
		                             //ReservationException,
		                             DeliveryPassException,
		                             FDPaymentInadequateException,
		                             RemoteException;	

	
	public String placeGiftCardOrder( FDIdentity identity,
            ErpCreateOrderModel createOrder,
            Set usedPromotionCodes,
            String rsvId,
            boolean sendEmail,
            CustomerRatingI cra,
            CrmAgentRole agentRole,
            EnumDlvPassStatus status,boolean isBulkOrder
          ) throws ServiceUnavailableException, FDResourceException,
          ErpFraudException,
          ErpAuthorizationException,
          ErpAddressVerificationException,
          RemoteException;
	

	public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo)
	throws ErpTransactionException, RemoteException;
	
	public void authorizeSale(String erpCustomerID, String saleID, EnumSaleType type,CustomerRatingI cra) throws FDResourceException, ErpSaleNotFoundException, RemoteException;
	public  Object[] getAutoRenewalInfo()throws FDResourceException, RemoteException;

	public boolean isOrderBelongsToUser(FDIdentity identity, String saleId) throws RemoteException, FDResourceException;
	/**
     * Get lightweight info object about a customer's past orders.
     *
     * @param identity the customer's identity reference
     */
    public OrderHistoryI getWebOrderHistoryInfo(FDIdentity identity) throws FDResourceException, RemoteException;
        
    public  String getAutoRenewSKU(String customerPK)throws FDResourceException, RemoteException;
    
    public void logCustomerVariant(String saleId, FDIdentity identity, String feature, String variantId) throws RemoteException, FDResourceException;

    public ErpAddressModel getLastOrderAddress(String lastOrderId) throws FDResourceException, RemoteException, SQLException;
    
    public void storeProductRequest(List productRequest,FDSurveyResponse survey) throws RemoteException, FDResourceException;

    public void storeProductRequest(List productRequest) throws RemoteException, FDResourceException;
    
    public ErpAddressModel getAddress(FDIdentity identity,String id) throws FDResourceException, RemoteException;


	public void assignAutoCaseToComplaint(ErpComplaintModel complaint, PrimaryKey autoCasePK) throws RemoteException, FDResourceException;
    
    //For Gift Cards
    public ErpGiftCardModel applyGiftCard(FDIdentity identity, String givexNum, FDActionInfo info) throws ServiceUnavailableException, InvalidCardException, CardInUseException, CardOnHoldException, FDResourceException, RemoteException;
    
    /**
     * Get all the Gift cards of the customer.
     *
     * @param identity the customer's identity reference
     *
     * @return collection of ErpPaymentMethodModel objects
     * @throws FDResourceException if an error occured using remote resources
     */
    public Collection getGiftCards(FDIdentity identity) throws FDResourceException, RemoteException;
    
    public List verifyStatusAndBalance(List giftcards, boolean reloadBalance ) throws FDResourceException, RemoteException;
    
    public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard, boolean reloadBalance ) throws FDResourceException, RemoteException;
    
    public List getGiftCardRecepientsForCustomer(FDIdentity identity) throws FDResourceException, RemoteException;

    public ErpGCDlvInformationHolder getRecipientDlvInfo(FDIdentity identity, String saleId, String certificationNum) throws FDResourceException, RemoteException;
    
    public boolean resendEmail(String saleId, String certificationNum, String resendEmailId, String recipName, String personMsg, EnumTransactionSource source) throws FDResourceException, RemoteException;
    
    public boolean resendEmail(String saleId, String certificationNum, String resendEmailId, String recipName, String personMsg, boolean toPurchaser, boolean toLastRecipient, EnumTransactionSource source) throws FDResourceException, RemoteException;
    
    public double getOutStandingBalance(ErpAbstractOrderModel order) throws FDResourceException, RemoteException;
    
    public EnumIPhoneCaptureType iPhoneCaptureEmail(String emailId) throws FDResourceException, RemoteException;

    public void doEmail(FTLEmailI email) throws RemoteException, FDResourceException;
    
    public void preAuthorizeSales(String salesId) throws RemoteException, FDResourceException;
    
	public List getGiftCardOrdersForCustomer(FDIdentity identity) throws RemoteException, FDResourceException;

	public Object getGiftCardRedemedOrders(FDIdentity identity, String certNum) throws RemoteException, FDResourceException;
	
	public Object getGiftCardRedemedOrders(String certNum) throws RemoteException, FDResourceException;

	public List getDeletedGiftCardForCustomer(FDIdentity identity) throws RemoteException, FDResourceException;
    
    public List getGiftCardRecepientsForOrder(String saleId) throws FDResourceException, RemoteException;
    
    public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum) throws InvalidCardException, RemoteException;
    
    public void transferGiftCardBalance(FDIdentity identity,String fromGivexNum,String toGivexNum,double amount) throws RemoteException;
    
    public String[] sendGiftCardCancellationEmail(String saleId, String certNum, boolean toRecipient, boolean toPurchaser, boolean newRecipient, String newRecipientEmail) throws RemoteException, FDResourceException;
    
    public String placeDonationOrder( FDIdentity identity,
            ErpCreateOrderModel createOrder,
            Set usedPromotionCodes,
            String rsvId,
            boolean sendEmail,
            CustomerRatingI cra,
            CrmAgentRole agentRole,
            EnumDlvPassStatus status, boolean isOptIn
          ) throws RemoteException, FDResourceException,
                   ErpFraudException,
                   ErpAuthorizationException ;
    
    public double getPerishableBufferAmount(ErpAbstractOrderModel order) throws RemoteException, FDResourceException;
    
    public Map getGiftCardRecepientsForOrders(List saleIds) throws RemoteException, FDResourceException ;
    
    public ErpGCDlvInformationHolder GetGiftCardRecipentByCertNum(String certNum) throws RemoteException,FDResourceException ;
    
    public void saveDonationOptIn(String custId, String saleId, boolean optIn)throws RemoteException,FDResourceException ;
    
    public void resubmitGCOrders() throws RemoteException, FDResourceException;
    
    public List getTopFaqs() throws FDResourceException, RemoteException;
    
    public List productRequestFetchAllDepts() throws FDResourceException, RemoteException;
    public List productRequestFetchAllCats() throws FDResourceException, RemoteException;
    public List productRequestFetchAllMappings() throws FDResourceException, RemoteException;
}

