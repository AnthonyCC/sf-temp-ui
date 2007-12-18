package com.freshdirect.payment.fraud;

import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.ExpiringReference;

public class PaymentFraudFactory {
	private final static PaymentFraudFactory INSTANCE = new PaymentFraudFactory();
	
	private static ExpiringReference patternsCache = new ExpiringReference(1440 * 60 * 1000) {
		protected Object load() {
			try {
				List list = PaymentFraudManager.loadAllPatterns();
				return list;
			} catch (EJBException ex) {
				throw new FDRuntimeException(ex);
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
				
	};
	
	private static ExpiringReference badAccountsCache = new ExpiringReference(1440 * 60 * 1000) {
		protected Object load() {
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

	public List getPatternListFromCache() {
		return (List)patternsCache.get();
	}

	public List getBadAccountListFromCache() {
		return (List)badAccountsCache.get();
	}

	public void forcePatternsCacheRefresh() {
		patternsCache.forceRefresh();
	}

	public void forceBadAccountsCacheRefresh() {
		badAccountsCache.forceRefresh();
	}

}
