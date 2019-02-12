package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicateDisplayNameException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.gateway.DlvPassManagerService;
import com.freshdirect.ecomm.gateway.GiftCardManagerService;
import com.freshdirect.ecomm.gateway.OrderServiceApiClient;
import com.freshdirect.ecomm.gateway.OrderServiceApiClientI;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.ecomm.gateway.CustomerAddressService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerComplaintService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerDeliveryPassService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerGiftCardService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerIdentityService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerInfoService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerNotificationService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerOrderService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerPaymentService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerPreferenceService;
import com.freshdirect.fdstore.ecomm.gateway.CustomersApi;
import com.freshdirect.fdstore.ecomm.gateway.FDSurveyService;
import com.freshdirect.fdstore.ecomm.gateway.OrderResourceApiClient;
import com.freshdirect.fdstore.ecomm.gateway.RegistrationService;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.iplocator.IpLocatorEventDTO;
import com.freshdirect.fdstore.mail.CrmSecurityCCCheckEmailVO;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.mail.TellAFriend;
import com.freshdirect.fdstore.mail.TellAFriendProduct;
import com.freshdirect.fdstore.mail.TellAFriendRecipe;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.fdstore.sms.shortsubstitute.ShortSubstituteResponse;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XSLTransformer;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.GiftCardApplicationStrategy;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.logistics.delivery.model.RouteStopInfo;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.storeapi.content.ContentFactory;


/**
 *
 *
 * @version $Revision:90$
 * @author $Author:Mike Rose$
 */
