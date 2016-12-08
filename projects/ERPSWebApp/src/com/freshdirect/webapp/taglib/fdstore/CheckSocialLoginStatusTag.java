package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class CheckSocialLoginStatusTag extends BodyTagSupportEx {
	
	private static Category LOGGER = LoggerFactory.getInstance(CheckSocialLoginStatusTag.class);
	
	private boolean isSocialLoginExist;
	
	private boolean isSocialLoginOnly;
	
	@Override
	public int doStartTag() throws JspException {
		
		HttpSession session = pageContext.getSession();
				
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);	
		
		if(user != null)
		{
		
			try {
			
				LOGGER.debug("Id: "+ user.getIdentity().getErpCustomerPK());
				
				isSocialLoginOnly =	ExternalAccountManager.isExternalLoginOnlyUser(user.getIdentity().getErpCustomerPK(),
						EnumExternalLoginSource.SOCIAL);
		
		
			} catch (FDResourceException e1) {
					e1.printStackTrace();
			}
		
		}
		
		return SKIP_BODY;
	}
	
	

}
