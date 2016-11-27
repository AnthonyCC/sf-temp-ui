package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;

public class EnableExpressCheckoutPagesTag extends TagSupport {
	private static final long serialVersionUID = 1275679826520203313L;

	private static final Logger LOGGER = LoggerFactory.getInstance(EnableExpressCheckoutPagesTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		
		final HttpSession session = pageContext.getSession();
		if (session == null) {
			throw new JspException("No session context is available");
		}
		
		final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		if (user == null) {
			throw new JspException("No customer object is available");
		}

		final MasqueradeContext ctx = user.getMasqueradeContext();
		final boolean isXCavailable = FDStoreProperties.isExpressCheckoutEnabledForCSR();
		
		final boolean skipXC = (ctx != null) && !isXCavailable;
		
		LOGGER.info("CRM_Masq? == " + Boolean.valueOf(ctx != null) + "; crm.xc.enabled == " + isXCavailable + " => " + (skipXC?"skip":"enable") );
		
		return skipXC ? SKIP_BODY : EVAL_BODY_INCLUDE;
	}
}
