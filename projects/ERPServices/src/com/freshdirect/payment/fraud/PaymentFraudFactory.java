package com.freshdirect.payment.fraud;

import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.ExpiringReference;

public class PaymentFraudFactory {
	private final static PaymentFraudFactory INSTANCE = new PaymentFraudFactory();
	
	private static ExpiringReference<List<RestrictedPaymentMethodModel>> patternsCache = 
		new ExpiringReference<List<RestrictedPaymentMethodModel>>(1440 * 60 * 1000) {
			protected List<RestrictedPaymentMethodModel> load() {
				try {
					return PaymentFraudManager.loadAllPatterns();
				} catch (EJBException ex) {
					throw new FDRuntimeException(ex);
				} catch (FDResourceException ex) {
					throw new FDRuntimeException(ex);
				}
			}				
	};
	
	private static ExpiringReference<List<RestrictedPaymentMethodModel>> badAccountsCache = 
		new ExpiringReference<List<RestrictedPaymentMethodModel>>(1440 * 60 * 1000) {
			protected List<RestrictedPaymentMethodModel> load() {
				try {
					return PaymentFraudManager.loadAllBadPaymentMethods();
				} catch (EJBException ex) {
					throw new FDRuntimeException(ex);
				} catch (FDResourceException ex) {
					throw new FDRuntimeException(ex);
				}
			}
	};

	private PaymentFraudFactory() {
	}

	public static PaymentFraudFactory getInstance() {
		return INSTANCE;
	}

	public List<RestrictedPaymentMethodModel> getPatternListFromCache() {
		return (List<RestrictedPaymentMethodModel>)patternsCache.get();
	}

	public List<RestrictedPaymentMethodModel> getBadAccountListFromCache() {
		return (List<RestrictedPaymentMethodModel>)badAccountsCache.get();
	}

	public void forcePatternsCacheRefresh() {
		patternsCache.forceRefresh();
	}

	public void forceBadAccountsCacheRefresh() {
		badAccountsCache.forceRefresh();
	}

}
