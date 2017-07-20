package com.freshdirect.webapp.ajax.reorder;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

/**
 * Tag support for QS bottom carousel
 * 
 * @ticket APPDEV-3901
 * 
 * @author segabor
 *
 */
public class QuickShopRecommenderPotatoTag extends SimpleTagSupport {
	private static final Logger LOGGER = LoggerFactory.getInstance(QuickShopRecommenderPotatoTag.class);
	
	private static final String POTATO_NAME = "qsBottomPotato";
	
	@Override
	public void doTag() throws JspException, IOException {
		ViewCartCarouselData carousels = null;
		PageContext pageContext = (PageContext) getJspContext();
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = request.getSession();
            FDSessionUser user = (FDSessionUser) QuickShopHelper.getUserFromSession(session);
            SessionInput input = QuickShopCarouselService.defaultService().createSessionInput(user, request);
            carousels = QuickShopCarouselService.defaultService().populateTabsRecommendationsAndCarousel(request, user, input);
			carousels.getRecommendationTabs().add(0, new RecommendationTab(QuickShopCrazyQuickshopRecommendationService.defaultService().getTheCrazyQuickshopTitle(null),
                    QuickShopCrazyQuickshopRecommendationService.QUICKSHOP_VIRTUAL_SITE_FEATURE));
		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		pageContext.setAttribute(POTATO_NAME, SoyTemplateEngine.convertToMap(carousels));
	}
}
