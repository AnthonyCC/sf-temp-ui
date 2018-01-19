package com.freshdirect.webapp.ajax.expresscheckout.cart.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class CartDataPotatoTag extends SimpleTagSupport {

	private String name = "cartDataPotato";
	private String standingOrder=null;
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		try {
            StandingOrderHelper.clearSO3Context(user, request.getParameter("isSO"), standingOrder);
	        
			CartData result = CartDataService.defaultService().loadCartData(request, user);
			Map<String, ?> potato = SoyTemplateEngine.convertToMap(result);
			context.setAttribute(name, potato);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (HttpErrorResponse e) {
			throw new JspException(e);
		}
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
