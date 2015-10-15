package com.freshdirect.webapp.ajax.expresscheckout.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutSuccessData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SinglePageCheckoutSuccessPotatoTag extends SimpleTagSupport {

	private static final String ORDER_ID = "orderId";

	private String name = "singlePageCheckoutSuccessPotato";

	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		String requestURI = request.getRequestURI();
		String orderId = request.getParameter(ORDER_ID);
		Map<String, Object> potato = new HashMap<String, Object>();
		if (orderId != null && !orderId.isEmpty()) {
			try {
                SinglePageCheckoutSuccessData result = SinglePageCheckoutFacade.defaultFacade().loadSuccess(requestURI, user, orderId, session);
				potato = SoyTemplateEngine.convertToMap(result);
				context.setAttribute(name, potato);
			} catch (FDResourceException e) {
				throw new JspException(e);
			} catch (TemplateException e) {
				throw new JspException(e);
			}
		} else {
			potato.put("redirectUrl", "/expressco/view_cart.jsp");
		}
		context.setAttribute(name, potato);
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
