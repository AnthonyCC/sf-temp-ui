package com.freshdirect.fdstore.customer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumAccountActivityType;
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
import com.freshdirect.customer.ErpClientCodeReport;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
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
import com.freshdirect.customer.ErpWebOrderHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
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
import com.freshdirect.ecomm.gateway.OrderResourceApiClient;
import com.freshdirect.ecomm.gateway.OrderResourceApiClientI;
import com.freshdirect.ecomm.gateway.OrderServiceApiClient;
import com.freshdirect.ecomm.gateway.OrderServiceApiClientI;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.erp.ejb.ErpEWalletHome;
import com.freshdirect.erp.ejb.ErpEWalletSB;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.services.impl.DlvManagerDecoder;
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
import com.freshdirect.fdstore.ecomm.gateway.CustomerGiftCardService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerIdentityService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerInfoService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerNotificationService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerPaymentService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerPreferenceService;
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
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.fdstore.sms.shortsubstitute.ShortSubstituteResponse;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.IgnoreCaseString;
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
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.sms.EnumSMSAlertStatus;
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
	private static ErpEWalletHome eWalletHome = null;
	private static MailerGatewayHome mailerHome = null;
	private static FDServiceLocator LOCATOR = FDServiceLocator.getInstance();
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

	public static RegistrationResult register(
			FDActionInfo info,
			ErpCustomerModel erpCustomer,
			FDCustomerModel fdCustomer,
			String cookie,
			boolean pickupOnly,
			boolean eligibleForPromotion,
			FDSurveyResponse survey, EnumServiceType serviceType, boolean isGiftCardBuyer)
			throws FDResourceException  , ErpDuplicateUserIdException,ErpFraudException {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.Registration)) {
				return RegistrationService.getInstance().register(info, erpCustomer, fdCustomer, cookie, pickupOnly,
						eligibleForPromotion, survey, serviceType, isGiftCardBuyer);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.register(info, erpCustomer, fdCustomer, cookie, pickupOnly, eligibleForPromotion, survey,
						serviceType, isGiftCardBuyer);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser createNewUser(String zipCode, EnumServiceType serviceType, EnumEStoreId eStoreId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.Registration)) {
				return RegistrationService.getInstance().createNewUser(zipCode, serviceType, eStoreId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.createNewUser(zipCode, serviceType, eStoreId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser createNewUser(AddressModel address, EnumServiceType serviceType, EnumEStoreId eStoreId) throws FDResourceException {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.Registration)) {
				return RegistrationService.getInstance().createNewUser(address != null? address.getZipCode() : null, serviceType, eStoreId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.createNewUser(address, serviceType, eStoreId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser createNewDepotUser(String depotCode, EnumServiceType serviceType, EnumEStoreId eStoreId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.createNewDepotUser(depotCode, serviceType, eStoreId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser recognize(String cookie, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {
		FDUser user = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerIdentity)) {
				user = CustomerIdentityService.getInstance().recognize(cookie, eStoreId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				user = sb.recognize(cookie, eStoreId);
			}
			populateShoppingCart(user, true, true);
			return user;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
	/*
	 * This new method was added as part of task PERF-22. This method
	 * will be called directly from CrmGetFDUserTag to set the application
	 * source as CSR so that the CRM application knows which order history
	 * object should be loaded before the FDSessionUser object is created
	 * where it is actually set.
	 */
	public static FDUser recognize(FDIdentity identity, EnumTransactionSource source, EnumEStoreId eStoreId,
			MasqueradeContext ctx, boolean updateUserState, boolean populateShoppingCart)
			throws FDAuthenticationException, FDResourceException {
		
		try {
			FDUser user = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerIdentity)) {
				user = CustomerIdentityService.getInstance().recognize(identity, eStoreId, false, false);
			} else {

				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				user = sb.recognize(identity, eStoreId, false, false);
			}
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

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/*
	 * This new method was added as part of task PERF-22. This method
	 * will be called directly from CrmGetFDUserTag to set the application
	 * source as CSR so that the CRM application knows which order history
	 * object should be loaded before the FDSessionUser object is created
	 * where it is actually set.
	 */
	public static FDUser recognizeForCRM(FDIdentity identity, EnumTransactionSource source, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.recognize(identity, eStoreId, true);
			user.setApplication(source);
			user.setCrmMode(true);
			populateShoppingCart(user, true, false);

			return user;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
					if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
						try {
							address = CustomerAddressService.getInstance().assumeDeliveryAddress(identity, partentOrderId);
						}catch(Exception e) {}
					} else {
						lookupManagerHome();
						FDCustomerManagerSB sb = managerHome.create();
						try {
							if (partentOrderId != null)
								address = sb.assumeDeliveryAddress(identity, partentOrderId, null);
							else
								address = sb.assumeDeliveryAddress(identity, null, user);
						}catch(Exception e) {}
					}
    			}catch (CreateException ce) {
        			invalidateManagerHome();
        			throw new FDResourceException(ce, "Error creating session bean");
        		} catch (RemoteException re) {
        			invalidateManagerHome();
        			throw new FDResourceException(re, "Error talking to session bean");
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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getUsedReservations(customerId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerIdentity)) {
				return CustomerIdentityService.getInstance().login(userId, password);
			} else {
				lookupManagerHome();

				FDCustomerManagerSB sb = managerHome.create();
				return sb.login(userId, password);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDIdentity login(String userId) throws FDAuthenticationException, FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerIdentity)) {
				return CustomerIdentityService.getInstance().login(userId, null);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.login(userId);
			}
		} catch (CreateException ce) {
			ce.printStackTrace();
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			re.printStackTrace();
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}





	public static PrimaryKey getCustomerId(String userId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				return CustomerInfoService.getInstance().getCustomerId(userId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getCustomerId(userId);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				return CustomerInfoService.getInstance().getCustomerInfo(identity);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getCustomerInfo(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerInfo getSOCustomerInfo(FDIdentity identity) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				return CustomerInfoService.getInstance().getSOCustomerInfo(identity);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getSOCustomerInfo(identity);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				return CustomerPaymentService.getInstance().getPaymentMethods(identity);
			}
			lookupManagerHome();
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getPaymentMethods(identity);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
		lookupeWalletHome();
		int rows=0;
		try {
			ErpEWalletSB erpEWalletSB = eWalletHome.create();
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpEWalletSB")) {
				rows = FDECommerceService.getInstance().insertCustomerLongAccessToken(custEWallet);
			} else {
				rows = erpEWalletSB.insertCustomerLongAccessToken(custEWallet);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
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
		lookupeWalletHome();
		 try {
			 ErpEWalletSB erpEWalletSB =  eWalletHome.create();
			ErpEWalletModel erpEWalletModel = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpEWalletSB")) {
				erpEWalletModel = FDECommerceService.getInstance().findEWalletByType(eWalletType);
			} else {
				erpEWalletModel = erpEWalletSB.findEWalletByType(eWalletType);
			}
			 if(erpEWalletModel!=null && erpEWalletModel.geteWalletStatus()!=null){
				 if(erpEWalletModel.getEwalletmStatus().equalsIgnoreCase("Y"))
				 	return true;
				 else
					return false;
			 }
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
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
		lookupeWalletHome();
		int rows=0;
		try {
			ErpEWalletSB erpEWalletSB = eWalletHome.create();
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpEWalletSB")) {
				rows = FDECommerceService.getInstance().updateLongAccessToken(custId, longAccessToken, eWalletType);
			} else {
				rows = erpEWalletSB.updateLongAccessToken(custId, longAccessToken, eWalletType);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
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
		lookupeWalletHome();
		int rows=0;
		try {
			ErpEWalletSB erpEWalletSB = eWalletHome.create();
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpEWalletSB")) {
				rows = FDECommerceService.getInstance().deleteLongAccessToken(custId, eWalletID);
			} else {
				rows = erpEWalletSB.deleteLongAccessToken(custId, eWalletID);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows;
	}

	public static ErpCustEWalletModel findLongAccessTokenByCustID(String customerId, String eWalletType){
		lookupeWalletHome();
		ErpCustEWalletModel custEWalletModel = null;
		try {
			ErpEWalletSB erpEWalletSB = eWalletHome.create();
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpEWalletSB")) {
				custEWalletModel = FDECommerceService.getInstance().getLongAccessTokenByCustID(customerId, eWalletType);
			} else {
				custEWalletModel = erpEWalletSB.getLongAccessTokenByCustID(customerId, eWalletType);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				CustomerPaymentService.getInstance().addPaymentMethod(info, paymentMethod, paymentechEnabled, isDebitCardSwitch);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.addPaymentMethod(info, paymentMethod, paymentechEnabled);

				if (isDebitCardSwitch) {
					String paymentMethodDefaultType = getpaymentMethodDefaultType(info.getIdentity().getFDCustomerPK())
							.getName();
					if (!paymentMethodDefaultType.equals(EnumPaymentMethodDefaultType.DEFAULT_CUST.getName())) {
						if (info.getSource().equals(EnumTransactionSource.CUSTOMER_REP)) {
							if (!paymentMethodDefaultType.equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())) {
								resetDefaultPaymentValueType(info.getIdentity().getFDCustomerPK());
							} else {
								return;
							}
						} else {
							Collection<ErpPaymentMethodI> paymentMethods = getPaymentMethods(info.getIdentity());
							List<ErpPaymentMethodI> paymentMethodList = new ArrayList<ErpPaymentMethodI>(
									paymentMethods);
							if (PaymentMethodUtil.isNewCardHigherPrioriy(paymentMethod, paymentMethodList)) {
								updatePaymentMethodDefaultCard(info, isDebitCardSwitch, false, paymentMethods);
							}
						}
					}
				}
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				CustomerPaymentService.getInstance().updatePaymentMethod(info, paymentMethod);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.updatePaymentMethod(info, paymentMethod);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				CustomerAddressService.getInstance().setDefaultShippingAddressPK(identity, shipToAddressPK);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.setDefaultShipToAddressPK(identity, shipToAddressPK);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				return CustomerAddressService.getInstance().getDefaultShipToAddressPK(identity);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getDefaultShipToAddressPK(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void setDefaultPaymentMethod(FDActionInfo info, PrimaryKey paymentMethodPK, EnumPaymentMethodDefaultType type, boolean isDebitCardSwitch) throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				CustomerPaymentService.getInstance().setDefaultPaymentMethod(info, paymentMethodPK.getId(), type, isDebitCardSwitch);
			} else {
				lookupManagerHome();

				FDCustomerManagerSB sb = managerHome.create();
				sb.setDefaultPaymentMethod(info, paymentMethodPK, type, isDebitCardSwitch);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * method to get the default depot location id for given customer identified by the identity
	 *
	 * @param FDIdentity for customer
	 * @return String default Depot location id
	 * @throws FDResourceException if there are problems in accessing remote objects
	 * @deprecated
	 */

	public static String getDefaultDepotLocationPK(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDefaultDepotLocationPK(identity);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	/**
	 * method to set the default depot location id for given customer identified by the identity
	 *
	 * @param FDIdentity for customer
	 * @param String depot location id to set
	 * @throws FDResourceException if there are problems in accessing remote objects
	 * @deprecated
	 */

	public static void setDefaultDepotLocationPK(FDIdentity identity, String locationId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setDefaultDepotLocationPK(identity, locationId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				return CustomerPaymentService.getInstance().getDefaultPaymentMethodPK(identity.getFDCustomerPK());
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getDefaultPaymentMethodPK(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				CustomerPaymentService.getInstance().removePaymentMethod(info, paymentMethod, isDebitCardSwitch);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.removePaymentMethod(info, paymentMethod);

				if (isDebitCardSwitch && null != paymentMethod) {
					Collection<ErpPaymentMethodI> paymentMethods = getPaymentMethods(info.getIdentity());
					if (paymentMethods.size() == 0) {
						resetDefaultPaymentValueType(info.getIdentity().getFDCustomerPK());
					} else {
						if (info.getSource().equals(EnumTransactionSource.CUSTOMER_REP)) {
							String paymentMethodDefaultType = getpaymentMethodDefaultType(
									info.getIdentity().getFDCustomerPK()).getName();
							if (getDefaultPaymentMethodPK(info.getIdentity()).equals(paymentMethod.getPK().getId())
									|| (!getDefaultPaymentMethodPK(info.getIdentity())
											.equals(paymentMethod.getPK().getId())
											&& paymentMethodDefaultType
													.equals(EnumPaymentMethodDefaultType.DEFAULT_SYS.getName()))) {
								resetDefaultPaymentValueType(info.getIdentity().getFDCustomerPK());
							} else {
								return;
							}
						} else if (FDCustomerManager.getDefaultPaymentMethodPK(info.getIdentity())
								.equals(paymentMethod.getPK().getId())) {
							updatePaymentMethodDefaultCard(info, isDebitCardSwitch, true, paymentMethods);
						}
					}
				}
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.updateCustomerInfo(info, customerInfo);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isDisplayNameUsed(String displayName,String custId) throws ErpDuplicateDisplayNameException, FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isDisplayNameUsed(displayName,custId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static void cancelReservation(
		FDUserI user,
		FDReservation reservation,
		EnumReservationType rsvType,
		FDActionInfo aInfo, TimeslotEvent event)
		throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.cancelReservation(user.getIdentity(), reservation, rsvType, aInfo, event);
			user.setReservation(null);
			resetUserContext(user);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static FDReservation makeReservation(
		FDUserI user,
		String timeslotId,
		EnumReservationType rsvType,
		String addressId,
		FDActionInfo aInfo, boolean chefsTable, TimeslotEvent event,boolean isForced)
		throws FDResourceException, ReservationException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDReservation rsv = sb.makeReservation(user, timeslotId, rsvType, addressId, aInfo, chefsTable, event, isForced);
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
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
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

	/*public static void updateWeeklyReservation(FDIdentity identity, FDTimeslot timeslot, String addressId, FDActionInfo aInfo) throws FDResourceException {
		lookupManagerHome();
		try{
			FDCustomerManagerSB sb = managerHome.create();
			sb.updateWeeklyReservation(identity, timeslot, addressId, aInfo);
		}catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}*/

	public static void updateRecurringReservation(
		FDIdentity identity,
		Date startTime,
		Date endTime,
		String addressId,
		String initiator,
		String fdUserId)
		throws FDResourceException {

		ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);
		int dayOfWeek = (startTime != null ? DateUtil.toCalendar(startTime).get(Calendar.DAY_OF_WEEK) : 0);
		custInfo.setRsvDayOfWeek(dayOfWeek);
		custInfo.setRsvStartTime(startTime);
		custInfo.setRsvEndTime(endTime);
		custInfo.setRsvAddressId(addressId);

		FDActionInfo aInfo = new FDActionInfo(EnumEStoreId.FD,EnumTransactionSource.WEBSITE, identity, initiator, "Updated Recurring Reservation", null,fdUserId);

		updateCustomerInfo(aInfo, custInfo);

	}

	public static FDReservation validateReservation(FDUserI user, FDReservation reservation, TimeslotEvent event) throws FDResourceException {
		//TODO have to implement this method correctly with Depot and COS handling
		ErpAddressModel address = getAddress(user.getIdentity(), reservation.getAddressId());
		return FDDeliveryManager.getInstance().validateReservation(user.getHistoricOrderSize(), reservation, address, event);
	}

	public static void updateUserId(FDActionInfo info, String userId) throws FDResourceException, ErpDuplicateUserIdException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				CustomerInfoService.getInstance().updateUserId(info, userId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.updateUserId(info, userId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void updatePasswordHint(FDIdentity identity, String passwordHint) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.updatePasswordHint(identity, passwordHint);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				return CustomerAddressService.getInstance().getShippingAddresses(identity);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getShipToAddresses(identity);
			}
			

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				return CustomerAddressService.getInstance().addShippingAddress(info, checkUniqueness, address);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.addShipToAddress(info, checkUniqueness, address);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
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
			throws FDResourceException, ErpDuplicateAddressException {
		boolean result = false;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				result = CustomerAddressService.getInstance().updateShippingAddress(info, checkUniqueness, address);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				result = sb.updateShipToAddress(info, checkUniqueness, address);
			}
			FDDeliveryManager.getInstance().sendShippingAddress(address);

			return result;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				CustomerAddressService.getInstance().removeShippingAddress(info, address.getPK());
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.removeShipToAddress(info, address.getPK());
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				CustomerInfoService.getInstance().storeUser(user);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeUser(user);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * [APPREQ-369] Store Cohort ID for the user
	 * @param user
	 * @throws FDResourceException
	 */
	public static void storeCohortName(FDUser user) throws FDResourceException {
		lookupManagerHome();

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				CustomerInfoService.getInstance().storeCohortName(user.getPK().getId(), user.getCohortName());
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeCohortName(user);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<DlvSaleInfo> getOrdersByTruck(String truckNumber, Date dlvDate) throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("ordersByTruck_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getOrdersByTruck(truckNumber, dlvDate);
	    	}else{
			
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrdersByTruck(truckNumber, dlvDate);
	    	}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDOrderI getOrderForCRM(FDIdentity identity, String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrderForCRM(identity, saleId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrder(identity, saleId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			System.out.println(re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static FDOrderI getOrderForCRM(String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrderForCRM(saleId);

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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrder(saleId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpSaleModel getErpSaleModel(String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getErpSaleModel(saleId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Collection<ErpSaleModel> getErpSaleModels(FDIdentity identity) throws FDResourceException {
		ErpOrderHistory erpOrderHistory = getErpOrderHistoryInfo(identity);
		Collection<ErpSaleInfo> erpSaleInfos = erpOrderHistory.getErpSaleInfos();
		List<ErpSaleModel> erpSaleModels=new ArrayList<ErpSaleModel>();
		if(erpSaleInfos!=null) {
			for ( ErpSaleInfo saleInfo : erpSaleInfos ) {
				ErpSaleModel saleModel = getErpSaleModel(saleInfo.getSaleId());
				saleModel.setCreateDate(saleInfo.getCreateDate());
				saleModel.setDeliveryType(saleInfo.getDeliveryType());
				erpSaleModels.add(saleModel);
			}
		}

		return erpSaleModels;
	}

	private static ErpOrderHistory getErpOrderHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpOrderHistory(Collections.<ErpSaleInfo>emptyList());
		}

		lookupManagerHome();
		try {

		if(FDStoreProperties.isSF2_0_AndServiceEnabled("orderHistory_Api")){
    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
    		return new ErpOrderHistory(service.getOrderHistory(identity.getErpCustomerPK()));
    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrderHistoryInfo(identity);
    	}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			return new ErpPromotionHistory(Collections.<String,Set<String>>emptyMap());
		}

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getPromoHistoryInfo(identity);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
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

		lookupManagerHome();

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

			FDCustomerManagerSB sb = managerHome.create();
			
			String orderId;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("placeOrder_Api")){
	    		OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
	    		orderId =  service.placeOrder(
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
	    	}else{

			orderId =
				sb.placeOrder(
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


	public static FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail, int currentDPExtendDays, boolean restoreReservation)
		throws FDResourceException, ErpTransactionException, DeliveryPassException {
		if (managerHome == null) {
			lookupManagerHome();
		}

		try {
			if (orderBelongsToUser(info.getIdentity(), saleId)) {
				FDCustomerManagerSB sb = managerHome.create();
				FDReservation reservation = null;
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("cancelOrder_Api")){
					OrderResourceApiClient service = OrderResourceApiClient.getInstance();
					DlvManagerDecoder.setMapper(OrderResourceApiClient.getMapper());
					reservation =  DlvManagerDecoder.converter(service.cancelOrder(info, saleId, sendEmail, currentDPExtendDays, restoreReservation));
				}else{
					reservation = sb.cancelOrder(info, saleId, sendEmail, currentDPExtendDays, restoreReservation);
				}

				//invalidate quickshop past orders cache
                CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

				return reservation;
			}

			throw new FDResourceException("Order not found in current user's order history.");

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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

		lookupManagerHome();
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

			FDCustomerManagerSB sb = managerHome.create();
//			EnumSaleType type = cart.getOriginalOrder().getOrderType();
			if (EnumSaleType.REGULAR.equals(type)){
				
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("modifyOrder_Api")){
					OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
		    		service.modifyOrder(
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
					OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
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
				}
				sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), saleId, type, cra);
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

		lookupManagerHome();

		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("orderBelongsToUser_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.isOrderBelongsToUser(identity.getErpCustomerPK(), saleId);
	    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			return sb.isOrderBelongsToUser(identity, saleId);
	    	}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
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


	/**
	 * Rejects the specified complaint.
	 *
	 * @param String complaintId
	 * @param String saleId
	 * @param boolean isApproved
	 * @param String csrId
	 * @param java.util.Date approvedDate
	 */
	public static void approveComplaint(String complaintId, boolean isApproved, String csrId, boolean sendMail,Double limit)
		throws FDResourceException, ErpComplaintException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.approveComplaint(complaintId, isApproved, csrId, sendMail,limit);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	/**
	 * Assigns auto case to complaint and saves it to database.
	 *
	 * @param complaint
	 * @param autoCasePK
	 *
	 * @throws FDResourceException
	 */
	public static void assignAutoCaseToComplaint(PrimaryKey complaintPk, PrimaryKey autoCasePK) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			// set set case PK
//			complaint.setAutoCaseId(autoCasePK.getId());

			// update complaint in DB
			sb.assignAutoCaseToComplaint(complaintPk, autoCasePK);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}



	public static XMLEmailI makePreviewCreditEmail(FDCustomerInfo custInfo,String saleId,ErpComplaintModel complaint) throws FDResourceException {
		try {
			FDOrderI order = getOrder(saleId);
			EnumEStoreId estoreId = order.getEStoreId();
			return FDEmailFactory.getInstance().createConfirmCreditEmail(custInfo,saleId,complaint,estoreId);
		} catch (FDResourceException re) {
			throw new FDResourceException(re.getMessage());
		}
	}


	public static void setActive(FDActionInfo info, boolean active) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setActive(info, active);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setAlert(FDActionInfo info, ErpCustomerAlertModel customerAlert, boolean isOnAlert) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setAlert(info, customerAlert, isOnAlert);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<ErpCustomerAlertModel> getAlerts(String customerId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getAlerts(new PrimaryKey(customerId));
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isOnAlert(String customerId, String alertType) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				return CustomerNotificationService.getInstance().isOnAlert(customerId, alertType);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.isOnAlert(new PrimaryKey(customerId), alertType);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isOnAlert(String customerId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isOnAlert(new PrimaryKey(customerId));
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isCustomerActive(String customerId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isCustomerActive(new PrimaryKey(customerId));
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	/**
	 * @return FDCartModel with unavailability info populated
	 */
	public static FDCartModel checkAvailability(FDIdentity identity, FDCartModel cart, long timeout,String isFromLogin) throws FDResourceException {
		lookupManagerHome();
		try {

			FDCustomerManagerSB sb = managerHome.create();

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
				if(d1.after(d2) || d1.before(d2)){
					sameDeliveryDate = false;
				}
				ErpDeliveryPlantInfoModel origPlantInfo=((FDModifyCartModel) cart).getOriginalOrder().getDeliveryPlantInfo();
				if(origPlantInfo!=null && !(origPlantInfo.getPlantId().equals(cart.getDeliveryPlantInfo().getPlantId()))) {

					skipModifyLines = false;
				}

			}


			// note: FDModifyCartLineI instances skipped
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart, skipModifyLines, sameDeliveryDate);

			long timer = System.currentTimeMillis();

			Map<String, FDAvailabilityI> fdInvMap = sb.checkAvailability(identity, createOrder, timeout, isFromLogin);
			timer = System.currentTimeMillis() - timer;

			Map<String,FDAvailabilityI> invs = FDAvailabilityMapper.mapInventory(cart, createOrder, fdInvMap, skipModifyLines, sameDeliveryDate);
			cart.setAvailability(new FDCompositeAvailability(invs));

			if (LOGGER.isInfoEnabled()) {
				int unavCount = 0;
				for ( String key : invs.keySet() ) {
					FDAvailabilityI inv = invs.get(key);
					FDReservation deliveryReservation = cart.getDeliveryReservation();
					DateRange requestedRange = new DateRange(deliveryReservation.getStartTime(), deliveryReservation.getEndTime());
					FDAvailabilityInfo info = inv.availableCompletely(requestedRange);
					if (!info.isAvailable()) {
						unavCount++;
						FDCartLineI cartLine = cart.getOrderLineById(new Integer(key));
						LOGGER.info(
							"User "
								+ identity
								+ " requested "
								+ cartLine.getQuantity()
								+ " "
								+ cartLine.getSalesUnit()
								+ " "
								+ cartLine.getSkuCode()
								+ " confirmed "
								+ (info instanceof FDStockAvailabilityInfo ? ((FDStockAvailabilityInfo) info).getQuantity() : 0));
					}
				}

				LOGGER.info(
					"ATP for user "
						+ identity
						+ " with "
						+ cart.numberOfOrderLines()
						+ " lines took "
						+ timer
						+ " msecs, affected "
						+ unavCount
						+ " lines");

			}

			return cart;

		} catch (CreateException ce) {
			invalidateManagerHome();
			LOGGER.warn("Error creating session bean", ce);
			throw new FDResourceException(ce, "Error creating session bean");

		} catch (RemoteException re) {
			invalidateManagerHome();
			LOGGER.warn("Error talking to session bean", re);
			throw new FDResourceException(re, "Error talking to session bean");

		}

	}


	/**
	 * Locate customer records matching the specified criteria
	 *
	 * @param custNumber
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 * @param phone
	 *
	 * @return Collection of CustomerSearchResult objects
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static List<FDCustomerOrderInfo> locateCustomers(FDCustomerSearchCriteria criteria) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.locateCustomers(criteria);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Locate order records matching the specified criteria
	 *
	 * @param String firstName
	 * @param String lastName
	 * @param String email
	 * @param String phone
	 * @param String orderNumber
	 * @param String zipCode
	 * @param String depotAddress
	 *
	 * @return Collection of <code>FDOrderI</code>s
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */

	public static List<FDCustomerOrderInfo> locateOrders(FDOrderSearchCriteria criteria) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.locateOrders(criteria);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void doEmail(XMLEmailI email) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				CustomerNotificationService.getInstance().doEmail(email);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.doEmail(email);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeSurvey(FDSurveyResponse survey) throws FDResourceException {
	    try {
	    	if(FDStoreProperties.isSF2_0_AndServiceEnabled("survey.ejb.FDSurveySB")){
        		IECommerceService service = FDECommerceService.getInstance();
        		FDSurveyResponseData surveyDataRequest = buildStoreSurveyRequest(survey);
        		 service.storeSurvey(surveyDataRequest);
        	}
			else{
                LOCATOR.getSurveySessionBean().storeSurvey(survey);
			}
            } catch (RemoteException re) {
                throw new FDResourceException(re, "Error talking to session bean");
            }
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

	public static void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setProfileAttribute(identity, key, value, info);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				return CustomerNotificationService.getInstance().sendPasswordEmail(emailAddress, toAltEmail);
			} else {
				lookupManagerHome();

				FDCustomerManagerSB sb = managerHome.create();
				return sb.sendPasswordEmail(emailAddress, toAltEmail);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isCorrectPasswordHint(String emailAddress, String hint) throws FDResourceException, ErpFraudException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isCorrectPasswordHint(emailAddress, hint);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isPasswordRequestExpired(String emailAddress, String passReq) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				return CustomerInfoService.getInstance().isPasswordRequestExpired(emailAddress, passReq);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.isPasswordRequestExpired(emailAddress, passReq);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				CustomerInfoService.getInstance().changePassword(info, emailAddress, password);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.changePassword(info, emailAddress, password);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setSignupPromotionEligibility(FDActionInfo info, boolean eligible) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setSignupPromotionEligibility(info, eligible);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getDepotCode(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDepotCode(identity);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setDepotCode(FDIdentity identity, String depotCode) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setDepotCode(identity, depotCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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

	public static void sendCrmCCSecurityEmail(CrmSecurityCCCheckEmailVO emailVO) throws FDResourceException {
		lookupMailerGatewayHome();
		lookupManagerHome();
		try {
			XMLEmailI email = null;
			email = FDEmailFactory.getInstance().createCrmCCSecurityEmail(emailVO);
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

	public static Map<String, Integer> getProductPopularity() throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getProductPopularity();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<String> getReminderListForToday() throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				return CustomerNotificationService.getInstance().getReminderListForToday();
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getReminderListForToday();
			}
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static void sendReminderEmail(String custId) throws FDResourceException {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				CustomerNotificationService.getInstance().sendReminderEmail(custId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.sendReminderEmail(new PrimaryKey(custId));
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void createCase(CrmSystemCaseInfo caseInfo) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.createCase(caseInfo);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getCreditHistory(identity);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeCustomerRequest(FDCustomerRequest req) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeCustomerRequest(req);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static String getNextId(String schema, String sequence) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getNextId(schema, sequence);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error Talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	private static void invalidateManagerHome() {
		managerHome = null;
	}

	private static void lookupManagerHome() {
		if (managerHome != null) {
			return;
		}
		managerHome = LOCATOR.getFDCustomerManagerHome();
	}

	private static void lookupeWalletHome() {
		if (eWalletHome != null) {
			return;
		}
		eWalletHome = LOCATOR.getErpEWalletHome();
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
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isECheckRestricted(identity);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static boolean isReferrerRestricted(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isReferrerRestricted(identity);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static Map<String, ProfileAttributeName> loadProfileAttributeNames() throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.loadProfileAttributeNames();
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static List<String> loadProfileAttributeNameCategories() throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.loadProfileAttributeNameCategories();
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}
	public static List<DeliveryPassModel> getDeliveryPassesByStatus(FDIdentity identity, EnumDlvPassStatus status) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDeliveryPassesByStatus(identity, status);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderHistory getOrdersByDlvPassId(FDIdentity identity, String dlvPassId) throws FDResourceException {
		lookupManagerHome();
		try {

			if(FDStoreProperties.isSF2_0_AndServiceEnabled("ordersByDlvPass_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return new FDOrderHistory(service.getOrdersByDlvPassId(identity.getErpCustomerPK(), dlvPassId));
	    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			ErpOrderHistory history = sb.getOrdersByDlvPassId(identity, dlvPassId);
			return new FDOrderHistory(history.getErpSaleInfos());
	    	}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("orderByDlvPass_recent_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getRecentOrdersByDlvPassId(identity.getErpCustomerPK(), dlvPassId, noOfDaysOld);
	    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			return sb.getRecentOrdersByDlvPassId(identity, dlvPassId, noOfDaysOld);
	    	}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Object> getDeliveryPassesInfo(FDUserI user) throws FDResourceException {
		Map<String, Object> dlvPassesInfo = new HashMap<String, Object>();
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDIdentity identity = user.getIdentity();
			List<DeliveryPassModel> dlvPasses = sb.getDeliveryPasses(identity, user.getUserContext().getStoreContext().getEStoreId());
			if(dlvPasses == null || ((dlvPasses!=null) && dlvPasses.size() == 0)){
				//Return Empty map.
				return dlvPassesInfo;
			}

			Map<String, DlvPassUsageInfo> usageInfos = null;

			if(FDStoreProperties.isSF2_0_AndServiceEnabled("dlvPassUsage_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		usageInfos =  service.getDlvPassesUsageInfo(identity.getErpCustomerPK());
	    	}else{
	    		usageInfos = sb.getDlvPassesUsageInfo(identity);
	    	}

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
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return dlvPassesInfo;
	}

	public static FDUserDlvPassInfo getDeliveryPassInfo(FDUserI user) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDeliveryPassInfo(user, user.getUserContext().getStoreContext().getEStoreId());
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static Map<String, List<FDCustomerOrderInfo>> cancelOrders(FDActionInfo actionInfo,  List<FDCustomerOrderInfo> customerOrders, boolean sendEmail) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.cancelOrders(actionInfo, customerOrders, sendEmail);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeRetentionSurvey(FDIdentity fdIdentity, String profileAttr
			, String profileValue, CrmSystemCaseInfo caseInfo) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeRetentionSurvey(fdIdentity, profileAttr, profileValue, caseInfo);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static boolean hasPurchasedPass(String customerPK) throws FDResourceException {

		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.DlvPassManagerSB)){
				return DlvPassManagerService.getInstance().hasPurchasedPass(customerPK);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.hasPurchasedPass(customerPK);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static EnumDPAutoRenewalType hasAutoRenewDP(String customerPK) throws FDResourceException {

		lookupManagerHome();
		try {
				FDCustomerManagerSB sb = managerHome.create();
				String value= sb.hasAutoRenewDP(customerPK);
				if(value==null) {
					return EnumDPAutoRenewalType.NONE;
				}
				else if(value.equalsIgnoreCase(EnumDPAutoRenewalType.YES.getValue())) {
					return EnumDPAutoRenewalType.YES;
				}
				else if(value.equalsIgnoreCase(EnumDPAutoRenewalType.NO.getValue())) {
					return EnumDPAutoRenewalType.NO;
				}
				return EnumDPAutoRenewalType.NONE;


		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setHasAutoRenewDP(String customerPK, EnumTransactionSource source , String initiator,boolean autoRenew)throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setHasAutoRenewDP( customerPK, source, initiator, autoRenew );
		} catch ( CreateException ce ) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static OrderHistoryI getWebOrderHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpWebOrderHistory(Collections.EMPTY_LIST);
		}
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("weborderHistory_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return new ErpWebOrderHistory(service.getWebOrderHistory(identity.getErpCustomerPK()));
	    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			return sb.getWebOrderHistoryInfo(identity);
	    	}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * This method was added to avoid unnecessary calls to getOrderHistoryInfo()
	 * method. Eg: CrmResubmitOrdersTag keeps calling the getOrderHistoryInfo()
	 * for every resubmitted order where you actually need just the valid order
	 * count.
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
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("validordercount_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getValidOrderCount(identity.getErpCustomerPK());
	    	}else{

			FDCustomerManagerSB sb = managerHome.create();
			return sb.getValidOrderCount(identity);
	    	}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Logges customer ID and variant ID for a placed order.
	 *
	 * @param identity Customer identity
	 * @param saleId Order ID
	 * @throws FDResourceException
	 */
	public static void logCustomerVariants(FDUserI user, String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();

			for ( EnumSiteFeature feature : EnumSiteFeature.getEnumList() ) {
				if (feature.isSmartStore()) {
					Variant variant = VariantSelectorFactory.getSelector(feature).select(user);
					if (variant != null) {
						sb.logCustomerVariant(saleId, user.getIdentity(), feature.getName(), variant.getId());
					}
				}
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDOrderI getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumEStoreId eStore) throws FDResourceException,ErpSaleNotFoundException {
		lookupManagerHome();
		FDCustomerManagerSB sb=null;
		try {
			sb = managerHome.create();
			FDOrderI order = sb.getLastNonCOSOrder( customerID, saleType, saleStatus, eStore );
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
		lookupManagerHome();
		FDCustomerManagerSB sb=null;
		try {
			sb = managerHome.create();
			FDOrderI order = sb.getLastNonCOSOrder( customerID, saleType, eStore );
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
      						  				   FDPaymentInadequateException{
		return placeSubscriptionOrder (info,cart,appliedPromos,sendEmail,cra,status,false);
	}
	public static String placeSubscriptionOrder( FDActionInfo info,
			 									 FDCartModel cart,
			 									 Set<String> appliedPromos,
			 									 boolean sendEmail,
			 									 CustomerRatingI cra,
			 									 EnumDlvPassStatus status, boolean isRealTimeAuthNeeded ) throws FDResourceException,
			                               						  				   ErpFraudException,
			                               						  				   //ReservationException,
			                               						  				   DeliveryPassException,
			                               						  				   FDPaymentInadequateException
			                               						  				    {
		lookupManagerHome();
		String orderId="";
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && (cra.isOnFDAccount()/*||EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())*/)) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
		    }
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart);
			createOrder.setTransactionSource(info.getSource());
			//createOrder.setTaxationType(info.getTaxationType());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());

			FDCustomerManagerSB sb = managerHome.create();

			if(FDStoreProperties.isSF2_0_AndServiceEnabled("placeSubscriptionOrder_Api")){
	    		OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
	    		orderId =  service.placeSubscriptionOrder( info,
						  createOrder,
                          appliedPromos,
                          cart.getDeliveryReservation().getPK().getId(),
                          sendEmail,
                          cra,
                          info.getAgent() == null ? null : info.getAgent().getRole(),
                          status, isRealTimeAuthNeeded
                        );
	    	}else{
				orderId=sb.placeSubscriptionOrder( info,
				 								  createOrder,
				                                 appliedPromos,
				                                 cart.getDeliveryReservation().getPK().getId(),
				                                 sendEmail,
				                                 cra,
				                                 info.getAgent() == null ? null : info.getAgent().getRole(),
				                                 status, isRealTimeAuthNeeded
				                               );
	    	}
			
				if(!isRealTimeAuthNeeded && null !=createOrder.getPaymentMethod() && !EnumPaymentMethodType.GIFTCARD.equals(createOrder.getPaymentMethod().getPaymentMethodType())){
					sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), orderId, EnumSaleType.SUBSCRIPTION, cra);
				}
				

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

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
	    		OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
	    		orderId =  service.placeGiftCardOrder(info, createOrder,
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

	public static FDUser getFDUser(FDIdentity identity)
			throws FDAuthenticationException, FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.recognize(identity);
			return user;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser getFDUserByEmail(String email, EnumEStoreId eStoreId)
			throws FDAuthenticationException, FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.recognizeByEmail(email,eStoreId);
			return user;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static Object[] getAutoRenewalInfo(EnumEStoreId eStore) throws FDResourceException {
		Object[] autoRenewInfo = null;
		lookupManagerHome();
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.DlvPassManagerSB)) {
				autoRenewInfo = DlvPassManagerService.getInstance().getAutoRenewalInfo(eStore);
			}else{	
			
			FDCustomerManagerSB sb = managerHome.create();
			autoRenewInfo = sb.getAutoRenewalInfo(eStore);
			}
			
			return autoRenewInfo;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	    public static void storeProductRequest(List<FDProductRequest> productRequest) throws FDResourceException {
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeProductRequest(productRequest);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

	public static String getAutoRenewSKU(String customerPK)
			throws FDResourceException {
		String arSKU = null;
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			arSKU = sb.getAutoRenewSKU(customerPK);
			return arSKU;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getLastOrderId(FDIdentity identity) throws FDResourceException {
		String lastOrderId = null;
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("lastOrderId_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getLastOrderId(identity.getErpCustomerPK());
	    	}else{

			FDCustomerManagerSB customerManagerSessionBean = managerHome.create();
			lastOrderId = customerManagerSessionBean.getLastOrderID(identity);
	    	}
		} catch (CreateException exception) {
			invalidateManagerHome();
			throw new FDResourceException(exception, "Error creating session bean");
		} catch (RemoteException exception) {
			invalidateManagerHome();
			throw new FDResourceException(exception, "Error talking session bean");
		}
		return lastOrderId;
	}


	public static String getLastOrderId(FDIdentity identity, EnumEStoreId eStoreId) throws FDResourceException {
		String lastOrderId = null;
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("lastOrderId_Estore_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getLastOrderId(identity.getErpCustomerPK(), eStoreId);
	    	}else{

			FDCustomerManagerSB customerManagerSessionBean = managerHome.create();
			lastOrderId = customerManagerSessionBean.getLastOrderID(identity, eStoreId);
	    	}
		} catch (CreateException exception) {
			invalidateManagerHome();
			throw new FDResourceException(exception, "Error creating session bean");
		} catch (RemoteException exception) {
			invalidateManagerHome();
			throw new FDResourceException(exception, "Error talking session bean");
		}
		return lastOrderId;
	}

	public static FDOrderI getLastOrder(FDIdentity identity) throws FDResourceException {
		FDOrderI lastOrder = null;
		String lastOrderId = getLastOrderId(identity);
		if (lastOrderId != null) {
			lastOrder = getOrder(lastOrderId);
		}
		return lastOrder;
	}

	public static FDOrderI getLastOrder(FDIdentity identity, EnumEStoreId eStoreId) throws FDResourceException {
		FDOrderI lastOrder = null;
		String lastOrderId = getLastOrderId(identity, eStoreId);
		if (lastOrderId != null) {
			lastOrder = getOrder(lastOrderId);
		}
		return lastOrder;
	}

	public static ErpAddressModel getLastOrderAddress(FDIdentity identity)
			throws FDResourceException {
		ErpAddressModel address = null;
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			String lastOrderId = sb.getLastOrderID(identity);
			address = sb.getLastOrderAddress(lastOrderId);
			return address;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		} catch (SQLException se) {
			invalidateManagerHome();
			throw new FDResourceException(se, "Error running SQL");
		}
	}

	public static void storeProductRequest(List<FDProductRequest> productRequest,
			FDSurveyResponse survey) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeProductRequest(productRequest, survey);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return ErpAddressModel for the specified user and addressId, null if the address is not found.
	 * @throws FDResourceException
	 */
	public static ErpAddressModel getAddress( FDIdentity identity, String id ) throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				return CustomerAddressService.getInstance().getAddress(identity, id);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getAddress( identity, id );
			}
		} catch ( CreateException ce ) {
			invalidateManagerHome();
			throw new FDResourceException( ce, "Error creating session bean" );
		} catch ( RemoteException re ) {
			invalidateManagerHome();
			throw new FDResourceException( re, "Error talking to session bean" );
		}
	}


	public static ErpGiftCardModel applyGiftCard(FDIdentity identity,
			String givexNum, FDActionInfo info)
			throws ServiceUnavailableException, InvalidCardException,
			CardInUseException, CardOnHoldException, FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				return CustomerGiftCardService.getInstance().applyGiftCard(identity, givexNum, info);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.applyGiftCard(identity, givexNum, info);
			}
			

		} catch (InvalidCardException ie) {
			invalidateManagerHome();
			throw ie;
		} catch (CardInUseException ce) {
			invalidateManagerHome();
			throw ce;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				return new FDGiftCardInfoList(CustomerGiftCardService.getInstance().getGiftCards(identity));
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();

				return new FDGiftCardInfoList(sb.getGiftCards(identity));
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGiftCardModel verifyStatusAndBalance(
			ErpGiftCardModel model, boolean reloadBalance)
			throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().verifyStatusAndBalance(model, reloadBalance);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.verifyStatusAndBalance(model, reloadBalance);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getGiftCardRecepientsForCustomer(FDIdentity identity)
			throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
				return GiftCardManagerService.getInstance().getGiftCardRecepientsForCustomer(identity.getErpCustomerPK());
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardRecepientsForCustomer(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map getGiftCardRecepientsForOrders(List saleIds)
			throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
				return GiftCardManagerService.getInstance().getGiftCardRecepientsForOrders(saleIds);
			}else {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardRecepientsForOrders(saleIds);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getGiftCardOrdersForCustomer(FDIdentity identity)
			throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().getGiftCardOrdersForCustomer(identity.getErpCustomerPK());
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardOrdersForCustomer(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGCDlvInformationHolder getRecipientDlvInfo(
			FDIdentity identity, String saleId, String certificationNum)
			throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				return CustomerGiftCardService.getInstance().getRecipientDlvInfo(identity, saleId, certificationNum);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getRecipientDlvInfo(identity, saleId, certificationNum);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean resendEmail(String saleId, String certificationNum,
			String resendEmailId, String recipName, String personalMsg,
			EnumTransactionSource source) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				return CustomerGiftCardService.getInstance().resendEmail(saleId, certificationNum, resendEmailId, recipName, personalMsg, source);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.resendEmail(saleId, certificationNum, resendEmailId, recipName, personalMsg, source);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static double getOutStandingBalance(FDCartModel cart) throws FDResourceException {
		try {

			ErpAbstractOrderModel order = null;
			boolean isModifyOrderModel = false;
			if (cart instanceof FDModifyCartModel) {
				order = FDOrderTranslator.getErpCreateOrderModel(cart);
				isModifyOrderModel = true;
			} else {
				order = FDOrderTranslator.getErpModifyOrderModel(cart);
			}
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				return CustomerGiftCardService.getInstance().getOutstandingBalance(order, isModifyOrderModel);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getOutStandingBalance(order);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static EnumIPhoneCaptureType iPhoneCaptureEmail(String emailId, EnumTransactionSource source)
			throws FDResourceException {
		try {
			
			lookupManagerHome();
			FDCustomerManagerSB sb = managerHome.create();
			return sb.iPhoneCaptureEmail(emailId, source);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Object getGiftCardRedemedOrders(FDIdentity identity,
			String certNum) throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().getGiftCardRedeemedOrders(identity.getErpCustomerPK(), certNum);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardRedemedOrders(identity, certNum);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Object getGiftCardRedemedOrders(String certNum)
			throws FDResourceException {
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().getGiftCardRedeemedOrders( certNum);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardRedemedOrders(certNum);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getDeletedGiftCardsForCustomer(FDIdentity identity)
			throws FDResourceException {
		// TODO Auto-generated method stub
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().getAllDeletedGiftCard(identity.getErpCustomerPK());
			}else {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDeletedGiftCardForCustomer(identity);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
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
		lookupManagerHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().getGiftCardRecepientsForOrder(saleId);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getGiftCardRecepientsForOrder(saleId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum)
			throws FDResourceException {

		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
				return GiftCardManagerService.getInstance().validateAndGetGiftCardBalance(givexNum);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.validateAndGetGiftCardBalance(givexNum);
			}
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public static void transferGiftCardBalance(FDIdentity identity,
			String fromGivexNum, String toGivexNum, double amount)
			throws FDResourceException {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
				GiftCardManagerService.getInstance().transferGiftCardBalance(identity.getErpCustomerPK(), fromGivexNum, toGivexNum,amount);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			sb.transferGiftCardBalance(identity, fromGivexNum, toGivexNum,
					amount);
			}

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public static double getPerishableBufferAmount(FDCartModel cart)
			throws FDResourceException {
		lookupManagerHome();
		try {
			ErpAbstractOrderModel order = null;
			if (cart instanceof FDModifyCartModel) {
				order = FDOrderTranslator.getErpCreateOrderModel(cart);
			} else {
				order = FDOrderTranslator.getErpModifyOrderModel(cart);
			}

			FDCustomerManagerSB sb = managerHome.create();
			return sb.getPerishableBufferAmount(order);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String placeDonationOrder(FDActionInfo info,
			FDCartModel cart, Set<String> appliedPromos, boolean sendEmail,
			CustomerRatingI cra, EnumDlvPassStatus status, boolean isOptIn)
			throws FDResourceException, ErpFraudException,
			ErpAuthorizationException {
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

			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null
					: info.getAgent().getUserId());
			// Clear all charges
			createOrder.setCharges(new ArrayList<ErpChargeLineModel>());
			FDCustomerManagerSB sb = managerHome.create();

			
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("placeDonationOrder_Api")){
	    		OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
	    		orderId =  service.placeDonationOrder(info, createOrder,
						appliedPromos, cart.getDeliveryReservation().getPK()
						.getId(), sendEmail, cra,
				info.getAgent() == null ? null : info.getAgent().getRole(),
				status, isOptIn);
			}else{
			orderId = sb.placeDonationOrder(info, createOrder,
					appliedPromos, cart.getDeliveryReservation().getPK()
							.getId(), sendEmail, cra,
					info.getAgent() == null ? null : info.getAgent().getRole(),
					status, isOptIn);
			}
			// sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(),
			// orderId, EnumSaleType.GIFTCARD, cra);

			//invalidate quickshop past orders cache
            CmsServiceLocator.ehCacheUtil().removeFromCache(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName, info.getIdentity().getErpCustomerPK());

			return orderId;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		} /*
		 * catch (ErpSaleNotFoundException e) { invalidateManagerHome(); throw
		 * new FDResourceException(e, "Error talking to session bean"); }
		 */
	}

	public static ErpGCDlvInformationHolder GetGiftCardRecipentByCertNum(
			String certNum) throws FDResourceException {
		lookupManagerHome();
		try {
			
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GiftCardManagerSB)){
			return GiftCardManagerService.getInstance().loadGiftCardRecipentByCertNum(certNum);
			}else{
			FDCustomerManagerSB sb = managerHome.create();
			return sb.GetGiftCardRecipentByCertNum(certNum);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void saveDonationOptIn(String custId, String saleId, boolean optIn) throws FDResourceException {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				CustomerGiftCardService.getInstance().saveDonationOptIn(custId, saleId, optIn);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.saveDonationOptIn(custId, saleId, optIn);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void resubmitGCOrders() {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerGiftCard)) {
				CustomerGiftCardService.getInstance().resubmitGCOrders();
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.resubmitGCOrders();
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			// throw new FDResourceException(ce, "Error creating session bean");
			LOGGER.warn("Error creating session bean:" + ce);
		} catch (RemoteException re) {
			invalidateManagerHome();
			// throw new FDResourceException(re,
			// "Error talking to session bean");
			LOGGER.warn("Error talking to session bean:" + re);
		} catch (FDResourceException fe) {
			LOGGER.warn("Error looking up for manager:" + fe);
		}
	}

	public static void startGiftCardNSMThread() {
		Thread thread = new GiftCardNSMThread();
		thread.setName("GiftCardNSMThread");
		thread.start();
	}

	public static class GiftCardNSMThread extends Thread {

		@Override
		public void run() {

			long refreshFrequency = FDStoreProperties.getNSMFreqSecsForGC() * 1000;
			long lastTime = System.currentTimeMillis();
			try {
				while (true) {
					Long currentTime = System.currentTimeMillis();
					if (currentTime - lastTime < refreshFrequency) {
						synchronized (this) {
							this.wait(refreshFrequency
									- (currentTime - lastTime));
						}
					}

					resubmitGCOrders();
					lastTime = System.currentTimeMillis();
				}
			} catch (InterruptedException e) {
				LOGGER.warn("GiftCardNSMThread interrupted:" + e);
			}

		}
	}


		public static List<String> getTopFaqs() throws FDResourceException {

				lookupManagerHome();


			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getTopFaqs();

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to bean");
			}
		}



		public static CrmClick2CallModel getClick2CallInfo() throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getClick2CallInfo();

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to bean");
			}
		}

	public static List<ErpClientCodeReport> findClientCodesBySale(String saleId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.findClientCodesBySale(saleId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static List<ErpClientCodeReport> findClientCodesByDateRange(FDIdentity customerId, Date start, Date end) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.findClientCodesByDateRange(customerId, start, end);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static SortedSet<IgnoreCaseString> getOrderClientCodesForUser(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrderClientCodesForUser(identity);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}


	public static void createCounter( String customerId, String counterId, int initialValue ) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			sb.createCounter( customerId, counterId, initialValue );

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static void updateCounter( String customerId, String counterId, int newValue ) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			sb.updateCounter( customerId, counterId, newValue );

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static Integer getCounter( String customerId, String counterId ) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			return sb.getCounter( customerId, counterId );

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	/**
	 * Convenience method for decrement type counters.
	 *
	 * @param customerId	id of the customer
	 * @param counterId		name of the counter
	 * @param initialValue	initial value of the counter
	 * @return	value of the counter
	 * @throws FDResourceException
	 */
	public static int decrementCounter( String customerId, String counterId, int initialValue ) throws FDResourceException {

		if ( customerId == null || customerId.trim().length() == 0 ) {
			return initialValue;
		}

		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			Integer counter = sb.getCounter( customerId, counterId );

			if ( counter == null ) {
				sb.createCounter( customerId, counterId, initialValue );
				counter = initialValue;
			}

			if ( counter > 0 ) {
				sb.updateCounter( customerId, counterId, --counter );
			}

			return counter;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}
	}




	/**
	 * Convenience method to get a counter value and create if not exits.
	 *
	 * @param customerId	Customer ID
	 * @param counterId		Counter identifier string
	 * @param initialValue	Positive integer number
	 * @return True if counter has not reached 0 yet.
	 * @throws FDResourceException
	 */
	public static boolean testCounter( String customerId, String counterId, int initialValue ) throws FDResourceException {
		if ( customerId == null || customerId.trim().length() == 0 ) {
			return true;
		}

		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			Integer counter = sb.getCounter( customerId, counterId );

			if ( counter == null ) {
				sb.createCounter( customerId, counterId, initialValue );
				counter = initialValue;
			}

			return counter > 0;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to bean");
		}

	}
	public static void sendSettlementFailedEmail(String saleId) throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				CustomerNotificationService.getInstance().sendSettlementFailedEmail(saleId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.sendSettlementFailedEmail(saleId);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void bulkModifyOrder(
			FDIdentity identity,
			FDActionInfo info,
			FDModifyCartModel cart,
			Set<String> appliedPromos,
			boolean sendEmail)
			throws FDResourceException,
			ErpTransactionException,
			ErpFraudException,
			ErpAuthorizationException,
			DeliveryPassException,
			ErpAddressVerificationException,
			FDPaymentInadequateException, InvalidCardException
			{
			try{
				lookupManagerHome();
				String saleId = cart.getOriginalOrder().getErpSalesId();
				ErpModifyOrderModel order = FDOrderTranslator.getErpModifyOrderModel(cart);
				order.setTransactionSource(info.getSource());
				order.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());
				String oldReservationId = cart.getOriginalReservationId();
				boolean hasCouponDiscounts = false;
				if(EnumSaleType.REGULAR.equals(cart.getOriginalOrder().getOrderType()) && (order.hasCouponDiscounts()||cart.getOriginalOrder().hasCouponDiscounts())){
					hasCouponDiscounts = true;
				}
				FDCustomerManagerSB sb = managerHome.create();
				sb.bulkModifyOrder(saleId, identity, info, order, oldReservationId, appliedPromos,
						info.getAgent() == null ? null : info.getAgent().getRole(), sendEmail,hasCouponDiscounts);
			}catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to bean");
			} catch (SQLException ie) {
				invalidateManagerHome();
				throw new FDResourceException(ie, "Error talking to bean SQLException");
			}


		}

	public static void logMassCancelActivity(ErpActivityRecord record) {
		ActivityLogHome home = getActivityLogHome();
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)){
				FDECommerceService.getInstance().logActivity(record);
			}else{
				ActivityLogSB logSB = home.create();
				logSB.logActivity(record);
			}
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (CreateException e) {
			throw new EJBException(e);
		}
	}

	private static ActivityLogHome getActivityLogHome() {
		try {
			return (ActivityLogHome) LOCATOR.getRemoteHome("freshdirect.customer.ActivityLog");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	public static void authorizeSale(String salesId) throws FDResourceException {
		authorizeSale(salesId, false);
		}

	/* APPDEV-1888 */
	public static String recordReferral(String customerId, String referralId, String customerEmail) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.recordReferral(customerId, referralId, customerEmail);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}


	public static String dupeEmailAddress(String email) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				return CustomerNotificationService.getInstance().getIdByEmail(email);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.dupeEmailAddress(email);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeMobilePreferences(String customerId, String fdCustomerId, String mobileNumber,
			String textOffers, String textDelivery, String orderNotices, String orderExceptions, String offers,
			String partnerMessages, EnumEStoreId eStoreId) throws FDResourceException {

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPreference)) {
				CustomerPreferenceService.getInstance().storeMobilePreferences(customerId, fdCustomerId, mobileNumber, textOffers, textDelivery, orderNotices,
						orderExceptions, offers, partnerMessages, eStoreId);
			} else {
				lookupManagerHome();

				FDCustomerManagerSB sb = managerHome.create();

				sb.storeMobilePreferences(fdCustomerId, mobileNumber, textOffers, textDelivery, orderNotices,
						orderExceptions, offers, partnerMessages, eStoreId);
				FDCustomerEStoreModel smsPreferenceModel = FDCustomerFactory.getFDCustomer(fdCustomerId)
						.getCustomerSmsPreferenceModel();

				if (EnumEStoreId.FD.getContentId().equalsIgnoreCase(eStoreId.getContentId())
						&& smsPreferenceModel != null) {
					FDDeliveryManager.getInstance().addSubscriptions(customerId,
							smsPreferenceModel.getMobileNumber() != null
									? smsPreferenceModel.getMobileNumber().getPhone()
									: "",
							textOffers, textDelivery,
							(EnumSMSAlertStatus.SUBSCRIBED.value().equals(smsPreferenceModel.getOrderNotices()))
									? EnumSMSAlertStatus.SUBSCRIBED.value()
									: EnumSMSAlertStatus.NONE.value(),
							(EnumSMSAlertStatus.SUBSCRIBED.value().equals(smsPreferenceModel.getOrderExceptions()))
									? EnumSMSAlertStatus.SUBSCRIBED.value()
									: EnumSMSAlertStatus.NONE.value(),
							null,
							(EnumSMSAlertStatus.SUBSCRIBED.value().equals(smsPreferenceModel.getPartnerMessages()))
									? EnumSMSAlertStatus.SUBSCRIBED.value()
									: EnumSMSAlertStatus.NONE.value(),
							new Date(), eStoreId.toString());
				} else {
					PhoneNumber phoneNumber = new PhoneNumber(mobileNumber);
					FDDeliveryManager.getInstance().addSubscriptions(customerId,
							phoneNumber != null ? phoneNumber.getPhone() : "", textOffers, textDelivery,
							"Y".equalsIgnoreCase(orderNotices) ? "S" : "N",
							"Y".equalsIgnoreCase(orderExceptions) ? "S" : "N", "Y".equalsIgnoreCase(offers) ? "S" : "N",
							"Y".equalsIgnoreCase(partnerMessages) ? "S" : "N", new Date(), eStoreId.toString());

				}
				logSmsActivity(customerId, orderNotices, orderExceptions, offers, smsPreferenceModel,
						eStoreId.getContentId());
			}

		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	private static void logSmsActivity(String customerId, String orderNotices, String orderExceptions, String offers, FDCustomerEStoreModel customerSmsPreferenceModel, String eStore){
		//Temp variables for sms Alerts:
		/*String _orderNotices = cm.getOrderNotices()!=null?cm.getOrderNotices().value():EnumSMSAlertStatus.NONE.value();
		String _orderExceptions = cm.getOrderExceptions()!=null?cm.getOrderExceptions().value():EnumSMSAlertStatus.NONE.value();
		String _offers = cm.getOffers()!=null?cm.getOffers().value():EnumSMSAlertStatus.NONE.value();
		*/
		String _orderNotices;
		String _orderExceptions;
		String _offers;

		if(EnumEStoreId.FD.getContentId().equals(eStore) && customerSmsPreferenceModel!=null){

			_orderNotices = customerSmsPreferenceModel.getOrderNotices()!=null?customerSmsPreferenceModel.getOrderNotices():EnumSMSAlertStatus.NONE.value();
			 _orderExceptions = customerSmsPreferenceModel.getOrderExceptions()!=null?customerSmsPreferenceModel.getOrderExceptions():EnumSMSAlertStatus.NONE.value();
			 _offers = customerSmsPreferenceModel.getOffers()!=null?customerSmsPreferenceModel.getOffers():EnumSMSAlertStatus.NONE.value();

			if(_orderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value()) || _orderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
					_offers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
				_orderNotices= "Y".equals(orderNotices)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
				_orderExceptions="Y".equals(orderExceptions)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
				_offers="Y".equals(offers)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
			} else{
				_orderNotices= "Y".equals(orderNotices)?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value();
				_orderExceptions="Y".equals(orderExceptions)?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value();
				_offers="Y".equals(offers)?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value();
			}

		}
		else{
			 _orderNotices= "Y".equals(orderNotices)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
				_orderExceptions="Y".equals(orderExceptions)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
				_offers="Y".equals(offers)?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value();
			}

		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(EnumAccountActivityType.SMS_ALERT);
		rec.setSource(EnumTransactionSource.WEBSITE);
		rec.setInitiator("CUSTOMER");
		rec.setCustomerId(customerId);
		rec.setDate(new Date());
		if(EnumEStoreId.FD.getContentId().equals(eStore))
	     rec.setNote("Updated SMS Flags- Order Notif:" + _orderNotices + ", OrderExp Notif:"+ _orderExceptions +", MrkOffers:"+_offers);
		else
			rec.setNote("Updated FDX SMS Flags- Delivery Updates:" + _orderNotices + ", order Status:"+ _orderExceptions +", Offers:"+_offers);

		logActivity(rec);
	}

	public static void storeSmsPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPreference)) {
				CustomerPreferenceService.getInstance().storeSmsPrefereceFlag(fdCustomerId, flag, eStoreId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeSmsPrefereceFlag(fdCustomerId, flag, eStoreId);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeGoGreenPreferences(String customerId, String goGreen) throws FDResourceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPreference)) {
				CustomerPreferenceService.getInstance().storeGoGreenPreferences(customerId, goGreen);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeGoGreenPreferences(customerId, goGreen);
				logGoGreenActivity(customerId, "Y".equals(goGreen)?"Y":"N", EnumAccountActivityType.GO_GREEN);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static String loadGoGreenPreference(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPreference)) {
				return CustomerPreferenceService.getInstance().loadGoGreenPreference(customerId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.loadGoGreenPreference(customerId);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeMobilePreferencesNoThanks(String customerId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeMobilePreferencesNoThanks(customerId);
			logGoGreenActivity(customerId, "Y", EnumAccountActivityType.NO_THANKS);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeSmsPreferencesNoThanks(String fdCustomerId, EnumEStoreId eStoreId) throws FDResourceException{
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeSmsPreferencesNoThanks(fdCustomerId, eStoreId);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeAllMobilePreferences(String customerId, String fdCustomerId, String mobileNumber, String textOffers, String textDelivery, String goGreen, String phone, String ext, boolean isCorpUser, EnumEStoreId eStoreId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeAllMobilePreferences(customerId, fdCustomerId, mobileNumber, textOffers, textDelivery, goGreen, phone, ext, isCorpUser, eStoreId);
			logGoGreenActivity(customerId, "Y".equals(goGreen)?"Y":"N", EnumAccountActivityType.GO_GREEN);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeSMSWindowDisplayedFlag(String customerId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeSMSWindowDisplayedFlag(customerId);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	private static void logGoGreenActivity(String customerId, String flag, EnumAccountActivityType activity) {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(activity);
		rec.setSource(EnumTransactionSource.WEBSITE);
		rec.setInitiator("CUSTOMER");
		rec.setCustomerId(customerId);
		rec.setDate(new Date());
		rec.setNote("Flag updated to:" + flag);
		logActivity(rec);
	}

	private static void logActivity(ErpActivityRecord record) {
		if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)) {
			try {
				FDECommerceService.getInstance().logActivity(record);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
		new ErpLogActivityCommand(LOCATOR, record).execute();
		}
	}


	public static void sendEmail(XMLEmailI email) throws FDResourceException {
		lookupMailerGatewayHome();
		lookupManagerHome();
		try {
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

	/* APPDEV-2475 DP T&C */
	public static void storeDPTCViews(String customerId, int dpTcViewCount) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeDPTCViews(customerId, dpTcViewCount);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static void storeDPTCAgreeDate(String customerId, Date dpTcAgreeDate) throws FDResourceException {
		storeDPTCAgreeDate(null, customerId, dpTcAgreeDate);
	}
	/* pass in info as non-null to log to activity log */
	public static void storeDPTCAgreeDate(FDActionInfo info, String customerId, Date dpTcAgreeDate) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			if (info == null) {
				sb.storeDPTCAgreeDate(customerId, dpTcAgreeDate);
			} else {
				sb.storeDPTCAgreeDate(info, customerId, dpTcAgreeDate);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static List<FDCartonInfo> getCartonDetails(FDOrderI order) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getCartonDetailsForSale(order);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}

	}

	public static Map getAssignedCustomerParams(FDUser user) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getAssignedCustomerParams(user);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUserI saveExternalCampaign(FDUserI user) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.saveExternalCampaign(user);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void logIpLocatorEvent(IpLocatorEventDTO ipLocatorEventDTO) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.logIpLocatorEvent(ipLocatorEventDTO);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static IpLocatorEventDTO loadIpLocatorEvent(String fdUserId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.loadIpLocatorEvent(fdUserId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static  boolean  isFeatureEnabled(String customerId, EnumSiteFeature feature) throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isFeatureEnabled(customerId, feature);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static CustomerAvgOrderSize getHistoricOrderSize(String customerId)  throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getHistoricOrderSize(customerId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDCartModel getSavedCart(FDIdentity identity, EnumEStoreId eStoreId) throws FDAuthenticationException, FDResourceException {

		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.getFDUserWithCart(identity,  eStoreId);
			populateShoppingCart(user, true, false);

			return user.getShoppingCart();

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ErpAddressModel getLastOrderAddress(FDIdentity identity,EnumEStoreId eStore)
			throws FDResourceException {

		ErpAddressModel address = null;
		if(identity==null)
			return address;
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			String lastOrderId = sb.getLastOrderID(identity,eStore);
			if(lastOrderId==null)
				return address;
			address = sb.getLastOrderAddress(lastOrderId);
			return address;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		} catch (SQLException se) {
			invalidateManagerHome();
			throw new FDResourceException(se, "Error running SQL");
		}
	}

	public static void updateOrderInModifyState(FDOrderAdapter order) throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.updateOrderInModifyState(order.getSale());

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static boolean isReadyForPick(String orderNum) throws FDResourceException {


		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isReadyForPick(orderNum);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}



	}

	public static void updateOrderInProcess(String orderNum) throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.updateOrderInProcess(orderNum);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void releaseModificationLock(String orderId) throws FDResourceException {


		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.releaseModificationLock(orderId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}


	}

	public static void setFdxSmsPreferences(
			FDCustomerEStoreModel customerSmsPreferenceModel, String ErpCustomerPK)throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setFdxSmsPreferences(customerSmsPreferenceModel, ErpCustomerPK);

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

		lookupManagerHome();
		boolean status=true;
		try {
			FDCustomerManagerSB sb = managerHome.create();
			status =sb.setAcknowledge(identity, acknowledge,ackType);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	return status;
	}

	public static boolean updateRAFClickIDPromoCode(FDIdentity identity, String rafclickid,
			String rafpromocode, EnumEStoreId eStoreId) throws FDResourceException {

		lookupManagerHome();
		boolean status=true;
		try {
			FDCustomerManagerSB sb = managerHome.create();
			status =sb.setRAFClickIDPromoCode(identity, rafclickid, rafpromocode, eStoreId);

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
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerAddress)) {
				parentOrderAddressId = CustomerAddressService.getInstance().getParentOrderAddressId(parentOrderId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				LOGGER.debug("IN FD Customer Manager Parent id is " + parentOrderId);
				parentOrderAddressId = sb.getParentOrderAddressId(parentOrderId);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	return parentOrderAddressId;
	}

	public static boolean getAddonOrderCount(String OrderId) throws FDResourceException {

		lookupManagerHome();
		boolean addOnOrderCount;
		try {
			FDCustomerManagerSB sb = managerHome.create();
			addOnOrderCount =sb.getAddonOrderCount(OrderId);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	return addOnOrderCount;
	}
		public static boolean reSendInvoiceEmail(String OrderId) throws FDResourceException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			 return sb.reSendInvoiceEmail(OrderId);

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
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerNotification)) {
				return CustomerNotificationService.getInstance().iPhoneCaptureEmail(email, zipCode, serviceType);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.iPhoneCaptureEmail(email, zipCode, serviceType);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void storeEmailPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPreference)) {
				CustomerPreferenceService.getInstance().storeEmailPreferenceFlag(fdCustomerId, flag, eStoreId);
			} else {
				lookupManagerHome();

				FDCustomerManagerSB sb = managerHome.create();
				sb.storeEmailPreferenceFlag(fdCustomerId, flag, eStoreId);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

		/**
		 * @param token
		 * @return
		 * @throws FDResourceException
		 */
		public static boolean isValidVaultToken(String token, String customerId) throws FDResourceException {
			
			boolean isValid=false;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaymentManagerSB)) {
					isValid = FDECommerceService.getInstance().isValidVaultToken(token, customerId);
				} else {
					lookupManagerHome();
					FDCustomerManagerSB sb = managerHome.create();
					isValid =sb.isValidVaultToken(token,customerId);
				}

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}

		return isValid;
		}


		public static List<String> getShippingInfoSalesId() throws FDResourceException {
			List<String>  salesIds=new ArrayList<String>();
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				salesIds= sb.getShippingInfoSalesId();

			} catch (RemoteException e) {
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}

			return salesIds;
		}

		public static Map<String,Map<String,Integer>> getShippingInfoCartonDetails(List<String> salesIds) throws FDResourceException {
			Map<String,Map<String,Integer>> cartonDetails=new HashMap<String, Map<String,Integer>>();
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				cartonDetails= sb.getShippingInfoCartonDetails(salesIds);

			} catch (RemoteException e) {
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}

			return cartonDetails;
		}

		public static int updateShippingInfoCartonDetails() throws FDResourceException {

			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.updateShippingInfoCartonDetails();

			} catch (RemoteException e) {
				LOGGER.error("Error while updating the shipping info carton details "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error while updating the shipping info carton details "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}

		}

		public static int[] updateShippingInfoTruckDetails() throws FDResourceException {

			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();

						return sb.updateShippingInfoTruckDetails();

			} catch (RemoteException e) {
				LOGGER.error("Error while updating the shipping info truck details "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error while updating the shipping info truck details "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (Exception e) {
				LOGGER.error("Error while updating the shipping info truck details "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

		public static List<FDCartLineI> getModifiedCartlines(String orderId, UserContext userContext) throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				List<FDCartLineI> modCartLines = sb.getModifiedCartlines(orderId, userContext);
				setUserContextToOrderLines(userContext, modCartLines);
				return modCartLines;
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		private static void setUserContextToOrderLines(UserContext userContext, List<FDCartLineI> cartLines) {
			if(null != cartLines){
				List<FDCartLineI> invalids=new ArrayList<FDCartLineI>(cartLines.size());
				for (FDCartLineI cartLine : cartLines) {
					cartLine.setUserContext(userContext);

					try {
						OrderLineUtil.cleanup(cartLine);
					} catch (FDInvalidConfigurationException e) {
						invalids.add(cartLine);
						LOGGER.warn(e.getMessage());
					} catch(FDResourceException e1){
						LOGGER.warn(e1.getMessage());
					}

				}

				for(FDCartLineI cartLine:invalids) {
					cartLines.remove(cartLine);
				}
			}
		}

		public static void storeModifiedCartline(FDUserI user, FDCartLineI newLine, String orderId)  throws FDResourceException {
			lookupManagerHome();

			try{
				FDCustomerManagerSB sb = managerHome.create();
				sb.saveModifiedCartline(((FDUser)user).getPK(), user.getUserContext().getStoreContext(), newLine, orderId);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static void removeModifiedCartline(FDCartLineI cartLine) throws FDResourceException {
			lookupManagerHome();

			try{
				FDCustomerManagerSB sb = managerHome.create();
				sb.removeModifiedCartline(cartLine);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static void updateModifiedCartlineQuantity(FDCartLineI cartLine) throws FDResourceException {
			lookupManagerHome();

			try{
				FDCustomerManagerSB sb = managerHome.create();
				sb.updateModifiedCartlineQuantity(cartLine);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static void clearModifyCartlines(String currentOrderId) throws FDResourceException {
			lookupManagerHome();

			try{
				FDCustomerManagerSB sb = managerHome.create();
				sb.clearModifyCartlines(currentOrderId);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static List<UnsettledOrdersInfo> getUnsettledOrders(Date date) throws FDResourceException {
			lookupManagerHome();

			try{
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getUnsettledOrders(date);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static ErpCustomerModel getCustomer(FDIdentity identity) throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getCustomer(identity);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static Map<String, List<PendingOrder>> getPendingDeliveries() throws FDResourceException {
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getPendingDeliveries();
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static boolean insertOrUpdateSilverPopup(SilverPopupDetails silverPopupDetails) throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.insertOrUpdateSilverPopup(silverPopupDetails);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static void updateSPSuccessDetails(SilverPopupDetails silverPopupDetails) throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.updateSPSuccessDetails(silverPopupDetails);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}

		public static String getCookieByFdCustomerId(String fdCustomerId) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getCookieByFdCustomerId(fdCustomerId);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}


		private static EnumPaymentMethodDefaultType getpaymentMethodDefaultType(String custId) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getpaymentMethodDefaultType(custId);
			}catch (RemoteException e) {
				LOGGER.error("Error verifying payment method "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error verifying payment method "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

	public static int resetDefaultPaymentValueType(String custId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerPayment)) {
				return CustomerPaymentService.getInstance().resetDefaultPaymentValueType(custId);
			} else {
				lookupManagerHome();
				FDCustomerManagerSB sb = managerHome.create();
				return sb.resetDefaultPaymentValueType(custId);
			}
		} catch (RemoteException e) {
			LOGGER.error("Error resetting default payment type in fdcustomer " + e);
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			LOGGER.error("Error resetting default payment type in fdcustomer " + e);
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}


		public static void updateDpFreeTrialOptin(boolean dpFreeTrialOptin,String custId, FDActionInfo info) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.setDpFreeTrialOptin(dpFreeTrialOptin, custId, info);
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

		public static Date getDpFreeTrialOptinDate(String custId) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getDpFreeTrialOptinDate(custId);
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

		public static  List<String> getAllCustIdsOfFreeTrialSubsOrder() throws FDResourceException{
			lookupManagerHome();
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.DlvPassManagerSB)) {
					return DlvPassManagerService.getInstance().getAllCustIdsOfFreeTrialSubsOrder();
				}else{
					FDCustomerManagerSB sb = managerHome.create();
					return sb.getAllCustIdsOfFreeTrialSubsOrder();
				}
				
				
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}

		public static boolean hasCustomerDpFreeTrial(String custId) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.hasCustomerDpFreeTrialOptin(custId);
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}
		
		public static void updateFDCustomerEStoreInfo(FDCustomerEStoreModel fdCustomerEStoreModel, String custId) throws FDResourceException {
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.updateFDCustomerEStoreInfo(fdCustomerEStoreModel, custId);
			}catch (RemoteException e) {
				LOGGER.error("Error updating FDCustomerEStoreModel for custId:" + custId + " "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error updating FDCustomerEStoreModel for custId:" + custId + " "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			}
		}
		
		public static ShortSubstituteResponse getShortSubstituteOrders(List<String> orderList) throws FDResourceException {
			lookupManagerHome();

			try {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.getShortSubstituteOrders(orderList);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				LOGGER.debug("RemoteException: ", re);
				throw new FDResourceException(re, "Error talking to session bean");
}
		}
		
		public static void updateDpOptinDetails(boolean isAutoRenewDp, String custId, String dpType, FDActionInfo info, EnumEStoreId eStore) throws FDResourceException{
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.updateDpOptinDetails(isAutoRenewDp, custId, dpType, info, eStore);
			}catch (RemoteException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
			} catch (CreateException e) {
				LOGGER.error("Error at delivery pass free trial in fdcustomer "+ e);
				invalidateManagerHome();
				throw new FDResourceException(e, "Error creating session bean");
}
		}
}
