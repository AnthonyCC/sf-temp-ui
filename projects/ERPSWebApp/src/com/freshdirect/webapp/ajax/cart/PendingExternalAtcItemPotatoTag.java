package com.freshdirect.webapp.ajax.cart;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class PendingExternalAtcItemPotatoTag extends SimpleTagSupport{
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getInstance( PendingExternalAtcItemPotatoTag.class );

	private String name = "pendingExternalAtcItemPotato";
	private String standingOrder=null;
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext ctx = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
		FDSessionUser user = (FDSessionUser) ctx.getSession().getAttribute(SessionName.USER);
        StandingOrderHelper.clearSO3Context(user, request.getParameter("isSO"), standingOrder);

		ctx.setAttribute(name, SoyTemplateEngine.convertToMap(PendingExternalAtcItemsPopulator.createPendingExternalAtcItemsData(user,request)));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the standingOrder
	 */
	public String getStandingOrder() {
		return standingOrder;
	}

	/**
	 * @param standingOrder the standingOrder to set
	 */
	public void setStandingOrder(String standingOrder) {
		this.standingOrder = standingOrder;
	}
	
}
