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
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SessionImpressionLog;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class Impression {

    final static Logger LOGGER    = Logger.getLogger(Impression.class);
    
    final Map           recServiceAudit;
    
    final Map           recStratServiceAudit;

    final String        requestId;

    int                 lastId    = 0;

    int                 tabId     = 0;

    int                 featureId = 0;
    

    Impression(FDUserI user, HttpServletRequest httpServletRequest) {
        this.requestId = SessionImpressionLog.getPageId();

        this.recServiceAudit = (Map) AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.get();
        this.recStratServiceAudit = (Map) AbstractRecommendationService.RECOMMENDER_STRATEGY_SERVICE_AUDIT.get();

        FDIdentity identity = user.getIdentity();

        String uri = httpServletRequest.getRequestURI();
        String sessionId = httpServletRequest.getSession().getId();

        String erpCustomerId = identity != null ? identity.getErpCustomerPK() : "";
        
        String queryString = httpServletRequest.getQueryString();
        
        init(user.getUserId(), uri, sessionId, erpCustomerId, queryString);
    }

    Impression(String requestId, String userId, String uri, String sessionId, String erpCustomerId, String queryString, Map serviceAudit, Map stratServiceAudit) {
        this.requestId = requestId;
        this.recServiceAudit = serviceAudit;
        this.recStratServiceAudit = stratServiceAudit;
        init(userId, uri, sessionId, erpCustomerId, queryString);
    }
    
    private void init(String userId, String uri, String sessionId, String erpCustomerId, String queryString) {
        String message = requestId + "," + createTimestamp() + "," + filter(userId) + ',' + filter(sessionId) + ',' + erpCustomerId + ',' + filter(uri) + ','
                + filter(queryString);
        ImpressionLogger.REQUEST.logEvent(message);
    }
    
    static String createTimestamp() {
        return QuickDateFormat.ISO_FORMATTER_2.format(new Date());
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

    public String logFeatureImpression(String parentFeatureImpId, String parentVariantId, Recommendations recommendations) {
        CategoryModel category = recommendations.getSessionInput().getCategory();
        ContentNodeModel trigger = recommendations.getSessionInput().getCurrentNode();
        YmalSource ymalSource = recommendations.getSessionInput().getYmalSource();
        
        return logFeatureImpression(parentFeatureImpId, parentVariantId, recommendations.getVariant(), category, trigger, ymalSource);
    }

    public String logFeatureImpression(String parentFeatureImpId, String parentVariantId, Variant variant, CategoryModel category, ContentNodeModel trigger, YmalSource ymalSource) {
        RecommendationServiceType type = variant.getServiceConfig().getType();
		if (!type.equals(RecommendationServiceType.CLASSIC_YMAL) &&
        		!type.equals(RecommendationServiceType.SMART_YMAL))
        	ymalSource = null;

		String generator = variant.getServiceConfig().get(SmartStoreServiceConfiguration.CKEY_GENERATOR);
        if (!type.equals(RecommendationServiceType.CLASSIC_YMAL) &&
        		!type.equals(RecommendationServiceType.SMART_YMAL) &&
        		!(type.equals(RecommendationServiceType.SCRIPTED) && generator != null
        				&& generator.indexOf("currentProduct") >= 0)) {
        	trigger = null;
        	if (!type.equals(RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY)
        			&& !type.equals(RecommendationServiceType.CANDIDATE_LIST)
        			&& !type.equals(RecommendationServiceType.FEATURED_ITEMS)
        			&& !type.equals(RecommendationServiceType.MANUAL_OVERRIDE)
        			&& !type.equals(RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS))
        		category = null;
        }
        
        String triggerProductId = (trigger instanceof ProductModel)? trigger.getContentKey().getId() : "";
        String triggerCategoryId = (category != null) ? category.getContentKey().getId() : "";
        String ymalSetId = ymalSource != null  
        		? (ymalSource.getActiveYmalSet() != null
        				? ymalSource.getActiveYmalSet().getContentKey().getId()
        				: "")
        		: "";

        return logFeatureImpression(parentFeatureImpId, parentVariantId, variant.getId(), triggerCategoryId, triggerProductId, ymalSetId);
    }

    public String logFeatureImpression(String parentFeatureImpId, String parentVariantId, String variantId, String triggeringCategoryId, String triggeringProductId, String ymalId) {
        featureId++;
        String uniqId = requestId + "_f" + featureId;
        String message = uniqId + ',' + requestId + ',' + (parentFeatureImpId!=null ? parentFeatureImpId : "") + ',' + variantId + ',' + triggeringCategoryId + ',' + triggeringProductId
                + ',' + ymalId + ',' + (parentVariantId != null ? parentVariantId : "");
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
                + getRecommenderId(contentId)+',' + getRecommenderStratId(contentId));
        return impId;
    }

    private Object getRecommenderId(String contentId) {
        Object res = (recServiceAudit != null ? recServiceAudit.get(contentId) : null);
        return res!=null ? res : "";
    }

    private Object getRecommenderStratId(String contentId) {
        Object res = (recStratServiceAudit  != null ? recStratServiceAudit.get(contentId) : null);
        return res!=null ? res : "";
    }
    
    public String logTab(String featureImpressionId, int number, String tabName) {
        tabId++;
        String impId = filter(requestId + "_t" + tabId);
        ImpressionLogger.TAB.logEvent(impId + ',' + filter(featureImpressionId) + ',' + number + ',' + filter(tabName));
        return impId;
    }

    public static void productClick(String impressionId, String trackingCode, String trackingOtherCode) {
        ImpressionLogger.PROD_CLICK.logEvent(SessionImpressionLog.getPageId() + ','+ createTimestamp() + ',' + filter(impressionId) + ',' + filter(trackingCode) + ',' + filter(trackingOtherCode));
    }

    public static void tabClick(String impressionId) {
        ImpressionLogger.TAB_CLICK.logEvent(SessionImpressionLog.getPageId() + ','+ createTimestamp() + ',' + filter(impressionId));
    }
    
    private static String filter(String value) {
        return value != null ? (value.replace('\n', ' ').replace('\r', ' ').replace(',', ' ')) : "";
    }
}