public class FDCustomerManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDCustomerManager.class);

	private static FDCustomerManagerHome managerHome = null;
	private static MailerGatewayHome mailerHome = null;
	//private static FDServiceLocator LOCATOR = FDServiceLocator.getInstance();
	FDCustomerEStoreModel customerSmsPreferenceModel=null;

	/**
	 * Register and log in a new customer.
	 *
	 * @param ErpCustomerModel erpCustomer
	 * @param FDCustomerModel fdCustomer
	 *
	 * @return the resulting RegistrationResult
	 * @throws FDResourceException if an error occured using remote resources
	 * @throws ErpDuplicateUserIdException if user enters an email address already in the system
	 * @throws ErpFraudException if a user enters information which violates a fraud prevention rule
	 */
	public static RegistrationResult register(
		FDActionInfo info,
		ErpCustomerModel erpCustomer,
		FDCustomerModel fdCustomer,
		String cookie,
		boolean pickupOnly,
		boolean eligibleForPromotion,
		FDSurveyResponse survey, EnumServiceType serviceType)
		throws FDResourceException, ErpDuplicateUserIdException,ErpFraudException {

		return register( info, erpCustomer, fdCustomer, cookie, pickupOnly, eligibleForPromotion, survey, serviceType, false);
	}

	public static RegistrationResult register(FDActionInfo info, ErpCustomerModel erpCustomer,
			FDCustomerModel fdCustomer, String cookie, boolean pickupOnly, boolean eligibleForPromotion,
			FDSurveyResponse survey, EnumServiceType serviceType, boolean isGiftCardBuyer)
			throws FDResourceException, ErpDuplicateUserIdException, ErpFraudException {

		try {
			return RegistrationService.getInstance().register(info, erpCustomer, fdCustomer, cookie, pickupOnly,
					eligibleForPromotion, survey, serviceType, isGiftCardBuyer);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser createNewUser(String zipCode, EnumServiceType serviceType, EnumEStoreId eStoreId)
			throws FDResourceException {
		try {
			return RegistrationService.getInstance().createNewUser(zipCode, serviceType, eStoreId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser createNewUser(AddressModel address, EnumServiceType serviceType, EnumEStoreId eStoreId)
			throws FDResourceException {

		try {
			return RegistrationService.getInstance().createNewUser(address != null ? address.getZipCode() : null,
					serviceType, eStoreId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser recognize(String cookie, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {
		FDUser user = null;
		try {
			user = CustomerIdentityService.getInstance().recognize(cookie, eStoreId);
			
			populateShoppingCart(user, true, true);
			return user;

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	private static void populateShoppingCart(FDUser user, boolean updateUserState, boolean userCtx)
			throws FDResourceException {
		restoreReservations(user);
		assumeDeliveryAddress(user, userCtx);
		//Set user Pricing context at this point before recalcualting the price during cleanup.
		user.getShoppingCart().setUserContextToOrderLines(user.getUserContext());

		user.getShoppingCart().doCleanup();
		classifyUser(user);
		if (updateUserState)
			user.updateUserState(false);
		//user.resetPricingContext();
		updateZoneInfo(user);
		//restoreReservations(user);
	}

	public static FDUser recognize(FDIdentity identity) throws FDAuthenticationException, FDResourceException {
		//The method was changed as part of task PERF-22.
		return recognize(identity, null, null,null);
	}
	public static FDUser recognize(FDIdentity identity, boolean updateUserState, EnumEStoreId eStoreId ) throws FDAuthenticationException, FDResourceException {
		return recognize(identity, null, eStoreId,null, updateUserState, true);
	}

	public static FDUser recognize(FDIdentity identity, MasqueradeContext ctx) throws FDAuthenticationException, FDResourceException {

		return recognize(identity, ctx, true);
	}

	public static FDUser recognize(FDIdentity identity, MasqueradeContext ctx, boolean updateUserState) throws FDAuthenticationException, FDResourceException {

		return recognize(identity, null, null, ctx, updateUserState, true);
	}

	public static FDUser recognize(FDIdentity identity, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {
		return recognize(identity, null, eStoreId,null);
	}

	public static FDUser recognize(FDIdentity identity, EnumTransactionSource source) throws FDAuthenticationException, FDResourceException {
		return recognize(identity, source, null,null);
	}

	public static FDUser recognize(FDIdentity identity, EnumTransactionSource source, EnumEStoreId eStoreId,MasqueradeContext ctx) throws FDAuthenticationException, FDResourceException {
		return recognize(identity, source, eStoreId, ctx, true, true);
	}
	
	public static FDUser recognize(FDIdentity identity, EnumTransactionSource source, EnumEStoreId eStoreId,
			MasqueradeContext ctx, boolean updateUserState, boolean populateShoppingCart)
			throws FDAuthenticationException, FDResourceException {
		
		try {
			FDUser user = null;
			user = CustomerIdentityService.getInstance().recognize(identity, eStoreId, false, false);
			
			user.setApplication(source);
			user.setMasqueradeContext(ctx);

			if (user.isVoucherHolder()
					&& EnumEStoreId.FDX.equals(user.getUserContext().getStoreContext().getEStoreId())) {
				throw new FDAuthenticationException("voucherredemption");
			}
			if (populateShoppingCart) {
				populateShoppingCart(user, updateUserState, true);
			}
			return user;

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void updateZoneInfo (FDUserI user) throws FDResourceException {
		ErpAddressModel address = user.getShoppingCart().getDeliveryAddress();
		if(address != null) {
			Date day = DateUtil.truncate(DateUtil.addDays(new Date(), 1));
			try {
					user.getShoppingCart().setZoneInfo(FDDeliveryManager.getInstance().getZoneInfo(address, day,user.getHistoricOrderSize(), user.getRegionSvcType(address.getId()), (user.getIdentity()!=null)?user.getIdentity().getErpCustomerPK():null));
			} catch (FDInvalidAddressException e) {
					LOGGER.info("Encountered InvalidAddressException while getting zoneInfo for address: "
						+ address.getAddress1()
						+ " "
						+ address.getAltApartment()
						+ " "
						+ address.getCity()
						+ " "
						+ address.getState()
						+ " "
						+ address.getZipCode());
				}
		}
	}

    private static void assumeDeliveryAddress(FDUser user, boolean userCtx) throws FDResourceException {
		FDIdentity identity = user.getIdentity();

		String partentOrderId=null;
		if(user.getMasqueradeContext()!=null)
		{
			 partentOrderId=user.getMasqueradeContext().getParentOrderId();
		}

		if(user.getShoppingCart()==null)
			return;
		/*
		 else if(user.getShoppingCart().getDeliveryAddress()!=null)

			return;
			*/
    	if(identity != null){
    		
    		ErpAddressModel address = null;

			try {
				address = CustomerAddressService.getInstance().assumeDeliveryAddress(identity, partentOrderId,
						ContentFactory.getInstance().getStoreKey().getId());
			} catch (Exception e) {
			}

    			if(address != null && user.getShoppingCart() != null){
   					user.getShoppingCart().setDeliveryAddress(address);
   					if(userCtx)user.resetUserContext();
   					user.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
    			}
    		
		}
	}

	private static void restoreReservations(FDUser user) throws FDResourceException {
		FDIdentity identity = user.getIdentity();
		if (identity != null) {

			List<FDReservation> rsvList = FDDeliveryManager.getInstance().getReservationsForCustomer((user.getMasqueradeContext()!=null)?EnumTransactionSource.CUSTOMER_REP.getCode():EnumTransactionSource.WEBSITE.getCode(), identity.getErpCustomerPK());
			if(rsvList!=null && rsvList.size() != 0){
				for ( FDReservation rsv : rsvList ) {

					//TimeslotLogic.applyOrderMinimum(user, rsv.getTimeslot());
					//TODO check if the reservation is already used by an order from the same customer
					List<String> rsvIds = getUsedReservations(identity.getErpCustomerPK());
					if(!rsvIds.contains(rsv.getId())){
						if (EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
							user.getShoppingCart().setDeliveryReservation(rsv);
							LOGGER.info(">>> Reservation is set to the cart "+rsv);
						} else {
							user.setReservation(rsv);
							LOGGER.info(">>> Reservation is set to the user "+rsv);
						}
					}
				}
			}
		}
	}

	public static List<String> getUsedReservations(String customerId) throws FDResourceException {

		try {
			return CustomerInfoService.getInstance().getUsedReservations(customerId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	private static void classifyUser(FDUser user) throws FDResourceException {

		Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();

		EnumServiceType lastServiceType = user.getSelectedServiceType();
		if (lastServiceType == null) {
			lastServiceType = EnumServiceType.HOME;
		}

		if (user.getDepotCode() != null) {
			availableServices.add(EnumServiceType.DEPOT);
		}

		FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(user.getZipCode(),
				(user.getUserContext()!=null
				&& user.getUserContext().getStoreContext()!=null)?user.getUserContext().getStoreContext().getEStoreId():EnumEStoreId.FD);
		EnumDeliveryStatus status = serviceResult.getServiceStatus(lastServiceType);
		availableServices.addAll(serviceResult.getAvailableServices());

		if (EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status)) {
			user.setSelectedServiceType(lastServiceType);
		} else {
			user.setSelectedServiceType(EnumServiceType.PICKUP);
		}

		user.setAvailableServices(availableServices);
	}

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
	public static FDIdentity login(String userId, String password) throws FDAuthenticationException, FDResourceException {
		try {
				return CustomerIdentityService.getInstance().login(userId, password);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDIdentity login(String userId) throws FDAuthenticationException, FDResourceException {
		try {
			return CustomerIdentityService.getInstance().login(userId, null);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static PrimaryKey getCustomerId(String userId) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getCustomerId(userId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getCustomerInfo(identity);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerInfo getSOCustomerInfo(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getSOCustomerInfo(identity);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Get all the payment methods of the customer.
	 *
	 * @param identity the customer's identity reference
	 *
	 * @return collection of ErpPaymentMethodModel objects
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerPaymentService.getInstance().getPaymentMethods(identity);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @param paymentMethods
	 * @return
	 */
	public static boolean disconnectInvalidPPAccount(ErpPaymentMethodI paymentMethod , FDIdentity identity){
		try{
		  if(paymentMethod != null ){
			  if (paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals(""+EnumEwalletType.PP.getValue())) {
				  boolean isValid = isValidVaultToken(paymentMethod.getProfileID(), paymentMethod.getCustomerId());
				  if(isValid){
					  return true;

				  }else{
					  // Delete the Vault Token as it is invalid
					  deleteLongAccessToken(identity.getErpCustomerPK(), ""+EnumEwalletType.PP.getValue());
					  return false;
			 	}
			  }
		   }
		}catch(FDResourceException exception){
			return true;
		}
		return true;
	}
	/**
	 * This method will call ErpEWalletSB class method
	 * @param custEWallet
	 * @return
	 */
	public static int insertLongAccessToken(ErpCustEWalletModel custEWallet){
		int rows=0;
		try {
				rows = FDECommerceService.getInstance().insertCustomerLongAccessToken(custEWallet);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		return rows;
	}

	/**
	 * This method Check the status of the given the Wallet - Enable/Disable
	 * @param custEWallet
	 * @return
	 */
	public static boolean getEwalletStatusByType(String eWalletType){
		if(EnumEwalletType.PP.getName().equalsIgnoreCase(eWalletType)){
			return FDStoreProperties.isPayPalEnabled();
		}else if(EnumEwalletType.MP.getName().equalsIgnoreCase(eWalletType)){
			return FDStoreProperties.isMasterpassEnabled();
		}
		return false;
	}

	/**
	 * This method will call ErpEWalletSB class method to get the Status of Masterpass (Enable/Disable) for Mobile
	 * @param custEWallet
	 * @return
	 */
	public static boolean getEwalletMobileStatusByType(String eWalletType){
		 try {
			ErpEWalletModel erpEWalletModel = null;
				erpEWalletModel = FDECommerceService.getInstance().findEWalletByType(eWalletType);
			
			 if(erpEWalletModel!=null && erpEWalletModel.geteWalletStatus()!=null){
				 if(erpEWalletModel.getEwalletmStatus().equalsIgnoreCase("Y"))
				 	return true;
				 else
					return false;
			 }
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @param custId
	 * @param longAccessToken
	 * @param eWalletType
	 * @return
	 */
	public static int updateLongAccessToken(String custId, String longAccessToken, String eWalletType){
		int rows=0;
		try {
				rows = FDECommerceService.getInstance().updateLongAccessToken(custId, longAccessToken, eWalletType);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * Delete the Long Access Token from the Database
	 * @param custId
	 * @param longAccessToken
	 * @param data.geteWalletType()
	 * @return
	 */
	public static int deleteLongAccessToken(String custId, String eWalletID){
		int rows=0;
		try {
			rows = FDECommerceService.getInstance().deleteLongAccessToken(custId, eWalletID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static ErpCustEWalletModel findLongAccessTokenByCustID(String customerId, String eWalletType){
		
		ErpCustEWalletModel custEWalletModel = null;
		try {
			custEWalletModel = FDECommerceService.getInstance().getLongAccessTokenByCustID(customerId, eWalletType);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return custEWalletModel;
	}


	public static ErpPaymentMethodI getPaymentMethod(FDIdentity identity, String paymentId) throws FDResourceException {
		if (null != paymentId && !"".equalsIgnoreCase(paymentId)) {		//COS17-45
			Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods(identity);
			for (ErpPaymentMethodI model : paymentMethods) {
				if (paymentId.equals(model.getPK().getId())) {
					return model;
				}
			}
		}
		return null;
	}

	/**
	 * Add a payment method for the customer.
	 *
	 * @param identity
	 *            the customer's identity reference
	 * @param paymentMethod
	 *            ErpPaymentMethodI to add
	 *
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static void addPaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean paymentechEnabled,
			boolean isDebitCardSwitch) throws FDResourceException, ErpPaymentMethodException, ErpFraudException {
		try {
			CustomerPaymentService.getInstance().addPaymentMethod(info, paymentMethod, paymentechEnabled, isDebitCardSwitch);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	private static void updatePaymentMethodDefaultCard(FDActionInfo info, boolean isDebitCardSwitch, boolean isVerificationRequired, Collection<ErpPaymentMethodI> paymentMethods) throws FDResourceException {
		ErpPaymentMethodI defaultPayment = PaymentMethodUtil.getSystemDefaultPaymentMethod(info, paymentMethods, isVerificationRequired);
		setDefaultPaymentMethod(info, defaultPayment.getPK(), EnumPaymentMethodDefaultType.DEFAULT_SYS, isDebitCardSwitch);
	}

	/**
	 * update a payment method for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param paymentMethod ErpPaymentMethodI to update
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static void updatePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod)
		throws FDResourceException,  ErpPaymentMethodException {		

		try {
			CustomerPaymentService.getInstance().updatePaymentMethod(info, paymentMethod);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 *setDefaultShipToAddressPK set the default ship-to-address PK for the customer
	 * @param identity the customers' identity
	 * @param shipToAddressPK ship-to-AddressPK that will be stored
	 *
	 * throws FDResourceException if an error occurs while using the remote interface
	 */
	public static void setDefaultShipToAddressPK(FDIdentity identity, String shipToAddressPK) throws FDResourceException {
		try {
			CustomerAddressService.getInstance().setDefaultShippingAddressPK(identity, shipToAddressPK, ContentFactory.getInstance().getStoreKey().getId());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	/**
	 *getDefaultShipToAddressPK the default ship to Address PK for the customer
	 * @param identity the customers' identity
	 *
	 * throws FDResourceException if an error occurs while using the remote interface
	 */
	public static String getDefaultShipToAddressPK(FDIdentity identity) throws FDResourceException {
		return CustomerAddressService.getInstance().getDefaultShipToAddressPK(identity, ContentFactory.getInstance().getStoreKey().getId());
		
	}

	public static void setDefaultPaymentMethod(FDActionInfo info, PrimaryKey paymentMethodPK, EnumPaymentMethodDefaultType type, boolean isDebitCardSwitch) throws FDResourceException {
		
		try {
			CustomerPaymentService.getInstance().setDefaultPaymentMethod(info, paymentMethodPK.getId(), type, isDebitCardSwitch);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	

	/**
	 *getDefaultPaymentMethodPK the default payment method for the customer
	 * @param identity the customers' identity
	 *
	 * throws FDResourceException if an error occurs while using the remote interface
	 */
	public static String getDefaultPaymentMethodPK(FDIdentity identity) throws FDResourceException {
		

		try {
			return CustomerPaymentService.getInstance().getDefaultPaymentMethodPK(identity.getFDCustomerPK());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	/**
	 * remove a payment method for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param paymentMethod ErppaymentMethodI to remove
	 *
	 * throws FDResourceException if an error occured using remote resources
	 */
	public static void removePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean isDebitCardSwitch) throws FDResourceException {
		
		try {
			CustomerPaymentService.getInstance().removePaymentMethod(info, paymentMethod, isDebitCardSwitch);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 *	update the customer info
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpCustomerInfoModel to update
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static boolean updateCustomerInfo(FDActionInfo info, ErpCustomerInfoModel customerInfo) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().updateCustomerInfo(info, customerInfo);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isDisplayNameUsed(String displayName, String custId)
			throws ErpDuplicateDisplayNameException, FDResourceException {
		try {
			return CustomerInfoService.getInstance().isDisplayNameUsed(displayName, custId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void cancelReservation(FDUserI user, FDReservation reservation, EnumReservationType rsvType,
			FDActionInfo aInfo, TimeslotEvent event) throws FDResourceException {
		try {
			CustomerInfoService.getInstance().cancelReservation(user.getIdentity(), reservation, aInfo, event);

			user.setReservation(null);
			resetUserContext(user);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static FDReservation makeReservation(FDUserI user,
		String timeslotId,
		EnumReservationType rsvType,
		String addressId,
		FDActionInfo aInfo, boolean chefsTable, TimeslotEvent event,boolean isForced)
		throws FDResourceException, ReservationException {
		try {
			FDReservation rsv;
			rsv = CustomerInfoService.getInstance().makeReservation(user, timeslotId, rsvType, addressId, aInfo, chefsTable, event, isForced);
			
			if(user.getShoppingCart()!=null
					&& !(user.getShoppingCart() instanceof FDModifyCartModel)
					&& user.getShoppingCart().getDeliveryReservation() !=null
					&& user.getShoppingCart().getDeliveryReservation().getType()!=null
					&& rsv!=null
					&& rsv.getType() !=null
					&& (user.getShoppingCart().getDeliveryReservation().getType().getName().equalsIgnoreCase(EnumReservationType.ONETIME_RESERVATION.getName())
							|| user.getShoppingCart().getDeliveryReservation().getType().getName().equalsIgnoreCase(EnumReservationType.RECURRING_RESERVATION.getName()))
					&& (rsv.getType().getName().equalsIgnoreCase(EnumReservationType.ONETIME_RESERVATION.getName())
							|| rsv.getType().getName().equalsIgnoreCase(EnumReservationType.RECURRING_RESERVATION.getName())))

				user.getShoppingCart().setDeliveryReservation(null);
			user.setReservation(rsv);

			resetUserContext(user);



			return rsv;
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error talking to session bean");
		} 
	}

	private static void resetUserContext(FDUserI user)
			throws FDResourceException {
		user.resetUserContext();
		user.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));

		if (!user.getShoppingCart().isEmpty()) {
			for (FDCartLineI cartLine : user.getShoppingCart().getOrderLines()) {
				cartLine.setUserContext(user.getUserContext());
				cartLine.setFDGroup(null);//clear the group
			}
		}
		try {
			user.getShoppingCart().refreshAll(true);
		} catch (FDInvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static FDReservation validateReservation(FDUserI user, FDReservation reservation, TimeslotEvent event) throws FDResourceException {
		//TODO have to implement this method correctly with Depot and COS handling
		ErpAddressModel address = getAddress(user.getIdentity(), reservation.getAddressId());
		return FDDeliveryManager.getInstance().validateReservation(user.getHistoricOrderSize(), reservation, address, event);
	}

	public static void updateUserId(FDActionInfo info, String userId) throws FDResourceException, ErpDuplicateUserIdException {
		try {
			CustomerInfoService.getInstance().updateUserId(info, userId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	/**
	 * Get the customer's every ship to address.
	 *
	 * @param identity the customer's identity reference
	 *
	 * @return collection of ErpAddresModel objects
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection<ErpAddressModel> getShipToAddresses(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerAddressService.getInstance().getShippingAddresses(identity);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return ErpAddressModel for the specified user and addressId, null if the address is not found.
	 *
	 * @deprecated This method was duplicated. Use {@link #getAddress( FDIdentity identity, String addressId )} instead.
	 */
	@Deprecated
	public static ErpAddressModel getShipToAddress( FDIdentity identity, String shipToAddressId ) throws FDResourceException {
		return getAddress( identity, shipToAddressId );
	}

	/**
	 * Add a ship to address for the customer.
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpAddressModel to add
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static boolean addShipToAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
	throws FDResourceException, ErpDuplicateAddressException {		
		try {
			return CustomerAddressService.getInstance().addShippingAddress(info, checkUniqueness, address);
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 *	update a ship to address for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpAddressModel to update
	 *
	 * @throws FDResourceException if an error occurred using remote resources
	 */
	public static boolean updateShipToAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
			throws FDResourceException {
		boolean result = false;
		try {
			result = CustomerAddressService.getInstance().updateShippingAddress(info, checkUniqueness, address);
			FDDeliveryManager.getInstance().sendShippingAddress(address);
			return result;
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * remove a ship to address for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpAddressModel to remove
	 *
	 * @throws FDResourceException if an error occurred using remote resources
	 */
	public static void removeShipToAddress(FDActionInfo info, ErpAddressModel address) throws FDResourceException {
		try {
			CustomerAddressService.getInstance().removeShippingAddress(info, address.getPK());
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Store the user.
	 *
	 * @param user the customer's user object
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static void storeUser(FDUser user) throws FDResourceException {

		try {
			CustomerInfoService.getInstance().storeUser(user);
		}catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * [APPREQ-369] Store Cohort ID for the user
	 * @param user
	 * @throws FDResourceException
	 */
	public static void storeCohortName(FDUser user) throws FDResourceException {

		try {
			CustomerInfoService.getInstance().storeCohortName(user.getPK().getId(), user.getCohortName());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<DlvSaleInfo> getOrdersByTruck(String truckNumber, Date dlvDate) throws FDResourceException {
		try {
				OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getOrdersByTruck(truckNumber, dlvDate);
	    	
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDOrderI getOrderForCRM(FDIdentity identity, String saleId) throws FDResourceException {

		FDOrderI order = getOrderForCRM(saleId);
		if (!order.getCustomerId().equals(identity.getErpCustomerPK())) {
			throw new FDResourceException("Sale doesn't belong to customer");
		}
		return order;

	}

	public static FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().getOrder(identity, saleId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderI getOrderForCRM(String saleId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerMisc2)) {
				ErpSaleModel saleModel = OrderServiceApiClient.getInstance().getOrder(saleId);
				if (saleModel != null && saleModel.geteStoreId() != null
						&& saleModel.geteStoreId().equals(EnumEStoreId.FDX)) {
					LOGGER.info("Fetching trip and stop from logistics for fdx order " + saleId);
					RouteStopInfo info = FDDeliveryManager.getInstance().getRouteStopInfo(saleId);
					if (info != null && info.getRoute() != null && info.getStop() != null
							&& saleModel.getShippingInfo() != null) {
						saleModel.getShippingInfo().setTruckNumber(info.getRoute());
						saleModel.getShippingInfo().setStopSequence(info.getStop());
						LOGGER.info("Fetched trip " + info.getRoute() + " and stop " + info.getStop()
								+ "from logistics for fdx order " + saleId);
					}
				}

				return new FDOrderAdapter(saleModel, true);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getOrderForCRM(saleId);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderI getOrder(String saleId) throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().getOrder(saleId);

		} catch (RemoteException re) {
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getOrderCustomerId(String orderId) throws FDResourceException {

		return CustomerOrderService.getInstance().getCustomerId(orderId);

	}
	private static ErpOrderHistory getErpOrderHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpOrderHistory(Collections.<ErpSaleInfo>emptyList());
		}

		try {

			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
    		return new ErpOrderHistory(service.getOrderHistory(identity.getErpCustomerPK()));
    	
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderHistory getOrderHistoryInfo(FDIdentity identity) throws FDResourceException {
		ErpOrderHistory history = getErpOrderHistoryInfo(identity);
		return new FDOrderHistory(history.getErpSaleInfos());
	}

	public static int getOrderCountForChefsTableEligibility(FDIdentity identity) throws FDResourceException {
		ErpOrderHistory history = getErpOrderHistoryInfo(identity);
		return history.getOrderCountForChefsTableEligibility();
	}

	public static ErpPromotionHistory getPromoHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpPromotionHistory(Collections.<String, Set<String>>emptyMap());
		}

		try {
			return CustomerInfoService.getInstance().getPromoHistoryInfo(identity.getErpCustomerPK());
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	//pass-through
	public static String placeOrder(
			FDActionInfo info, FDCartModel cart, Set<String> appliedPromos,
			boolean sendEmail, CustomerRatingI cra, EnumDlvPassStatus status, boolean isFriendReferred)
		throws FDResourceException, ErpFraudException, ErpAuthorizationException,ErpAddressVerificationException, ReservationException,
			FDPaymentInadequateException, ErpTransactionException, DeliveryPassException {
		return placeOrder(info, cart, appliedPromos, sendEmail, cra, status, isFriendReferred, -1); //-1 for "not set"
	}
	public static String placeOrder(
		FDActionInfo info, FDCartModel cart, Set<String> appliedPromos,
		boolean sendEmail, CustomerRatingI cra, EnumDlvPassStatus status, boolean isFriendReferred, int fdcOrderCount)
	throws FDResourceException, ErpFraudException, ErpAuthorizationException,ErpAddressVerificationException, ReservationException,
		FDPaymentInadequateException, ErpTransactionException, DeliveryPassException {
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && (cra.isOnFDAccount()/*||EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())*/)) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
			}
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart);
			ErpChargeLineModel chargeLine=createOrder.getCharge(EnumChargeType.DELIVERY);
			if(createOrder.isDlvPassApplied())
				chargeLine.setDiscount(null);//Forcing this for delivery pass issue: APPDEV 5587
			//createOrder.setTaxationType(info.getTaxationType());
			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());
		
			String orderId;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("placeOrder_Api")){
	    		orderId = OrderResourceApiClient.getInstance().placeOrder(
						info,
						createOrder,
						appliedPromos,
						cart.getDeliveryReservation().getPK().getId(),
						sendEmail,
						cra,
						info.getAgent() == null ? null : info.getAgent().getRole(),
						status,
						isFriendReferred,
						fdcOrderCount
				);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				orderId = sb.placeOrder(info, createOrder, appliedPromos, cart.getDeliveryReservation().getPK().getId(),
						sendEmail, cra, info.getAgent() == null ? null : info.getAgent().getRole(), status,
						isFriendReferred, fdcOrderCount);
			}

			LOGGER.info(">>> Reservation "+cart.getDeliveryReservation().getPK().getId()+" "+" Order "+ orderId);

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

			return orderId;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();

			Throwable ex=re.getCause();
			if(ex instanceof ErpAddressVerificationException) throw (ErpAddressVerificationException)ex;

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (InvalidCardException ie) {
			invalidateManagerHome();
			throw new FDResourceException(ie, "Error creating session bean InvalidCardException");
		}
	}
	
	public static FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail,
			int currentDPExtendDays, boolean restoreReservation)
			throws FDResourceException, ErpTransactionException, DeliveryPassException {
		try {
			if (orderBelongsToUser(info.getIdentity(), saleId)) {

				FDReservation reservation = null;
				reservation = OrderResourceApiClient.getInstance().cancelOrder(info, saleId, sendEmail,
							currentDPExtendDays, restoreReservation);

				// invalidate quickshop past orders cache
				CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName,
						info.getIdentity().getErpCustomerPK());

				return reservation;
			}

			throw new FDResourceException("Order not found in current user's order history.");

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	//pass-through

	public static void modifyOrder(
		FDActionInfo info,
		FDModifyCartModel cart,
		Set<String> appliedPromos,
		boolean sendEmail,
		CustomerRatingI cra,
		EnumDlvPassStatus status,
		boolean hasSomeCaptures)
		throws FDResourceException, ErpTransactionException, ErpFraudException, ErpAuthorizationException,DeliveryPassException,ErpAddressVerificationException,
		FDPaymentInadequateException
		{
			modifyOrder(info, cart, appliedPromos, sendEmail, cra, status, hasSomeCaptures, -1); //-1 for "not set"
		}
	public static void modifyOrder(
		FDActionInfo info,
		FDModifyCartModel cart,
		Set<String> appliedPromos,
		boolean sendEmail,
		CustomerRatingI cra,
		EnumDlvPassStatus status,
		boolean hasSomeCaptures,
		int fdcOrderCount)
		throws FDResourceException, ErpTransactionException, ErpFraudException, ErpAuthorizationException,DeliveryPassException,ErpAddressVerificationException,
		FDPaymentInadequateException
		{

		
		try {
			String saleId = cart.getOriginalOrder().getErpSalesId();
			if (!orderBelongsToUser(info.getIdentity(), saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && (cra.isOnFDAccount()/*|| EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())*/)) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
			}

			ErpModifyOrderModel order = FDOrderTranslator.getErpModifyOrderModel(cart);
			//order.setTaxationType(info.getTaxationType());
			order.setTransactionSource(info.getSource());
			order.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());
			EnumSaleType type = cart.getOriginalOrder().getOrderType();
			boolean hasCouponDiscounts = false;
			if(EnumSaleType.REGULAR.equals(type) && (order.hasCouponDiscounts()||cart.getOriginalOrder().hasCouponDiscounts()) && !hasSomeCaptures){
				hasCouponDiscounts = true;
			}

			FDCustomerManagerSB sb = null;
//			EnumSaleType type = cart.getOriginalOrder().getOrderType();
			if (EnumSaleType.REGULAR.equals(type)){
				
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("modifyOrder_Api")){
					OrderResourceApiClient.getInstance().modifyOrder(
							info,
							saleId,
							order,
							appliedPromos,
							cart.getOriginalReservationId(),
							sendEmail,
							cra,
							info.getAgent() == null ? null : info.getAgent().getRole(),
							status,
							hasCouponDiscounts,
							fdcOrderCount
						);
					
				}else{
					if (sb == null) {
						lookupManagerHome();
						sb = managerHome.create();
					}
					
					sb.modifyOrder(
						info,
						saleId,
						order,
						appliedPromos,
						cart.getOriginalReservationId(),
						sendEmail,
						cra,
						info.getAgent() == null ? null : info.getAgent().getRole(),
						status,
						hasCouponDiscounts,
						fdcOrderCount
					);
				}
			}else if (EnumSaleType.SUBSCRIPTION.equals(type)){
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("modifyAutoRenewOrder_Api")){
					com.freshdirect.fdstore.ecomm.gateway.OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
		    		service.modifyAutoRenewOrder(
							info,
							saleId,
							order,
							appliedPromos,
							cart.getOriginalReservationId(),
							sendEmail,
							cra,
							info.getAgent() == null ? null : info.getAgent().getRole(),
							status
						);
					
				}else{
					if (sb == null) {
						lookupManagerHome();
						sb = managerHome.create();
					}
				sb.modifyAutoRenewOrder(
					info,
					saleId,
					order,
					appliedPromos,
					cart.getOriginalReservationId(),
					sendEmail,
					cra,
					info.getAgent() == null ? null : info.getAgent().getRole(),
					status
				);
				sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), saleId, type, cra);
				}
				
			}

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			Exception ex=(Exception)re.getCause();
			if(ex instanceof ErpAddressVerificationException) throw (ErpAddressVerificationException)ex;
			throw new FDResourceException(re, "Error talking to session bean");
		}catch (ErpSaleNotFoundException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (InvalidCardException ie) {
			invalidateManagerHome();
			throw new FDResourceException(ie, "Error talking to session bean InvalidCardException");
		}
	}

	/**
	 * Utility method for determining whether a given order belongs to a given user.
	 * @param FDIdentity current user
	 * @param String sale id
	 * @return boolean
	 */
	public static boolean orderBelongsToUser(FDIdentity identity, String saleId) throws FDResourceException {
	/*	Collection orders = getOrderHistoryInfo(identity).getFDOrderInfos();
		for (Iterator it = orders.iterator(); it.hasNext();) {
			FDOrderInfoI orderInfo = (FDOrderInfoI) it.next();
			if (orderInfo.getErpSalesId().equals(saleId)) {
				LOGGER.debug("verified order belongs to user");
				return true;
			}
		}
		return false;*/

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return false;
		}

		

		try {
			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
			return service.isOrderBelongsToUser(identity.getErpCustomerPK(), saleId);

		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Adds a complaint to the user's list of complaints and begins the associated credit issuing process
	 *
	 * @param ErpComplaintModel represents the complaint
	 * @param String the PK of the sale to which the complaint is to be added
	 * @throws ErpComplaintException if order was not in proper state to accept complaints
	 */
	public static PrimaryKey addComplaint(ErpComplaintModel complaint, String saleId, FDIdentity identity,
			boolean autoApproveAuthorized, Double limit) throws FDResourceException, ErpComplaintException {

		try {
			PrimaryKey complaintPk;
			//
			// add the complaint to the sale
			//
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerComplaint)) {
				complaintPk = new PrimaryKey(CustomerComplaintService.getInstance().addComplaint(complaint, saleId,
						identity.getErpCustomerPK(), identity.getFDCustomerPK(), autoApproveAuthorized, limit));
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();

				complaintPk = sb.addComplaint(complaint, saleId, identity.getErpCustomerPK(),
						identity.getFDCustomerPK(), autoApproveAuthorized, limit);
			}

			LOGGER.info("Complaint Id:" + complaintPk);
			return complaintPk;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setActive(FDActionInfo info, boolean active) throws FDResourceException {
		
		try {
			CustomerInfoService.getInstance().setActive(info, active);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isOnAlert(String customerId, String alertType) throws FDResourceException {
		try {
			return CustomerNotificationService.getInstance().isOnAlert(customerId, alertType);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return FDCartModel with unavailability info populated
	 */
	public static FDCartModel checkAvailability(FDIdentity identity, FDCartModel cart, long timeout, String isFromLogin)
			throws FDResourceException {

		boolean skipModifyLines = true;
		boolean sameDeliveryDate = true;
		if (cart instanceof FDModifyCartModel) {
			FDReservation originalReservation = ((FDModifyCartModel) cart).getOriginalOrder().getDeliveryReservation();
			Date d1 = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
			Date d2 = DateUtil.truncate(originalReservation.getStartTime());
			if (d1.before(d2)) {
				// order moved to a prior day, need to re-check everything
				skipModifyLines = false;
			}
			if (d1.after(d2) || d1.before(d2)) {
				sameDeliveryDate = false;
			}
			ErpDeliveryPlantInfoModel origPlantInfo = ((FDModifyCartModel) cart).getOriginalOrder()
					.getDeliveryPlantInfo();
			if (origPlantInfo != null
					&& !(origPlantInfo.getPlantId().equals(cart.getDeliveryPlantInfo().getPlantId()))) {

				skipModifyLines = false;
			}

		}

		// note: FDModifyCartLineI instances skipped
		ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart, skipModifyLines,
				sameDeliveryDate);
		Map<String, FDAvailabilityI> fdInvMap = null;
		long timer = System.currentTimeMillis();
		fdInvMap = OrderResourceApiClient.getInstance().checkAvailability(identity, createOrder, timeout, isFromLogin);
		timer = System.currentTimeMillis() - timer;
		Map<String, FDAvailabilityI> invs = FDAvailabilityMapper.mapInventory(cart, createOrder, fdInvMap,
				skipModifyLines, sameDeliveryDate);
		cart.setAvailability(new FDCompositeAvailability(invs));

		if (FDStoreProperties.isAtpAvailabiltyLogEnabled()) {
			int unavCount = 0;
			for (String key : invs.keySet()) {
				FDAvailabilityI inv = invs.get(key);
				FDReservation deliveryReservation = cart.getDeliveryReservation();
				DateRange requestedRange = new DateRange(deliveryReservation.getStartTime(),
						deliveryReservation.getEndTime());
				FDAvailabilityInfo info = inv.availableCompletely(requestedRange);
				if (!info.isAvailable()) {
					unavCount++;
					FDCartLineI cartLine = cart.getOrderLineById(new Integer(key));
					LOGGER.info("User " + identity + " requested " + cartLine.getQuantity() + " "
							+ cartLine.getSalesUnit() + " " + cartLine.getSkuCode() + " confirmed "
							+ (info instanceof FDStockAvailabilityInfo ? ((FDStockAvailabilityInfo) info).getQuantity()
									: 0));
				}
			}

			LOGGER.info("ATP for user " + identity + " with " + cart.numberOfOrderLines() + " lines took " + timer
					+ " msecs, affected " + unavCount + " lines");

		}

		return cart;

	}

	
	public static void doEmail(XMLEmailI email) throws FDResourceException {
		try {
			CustomerNotificationService.getInstance().doEmail(email);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeSurvey(FDSurveyResponse survey) throws FDResourceException {

		FDSurveyResponseData surveyDataRequest = buildStoreSurveyRequest(survey);
		FDSurveyService.getInstance().storeSurvey(surveyDataRequest);

	}

	private static FDSurveyResponseData buildStoreSurveyRequest(
			FDSurveyResponse survey) {
		FDSurveyResponseData surveyRequest = new FDSurveyResponseData();
		FDIdentityData fdIdentity = new FDIdentityData();
		fdIdentity.setErpCustomerPK(survey.getIdentity().getErpCustomerPK());
		fdIdentity.setFdCustomerPK(survey.getIdentity().getFDCustomerPK());
		SurveyKeyData key = new SurveyKeyData();
		key.setSurveyType(survey.getKey().getSurveyType().getLabel());
		key.setUserType(survey.getKey().getUserType().toString());
		surveyRequest.setAnswers(survey.getAnswers());
		surveyRequest.setIdentity(fdIdentity);
		surveyRequest.setKey(key);
		if(survey.getSalePk()  != null && StringUtil.isEmpty(survey.getSalePk().getId())){
			surveyRequest.setSalePk(survey.getSalePk().getId());
		}
		return surveyRequest;
	}

	public static void setProfileAttribute(FDIdentity identity, String key, String value) throws FDResourceException {
		setProfileAttribute(identity, key, value, null);
	}

	public static void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info)
			throws FDResourceException {
		try {
			CustomerInfoService.getInstance().setProfileAttribute(identity, key, value, info);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void removeProfileAttribute(FDIdentity identity, String key, FDActionInfo info) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.removeProfileAttribute(identity, key, info);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Takes an email address, verifies that it belongs to an existing customer and sends
	 * an email to that address so that a customer may recover/change a lost password.
	 * @param String email address
	 * @return success / failure
	 * @throws FDResourceException on technical error or no customer for given email address
	 */
	public static boolean sendPasswordEmail(String emailAddress, boolean toAltEmail)
			throws FDResourceException, PasswordNotExpiredException {

		try {
			return CustomerNotificationService.getInstance().sendPasswordEmail(emailAddress, toAltEmail);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isPasswordRequestExpired(String emailAddress, String passReq) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().isPasswordRequestExpired(emailAddress, passReq);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void changePassword(FDActionInfo info, String emailAddress, String password)
			throws FDResourceException, ErpInvalidPasswordException {
		try {
			//
			// Check for valid password length
			//
			if (password.length() < 6)
				throw new ErpInvalidPasswordException("Please enter a password that is at least six characters long.");
			CustomerInfoService.getInstance().changePassword(info, emailAddress, password);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static String makePreviewEmail(TellAFriend mailInfo) throws FDResourceException {


		try {
			XMLEmailI email = FDEmailFactory.getInstance().createTellAFriendEmail(mailInfo, true);
			XSLTransformer transformer = new XSLTransformer();
			return transformer.transform(email.getXML(), email.getXslPath());
		} catch (TransformerException te) {
			throw new FDResourceException(te, "Cannot transform given Email");
		}

	}


	public static void sendTellAFriendEmail(TellAFriend mailInfo, FDUserI fdUser) throws FDResourceException {

		LOGGER.debug("inside sendTellAFriendEmail");

		if (mailInfo instanceof TellAFriendProduct || mailInfo instanceof TellAFriendRecipe) {
			sendFriendEmail(mailInfo);
		} else {
			ReferralProgramInvitaionModel model=new ReferralProgramInvitaionModel();
			model.loadReferralProgInvtModel(mailInfo);
			ReferralProgramInvitaionModel referral = FDReferralManager.createReferralInvitee(model,fdUser);
			if (referral != null && referral.getPK() != null &&
					(referral.getStatus().equals(EnumReferralStatus.REFERRED))) {
				mailInfo.setReferralId(referral.getPK().getId());
				mailInfo.setReferralProgramId(referral.getReferralProgramId());
				sendFriendEmail(mailInfo);
			}
		}
	}



	private static void sendFriendEmail(TellAFriend mailInfo)  throws FDResourceException  {
		try {
			lookupMailerGatewayHome();
			MailerGatewaySB mailer = mailerHome.create();
			XMLEmailI email = FDEmailFactory.getInstance().createTellAFriendEmail(mailInfo, false);
			LOGGER.info(
				"Sending TAF email to: "
					+ email.getRecipient()
					+ " From: "
					+ email.getFromAddress().getName()
					+ " XSL Path: "
					+ email.getXslPath());
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(email);
			} else {
				mailer.enqueueEmail(email);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewaySB");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewaySB");
		}
	}

	public static void sendContactServiceEmail(FDCustomerInfo customer, String subject, String body, boolean chefstable, boolean feedback, boolean vending) throws FDResourceException {
		lookupMailerGatewayHome();
		lookupManagerHome();
		try {
			XMLEmailI email = null;
			if(ContentFactory.getInstance() != null && ContentFactory.getInstance().getStore() !=  null &&
					ContentFactory.getInstance().getStore().getContentName() != null && !ContentFactory.getInstance().getStore().getContentName().equals("FDX")) {
			if(chefstable){
				email = FDEmailFactory.getInstance().createChefsTableEmail(customer, subject, body);
			}else{
				if(feedback){
					email = FDEmailFactory.getInstance().createFeedbackEmail(customer, subject, body);
				}else if(vending){
					email = FDEmailFactory.getInstance().createVendingEmail(customer, subject, body);
				}else{
					email = FDEmailFactory.getInstance().createContactServiceEmail(customer, subject, body);
				}
			}
			}
			else
			{
				if(vending){
					email = FDEmailFactory.getInstance().createVendingEmail(customer, subject, body);
				}else{
					email = FDEmailFactory.getInstance().createContactServiceEmail(customer, subject, body);
				}
			}
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(email);
			} else {
				MailerGatewaySB mailer = mailerHome.create();
				mailer.enqueueEmail(email);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewaySB");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewaySB");
		}
	}

	public static List<String> getReminderListForToday() throws FDResourceException {

		try {
			return CustomerNotificationService.getInstance().getReminderListForToday();

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static void sendReminderEmail(String custId) throws FDResourceException {

		try {
			CustomerNotificationService.getInstance().sendReminderEmail(custId);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void createCase(CrmSystemCaseInfo caseInfo) throws FDResourceException {

		try {
			CustomerInfoService.getInstance().createCase(caseInfo);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getCreditHistory(identity);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static FDCustomerCreditHistoryModel getPendingCreditHistory(FDIdentity identity) throws FDResourceException {

		return CustomerInfoService.getInstance().getPendingCreditHistory(identity);

	}

	public static String getNextId(String schema, String sequence) throws FDResourceException {

		try {
			return CustomerInfoService.getInstance().getNextId(schema, sequence);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error Talking to session bean");
		}
	}

	private static void invalidateManagerHome() {
		managerHome = null;
	}

	private static void lookupManagerHome() {
		if (managerHome != null) {
			return;
		}
		managerHome = FDServiceLocator.getInstance().getFDCustomerManagerHome();
	}
	private static void lookupMailerGatewayHome() throws FDResourceException {
		if (mailerHome != null) {
			return;
		}
		Context ctx = null;
		try {

			ctx = FDStoreProperties.getInitialContext();
			mailerHome = (MailerGatewayHome) ctx.lookup("freshdirect.mail.MailerGateway");
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}

	public static boolean isECheckRestricted(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().isECheckRestricted(identity);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	public static boolean isCreditRestricted(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().isCreditRestricted(identity);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static List<DeliveryPassModel> getDeliveryPassesByStatus(FDIdentity identity, EnumDlvPassStatus status,
			EnumEStoreId eStoreId) throws FDResourceException {

		try {
			return DlvPassManagerService.getInstance().getDlvPassesByStatus(identity.getErpCustomerPK(), status,
					eStoreId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderHistory getOrdersByDlvPassId(FDIdentity identity, String dlvPassId) throws FDResourceException {
		
		try {

			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
			return new FDOrderHistory(service.getOrdersByDlvPassId(identity.getErpCustomerPK(), dlvPassId));

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	/**
	 * This method populates the recent order ids that are x no.of days old and used the given delivery
	 * pass.
	 * @param customerPk
	 * @param dlvPassId
	 * @return
	 * @throws FDResourceException
	 */
	public static List<DlvPassUsageLine> getRecentOrdersByDlvPassId(FDIdentity identity, String dlvPassId, int noOfDaysOld) throws FDResourceException {
		
		try {
			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
			return service.getRecentOrdersByDlvPassId(identity.getErpCustomerPK(), dlvPassId, noOfDaysOld);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Object> getDeliveryPassesInfo(FDUserI user) throws FDResourceException {
		Map<String, Object> dlvPassesInfo = new HashMap<String, Object>();
		
		try {
			FDIdentity identity = user.getIdentity();
			List<DeliveryPassModel> dlvPasses = null;
			dlvPasses = DlvPassManagerService.getInstance().getDeliveryPasses(identity.getErpCustomerPK(), user.getUserContext().getStoreContext().getEStoreId());
			
			
			if(dlvPasses == null || ((dlvPasses!=null) && dlvPasses.size() == 0)){
				//Return Empty map.
				return dlvPassesInfo;
			}

			Map<String, DlvPassUsageInfo> usageInfos = null;

			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
			usageInfos = service.getDlvPassesUsageInfo(identity.getErpCustomerPK());

			List<Object> historyInfo = null;
			for ( DeliveryPassModel model : dlvPasses ) {
				String dlvPassId = model.getPK().getId();
				DlvPassUsageInfo usageInfo = usageInfos.get(dlvPassId);

				DeliveryPassInfo info = new DeliveryPassInfo(model, usageInfo);

				EnumDlvPassStatus status = model.getStatus();
				if (model.getExpirationDate() != null) {
					// Make sure the pass has not expired.
					Date today = new Date();
					if (today.after(model.getExpirationDate())
							&& EnumDlvPassStatus.ACTIVE.equals(model
									.getStatus())) {
						status = EnumDlvPassStatus.EXPIRED;
					}
				}
				if(status == user.getDeliveryPassStatus() && DeliveryPassUtil.isDlvPassExistsStatus(status)){
					//Put it as Active item in the Map.
					Object obj=dlvPassesInfo.get(DlvPassConstants.ACTIVE_ITEM);
					if(obj!=null) {
						if(historyInfo == null){
							historyInfo = new ArrayList<Object>();
						}
						historyInfo.add(obj);
					}

					dlvPassesInfo.put(DlvPassConstants.ACTIVE_ITEM, info);
					//Calculate Refund.
					double refundAmt = DeliveryPassUtil.calculateRefund(info);
					dlvPassesInfo.put(DlvPassConstants.REFUND_AMOUNT, new Double(refundAmt));
				}else{
					if(historyInfo == null){
						historyInfo = new ArrayList<Object>();
					}
					historyInfo.add(info);
				}
			}
			dlvPassesInfo.put(DlvPassConstants.PASS_HISTORY, historyInfo);
		
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return dlvPassesInfo;
	}

	public static FDUserDlvPassInfo getDeliveryPassInfo(FDUserI user) throws FDResourceException {
		try {
			return CustomerDeliveryPassService.getInstance().getDeliveryPassInfo(user.getIdentity(), user.getUserContext().getStoreContext().getEStoreId());
			
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static boolean hasPurchasedPass(String customerPK) throws FDResourceException {

		
		try {
			return DlvPassManagerService.getInstance().hasPurchasedPass(customerPK);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static EnumDPAutoRenewalType hasAutoRenewDP(String customerPK) throws FDResourceException {
		try {
			String value = CustomerDeliveryPassService.getInstance().hasAutoRenewDP(customerPK);
			

			if (value == null) {
				return EnumDPAutoRenewalType.NONE;
			} else if (value.equalsIgnoreCase(EnumDPAutoRenewalType.YES.getValue())) {
				return EnumDPAutoRenewalType.YES;
			} else if (value.equalsIgnoreCase(EnumDPAutoRenewalType.NO.getValue())) {
				return EnumDPAutoRenewalType.NO;
			}
			return EnumDPAutoRenewalType.NONE;

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * This method was added to avoid unnecessary calls to getOrderHistoryInfo()
	 * method. 
	 * @param identity
	 * @return
	 * @throws FDResourceException
	 */
	public static int getValidOrderCount(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return 0;
		}
		
		try {
				OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getValidOrderCount(identity.getErpCustomerPK());
	    	

		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderI getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumEStoreId eStore) throws FDResourceException,ErpSaleNotFoundException {
		
		FDCustomerManagerSB sb=null;
		try {
			FDOrderI order =null;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("nonCOSOrderByStatusStore_Api")){
				order=CustomersApi.getInstance().getLastNonCOSOrder(customerID, saleType, saleStatus, eStore);
			}else{
				lookupManagerHome();
				sb = managerHome.create();
				order = sb.getLastNonCOSOrder( customerID, saleType, saleStatus, eStore );	
			}

			return order;
		} catch ( CreateException ce ) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static FDOrderI getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumEStoreId eStore) throws FDResourceException,ErpSaleNotFoundException {
		
		FDCustomerManagerSB sb=null;
		FDOrderI order=null;
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("nonCOSOrderByStatusStore_Api")){
				order=CustomersApi.getInstance().getLastNonCOSOrder(customerID, saleType, eStore );

			}else {
				lookupManagerHome();
				sb = managerHome.create();
				order = sb.getLastNonCOSOrder( customerID, saleType, eStore );
			}
			return order;
		} catch ( CreateException ce ) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String placeSubscriptionOrder( FDActionInfo info,
			 FDCartModel cart,
			 Set<String> appliedPromos,
			 boolean sendEmail,
			 CustomerRatingI cra,
			 EnumDlvPassStatus status ) throws FDResourceException,
      						  				   ErpFraudException,
      						  				   //ReservationException,
      						  				   DeliveryPassException,
      						  				   FDPaymentInadequateException,
      						  				   InvalidCardException,
      						  				   ErpTransactionException{
		return placeSubscriptionOrder (info,cart,appliedPromos,sendEmail,cra,status,false);
	}

	public static String placeSubscriptionOrder(FDActionInfo info, FDCartModel cart, Set<String> appliedPromos,
			boolean sendEmail, CustomerRatingI cra, EnumDlvPassStatus status, boolean isRealTimeAuthNeeded)
			throws FDResourceException, ErpFraudException,
			DeliveryPassException, FDPaymentInadequateException,InvalidCardException, ErpTransactionException {
		String orderId = "";
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt)
					&& (cra.isOnFDAccount()/*
											 * ||EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().
											 * getPaymentMethodType())
											 */)) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
			}
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart);
			createOrder.setTransactionSource(info.getSource());
			// createOrder.setTaxationType(info.getTaxationType());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());

			if (FDStoreProperties.isSF2_0_AndServiceEnabled("placeSubscriptionOrder_Api")) {
				orderId = OrderResourceApiClient.getInstance().placeSubscriptionOrder(info, createOrder, appliedPromos,
						cart.getDeliveryReservation().getPK().getId(), sendEmail, cra,
						info.getAgent() == null ? null : info.getAgent().getRole(), status, isRealTimeAuthNeeded);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				orderId = sb.placeSubscriptionOrder(info, createOrder, appliedPromos,
						cart.getDeliveryReservation().getPK().getId(), sendEmail, cra,
						info.getAgent() == null ? null : info.getAgent().getRole(), status, isRealTimeAuthNeeded);
				if (!isRealTimeAuthNeeded && null != createOrder.getPaymentMethod() && !EnumPaymentMethodType.GIFTCARD
						.equals(createOrder.getPaymentMethod().getPaymentMethodType())) {
					sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), orderId,
							EnumSaleType.SUBSCRIPTION, cra);
				}
			}

			// invalidate quickshop past orders cache
			CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName,
					info.getIdentity().getErpCustomerPK());

			return orderId;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		} catch (ErpSaleNotFoundException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static String placeGiftCardOrder( FDActionInfo info,
			 FDCartModel cart,
			 Set<String> appliedPromos,
			 boolean sendEmail,
			 CustomerRatingI cra,
			 EnumDlvPassStatus status,
			 List<ErpRecipentModel> repList, boolean isBulkOrder) throws ServiceUnavailableException, FDResourceException,
      						  				   ErpFraudException,
      						  				   ErpAuthorizationException,ErpAddressVerificationException
	{
		lookupManagerHome();
		String orderId = "";
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && (cra.isOnFDAccount()/*||EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())*/)) {
				cart.getPaymentMethod().setPaymentType(
						EnumPaymentType.ON_FD_ACCOUNT);
			}
			ErpCreateOrderModel createOrder = FDOrderTranslator
					.getErpCreateOrderModel(cart);

			/*  -- */
			createOrder.setRecepientsList(repList);
			String custId = info.getIdentity().getErpCustomerPK();
			updateOrderLineInRecipentModels(createOrder, repList, custId);
			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null
					: info.getAgent().getUserId());
			// Clear all charges
			createOrder.setCharges(new ArrayList<ErpChargeLineModel>());

			/*  -- */
			
			
			FDCustomerManagerSB sb = managerHome.create();

			if(FDStoreProperties.isSF2_0_AndServiceEnabled("placeGiftCardOrder_Api")){
	    		orderId =  OrderResourceApiClient.getInstance().placeGiftCardOrder(info, createOrder,
						appliedPromos, cart.getDeliveryReservation().getPK()
						.getId(), sendEmail, cra,
				info.getAgent() == null ? null : info.getAgent().getRole(),
				status, isBulkOrder);
	    	}else{

			orderId = sb.placeGiftCardOrder(info, createOrder,
					appliedPromos, cart.getDeliveryReservation().getPK()
							.getId(), sendEmail, cra,
					info.getAgent() == null ? null : info.getAgent().getRole(),
					status, isBulkOrder);
	    	}

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

			return orderId;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();

			Exception ex = (Exception) re.getCause();
			if (ex instanceof ErpAddressVerificationException)
				throw (ErpAddressVerificationException) ex;

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


    public static void updateOrderLineInRecipentModels(ErpCreateOrderModel model,List<ErpRecipentModel> recipentList,String custId){
    	List<ErpOrderLineModel> orderLines=model.getOrderLines();
    	if(orderLines!=null && orderLines.size()>0){
    		//System.out.println("orderline size :"+orderLines.size());
    		for(int i=0;i<orderLines.size();i++){
    			ErpOrderLineModel lineModel=orderLines.get(i);
    			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(lineModel.getSku().getSkuCode())){
    			  ErpRecipentModel rModel=recipentList.get(i);
    			  rModel.setCustomerId(custId);
    			  System.out.println("lineModel.getOrderLineNumber() :"+lineModel.getOrderLineNumber());
    			  System.out.println("lineModel.getPrice() :"+lineModel.getPrice());
    			  rModel.setOrderLineId(lineModel.getOrderLineNumber());
    			}

    		}
    	}
    }

	public static FDUser getFDUser(FDIdentity identity) throws FDAuthenticationException, FDResourceException {
		try {
			return CustomerIdentityService.getInstance().recognize(identity);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Object[] getAutoRenewalInfo(EnumEStoreId eStore) throws FDResourceException {
		Object[] autoRenewInfo = null;
		
		try {
			autoRenewInfo = DlvPassManagerService.getInstance().getAutoRenewalInfo(eStore);
			
			return autoRenewInfo;
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeProductRequest(List<FDProductRequest> productRequest) throws FDResourceException {
		try {
			CustomerInfoService.getInstance().storeProductRequest(productRequest);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getLastOrderId(FDIdentity identity, EnumEStoreId eStoreId) throws FDResourceException {
		try {
			OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
			return service.getLastOrderId(identity.getErpCustomerPK(), eStoreId);

		} catch (RemoteException exception) {
			throw new FDResourceException(exception, "Error talking session bean");
		}
	}

	public static FDOrderI getLastOrder(FDIdentity identity, EnumEStoreId eStoreId) {
		FDOrderI lastOrder = null;
		String lastOrderId;
		try {
			lastOrderId = getLastOrderId(identity, eStoreId);
		} catch (Exception e) {
			LOGGER.error("Error in getLastOrderId: identity=" + identity + ", eStoreId=" + eStoreId, e);
			return null;
		}
		try {
			if (lastOrderId != null) {
				lastOrder = getOrder(lastOrderId);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getLastOrder: lastOrderId=" + lastOrderId, e);
		}
		return lastOrder;
	}

	public static ErpAddressModel getLastOrderAddress(FDIdentity identity)
			throws FDResourceException {
		
		return getLastOrderAddress(identity, null);
		
	}
	
	/**
	 * @return ErpAddressModel for the specified user and addressId, null if the address is not found.
	 * @throws FDResourceException
	 */
	public static ErpAddressModel getAddress( FDIdentity identity, String id ) throws FDResourceException {
		
		try {
			return CustomerAddressService.getInstance().getAddress(identity, id);
		} catch ( RemoteException re ) {
			throw new FDResourceException( re, "Error talking to session bean" );
		}
	}


	public static ErpGiftCardModel applyGiftCard(FDIdentity identity,
			String givexNum, FDActionInfo info)
			throws ServiceUnavailableException, InvalidCardException,
			CardInUseException, CardOnHoldException, FDResourceException {
		
		try {
			return CustomerGiftCardService.getInstance().applyGiftCard(identity, givexNum, info);
			

		} catch (InvalidCardException ie) {
			throw ie;
		} catch (CardInUseException ce) {
			throw ce;
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Get all the payment methods of the customer.
	 *
	 * @param identity
	 *            the customer's identity reference
	 *
	 * @return collection of ErpPaymentMethodModel objects
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static FDGiftCardInfoList getGiftCards(FDIdentity identity) throws FDResourceException {
		try {
			return new FDGiftCardInfoList(CustomerGiftCardService.getInstance().getGiftCards(identity));
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGiftCardModel verifyStatusAndBalance(
			ErpGiftCardModel model, boolean reloadBalance)
			throws FDResourceException {
		try {
			return GiftCardManagerService.getInstance().verifyStatusAndBalance(model, reloadBalance);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getGiftCardRecepientsForCustomer(FDIdentity identity)
			throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardRecepientsForCustomer(identity.getErpCustomerPK());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map getGiftCardRecepientsForOrders(List saleIds)
			throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardRecepientsForOrders(saleIds);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getGiftCardOrdersForCustomer(FDIdentity identity)
			throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardOrdersForCustomer(identity.getErpCustomerPK());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGCDlvInformationHolder getRecipientDlvInfo(
			FDIdentity identity, String saleId, String certificationNum)
			throws FDResourceException {
		try {
				return CustomerGiftCardService.getInstance().getRecipientDlvInfo(identity, saleId, certificationNum);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean resendEmail(String saleId, String certificationNum,
			String resendEmailId, String recipName, String personalMsg,
			EnumTransactionSource source) throws FDResourceException {
		try {
			return CustomerGiftCardService.getInstance().resendEmail(saleId, certificationNum, resendEmailId, recipName, personalMsg, source);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static GiftCardApplicationStrategy getGiftCardApplicationStrategy(FDCartModel cart) throws FDResourceException {
		
		ErpAbstractOrderModel order = null;
		if (cart instanceof FDModifyCartModel) {
			order = FDOrderTranslator.getErpModifyOrderModel(cart);
		} else {
			order = FDOrderTranslator.getErpCreateOrderModel(cart);
		}
		// Generate Applied gift cards info.
		GiftCardApplicationStrategy strategy = new GiftCardApplicationStrategy(order, null);
		strategy.generateAppliedGiftCardsInfo();
		return strategy;
	}
	public static double getOutStandingBalance(FDCartModel cart) throws FDResourceException {
		GiftCardApplicationStrategy strategy = getGiftCardApplicationStrategy(cart);
		return strategy.getRemainingBalance();

	}

	public static Object getGiftCardRedemedOrders(FDIdentity identity,
			String certNum) throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardRedeemedOrders(identity.getErpCustomerPK(), certNum);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Object getGiftCardRedemedOrders(String certNum)
			throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardRedeemedOrders( certNum);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getDeletedGiftCardsForCustomer(FDIdentity identity)
			throws FDResourceException {
		try {
			return GiftCardManagerService.getInstance().getAllDeletedGiftCard(identity.getErpCustomerPK());
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 *
	 * @param customerId
	 * @return
	 * @throws FDResourceException
	 */
	public static List getGiftCardRecepientsForOrder(String saleId)
			throws FDResourceException {
		
		try {
			return GiftCardManagerService.getInstance().getGiftCardRecepientsForOrder(saleId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum)
			throws FDResourceException {

		try {
			return GiftCardManagerService.getInstance().validateAndGetGiftCardBalance(givexNum);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} 
	}

	public static void transferGiftCardBalance(FDIdentity identity,
			String fromGivexNum, String toGivexNum, double amount)
			throws FDResourceException {
		try {
				GiftCardManagerService.getInstance().transferGiftCardBalance(identity.getErpCustomerPK(), fromGivexNum, toGivexNum,amount);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} 
	}

	public static double getPerishableBufferAmount(FDCartModel cart) throws FDResourceException {
		GiftCardApplicationStrategy strategy = getGiftCardApplicationStrategy(cart);
		return strategy.getPerishableBufferAmount();
	}

	public static String placeDonationOrder(FDActionInfo info,
			FDCartModel cart, Set<String> appliedPromos, boolean sendEmail,
			CustomerRatingI cra, EnumDlvPassStatus status, boolean isOptIn)
			throws FDResourceException, ErpFraudException,
			ErpAuthorizationException {
		
		String orderId = "";
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && (cra.isOnFDAccount()/*||EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())*/)) {
				cart.getPaymentMethod().setPaymentType(
						EnumPaymentType.ON_FD_ACCOUNT);
			}
			ErpCreateOrderModel createOrder = FDOrderTranslator
					.getErpCreateOrderModel(cart);

			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null
					: info.getAgent().getUserId());
			// Clear all charges
			createOrder.setCharges(new ArrayList<ErpChargeLineModel>());
		
			orderId =  OrderResourceApiClient.getInstance().placeDonationOrder(info, createOrder,
					appliedPromos, cart.getDeliveryReservation().getPK()
					.getId(), sendEmail, cra,
			info.getAgent() == null ? null : info.getAgent().getRole(),
			status, isOptIn);
			

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

			return orderId;
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGCDlvInformationHolder GetGiftCardRecipentByCertNum(
			String certNum) throws FDResourceException {

		try {
			return GiftCardManagerService.getInstance().loadGiftCardRecipentByCertNum(certNum);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void saveDonationOptIn(String custId, String saleId, boolean optIn) throws FDResourceException {

		try {
			CustomerGiftCardService.getInstance().saveDonationOptIn(custId, saleId, optIn);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void resubmitGCOrders() {

		try {
			CustomerGiftCardService.getInstance().resubmitGCOrders();
		} catch (RemoteException re) {
			LOGGER.warn("Error talking to session bean:" + re);
		} catch (FDResourceException fe) {
			LOGGER.warn("Error looking up for manager:" + fe);
		}
	}

	public static List<String> getTopFaqs() throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getTopFaqs();

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static CrmClick2CallModel getClick2CallInfo() throws FDResourceException {

		try {
			return CustomerInfoService.getInstance().getClick2CallInfo();

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static void sendSettlementFailedEmail(String saleId) throws FDResourceException {

		try {
			CustomerNotificationService.getInstance().sendSettlementFailedEmail(saleId);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		} 
	}

	public static void logMassCancelActivity(ErpActivityRecord record) {

		try {

			FDECommerceService.getInstance().logActivity(record);

		} catch (RemoteException e) {
			throw new EJBException(e);
		}
	}

	public static void authorizeSale(String salesId) throws FDResourceException {
		authorizeSale(salesId, false);
		}

	public static String dupeEmailAddress(String email) throws FDResourceException {
		try {
			return CustomerNotificationService.getInstance().getIdByEmail(email);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeMobilePreferences(String customerId, String fdCustomerId, String mobileNumber,
			String textOffers, String textDelivery, String orderNotices, String orderExceptions, String offers,
			String partnerMessages, EnumEStoreId eStoreId) throws FDResourceException {

		try {
			CustomerPreferenceService.getInstance().storeMobilePreferences(customerId, fdCustomerId, mobileNumber,
					textOffers, textDelivery, orderNotices, orderExceptions, offers, partnerMessages, eStoreId);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeSmsPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException {
		try {
			CustomerPreferenceService.getInstance().storeSmsPrefereceFlag(fdCustomerId, flag, eStoreId);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeGoGreenPreferences(String customerId, String goGreen) throws FDResourceException {

		try {
			CustomerPreferenceService.getInstance().storeGoGreenPreferences(customerId, goGreen);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static String loadGoGreenPreference(String customerId) throws FDResourceException {
		try {
				return CustomerPreferenceService.getInstance().loadGoGreenPreference(customerId);
			
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void sendEmail(XMLEmailI email) throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(email);
			} else {
				lookupMailerGatewayHome();
				lookupManagerHome();
				MailerGatewaySB mailer = mailerHome.create();
				mailer.enqueueEmail(email);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewaySB");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewaySB");
		}
	}

	public static void authorizeSale(String salesId, boolean force) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			 sb.authorizeSale(salesId, force);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Map<String, AssignedCustomerParam> getAssignedCustomerParams(FDUserI user)
			throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getAssignedCustomerParams(user);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void logIpLocatorEvent(IpLocatorEventDTO ipLocatorEventDTO) throws FDResourceException {
		try {
			CustomerInfoService.getInstance().logIpLocatorEvent(ipLocatorEventDTO);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static IpLocatorEventDTO loadIpLocatorEvent(String fdUserId) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().loadIpLocatorEvent(fdUserId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static CustomerAvgOrderSize getHistoricOrderSize(String customerId) throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().getHistoricOrderSize(customerId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDCartModel getSavedCart(FDIdentity identity, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {

		try {
			FDUser user;
				user = CustomerIdentityService.getInstance().recognize(identity, eStoreId, false, true);
			
			populateShoppingCart(user, true, false);

			return user.getShoppingCart();

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpAddressModel getLastOrderAddress(FDIdentity identity,EnumEStoreId eStore)
			throws FDResourceException {

		ErpAddressModel address = null;
		if (identity == null)
			return address;

		try {
			return CustomerOrderService.getInstance().getLastOrderAddress(identity, eStore);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void updateOrderInModifyState(FDOrderAdapter order) throws FDResourceException {
		try {
			CustomerOrderService.getInstance().updateOrderInModifyState(order.getSale());

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static boolean isReadyForPick(String orderNum) throws FDResourceException {
		try {
			boolean result;
			result=CustomersApi.getInstance().isReadyForPick(orderNum);
			
			return result;
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void updateOrderInProcess(String orderNum) throws FDResourceException {
		try {
			CustomerOrderService.getInstance().updateOrderInProcess(orderNum);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void releaseModificationLock(String orderId) throws FDResourceException {


		

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("ReleaseModificationLock_Api")) {
				CustomersApi.getInstance().releaseModificationLock(orderId);
			}else{
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.releaseModificationLock(orderId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}


	}

	public static boolean updateAck(FDIdentity identity, boolean acknowledge,
			String ackType) throws FDResourceException {
		boolean status=true;
		try {
				status = CustomerInfoService.getInstance().setAcknowledge(identity, acknowledge, ackType);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	return status;
	}

	public static boolean updateRAFClickIDPromoCode(FDIdentity identity, String rafclickid,
			String rafpromocode, EnumEStoreId eStoreId) throws FDResourceException {

		
		boolean status=true;
		try {
			LOGGER.info("updateRAFClickIDPromoCode: rafclickid= "  + rafclickid + ", rafpromocode=" + rafpromocode);
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerMisc1)) {
				status = CustomerInfoService.getInstance().setRAFClickIDPromoCode(identity, rafclickid, rafpromocode,
						eStoreId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				status =sb.setRAFClickIDPromoCode(identity, rafclickid, rafpromocode, eStoreId);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	
	return status;
	}

	public static String getParentOrderAddressId(String parentOrderId) throws FDResourceException {

		String parentOrderAddressId = null;
		parentOrderAddressId = CustomerAddressService.getInstance().getParentOrderAddressId(parentOrderId);

		return parentOrderAddressId;
	}

	public static boolean reSendInvoiceEmail(String OrderId) throws FDResourceException {

		try {
			LOGGER.info("reSendInvoiceEmail: OrderId= "  + OrderId);
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerMisc1)) {
				return CustomerNotificationService.getInstance().resendInvoiceEmail(OrderId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.reSendInvoiceEmail(OrderId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static boolean iPhoneCaptureEmail(String email, String zipCode, String serviceType)
			throws FDResourceException {
		try {
			return CustomerNotificationService.getInstance().iPhoneCaptureEmail(email, zipCode, serviceType);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void storeEmailPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException {
		try {
			CustomerPreferenceService.getInstance().storeEmailPreferenceFlag(fdCustomerId, flag, eStoreId);

		} catch (RemoteException e) {
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	/**
	 * @param token
	 * @return
	 * @throws FDResourceException
	 */
	public static boolean isValidVaultToken(String token, String customerId) throws FDResourceException {

		boolean isValid = false;
		try {
			isValid = FDECommerceService.getInstance().isValidVaultToken(token, customerId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

		return isValid;
	}

	public static int updateShippingInfoCartonDetails() throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().updateShippingInfoCartonDetails();

		} catch (RemoteException e) {
			LOGGER.error("Error while updating the shipping info carton details " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}

	}

	public static int[] updateShippingInfoTruckDetails() throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().updateShippingInfoTruckDetails();

		} catch (Exception e) {
			LOGGER.error("Error while updating the shipping info truck details " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static List<FDCartLineI> getModifiedCartlines(String orderId, UserContext userContext)
			throws FDResourceException {
		List<FDCartLineI> modCartLines;
		try {
			modCartLines = CustomerOrderService.getInstance().getModifiedCartlines(orderId, userContext);

			setUserContextToOrderLines(userContext, modCartLines);
			return modCartLines;

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	private static void setUserContextToOrderLines(UserContext userContext, List<FDCartLineI> cartLines) {
		if (null != cartLines) {
			List<FDCartLineI> invalids = new ArrayList<FDCartLineI>(cartLines.size());
			for (FDCartLineI cartLine : cartLines) {
				cartLine.setUserContext(userContext);

				try {
					OrderLineUtil.cleanup(cartLine);
				} catch (FDInvalidConfigurationException e) {
					invalids.add(cartLine);
					LOGGER.warn(e.getMessage());
				} catch (FDResourceException e1) {
					LOGGER.warn(e1.getMessage());
				}

			}

			for (FDCartLineI cartLine : invalids) {
				cartLines.remove(cartLine);
			}
		}
	}

	public static void storeModifiedCartline(FDUserI user, FDCartLineI newLine, String orderId)
			throws FDResourceException {
		try {
			CustomerOrderService.getInstance().saveModifiedCartline(((FDUser) user).getPK(),
					user.getUserContext().getStoreContext(), newLine, orderId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void removeModifiedCartline(FDCartLineI cartLine) throws FDResourceException {
		try {
			CustomerOrderService.getInstance().removeModifiedCartline(cartLine);
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void updateModifiedCartlineQuantity(FDCartLineI cartLine) throws FDResourceException {
		try {
			CustomerOrderService.getInstance().updateModifiedCartlineQuantity(cartLine);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void clearModifyCartlines(String currentOrderId) throws FDResourceException {
		try {
			CustomerOrderService.getInstance().clearModifyCartlines(currentOrderId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<UnsettledOrdersInfo> getUnsettledOrders(Date date) throws FDResourceException {
		try {
			return CustomerOrderService.getInstance().getUnsettledOrders();

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpCustomerModel getCustomerPaymentAndCredit(FDIdentity identity) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().getCustomerPaymentAndCredit(identity);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, List<PendingOrder>> getPendingDeliveries() throws FDResourceException {

		try {
			return CustomerOrderService.getInstance().getPendingDeliveries();

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean insertOrUpdateSilverPopup(SilverPopupDetails silverPopupDetails) throws FDResourceException {
		try {
			return CustomerInfoService.getInstance().insertOrUpdateSilverPopup(silverPopupDetails);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getCookieByFdCustomerId(String fdCustomerId) throws FDResourceException {

		try {
			return CustomerInfoService.getInstance().getCookieByFdCustomerId(fdCustomerId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static int resetDefaultPaymentValueType(String custId) throws FDResourceException {
		try {
			return CustomerPaymentService.getInstance().resetDefaultPaymentValueType(custId);
		} catch (RemoteException e) {
			LOGGER.error("Error resetting default payment type in fdcustomer " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void updateDpFreeTrialOptin(boolean dpFreeTrialOptin, String custId, FDActionInfo info)
			throws FDResourceException {
		try {
			CustomerDeliveryPassService.getInstance().setDpFreeTrialOptin(dpFreeTrialOptin, custId, info);
		} catch (RemoteException e) {
			LOGGER.error("Error at delivery pass free trial in fdcustomer " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static Date getDpFreeTrialOptinDate(String custId) throws FDResourceException {

		try {
			return CustomerDeliveryPassService.getInstance().getDpFreeTrialOptinDate(custId);

		} catch (RemoteException e) {
			LOGGER.error("Error at delivery pass free trial in fdcustomer " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

		public static  List<String> getAllCustIdsOfFreeTrialSubsOrder() throws FDResourceException{
			
			try {
				return DlvPassManagerService.getInstance().getAllCustIdsOfFreeTrialSubsOrder();
				
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

	
	public static void updateFDCustomerEStoreInfo(FDCustomerEStoreModel fdCustomerEStoreModel, String custId)
			throws FDResourceException {
		try {
				CustomerInfoService.getInstance().updateFDCustomerEStoreInfo(fdCustomerEStoreModel, custId);
		} catch (RemoteException e) {
			LOGGER.error("Error updating FDCustomerEStoreModel for custId:" + custId + " " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

		public static ShortSubstituteResponse getShortSubstituteOrders(List<String> orderList) throws FDResourceException {
			
			ShortSubstituteResponse ssResponse = null;
			try {
				ssResponse=CustomersApi.getInstance().getShortSubstituteOrders(orderList);
				
				return ssResponse;
			} catch (RemoteException re) {
				LOGGER.debug("RemoteException: ", re);
				throw new FDResourceException(re, "Error talking to session bean");
}
		}
		
	public static void updateDpOptinDetails(boolean isAutoRenewDp, String custId, String dpType, FDActionInfo info,
			EnumEStoreId eStore) throws FDResourceException {

		try {
			CustomerDeliveryPassService.getInstance().updateDpOptinDetails(isAutoRenewDp, custId, dpType, info, eStore);
		} catch (RemoteException e) {
			LOGGER.error("Error at delivery pass free trial in fdcustomer " + e);
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
}
