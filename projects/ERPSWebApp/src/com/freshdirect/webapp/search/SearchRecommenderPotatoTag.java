package com.freshdirect.webapp.search;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ProductRecommenderUtil;


public class SearchRecommenderPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( SearchRecommenderPotatoTag.class );
		
	private static final String POTATO_NAME = "searchRecommenderPotato";
	
	private String productId;
	
	@Override
	public void doTag() throws JspException {
		try {
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = request.getSession();
            FDSessionUser user = (FDSessionUser) QuickShopHelper.getUserFromSession(session);
            Recommendations recommendations = ProductRecommenderUtil.getSearchPageRecommendations(user, getProductId());
    		if (recommendations != null && recommendations.getAllProducts().size() > 0) {
    			CarouselData carouselData = CarouselService.defaultService().createCarouselData(null, "You Might Also Like",
                            recommendations.getAllProducts(), user, "", recommendations.getVariant().getId());
    			HashMap<String, Object > data = new HashMap<String, Object>();
    			data.put("carousel", SoyTemplateEngine.convertToMap(carouselData));
    			pageContext.setAttribute(POTATO_NAME, data);
    			
    		} else {
    			pageContext.setAttribute(POTATO_NAME, null);
    		}
			
		} catch (Exception e) {
			LOGGER.error("search recommendation failed", e);
		}
		
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

}
