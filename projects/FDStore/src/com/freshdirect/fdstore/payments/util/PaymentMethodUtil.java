package com.freshdirect.fdstore.payments.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAlertType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;

public class PaymentMethodUtil {
	private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodUtil.class );

	public static ErpPaymentMethodI getSystemDefaultPaymentMethod(FDActionInfo actionInfo, Collection<ErpPaymentMethodI> pMethods, boolean isVerificationRequired/*WHat do we do with this?*/) {
		ErpPaymentMethodI defaultPayment = null;
		List<ErpPaymentMethodI> paymentMethods = new ArrayList<ErpPaymentMethodI>(pMethods);
		if(paymentMethods.size() == 1){
			defaultPayment = paymentMethods.get(0);
		}
		else if(paymentMethods.size() > 1){
		sortPaymentMethodsByPriority(paymentMethods);
		for(ErpPaymentMethodI paymentMethod : paymentMethods) {
			if(null != paymentMethod && null != paymentMethod.getPK()){
			if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType()) || EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {

					try {
						if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType()) && ((null != paymentMethod.getExpirationDate() &&
								paymentMethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
								|| (paymentMethod.isAvsCkeckFailed() && !paymentMethod.isBypassAVSCheck()))){
								continue;

						}
							else{
								if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())){
									if(!FDCustomerManager.isECheckRestricted(actionInfo.getIdentity()) && !FDCustomerManager.isOnAlert(actionInfo.getIdentity().getErpCustomerPK(), EnumAlertType.ECHECK.getName())){
										defaultPayment = paymentMethod;
										break;
									}
								} else {
									defaultPayment = paymentMethod;
									break;
								}

							}
					} catch (FDResourceException e) {
						LOGGER.error(e);
					}
				} else
				try {
					if(EnumPaymentMethodType.PAYPAL.equals(paymentMethod.getPaymentMethodType()) || (EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType()) && !FDCustomerManager.isOnAlert(actionInfo.getIdentity().getErpCustomerPK(), EnumAlertType.EBT.getName()))){
							defaultPayment = paymentMethod;
							break;
						}
				}catch (FDResourceException e) {
					LOGGER.error(e);
				}
			}
		}
		}
		return defaultPayment;
	}

	private static void sortPaymentMethodsByPriority(List<ErpPaymentMethodI> paymentMethods) {
		    	Collections.sort(paymentMethods, new PaymentMethodDefaultComparator());
	}

	public static boolean isNewCardHigherPrioriy(ErpPaymentMethodI newPaymentMethod, List<ErpPaymentMethodI> paymentMethods){
		if(paymentMethods.size() == 1){
			return true;
		}
		if(paymentMethods.size() >1){
		sortPaymentMethodsByPriority(paymentMethods);
		if(newPaymentMethod.getCardType().getPriority() <= paymentMethods.get(0).getCardType().getPriority()){
			return true;
			}
		}
		return false;
	}

	public static void updateDefaultPaymentMethod(FDActionInfo info, Collection<ErpPaymentMethodI> pMethods, String paymentId, EnumPaymentMethodDefaultType type, boolean isVerificationRequired) {
		if(null == paymentId || "".equals(paymentId)){
			return;
		}

		List<ErpPaymentMethodI> paymentMethods = new ArrayList<ErpPaymentMethodI>(pMethods);
		ErpPaymentMethodI pmethod = getPaymentMethod(paymentId, paymentMethods);
		try {
			if(null == pmethod){
				throw new FDResourceException("Payment method not registered with user");
			}
			if (EnumPaymentMethodType.CREDITCARD.equals(pmethod.getPaymentMethodType()) || EnumPaymentMethodType.ECHECK.equals(pmethod.getPaymentMethodType())) {
				ErpAuthorizationModel authModel=null;
					try {
						if(paymentMethods.size() >1 && EnumPaymentMethodType.CREDITCARD.equals(pmethod.getPaymentMethodType())&&  ((null != pmethod.getExpirationDate() &&
								pmethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
								|| (pmethod.isAvsCkeckFailed() && !pmethod.isBypassAVSCheck()))){
								return;
							}
							else{
								FDCustomerManager.setDefaultPaymentMethod(info, pmethod.getPK(), type, true);
								return;

							}
					} catch (FDResourceException e) {
						LOGGER.error(e);
					}
				}
			FDCustomerManager.setDefaultPaymentMethod(info, pmethod.getPK(), type, true);
		} catch (FDResourceException e) {
			LOGGER.error(e);
		}
	}

    public static ErpPaymentMethodI getPaymentMethod(String paymentId, Collection<ErpPaymentMethodI> paymentMethods) {
		ErpPaymentMethodI pmethod = null;
		for(ErpPaymentMethodI paymentMethod : paymentMethods){
			if(paymentMethod.getPK().getId().equals(paymentId)){
				pmethod = paymentMethod;
				break;
			}
		}
		return pmethod;
	}
    
	public static String getLast4AccNumber(ErpPaymentMethodI paymentMethod) {
		int cardLength = paymentMethod.getAccountNumLast4().length();
		String lastFourDigits = paymentMethod.getAccountNumLast4().substring(cardLength - 4, cardLength);
		return lastFourDigits;
	}
    
	public static String getExpDateMMYYYY(ErpPaymentMethodI paymentMethod) {
		String expDateMMYYYY = "";
		if (null != paymentMethod && null != paymentMethod.getExpirationDate()) {
			Date date = paymentMethod.getExpirationDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			expDateMMYYYY = " " + (cal.get(Calendar.MONTH) + 1);
			expDateMMYYYY = expDateMMYYYY + "/";
			expDateMMYYYY = expDateMMYYYY + cal.get(Calendar.YEAR);
		}
		return expDateMMYYYY;
	}
}
