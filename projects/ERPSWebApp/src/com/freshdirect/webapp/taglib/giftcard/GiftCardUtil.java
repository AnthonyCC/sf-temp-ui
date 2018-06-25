package com.freshdirect.webapp.taglib.giftcard;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.*;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;

public class GiftCardUtil {
	
	 private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodUtil.class );

	public static boolean resendEmail(HttpServletRequest request, String saleId, String certificationNum, String resendEmailId, String recipName, String personalMsg) {
		boolean success = false;
		try{
			FDActionInfo actionInfo = AccountActivityUtil.getActionInfo(request.getSession());
			FDCustomerManager.resendEmail(saleId, certificationNum, resendEmailId, recipName, personalMsg, actionInfo.getSource());
			success = true;
		}catch(FDResourceException fe){
			LOGGER.debug(fe);
		}
		return success;
	}
	
	public static String getTemplateName(String templateId) {
        try {
        	return ContentFactory.getInstance().getDomainValueById(templateId).getLabel();
        }catch(Exception e){
            return "Default";
        }
	}

	

}