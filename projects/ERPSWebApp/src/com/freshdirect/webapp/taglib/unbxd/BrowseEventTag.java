package com.freshdirect.webapp.taglib.unbxd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.webapp.cos.util.CosFeatureUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.service.EventLoggerService;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;
import com.freshdirect.webapp.util.RequestUtil;

public class BrowseEventTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(BrowseEventTag.class);

    @Override
    public void doTag() throws JspException {
        final PageContext pageContext = (PageContext) getJspContext();
        final HttpSession session = pageContext.getSession();
        final HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        if (user != null) {
            final String contentId = req.getParameter("id");
            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, req.getCookies(), user)) {
                // check input
                if (contentId == null) {
                    throw new JspException("Parameter error: Null content ID was given");
                }
                doSendEvent(contentId, user, req);
            } else {
                LOGGER.debug("UNBXD feature is off, not sending event ...");
            }
        }
    }

    public static void doSendEvent(String containerId, FDUserI user, HttpServletRequest request) {
        final ContentNodeModel model = ContentFactory.getInstance().getContentNode(containerId);
        final boolean cosAction = CosFeatureUtil.isUnbxdCosAction(user, request.getCookies());
        
        if (model instanceof ProductContainer) {
            final Visitor visitor = Visitor.withUser(user);
            final LocationInfo loc = LocationInfo.withUrlAndReferer(RequestUtil.getFullRequestUrl(request), request.getHeader(HttpHeaders.REFERER));

            AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.BROWSE, visitor, loc, null, model, null, cosAction);

            LOGGER.debug("Sending browse event for content ID " + containerId);

            // log event
            EventLoggerService.getInstance().log(event);

        } else {
            LOGGER.debug("Discard event for content ID " + containerId);
        }
    }
}
