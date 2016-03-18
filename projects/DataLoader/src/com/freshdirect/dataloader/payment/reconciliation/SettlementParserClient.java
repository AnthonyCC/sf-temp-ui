package com.freshdirect.dataloader.payment.reconciliation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Category;

import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;

public abstract class SettlementParserClient implements SynchronousParserClient {
	
	private final static Category LOGGER = LoggerFactory.getInstance(SettlementParserClient.class);

	protected final SettlementBuilderI builder;
	protected final ReconciliationSB reconciliationSB;
	protected final PayPalReconciliationSB ppReconSB;

	public SettlementParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB) {
		this.builder = builder;
		this.reconciliationSB = reconciliationSB;
		this.ppReconSB = null;
	}
	
	public SettlementParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB, PayPalReconciliationSB ppReconSB) {
		this.builder = builder;
		this.reconciliationSB = reconciliationSB;
		this.ppReconSB = ppReconSB;
	}
	

	public void accept(Object o) {
	
		try {
			Method m = this.getClass().getMethod("process", new Class[] { o.getClass()});
			m.invoke(this, new Object[] { o });
	
		} catch (IllegalAccessException e) {
			LOGGER.error("IllegalAccessException occured", e);
			// !!!
	
		} catch (InvocationTargetException e) {
			LOGGER.error("InvocationTargetException occured", e.getTargetException());
			// !!!
	
		} catch (NoSuchMethodException e) {
			LOGGER.error("NoSuchMethodException occured", e);
			// !!!
		}
	
	}

	

}
