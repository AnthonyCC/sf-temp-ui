package com.freshdirect.webapp.taglib.giftcard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.fdstore.content.*;

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
	
	public static boolean resendEmail(HttpServletRequest request, String saleId, String certificationNum, String resendEmailId, String recipName, String personalMsg,boolean toPurchaser, boolean toLastRecipient) {
		boolean success = false;
		try{
			FDActionInfo actionInfo = AccountActivityUtil.getActionInfo(request.getSession());
			FDCustomerManager.resendEmail(saleId, certificationNum, resendEmailId, recipName, personalMsg,toPurchaser, toLastRecipient, actionInfo.getSource());
			success = true;
		}catch(FDResourceException fe){
			LOGGER.debug(fe);
		}
		return success;
	}
	
	
	public static String[] sendGiftCardCancellationEmail(HttpServletRequest request, String saleId, String givexNum, boolean toRecipient, boolean toPurchaser, boolean newRecipient, String newRecipientEmail) {
		String[] sentEmailAddresses = null;
//		sentEmailAddresses= new String[]{"abc","",""};
		try{		
			sentEmailAddresses= FDCustomerManager.sendGiftCardCancellationEmail(saleId, givexNum, toRecipient, toPurchaser, newRecipient, newRecipientEmail);
			
		}catch(FDResourceException fe){
			LOGGER.debug(fe);
		}
		return sentEmailAddresses;
	}

	
	public static String getTemplateName(String templateId) {
        try {
        	return ContentFactory.getInstance().getDomainValueById(templateId).getLabel();
        }catch(Exception e){
            return "Default";
        }
	}

	

}