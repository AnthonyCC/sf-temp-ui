package com.freshdirect.webapp.taglib.smartstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.DYFUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * SmartStore DYF Recommendations Tag
 * 
 * @author segabor
 * 
 */
public class DYFRecommendationsTag extends RecommendationsTag implements SessionName {

    private static final long serialVersionUID = -3790027913916829707L;

    private static Category   LOGGER           = LoggerFactory.getInstance(DYFRecommendationsTag.class);

    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute("fd.user");

//        if (skipCheck) {
            //return null;
//        }

        Recommendations results = null;

        if (errorOccurred) {
        	ServletRequest request = pageContext.getRequest();
    		// reconstruct recommendations
    		String variantId = request.getParameter("variant");
    		String siteFeatureName = request.getParameter("siteFeature");

    		// results = new Recommendations(new Variant(variantId, EnumSiteFeature.getEnum(siteFeatureName), null), null);
            // results.deserializeContentNodes(request.getParameter("rec_product_ids"));
    		if (variantId != null && siteFeatureName != null) {
    			final EnumSiteFeature sf = EnumSiteFeature.getEnum(siteFeatureName);
    			Map svcMap = SmartStoreServiceConfiguration.getInstance().getServices(sf);
    			RecommendationService svc = (RecommendationService) svcMap.get(variantId);
    			results = new Recommendations(svc.getVariant(),	request.getParameter("rec_product_ids"));
    		}
        }

        // get recommendations by recommender
        if (results == null) {
        	Trigger trigger;
        	EnumSiteFeature sf = DYFUtil.isCustomerEligible(user) ?
        			EnumSiteFeature.DYF :
        			EnumSiteFeature.FAVORITES;
        	trigger = new Trigger(sf, itemCount);
            FDStoreRecommender recommender = FDStoreRecommender.getInstance();

            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            String overriddenVariantID = request.getParameter("SmartStore.VariantID");
            if (overriddenVariantID != null)
                session.setAttribute("SmartStore.VariantID", overriddenVariantID);

            results = recommender.getRecommendations(trigger, session);
        }
        return results;
    }

    public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
