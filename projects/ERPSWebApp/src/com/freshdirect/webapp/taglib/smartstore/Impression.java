package com.freshdirect.webapp.taglib.smartstore;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SessionImpressionLog;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class Impression {

    final static Logger LOGGER    = Logger.getLogger(Impression.class);
    
    final Map           recServiceAudit;

    final String        requestId;

    int                 lastId    = 0;

    int                 tabId     = 0;

    int                 featureId = 0;

    Impression(FDUserI user, HttpServletRequest httpServletRequest) {
        this.requestId = SessionImpressionLog.getPageId();

        this.recServiceAudit = (Map) AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.get();

        FDIdentity identity = user.getIdentity();

        String uri = httpServletRequest.getRequestURI();
        String sessionId = httpServletRequest.getSession().getId();

        String erpCustomerId = identity != null ? identity.getErpCustomerPK() : "";

        String message = requestId + ","+QuickDateFormat.ISO_FORMATTER_2.format(new Date())+","+ filter(user.getUserId()) + ',' + 
            filter(sessionId) + ',' + erpCustomerId + ',' + filter(uri) + ','
                + filter(httpServletRequest.getQueryString());
        ImpressionLogger.REQUEST.logEvent(message);
    }
    
    /**
     * Acquire and initialize an Impression object from the request. This way, it's ensured that for every request
     * at most one Impression object exists.
     * 
     * @param request
     * @return
     */
    public static Impression get(FDUserI user, HttpServletRequest request) {
        Impression imp = (Impression) request.getAttribute(SessionName.IMPRESSION);
        if (imp == null) {
            imp = new Impression(user, request);
            request.setAttribute(SessionName.IMPRESSION, imp);
        }
        return imp;
    }

    public String logFeatureImpression(String parentFeatureImpId, Recommendations recommendations) {
        ContentNodeModel trigger = recommendations.getSessionInput().getCurrentNode();
        YmalSource ymalSource = recommendations.getSessionInput().getYmalSource();
        
        return logFeatureImpression(parentFeatureImpId, recommendations.getVariant().getId(), trigger, ymalSource);
    }

    public String logFeatureImpression(String parentFeatureImpId, String variantId, ContentNodeModel trigger, YmalSource ymalSource) {
        String triggerProductId = (trigger instanceof ProductModel)? trigger.getContentKey().getId() : "";
        String triggerCategoryId= (trigger instanceof CategoryModel)? trigger.getContentKey().getId() : "";
        String ymalSourceId = ymalSource != null ? ymalSource.getContentKey().getId() : "";

        return logFeatureImpression(parentFeatureImpId, variantId, triggerCategoryId, triggerProductId, ymalSourceId);
    }

    public String logFeatureImpression(String parentFeatureImpId, String variantId, String triggeringCategoryId, String triggeringProductId, String ymalId) {
        featureId++;
        String uniqId = requestId + "_f" + featureId;
        String message = uniqId + ',' + requestId + ',' + (parentFeatureImpId!=null ? parentFeatureImpId : "") + ',' + variantId + ',' + triggeringCategoryId + ',' + triggeringProductId
                + ',' + ymalId;
        ImpressionLogger.FEATURE.logEvent(message);
        return uniqId;
    }

    /**
     * Log the content key, with a supplied rank, and return an unique
     * impression ID.
     * 
     * @param rank
     * @param key
     * @return
     */
    public String logProduct(String featureImpressionId, int rank, ContentKey key) {
        lastId++;
        String impId = filter(requestId + "_p" + lastId);
        String contentId = key.getId();
        ImpressionLogger.PRODUCT.logEvent(impId + ',' + featureImpressionId + ',' + contentId + ',' + rank + ','
                + (recServiceAudit != null ? recServiceAudit.get(contentId) : ""));
        return impId;
    }

    public String logTab(String featureImpressionId, int number, String tabName) {
        tabId++;
        String impId = filter(requestId + "_t" + tabId);
        ImpressionLogger.TAB.logEvent(impId + ',' + filter(featureImpressionId) + ',' + number + ',' + filter(tabName));
        return impId;
    }

    public static void productClick(String impressionId, String trackingCode, String trackingOtherCode) {
        ImpressionLogger.PROD_CLICK.logEvent(filter(impressionId) + ',' + filter(trackingCode) + ',' + filter(trackingOtherCode));
    }

    public static void tabClick(String impressionId) {
        ImpressionLogger.TAB_CLICK.logEvent(filter(impressionId));
    }
    
    private static String filter(String value) {
        return value != null ? (value.replace('\n', ' ').replace('\r', ' ').replace(',', ' ')) : "";
    }
}
