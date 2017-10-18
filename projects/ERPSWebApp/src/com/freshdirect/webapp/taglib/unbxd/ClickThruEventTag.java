package com.freshdirect.webapp.taglib.unbxd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.service.EventLoggerService;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;
import com.freshdirect.webapp.util.RequestUtil;

public class ClickThruEventTag extends SimpleTagSupport {
    
    private static final Logger LOGGER = LoggerFactory.getInstance(ClickThruEventTag.class);

    private ProductModel product = null;
    
    public void setProduct(ProductModel product) {
        this.product = product;
    }

    @Override
    public void doTag() {
        final PageContext pageContext = (PageContext) getJspContext();
        final HttpSession session = pageContext.getSession();
        final HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        if (user != null) {
            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, req.getCookies(), user)) {
                doSendEvent(user, req, product);
            } else {
                LOGGER.debug("UNBXD feature is off, not sending event ...");
            }
        }
    }
    
    public static void doSendEvent(FDUserI user, HttpServletRequest request, ProductModel model) {
    		final boolean cosAction = false; // TODO
    		doSendEvent(user, RequestUtil.getFullRequestUrl(request), request.getHeader(HttpHeaders.REFERER), model, cosAction);
    }
    
    public static void doSendEvent(FDUserI user, String requestedUrl, String referer, ProductModel model, boolean cosAction) {
        final Visitor visitor = Visitor.withUser(user);
        final LocationInfo loc = LocationInfo.withUrlAndReferer(requestedUrl, referer);

        AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.CLICK_THRU, visitor, loc, null, model, null, cosAction);

        // log event
        EventLoggerService.getInstance().log(event);
    }
}
