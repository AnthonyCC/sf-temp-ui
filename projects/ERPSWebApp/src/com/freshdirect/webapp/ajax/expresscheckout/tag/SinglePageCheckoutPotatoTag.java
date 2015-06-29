package com.freshdirect.webapp.ajax.expresscheckout.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SinglePageCheckoutPotatoTag extends SimpleTagSupport {

	private String name = "singlePageCheckoutPotato";

	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		try {
			SinglePageCheckoutData result = SinglePageCheckoutFacade.defaultFacade().load(user, request);
			Map<String, ?> potato = SoyTemplateEngine.convertToMap(result);
			context.setAttribute(name, potato);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (TemplateException e) {
			throw new JspException(e);
		} catch (RedirectToPage e) {
			throw new JspException(e);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
