/*
 * $Workfile:FDCustomerManager.java$
 *
 * $Date:8/28/2003 1:19:25 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpWebOrderHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.mail.TellAFriend;
import com.freshdirect.fdstore.mail.TellAFriendProduct;
import com.freshdirect.fdstore.mail.TellAFriendRecipe;
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.ejb.FDSurveyHome;
import com.freshdirect.fdstore.survey.ejb.FDSurveySB;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XSLTransformer;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.routing.ejb.RoutingGatewayHome;
import com.freshdirect.routing.ejb.RoutingGatewaySB;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;


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
	private static RoutingGatewayHome routingGatewayHome = null;
	private static FDSurveyHome surveyHome = null;

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
		throws FDResourceException, ErpDuplicateUserIdException {

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.register(info, erpCustomer, fdCustomer, cookie, pickupOnly, eligibleForPromotion, survey, serviceType);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser createNewUser(String zipCode, EnumServiceType serviceType) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.createNewUser(zipCode, serviceType);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser createNewUser(AddressModel address, EnumServiceType serviceType) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.createNewUser(address, serviceType);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static FDUser createNewDepotUser(String depotCode, EnumServiceType serviceType) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.createNewDepotUser(depotCode, serviceType);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser recognize(String cookie) throws FDAuthenticationException, FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.recognize(cookie);
			user.getShoppingCart().doCleanup();
			classifyUser(user);
			assumeDeliveryAddress(user);
			user.updateUserState();
			
			updateZoneInfo(user);
			restoreReservations(user);
			
			return user;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDUser recognize(FDIdentity identity) throws FDAuthenticationException, FDResourceException {
		//The method was changed as part of task PERF-22.
		return recognize(identity, null);
	}
	
	/*
	 * This new method was added as part of task PERF-22. This method
	 * will be called directly from CrmGetFDUserTag to set the application
	 * source as CSR so that the CRM application knows which order history
	 * object should be loaded before the FDSessionUser object is created
	 * where it is actually set.
	 */
	public static FDUser recognize(FDIdentity identity, EnumTransactionSource source) throws FDAuthenticationException, FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDUser user = sb.recognize(identity);
			user.setApplication(source);
			user.getShoppingCart().doCleanup();
			classifyUser(user);
			assumeDeliveryAddress(user);
			user.updateUserState();
			
			updateZoneInfo(user);
			restoreReservations(user);
			
			return user;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	private static void updateZoneInfo (FDUserI user) throws FDResourceException {
		ErpAddressModel address = user.getShoppingCart().getDeliveryAddress();
		if(address != null) {
			Date day = DateUtil.truncate(DateUtil.addDays(new Date(), 1));
			if(address instanceof ErpDepotAddressModel) {
				ErpDepotAddressModel depotAddress = (ErpDepotAddressModel)address;
				DlvDepotModel depot = FDDepotManager.getInstance().getDepotByLocationId(depotAddress.getLocationId());
				if(depot != null) {
					DlvLocationModel location = depot.getLocation(depotAddress.getLocationId());
					if(location != null) {
						DlvZoneInfoModel info = FDDeliveryManager.getInstance().getZoneInfoForDepot(depot.getRegionId(), location.getZoneCode(), day);
						user.getShoppingCart().setZoneInfo(info);
					}
				}
			} else {
				try {
					user.getShoppingCart().setZoneInfo(FDDeliveryManager.getInstance().getZoneInfo(address, day));
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
	}
	
    private static void assumeDeliveryAddress(FDUser user) throws FDResourceException {
		FDIdentity identity = user.getIdentity();
    	if(identity != null){
    		lookupManagerHome();
    		try{
    			FDCustomerManagerSB sb = managerHome.create();
    			ErpAddressModel address = sb.assumeDeliveryAddress(identity, user.getOrderHistory().getLastOrderId());
    			if(address != null && user.getShoppingCart() != null){
    				user.getShoppingCart().setDeliveryAddress(address);
    			}
    		} catch (CreateException ce) {
    			invalidateManagerHome();
    			throw new FDResourceException(ce, "Error creating session bean");
    		} catch (RemoteException re) {
    			invalidateManagerHome();
    			throw new FDResourceException(re, "Error talking to session bean");
    		}
		}
	}
    
	private static void restoreReservations(FDUser user) throws FDResourceException {
		FDIdentity identity = user.getIdentity();
		if (identity != null) {
			List rsvList = FDDeliveryManager.getInstance().getResevervationsForCustomer(identity.getErpCustomerPK());
			for (Iterator i = rsvList.iterator(); i.hasNext();) {
				FDReservation rsv = (FDReservation) i.next();
				if (EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
					user.getShoppingCart().setDeliveryReservation(rsv);
				} else {
					user.setReservation(rsv);
				}
			}
		}
	}
	
	private static void classifyUser(FDUser user) throws FDResourceException {

		Set availableServices = new HashSet();
		
		EnumServiceType lastServiceType = user.getSelectedServiceType();
		if (lastServiceType == null) {
			lastServiceType = EnumServiceType.HOME;
		}

		if (user.getDepotCode() != null) {
			availableServices.add(EnumServiceType.DEPOT);
		}
		
		DlvServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().checkZipCode(user.getZipCode());
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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.login(userId, password);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getCustomerInfo(identity);

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
	public static Collection getPaymentMethods(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();

		try {
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

	public static ErpPaymentMethodI getPaymentMethod(FDIdentity identity, String paymentId) throws FDResourceException {
		Collection paymentMethods = FDCustomerManager.getPaymentMethods(identity);
		for (Iterator i = paymentMethods.iterator(); i.hasNext();) {
			ErpPaymentMethodModel model = (ErpPaymentMethodModel) i.next();
			if (paymentId.equals(model.getPK().getId())) {
				return model;
			}
		}
		return null;
	}

	/**
	 * Add a payment method for the customer.
	 *
	 * @param identity the customer's identity reference
	 * @param paymentMethod ErpPaymentMethodI to add
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static void addPaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod)
		throws FDResourceException, ErpDuplicatePaymentMethodException, ErpPaymentMethodException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.addPaymentMethod(info, paymentMethod);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
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
		throws FDResourceException, ErpDuplicatePaymentMethodException, ErpPaymentMethodException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.updatePaymentMethod(info, paymentMethod);

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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.setDefaultShipToAddressPK(identity, shipToAddressPK);
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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDefaultShipToAddressPK(identity);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void setDefaultPaymentMethod(FDActionInfo info, PrimaryKey paymentMethodPK) throws FDResourceException {
		lookupManagerHome();
		try {

			FDCustomerManagerSB sb = managerHome.create();
			sb.setDefaultPaymentMethod(info, paymentMethodPK);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean checkBillToAddressFraud(FDActionInfo info, ErpPaymentMethodI paymentMethod) throws FDResourceException {
		lookupManagerHome();
		try {

			FDCustomerManagerSB sb = managerHome.create();
			return sb.checkBillToAddressFraud(info, paymentMethod);

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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDefaultPaymentMethodPK(identity);
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
	public static void removePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.removePaymentMethod(info, ((ErpPaymentMethodModel) paymentMethod).getPK());

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

	public static void cancelReservation(
		FDIdentity identity,
		FDReservation reservation,
		EnumReservationType rsvType,
		FDActionInfo aInfo)
		throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.cancelReservation(identity, reservation, rsvType, aInfo);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

	public static FDReservation makeReservation(
		FDIdentity identity,
		FDTimeslot timeslot,
		EnumReservationType rsvType,
		String addressId,
		FDActionInfo aInfo, boolean chefsTable)
		throws FDResourceException, ReservationException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			return sb.makeReservation(identity, timeslot, rsvType, addressId, aInfo, chefsTable);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static void updateWeeklyReservation(FDIdentity identity, FDTimeslot timeslot, String addressId, FDActionInfo aInfo) throws FDResourceException {
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
	}

	public static FDReservation changeReservation(
		FDIdentity identity,
		FDReservation oldReservation,
		FDTimeslot timeslot,
		EnumReservationType rsvType,
		String addressId,
		FDActionInfo aInfo)
		throws FDResourceException, ReservationException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();

			return sb.changeReservation(identity, oldReservation, timeslot, rsvType, addressId, aInfo);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}

	}

	public static void updateRecurringReservation(
		FDIdentity identity,
		Date startTime,
		Date endTime,
		String addressId,
		String initiator)
		throws FDResourceException {

		ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);
		int dayOfWeek = (startTime != null ? DateUtil.toCalendar(startTime).get(Calendar.DAY_OF_WEEK) : 0);
		custInfo.setRsvDayOfWeek(dayOfWeek);
		custInfo.setRsvStartTime(startTime);
		custInfo.setRsvEndTime(endTime);
		custInfo.setRsvAddressId(addressId);

		FDActionInfo aInfo = new FDActionInfo(EnumTransactionSource.WEBSITE, identity, initiator, "Updated Recurring Reservation", null);

		updateCustomerInfo(aInfo, custInfo);

	}

	public static FDReservation validateReservation(FDUserI user, FDReservation reservation) throws FDResourceException {
		//TODO have to implement this method correctly with Depot and COS handling
		ErpAddressModel address = getShipToAddress(user.getIdentity(), reservation.getAddressId());
		if (address == null) {
			FDDeliveryManager.getInstance().removeReservation(reservation.getPK().getId());
			if (EnumReservationType.RECURRING_RESERVATION.equals(reservation.getReservationType())) {
				updateRecurringReservation(user.getIdentity(), null, null, null, "CUSTOMER");
			}
			return null;
		} else {
			try {
				DlvZoneInfoModel zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, reservation.getStartTime());
				if (reservation.getZoneId().equals(zoneInfo.getZoneId())) {
					return reservation;
				} else {
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(reservation.getStartTime());
					endCal.add(Calendar.DATE, 1);
					List timeslots =
						FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(
							reservation.getStartTime(),
							endCal.getTime(),
							address);
					FDTimeslot matchingTimeslot = null;
					for (Iterator i = timeslots.iterator(); i.hasNext();) {
						FDTimeslot t = (FDTimeslot) i.next();
						if (t.getBegDateTime().equals(reservation.getStartTime())
							&& t.getEndDateTime().equals(reservation.getEndTime())
							&& t.getCutoffDateTime().equals(reservation.getCutoffTime())
							&& t.getTotalAvailable() > 0) {
							matchingTimeslot = t;
							break;
						}
					}
					FDDeliveryManager.getInstance().removeReservation(reservation.getPK().getId());
					if (EnumReservationType.RECURRING_RESERVATION.equals(reservation.getReservationType())) {
						updateRecurringReservation(user.getIdentity(), null, null, null, "CUSTOMER");
					}
					if (matchingTimeslot != null) {
						long duration = Math.abs(System.currentTimeMillis() - reservation.getCutoffTime().getTime());
						return FDDeliveryManager.getInstance().reserveTimeslot(
							matchingTimeslot,
							user.getIdentity().getErpCustomerPK(),
							duration,
							reservation.getReservationType(),
							address.getPK().getId(),
							reservation.isChefsTable());
					} else {
						return null;
					}
				}
			} catch (FDInvalidAddressException e) {
				//TODO have to fix the exception handling in this case
				throw new FDResourceException(e);
			} catch (ReservationException e) {
				//TODO have to fix the exception handling in this case
				throw new FDResourceException(e);
			}
		}
	}

	public static void updateUserId(FDActionInfo info, String userId) throws FDResourceException, ErpDuplicateUserIdException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.updateUserId(info, userId);

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
	public static Collection getShipToAddresses(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();

			return sb.getShipToAddresses(identity);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return null if the address is not found
	 */
	public static ErpAddressModel getShipToAddress(FDIdentity identity, String shipToAddressId) throws FDResourceException {
		Collection shipToAddresses = FDCustomerManager.getShipToAddresses(identity);
		for (Iterator saItr = shipToAddresses.iterator(); saItr.hasNext();) {
			ErpAddressModel addressModel = (ErpAddressModel) saItr.next();
			if (addressModel.getPK().getId().equals(shipToAddressId)) {
				return addressModel;
			}
		}
		return null;
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
		lookupManagerHome();


		try {
			FDCustomerManagerSB sb = managerHome.create();
			boolean result =   sb.addShipToAddress(info, checkUniqueness, address);

			sendShippingAddress(address);

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
	 *	update a ship to address for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpAddressModel to update
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static boolean updateShipToAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
	throws FDResourceException, ErpDuplicateAddressException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			boolean result =  sb.updateShipToAddress(info, checkUniqueness, address);

			sendShippingAddress(address);

			return result;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	private static void sendShippingAddress(AddressI address) throws FDResourceException {

		if(FDStoreProperties.canSendRoutingAddress()) {
			lookupRoutingGatewayHome();

			try {
				RoutingGatewaySB routingSB = routingGatewayHome.create();
				routingSB.sendShippingAddress(address);

			} catch (CreateException ce) {
				invalidateRoutingGatewayHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateRoutingGatewayHome();
				throw new FDResourceException(re, "Error creating session bean");
			}
		}	

	}

	/**
	 * remove a ship to address for the customer
	 *
	 * @param identity the customer's identity reference
	 * @param address ErpAddressModel to remove
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static void removeShipToAddress(FDActionInfo info, ErpAddressModel address) throws FDResourceException {
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.removeShipToAddress(info, address.getPK());

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
		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeUser(user);

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
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeCohortName(user);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getOrdersByTruck(String truckNumber, Date dlvDate) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrdersByTruck(truckNumber, dlvDate);

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

	private static ErpOrderHistory getErpOrderHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpOrderHistory(Collections.EMPTY_LIST);
		}

		lookupManagerHome();

		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getOrderHistoryInfo(identity);

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
	
	public static double getOrderTotalForChefsTableEligibility(FDIdentity identity) throws FDResourceException {
		ErpOrderHistory history = getErpOrderHistoryInfo(identity);
		return history.getOrderTotalForChefsTableEligibility();
	}

	public static ErpPromotionHistory getPromoHistoryInfo(FDIdentity identity) throws FDResourceException {

		if (identity == null) {
			// !!! this happens eg. when calculating promotions for an anon user..
			// but i don't think this should be called then...
			return new ErpPromotionHistory(Collections.EMPTY_MAP);
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
	
/*	public static List loadPromotions() throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.loadPromotions();
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}
*/
	public static String placeOrder(
		FDActionInfo info,
		FDCartModel cart,
		Set appliedPromos,
		boolean sendEmail,
		CustomerRatingI cra,
		EnumDlvPassStatus status) throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException,DeliveryPassException {
		
		lookupManagerHome();

		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && cra.isOnFDAccount()) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
			}
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart);
			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());

			FDCustomerManagerSB sb = managerHome.create();
			String orderId =
				sb.placeOrder(
					info.getIdentity(),
					createOrder,
					appliedPromos,
					cart.getDeliveryReservation().getPK().getId(),
					sendEmail,
					cra,
					info.getAgent() == null ? null : info.getAgent().getRole(),
					status
				);

			return orderId;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail)
		throws FDResourceException, ErpTransactionException, DeliveryPassException {
		if (managerHome == null) {
			lookupManagerHome();
		}

		try {
			if (orderBelongsToUser(info.getIdentity(), saleId)) {
				FDCustomerManagerSB sb = managerHome.create();
				return sb.cancelOrder(info, saleId, sendEmail);
			} else {
				throw new FDResourceException("Order not found in current user's order history.");
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void modifyOrder(
		FDActionInfo info,
		FDModifyCartModel cart,
		Set appliedPromos,
		boolean sendEmail,
		CustomerRatingI cra,
		EnumDlvPassStatus status)
		throws FDResourceException, ErpTransactionException, ErpFraudException, ErpAuthorizationException,DeliveryPassException {
		
		lookupManagerHome();
		try {
			String saleId = cart.getOriginalOrder().getErpSalesId();
			if (!orderBelongsToUser(info.getIdentity(), saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && cra.isOnFDAccount()) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
			}
			
			ErpModifyOrderModel order = FDOrderTranslator.getErpModifyOrderModel(cart);
			order.setTransactionSource(info.getSource());
			order.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());

			FDCustomerManagerSB sb = managerHome.create();
			EnumSaleType type = cart.getOriginalOrder().getOrderType();
			if (EnumSaleType.REGULAR.equals(type)){
				sb.modifyOrder(
						info.getIdentity(),
						saleId,
						order,
						appliedPromos,
						cart.getOriginalReservationId(),
						sendEmail,
						cra,
						info.getAgent() == null ? null : info.getAgent().getRole(),
						status);
			}else if (EnumSaleType.SUBSCRIPTION.equals(type)){
				sb.modifyAutoRenewOrder(
						info.getIdentity(),
						saleId,
						order,
						appliedPromos,
						cart.getOriginalReservationId(),
						sendEmail,
						cra,
						info.getAgent() == null ? null : info.getAgent().getRole(),
						status);
				sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), saleId, type, cra);
			}

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}catch (ErpSaleNotFoundException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
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
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isOrderBelongsToUser(identity, saleId);

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
	public static void addComplaint(ErpComplaintModel complaint, String saleId,FDIdentity identity ) throws FDResourceException, ErpComplaintException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			//
			// add the complaint to the sale
			//
			sb.addComplaint(complaint, saleId,identity.getErpCustomerPK(),identity.getFDCustomerPK());

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
	public static void approveComplaint(String complaintId, boolean isApproved, String csrId, boolean sendMail)
		throws FDResourceException, ErpComplaintException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.approveComplaint(complaintId, isApproved, csrId, sendMail);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static XMLEmailI makePreviewCreditEmail(FDCustomerInfo custInfo,String saleId,ErpComplaintModel complaint) {
		return FDEmailFactory.getInstance().createConfirmCreditEmail(custInfo,saleId,complaint);
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

	public static List getAlerts(String customerId) throws FDResourceException {
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
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isOnAlert(new PrimaryKey(customerId), alertType);
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
	public static FDCartModel checkAvailability(FDIdentity identity, FDCartModel cart, long timeout) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();

			boolean skipModifyLines = true;
			if (cart instanceof FDModifyCartModel) {
				FDReservation originalReservation = ((FDModifyCartModel) cart).getOriginalOrder().getDeliveryReservation();
				Date d1 = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
				Date d2 = DateUtil.truncate(originalReservation.getStartTime());
				if (d1.before(d2)) {
					// order moved to a prior day, need to re-check everything
					skipModifyLines = false;
				}
			}

			
			// note: FDModifyCartLineI instances skipped
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart, skipModifyLines);

			long timer = System.currentTimeMillis();
			Map fdInvMap = sb.checkAvailability(identity, createOrder, timeout);
			timer = System.currentTimeMillis() - timer;

			Map invs = FDAvailabilityMapper.mapInventory(cart, createOrder, fdInvMap, skipModifyLines);
			cart.setAvailability(new FDCompositeAvailability(invs));

			if (LOGGER.isInfoEnabled()) {
				int unavCount = 0;
				for (Iterator i = invs.keySet().iterator(); i.hasNext();) {
					Integer key = (Integer) i.next();
					FDAvailabilityI inv = (FDAvailabilityI) invs.get(key);
					FDReservation deliveryReservation = cart.getDeliveryReservation();
					DateRange requestedRange = new DateRange(deliveryReservation.getStartTime(), deliveryReservation.getEndTime());
					FDAvailabilityInfo info = inv.availableCompletely(requestedRange);
					if (!info.isAvailable()) {
						unavCount++;
						FDCartLineI cartLine = cart.getOrderLineById(key.intValue());
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
	public static List locateCustomers(FDCustomerSearchCriteria criteria) throws FDResourceException {
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

	public static List locateOrders(FDOrderSearchCriteria criteria) throws FDResourceException {
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
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.doEmail(email);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeSurvey(FDSurveyResponse survey) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.storeSurvey(survey);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
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
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.sendPasswordEmail(emailAddress, toAltEmail);

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
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.isPasswordRequestExpired(emailAddress, passReq);

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
		lookupManagerHome();
		try {
			//
			// Check for valid password length
			//
			if (password.length() < 4)
				throw new ErpInvalidPasswordException("Please enter a password that is at least four characters long.");
			FDCustomerManagerSB sb = managerHome.create();
			sb.changePassword(info, emailAddress, password);

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
			//FDReferralProgramModel referralProgram = FDReferralManager.loadLastestActiveReferralProgram();
			//mailInfo.setReferralProgram(referralProgram);
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
			mailer.enqueueEmail(email);						
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewaySB");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewaySB");
		}		
	}

	public static void sendContactServiceEmail(FDCustomerInfo customer, String subject, String body, boolean chefstable, boolean feedback) throws FDResourceException {
		lookupMailerGatewayHome();
		lookupManagerHome();
		try {
			MailerGatewaySB mailer = mailerHome.create();
			XMLEmailI email = null;
			if(chefstable){
				email = FDEmailFactory.getInstance().createChefsTableEmail(customer, subject, body);
			}else{
				if(feedback){
					email = FDEmailFactory.getInstance().createFeedbackEmail(customer, subject, body);
				}else{
					email = FDEmailFactory.getInstance().createContactServiceEmail(customer, subject, body);
				}
			}
			mailer.enqueueEmail(email);
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewaySB");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewaySB");
		}
	}
	
	public static Map getProductPopularity() throws FDResourceException {
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
	
	public static List getReminderListForToday() throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getReminderListForToday();
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}

	public static void sendReminderEmail(String custId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			sb.sendReminderEmail(new PrimaryKey(custId));
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

	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDCustomerManagerHome) ctx.lookup(FDStoreProperties.getFDCustomerManagerHome());
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

	public static void chargeOrder(
			FDActionInfo info,
			String saleId,
			ErpPaymentMethodI paymentMethod,
			boolean sendEmail,
			CustomerRatingI cra,
			double additionalCharge)
			throws FDResourceException, ErpTransactionException, ErpFraudException, ErpAuthorizationException {
			
			lookupManagerHome();
			try {
				if (!orderBelongsToUser(info.getIdentity(), saleId)) {
					throw new FDResourceException("Order not found in current user's order history.");
				}

				FDCustomerManagerSB sb = managerHome.create();
				sb.chargeOrder(
					info.getIdentity(),
					saleId,
					paymentMethod,
					sendEmail,
					cra,
					info.getAgent(),
					additionalCharge);

			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
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

	public static Map loadProfileAttributeNames() throws FDResourceException {
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
		
	public static List loadProfileAttributeNameCategories() throws FDResourceException {
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
	public static List getDeliveryPassesByStatus(FDIdentity identity, EnumDlvPassStatus status) throws FDResourceException {
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
			FDCustomerManagerSB sb = managerHome.create();
			ErpOrderHistory history = sb.getOrdersByDlvPassId(identity, dlvPassId);
			return new FDOrderHistory(history.getErpSaleInfos());
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
	public static List getRecentOrdersByDlvPassId(FDIdentity identity, String dlvPassId, int noOfDaysOld) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getRecentOrdersByDlvPassId(identity, dlvPassId, noOfDaysOld);

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Map getDeliveryPassesInfo(FDUserI user) throws FDResourceException {
		Map dlvPassesInfo = new HashMap();
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			FDIdentity identity = user.getIdentity();
			List dlvPasses = sb.getDeliveryPasses(identity);
			if(dlvPasses == null || ((dlvPasses!=null) && dlvPasses.size() == 0)){
				//Return Empty map.
				return dlvPassesInfo;
			}
			Map usageInfos = sb.getDlvPassesUsageInfo(identity);
			
			List historyInfo = null;
			Iterator iter = dlvPasses.iterator();
			while(iter.hasNext()){
				DeliveryPassModel model = (DeliveryPassModel) iter.next();
				String dlvPassId = model.getPK().getId();
				DlvPassUsageInfo usageInfo = (DlvPassUsageInfo)usageInfos.get(dlvPassId);
				
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
							historyInfo = new ArrayList();
						}
						historyInfo.add(obj);
					}
					
					dlvPassesInfo.put(DlvPassConstants.ACTIVE_ITEM, info);
					//Calculate Refund.
					double refundAmt = DeliveryPassUtil.calculateRefund(info);
					dlvPassesInfo.put(DlvPassConstants.REFUND_AMOUNT, new Double(refundAmt));
				}else{
					if(historyInfo == null){
						historyInfo = new ArrayList();
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
	private static int getCreditCount(Collection creditCol, String dlvPassId) {
		int credits = 0;
		Iterator iter = creditCol.iterator();
		while(iter.hasNext()){
			ErpActivityRecord activity = (ErpActivityRecord)iter.next();
			if(activity.getDeliveryPassId().equals(dlvPassId)){
				credits++;
			}
		}
		return credits;
	}
	public static FDUserDlvPassInfo getDeliveryPassInfo(FDUserI user) throws FDResourceException {
		lookupManagerHome();
		try {
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getDeliveryPassInfo(user);
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error talking to session bean");
		}
	}
	
	public static Map cancelOrders(FDActionInfo actionInfo,  List customerOrders, boolean sendEmail) throws FDResourceException {
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
			sb.storeRetentionSurvey(fdIdentity, profileAttr
					, profileValue, caseInfo);
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
				FDCustomerManagerSB sb = managerHome.create();
				return sb.hasPurchasedPass(customerPK);

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
	//public static void flipAutoRenewDP(String customerPK)throws FDResourceException {
		lookupManagerHome();
		try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.setHasAutoRenewDP(customerPK,source, initiator, autoRenew);
				//sb.flipAutoRenewDP(customerPK);

		} catch (CreateException ce) {
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
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getWebOrderHistoryInfo(identity);

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
			FDCustomerManagerSB sb = managerHome.create();
			return sb.getValidOrderCount(identity);

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



			for (Iterator it = EnumSiteFeature.getEnumList().iterator(); it.hasNext();) {
				EnumSiteFeature feature = (EnumSiteFeature) it.next();
				if (feature.isSmartStore()) {
					RecommendationService recommendationService = SmartStoreUtil.getRecommendationService(user, feature, null);
					if (recommendationService != null) {
						sb.logCustomerVariant(saleId, user.getIdentity(), feature.getName(), recommendationService.getVariant().getId());
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

	public static FDOrderI getLastNonCOSOrderUsingCC(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus) throws FDResourceException,ErpSaleNotFoundException {
		lookupManagerHome();
		FDCustomerManagerSB sb=null;
		try {
				 sb= managerHome.create();
				FDOrderI order=sb.getLastNonCOSOrderUsingCC(customerID, saleType, saleStatus);
				return order;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static String placeSubscriptionOrder( FDActionInfo info,
			 									 FDCartModel cart,
			 									 Set appliedPromos,
			 									 boolean sendEmail,
			 									 CustomerRatingI cra,
			 									 EnumDlvPassStatus status ) throws FDResourceException, 
			                               						  				   ErpFraudException, 
			                               						  				   //ReservationException,
			                               						  				   DeliveryPassException
			                               						  				    {
		lookupManagerHome();
		String orderId="";
		try {
			EnumPaymentType pt = cart.getPaymentMethod().getPaymentType();
			if (EnumPaymentType.REGULAR.equals(pt) && cra.isOnFDAccount()) {
				cart.getPaymentMethod().setPaymentType(EnumPaymentType.ON_FD_ACCOUNT);
		    }
			ErpCreateOrderModel createOrder = FDOrderTranslator.getErpCreateOrderModel(cart);
			createOrder.setTransactionSource(info.getSource());
			createOrder.setTransactionInitiator(info.getAgent() == null ? null : info.getAgent().getUserId());
			
			FDCustomerManagerSB sb = managerHome.create();
				
				orderId=sb.placeSubscriptionOrder( info.getIdentity(),
				 								  createOrder,
				                                 appliedPromos,
				                                 cart.getDeliveryReservation().getPK().getId(),
				                                 sendEmail,
				                                 cra,
				                                 info.getAgent() == null ? null : info.getAgent().getRole(),
				                                 status
				                               );
				sb.authorizeSale(info.getIdentity().getErpCustomerPK().toString(), orderId, EnumSaleType.SUBSCRIPTION, cra);
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
	
	public static FDUser getFDUser(FDIdentity identity) throws FDAuthenticationException, FDResourceException {
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
	   
	    public static Object[] getAutoRenewalInfo() throws FDResourceException {
	    	Object[] autoRenewInfo=null;
			lookupManagerHome();
			try {
					FDCustomerManagerSB sb = managerHome.create();
					autoRenewInfo = sb.getAutoRenewalInfo();
					return autoRenewInfo;
			} catch (CreateException ce) {
					invalidateManagerHome();
					throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
					invalidateManagerHome();
					throw new FDResourceException(re, "Error talking to session bean");
			}
	    }
	    
	    public static String getAutoRenewSKU(String customerPK) throws FDResourceException {
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
	    public static ErpAddressModel getLastOrderAddress(FDIdentity identity) throws FDResourceException {
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
			}catch (SQLException se) {
				invalidateManagerHome();
				throw new FDResourceException(se, "Error running SQL");
			}
	    }	
	    

	    public static void storeProductRequest(List productRequest,FDSurveyResponse survey) throws FDResourceException {
			lookupManagerHome();
			try {
				FDCustomerManagerSB sb = managerHome.create();
				sb.storeProductRequest(productRequest,survey);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}
	    
	    private static void invalidateRoutingGatewayHome() {
	    	routingGatewayHome = null;
	    }

	    private static void lookupRoutingGatewayHome() throws FDResourceException {
	    	if (routingGatewayHome != null) {
	    		return;
	    	}
	    	Context ctx = null;
	    	try {

	    		ctx = FDStoreProperties.getInitialContext();
	    		routingGatewayHome = (RoutingGatewayHome) ctx.lookup("freshdirect.routing.Gateway");
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
	    
	    public static FDSurveyResponse getCustomerProfileSurveyInfo(FDIdentity identity) throws FDResourceException {
	    	lookupSurveyHome();
			try {
				FDSurveySB sb = surveyHome.create();
				return sb.getCustomerProfile(identity);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
		}
	    
	    public static FDSurveyResponse getSurveyResponse(FDIdentity identity, String survey) throws FDResourceException {
	    	lookupSurveyHome();
			try {
				FDSurveySB sb = surveyHome.create();
				return sb.getSurveyResponse(identity, survey);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
	    }
	    
	    public static FDSurveyResponse getSurveyResponse(FDIdentity identity, EnumSurveyType survey) throws FDResourceException {
	        
	    	return getSurveyResponse(identity, survey.getName());
	    }
	    
	    protected static void lookupSurveyHome() throws FDResourceException {
			Context ctx = null;
			try {
				ctx = FDStoreProperties.getInitialContext();
				surveyHome = (FDSurveyHome) ctx.lookup( FDStoreProperties.getFDSurveyHome() );
			} catch (NamingException ne) {
				throw new FDResourceException(ne);
			} finally {
				try {
					if (ctx != null) {
						ctx.close();
					}
				} catch (NamingException e) {
				}
			}
		}

}
