package com.freshdirect.webapp.ajax.cart;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class PendingExternalAtcItemPotatoTag extends SimpleTagSupport{
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getInstance( PendingExternalAtcItemPotatoTag.class );

	private String name = "pendingExternalAtcItemPotato";
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext ctx = (PageContext) getJspContext();
		FDSessionUser user = (FDSessionUser) ctx.getSession().getAttribute(SessionName.USER);
		ctx.setAttribute(name, SoyTemplateEngine.convertToMap(PendingExternalAtcItemsPopulator.createPendingExternalAtcItemsData(user)));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
