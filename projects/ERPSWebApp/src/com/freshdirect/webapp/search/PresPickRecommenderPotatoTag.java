package com.freshdirect.webapp.search;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CarouselDataCointainer;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class PresPickRecommenderPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance(PresPickRecommenderPotatoTag.class);

	private static final String POTATO_NAME = "presPickRecommenderPotato";

	@Override
	public void doTag() throws JspException {
		try {
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpSession session = request.getSession();
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

			SessionInput si = new SessionInput(user);
			si.setCurrentNode(ContentFactory.getInstance().getContentNode("gro"));
			Recommendations groRecommendations = ProductRecommenderUtil.doRecommend(user,
					EnumSiteFeature.BRAND_NAME_DEALS, si);
			CarouselDataCointainer carouselData = new CarouselDataCointainer();
			if (groRecommendations != null && groRecommendations.getAllProducts().size() > 0) {
				carouselData.setCarousel1(CarouselService.defaultService().createCarouselData(null,
						groRecommendations.getVariant().getServiceConfig().get("prez_title_gro"),
						groRecommendations.getAllProducts(), user, "", groRecommendations.getVariant().getId()));
			}
			si.setCurrentNode(ContentFactory.getInstance().getContentNode("fro"));
			Recommendations froRecommendations = ProductRecommenderUtil.doRecommend(user,
					EnumSiteFeature.BRAND_NAME_DEALS, si);
			if (froRecommendations != null && froRecommendations.getAllProducts().size() > 0) {
				carouselData.setCarousel2(CarouselService.defaultService().createCarouselData(null,
						froRecommendations.getVariant().getServiceConfig().get("prez_title_fro"),
						froRecommendations.getAllProducts(), user, "", froRecommendations.getVariant().getId()));
			}
			si.setCurrentNode(ContentFactory.getInstance().getContentNode("dai"));
			Recommendations daiRecommendations = ProductRecommenderUtil.doRecommend(user,
					EnumSiteFeature.BRAND_NAME_DEALS, si);
			if (daiRecommendations != null && daiRecommendations.getAllProducts().size() > 0) {
				carouselData.setCarousel3(CarouselService.defaultService().createCarouselData(null,
						daiRecommendations.getVariant().getServiceConfig().get("prez_title_dai"),
						daiRecommendations.getAllProducts(), user, "", daiRecommendations.getVariant().getId()));
			}
			Map<String, Object> data = SoyTemplateEngine.convertToMap(carouselData);
			pageContext.setAttribute(POTATO_NAME, data);

		} catch (Exception e) {
			LOGGER.error("search recommendation failed", e);
		}

	}

}
