package com.freshdirect.webapp.ajax.viewcart;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;

public class ViewCartPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance(ViewCartPotatoTag.class);
	
	private static final String VIEW_CART_POTATO_NAME = "viewCartPotato";
	
	@Override
	public void doTag() throws JspException, IOException {
		ViewCartCarouselData carousels = null;
		PageContext pageContext = (PageContext) getJspContext();
		try {
			carousels = ViewCartCarouselService.defaultService().populateViewCartTabsRecommendationsAndCarousel((HttpServletRequest) pageContext.getRequest());
		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		pageContext.setAttribute(VIEW_CART_POTATO_NAME, carousels);
	}

}
