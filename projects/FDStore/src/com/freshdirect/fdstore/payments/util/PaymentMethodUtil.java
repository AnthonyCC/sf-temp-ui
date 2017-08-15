package com.freshdirect.fdstore.payments.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;

public class PaymentMethodUtil {
	private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodUtil.class );
	
	public static ErpPaymentMethodI getSystemDefaultPaymentMethod(FDActionInfo actionInfo, Collection<ErpPaymentMethodI> pMethods) {
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
				ErpAuthorizationModel authModel=null;
					try {
						if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType()) && ((null != paymentMethod.getExpirationDate() && 
								paymentMethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
								|| (paymentMethod.isAvsCkeckFailed() && !paymentMethod.isBypassAVSCheck()))){
								continue;
							
						}
							else{								
						if(FDStoreProperties.isPaymentVerificationEnabled()){
							if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())){
								if(!FDCustomerManager.isECheckRestricted(actionInfo.getIdentity())){
								authModel = FDCustomerManager.verifyCard(actionInfo, paymentMethod, FDStoreProperties.isPaymentechGatewayEnabled());
								}else{
									continue;
								}
							}else{
								authModel = FDCustomerManager.verify(actionInfo, paymentMethod);
							}
						
						if(null != authModel && authModel.isApproved()){
							defaultPayment = paymentMethod;
							break;
						}
						}else{
							defaultPayment = paymentMethod;
							break;
						}
						}
					} catch (FDResourceException e) {
						LOGGER.error(e);
					} catch (ErpTransactionException e) {
						LOGGER.error(e);
					} catch (ErpAuthorizationException e) {
						LOGGER.error(e);
					}
				}
			else if(EnumPaymentMethodType.PAYPAL.equals(paymentMethod.getPaymentMethodType()) || EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())){
					defaultPayment = paymentMethod;
					break;			
				}
			}	
		}
		}
		return defaultPayment;
	}

	private static void sortPaymentMethodsByPriority(List<ErpPaymentMethodI> paymentMethods) {
		    	Collections.sort(paymentMethods, new PaymentMethodDefaultComparator());
	}
	
	public static void updateDefaultPaymentMethod(FDActionInfo info, Collection<ErpPaymentMethodI> pMethods, String paymentId, EnumPaymentMethodDefaultType type, boolean isVerificationRequired) {		
		if(null == paymentId || "".equals(paymentId)){
			return;
		}
		ErpPaymentMethodI pmethod = null;
		List<ErpPaymentMethodI> paymentMethods = new ArrayList<ErpPaymentMethodI>(pMethods);
		for(ErpPaymentMethodI paymentMethod : paymentMethods){
			if(paymentMethod.getPK().getId().equals(paymentId)){
				pmethod = paymentMethod;
				break;
			}
		}
		try {
			if(null == pmethod){
				throw new FDResourceException("Payment method not registered with user");
			}
			if (EnumPaymentMethodType.CREDITCARD.equals(pmethod.getPaymentMethodType()) || EnumPaymentMethodType.ECHECK.equals(pmethod.getPaymentMethodType())) {
				ErpAuthorizationModel authModel=null;
					try {
						if(EnumPaymentMethodType.CREDITCARD.equals(pmethod.getPaymentMethodType())&&  ((null != pmethod.getExpirationDate() && 
								pmethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
								|| (pmethod.isAvsCkeckFailed() && !pmethod.isBypassAVSCheck()))){
								return;
							}
							else{
						if(FDStoreProperties.isPaymentVerificationEnabled() && isVerificationRequired){
							if(EnumPaymentMethodType.ECHECK.equals(pmethod.getPaymentMethodType())){
								if(!FDCustomerManager.isECheckRestricted(info.getIdentity())){
								authModel = FDCustomerManager.verifyCard(info, pmethod, FDStoreProperties.isPaymentechGatewayEnabled());
								}else{
									return;
								}
							}else{
								authModel = FDCustomerManager.verify(info, pmethod);
							}
						if(null != authModel && authModel.isApproved() && pmethod.getExpirationDate() != null){
							FDCustomerManager.setDefaultPaymentMethod(info, pmethod.getPK(), type, true);
							return;
						}
						}else{
							FDCustomerManager.setDefaultPaymentMethod(info, pmethod.getPK(), type, true);
							return;
						}
							}
					} catch (FDResourceException e) {
						LOGGER.error(e);
					} catch (ErpTransactionException e) {
						LOGGER.error(e);
					} catch (ErpAuthorizationException e) {
						LOGGER.error(e);
					}
				}
			FDCustomerManager.setDefaultPaymentMethod(info, pmethod.getPK(), type, true);
		} catch (FDResourceException e) {
			LOGGER.error(e);
		}		
	}
}
