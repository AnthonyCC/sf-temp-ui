package com.freshdirect.webapp.ajax.viewcart;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.viewcart.data.ProductSamplesCarousel;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class ViewCartPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(ViewCartPotatoTag.class);

    private static final String VIEW_CART_POTATO_NAME = "viewCartPotato";

    @Override
    public void doTag() throws JspException, IOException {
        ViewCartCarouselData carousels = null;
        ProductSamplesCarousel productSamplesTab = null;
        PageContext pageContext = (PageContext) getJspContext();
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            carousels = ViewCartCarouselService.defaultService().populateViewCartTabsRecommendationsAndCarousel(request);
          
            //APPDEV-5516 If the property is true, populate the Donation Carousel , else fall back to Product Sample Carousel
            if(FDStoreProperties.isPropDonationProductSamplesEnabled()){
            	productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageDonationProductSampleCarousel(request);
            } else {
                productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageProductSampleCarousel(request);
            }
            carousels.setProductSamplesTab(productSamplesTab);
        } catch (Exception e) {
            LOGGER.error("recommendation failed", e);
        }
        pageContext.setAttribute(VIEW_CART_POTATO_NAME, SoyTemplateEngine.convertToMap(carousels));
    }

}
