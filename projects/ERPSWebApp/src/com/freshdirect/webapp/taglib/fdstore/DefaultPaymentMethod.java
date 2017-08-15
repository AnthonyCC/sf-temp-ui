package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collection;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DefaultPaymentMethod implements Runnable {
	
	private static Category LOGGER = LoggerFactory.getInstance( DefaultPaymentMethod.class );
	
	private FDActionInfo info;
	private EnumPaymentMethodDefaultType defaultPaymentType;
	Collection<ErpPaymentMethodI> pMethods;

	
	public DefaultPaymentMethod(FDActionInfo info, EnumPaymentMethodDefaultType defaultSys, Collection<ErpPaymentMethodI> pMethods) {
		this.info = info;
		this.defaultPaymentType = defaultSys;
		this.pMethods = pMethods;
	}

	@Override
	public void run() {		
		   
			ErpPaymentMethodI defaultPaymentMethod = com.freshdirect.fdstore.payments.util.PaymentMethodUtil.getSystemDefaultPaymentMethod(info, pMethods);
			if(null != defaultPaymentMethod){
			com.freshdirect.fdstore.payments.util.PaymentMethodUtil.updateDefaultPaymentMethod(info, pMethods,
					defaultPaymentMethod.getPK().getId(), defaultPaymentType, false);			
		}
	}

}
