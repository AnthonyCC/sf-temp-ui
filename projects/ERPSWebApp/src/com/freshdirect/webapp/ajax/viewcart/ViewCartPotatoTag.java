package com.freshdirect.webapp.ajax.viewcart;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ViewCartPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(ViewCartPotatoTag.class);

    private static final String VIEW_CART_POTATO_NAME = "viewCartPotato";

    @Override
    public void doTag() throws JspException, IOException {
        ViewCartCarouselData carousels = null;
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        try {
            SessionInput input = ViewCartCarouselService.getDefaultService().createSessionInput(user, request);
            carousels = ViewCartCarouselService.getDefaultService().populateViewCartTabsRecommendationsAndCarouselSampleCarousel(request, user, input);
        } catch (Exception e) {
            LOGGER.error("recommendation failed", e);
        }
        pageContext.setAttribute(VIEW_CART_POTATO_NAME, SoyTemplateEngine.convertToMap(carousels));
    }

}
