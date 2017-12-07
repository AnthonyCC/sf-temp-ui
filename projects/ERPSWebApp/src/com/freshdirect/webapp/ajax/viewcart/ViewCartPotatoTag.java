package com.freshdirect.webapp.ajax.viewcart;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.CheckoutCarouselService;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ViewCartPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(ViewCartPotatoTag.class);

    private static final String VIEW_CART_POTATO_NAME = "viewCartPotato";
    private String name;
	public String getName() {
		return name == null? VIEW_CART_POTATO_NAME: name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
    @Override
    public void doTag() throws JspException, IOException {
        ViewCartCarouselData carousels = null;
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        try {
        	SessionInput input;
        	if( VIEW_CART_POTATO_NAME.equals(getName()) ) {
        		input = ViewCartCarouselService.getDefaultService().createSessionInput(user, request);
        		carousels = ViewCartCarouselService.getDefaultService().populateTabsRecommendationsAndCarousel(request, user, input);
        	} else {
        		input = QuickShopCarouselService.defaultService().createSessionInput(user, request);
        		carousels = CheckoutCarouselService.getDefaultService().populateTabsRecommendationsAndCarousel(request, user, input);
        	}
            input.setError(request.getParameter("warning_message") != null);

        } catch (Exception e) {
            LOGGER.error("recommendation failed", e);
        }
        pageContext.setAttribute(getName(), SoyTemplateEngine.convertToMap(carousels));
    }

}
