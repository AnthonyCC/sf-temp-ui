package com.freshdirect.payment.fraud;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAccountVerificationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.ejb.CPMServerGateway;
import com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodHome;
import com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodSB;

public class PaymentFraudManager {

	private static Category LOGGER = LoggerFactory.getInstance(PaymentFraudManager.class);

	private static  RestrictedPaymentMethodHome restrictedPaymentMethodHome = null;

	public static PrimaryKey createRestrictedPaymentMethod(RestrictedPaymentMethodModel restrictedPaymentMethod) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.createRestrictedPaymentMethod(restrictedPaymentMethod);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}		
	}

	public static RestrictedPaymentMethodModel getRestrictedPaymentMethod(PrimaryKey pk) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.findRestrictedPaymentMethodByPrimaryKey(pk);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}
	
	public static List<RestrictedPaymentMethodModel> getRestrictedPaymentMethodsByCustomerId(String customerId, EnumRestrictedPaymentMethodStatus status) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.findRestrictedPaymentMethodByCustomerId(customerId, status);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static RestrictedPaymentMethodModel getRestrictedPaymentMethodByPaymentMethodId(String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.findRestrictedPaymentMethodByPaymentMethodId(paymentMethodId, status);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static List<RestrictedPaymentMethodModel> getRestrictedPaymentMethods(RestrictedPaymentMethodCriteria criteria) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.findRestrictedPaymentMethods(criteria);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}
	
	public static void storeRestrictedPaymentMethod(RestrictedPaymentMethodModel restrictedPaymentMethod) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			sb.storeRestrictedPaymentMethod(restrictedPaymentMethod);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}
	
	public static void removeRestrictedPaymentMethod(PrimaryKey pk, String lastModifyUser) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			sb.removeRestrictedPaymentMethod(pk, lastModifyUser);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}						
	}
	
	public static List<RestrictedPaymentMethodModel> loadAllPatterns() throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.loadAllPatterns();
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static List<RestrictedPaymentMethodModel> loadAllRestrictedPaymentMethods() throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.loadAllRestrictedPaymentMethods();
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static List<RestrictedPaymentMethodModel> loadAllBadPaymentMethods() throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.loadAllBadPaymentMethods();
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod, boolean useBadAccountCache) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.checkBadAccount(erpPaymentMethod, useBadAccountCache);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static ErpPaymentMethodI getPaymentMethodByAccountInfo(RestrictedPaymentMethodModel restrictedPaymentMethod) throws FDResourceException {
		lookupRestrictedPaymentMethodHome();
		try {
			RestrictedPaymentMethodSB sb = restrictedPaymentMethodHome.create();
			return sb.findPaymentMethodByAccountInfo(restrictedPaymentMethod);
		} catch (RemoteException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateRestrictedPaymentMethodHome();
			throw new FDResourceException(e, "Error creating session bean");
		}				
	}

	public static void addRestrictedPaymentMethod(ErpPaymentMethodI paymentMethod, EnumRestrictionReason restrictionReason, String note) {
		
		try {
			RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
			if (paymentMethod.getBankAccountType() != null) {
				criteria.setBankAccountType(paymentMethod.getBankAccountType());
			}
			if (paymentMethod.getAbaRouteNumber() != null) {
				criteria.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			}
			criteria.setAccountNumber(paymentMethod.getAccountNumber());
			criteria.setStatus(EnumRestrictedPaymentMethodStatus.BAD);
			List<RestrictedPaymentMethodModel> list = PaymentFraudManager.getRestrictedPaymentMethods(criteria);

			// add only if it's not already in there
			if (list == null || list.size() == 0) {
				RestrictedPaymentMethodModel model = new RestrictedPaymentMethodModel();
				model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
				if (paymentMethod.getBankAccountType() != null) {
					model.setBankAccountType(paymentMethod.getBankAccountType());
				}
				if (paymentMethod.getAbaRouteNumber() != null) {
					model.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
				}
				model.setAccountNumber(paymentMethod.getAccountNumber());
				// set the customer related information of the payment method
				ErpPaymentMethodModel pm = (ErpPaymentMethodModel)PaymentFraudManager.getPaymentMethodByAccountInfo(model);			
				if (pm != null) { 					
					if (pm.getPK() != null) {
						model.setPaymentMethodId(pm.getPK().getId());
					}
					model.setCustomerId(pm.getCustomerId());					
					// set the customer name
					String name = pm.getName();
					int i = name.indexOf(" ");
					String firstName = (i > -1) ? name.substring(0, i) : "";
					String lastName = (i > -1) ? name.substring(i+1) : name;
					model.setFirstName(firstName);
					model.setLastName(lastName);
				}
				model.setStatus(EnumRestrictedPaymentMethodStatus.BAD);
				model.setSource(EnumTransactionSource.SYSTEM);
				model.setCreateDate(new Date());
				model.setCreateUser("sys");
				model.setReason(restrictionReason);
				model.setNote(note);

				createRestrictedPaymentMethod(model);
			}			
		} catch (FDResourceException e) {
			LOGGER.error("addRestrictedPaymentMethod: Account Number = " + StringUtil.maskCreditCard(paymentMethod.getAccountNumber()) + 
					" Aba Route Number" + paymentMethod.getAbaRouteNumber() + "-"+ e.getMessage());
			throw new EJBException(e);
		}
		
	}
			
	public static void removeRestrictedPaymentMethod(ErpPaymentMethodI paymentMethod, boolean removeOnlyIfSystemCreated) {
		
		try {
			RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
			if (paymentMethod.getBankAccountType() != null) {
				criteria.setBankAccountType(paymentMethod.getBankAccountType());
			}
			if (paymentMethod.getAbaRouteNumber() != null) {
				criteria.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			}
			criteria.setAccountNumber(paymentMethod.getAccountNumber());
			criteria.setStatus(EnumRestrictedPaymentMethodStatus.BAD);
			List<RestrictedPaymentMethodModel> list = PaymentFraudManager.getRestrictedPaymentMethods(criteria);

			// remove only if there is one
			if (list != null && list.size() == 1) {
				RestrictedPaymentMethodModel model = (RestrictedPaymentMethodModel) list.get(0);
				String lastUser = (model.getLastModifyUser() != null) ? model.getLastModifyUser() : model.getCreateUser(); 
				if ((removeOnlyIfSystemCreated && EnumTransactionSource.SYSTEM.getCode().equalsIgnoreCase(lastUser))
						|| !removeOnlyIfSystemCreated) {
					removeRestrictedPaymentMethod(model.getPK(), EnumTransactionSource.SYSTEM.getCode());
				}
			}			
		} catch (FDResourceException e) {
			LOGGER.error("removeRestrictedPaymentMethod: Account Number = " + StringUtil.maskCreditCard(paymentMethod.getAccountNumber()) + 
					" Aba Route Number" + paymentMethod.getAbaRouteNumber() + "-"+ e.getMessage());
			throw new EJBException(e);
		}
		
	}

	public static EnumRestrictionReason translateRestrictionReason(EnumPaymentResponse paymentResponse) {
		
		EnumRestrictionReason restrictedReason = EnumRestrictionReason.GENERAL_ACCOUNT_PROBLEM;		
		
		if (EnumPaymentResponse.ACCOUNT_HOLDER_DECEASED_PT.equals(paymentResponse) || EnumPaymentResponse.ACCOUNT_HOLDER_DECEASED_R.equals(paymentResponse)
				|| EnumPaymentResponse.ACCOUNT_HOLDER_DECEASED_D.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.CUSTOMER_DECEASED; 
		} else if (EnumPaymentResponse.BENEFICIARY_DECEASE_PT.equals(paymentResponse) || EnumPaymentResponse.BENEFICIARY_DECEASED_R.equals(paymentResponse)
				|| EnumPaymentResponse.BENEFICIARY_DECEASED_D.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.BENEFICIARY_DECEASED; 			
		} else if (EnumPaymentResponse.BAD_ACCOUNT_NUMBER_DATA_PT.equals(paymentResponse) || EnumPaymentResponse.INVALID_ACCOUNT_NUM_FORMAT_PT.equals(paymentResponse)
				|| EnumPaymentResponse.INVALID_ACCOUNT_NUMBER_PT.equals(paymentResponse) || EnumPaymentResponse.INVALID_ACCOUNT_NUMBER_D.equals(paymentResponse)
				|| EnumPaymentResponse.INVALID_ACCOUNT_NUMBER_R.equals(paymentResponse) || EnumPaymentResponse.UNKNOWN_ACCOUNT_R.equals(paymentResponse) 
				|| EnumPaymentResponse.UNKNOWN_ACCOUNT_D.equals(paymentResponse) || EnumPaymentResponse.UNKNOWN_ACCOUNT_PT.equals(paymentResponse)
				|| EnumPaymentResponse.NON_CONVERTIBLE_ACCOUNT_PT.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.INVALID_ACCOUNT_NUMBER; 
		} else if (EnumPaymentResponse.CLOSED_ACCOUNT_PT.equals(paymentResponse) || EnumPaymentResponse.CLOSED_ACCOUNT_R.equals(paymentResponse)
				|| EnumPaymentResponse.CLOSED_ACCOUNT_D.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.CLOSED_ACCOUNT; 			
		} else if (EnumPaymentResponse.FROZEN_ACCOUNT_PT.equals(paymentResponse) || EnumPaymentResponse.FROZEN_ACCOUNT_R.equals(paymentResponse)
				|| EnumPaymentResponse.FROZEN_ACCOUNT_D.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.FROZEN_ACCOUNT; 			
		} else if (EnumPaymentResponse.INSUFFIENT_FUNDS_R.equals(paymentResponse) || EnumPaymentResponse.INSUFFIENT_FUNDS_D.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.INSUFFICIENT_FUNDS; 			
		} else if (EnumPaymentResponse.INVALID_ACCOUNT_TYPE_PT.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.INVALID_ACCOUNT_TYPE; 			
		} else if (EnumPaymentResponse.INVALID_TRANSIT_NUMBER_PT.equals(paymentResponse) || EnumPaymentResponse.UNKNOWN_TRANSIT_NUMBER_PT.equals(paymentResponse)
				|| EnumPaymentResponse.ACH_NON_PARTICIPANT_PT.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.INVALID_ABA_ROUTE_NUMBER; 			
		} else if (EnumPaymentResponse.ON_NEGATIVE_FILE_PT.equals(paymentResponse)) {
			restrictedReason = EnumRestrictionReason.ACCOUNT_FRAUD; 			
		}

		return restrictedReason;
		
	}

	public boolean verifyAccountExternal(ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		try {			
			if ("true".equalsIgnoreCase(FDStoreProperties.getCheckExternalForPaymentMethodFraud())) {	
		        if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())){
		        	ErpAccountVerificationModel model = CPMServerGateway.verifyECAccount(paymentMethod);
		        	if (model != null && (EnumPaymentResponse.APPROVED.getCode().equals(model.getResponseCode())
		        			|| EnumPaymentResponse.APPROVED.getCode().equals(model.getVerificationResult()))) {
		        		return true;
		        	} else {
		        		return false;
		        	}
				}
			}
			return true;
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}
	
	private static void invalidateRestrictedPaymentMethodHome() {
		restrictedPaymentMethodHome = null;
	}

	private static void lookupRestrictedPaymentMethodHome() throws FDResourceException {
		if (restrictedPaymentMethodHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			restrictedPaymentMethodHome = (RestrictedPaymentMethodHome) ctx.lookup("freshdirect.payment.RestrictedPaymentMethod");
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
	
}
