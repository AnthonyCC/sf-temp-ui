package com.freshdirect.webapp.taglib.smartstore;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.DYFUtil;

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
        	if(user.isEligibleForSavings(EnumSiteFeature.SOYF)) {
            	results = extractRecommendations(session, EnumSiteFeature.SOYF);
        	}
        	else if(user.isEligibleForSavings(EnumSiteFeature.SAVE_ON_FAVORITES)) {
            	results = extractRecommendations(session, EnumSiteFeature.SAVE_ON_FAVORITES);
        	}
        }
        
        if (results == null || results.getProducts().size() == 0) {
        	if (DYFUtil.isCustomerEligible(user)) {
        		results = extractRecommendations(session, EnumSiteFeature.DYF);
        		if (results.getProducts().size() == 0) {
        			results = extractRecommendations(session, EnumSiteFeature.FAVORITES);
        		}
        	} else {
        		results = extractRecommendations(session, EnumSiteFeature.FAVORITES);
        	}
        }
        return results;
    }

	private Recommendations extractRecommendations(HttpSession session,
			EnumSiteFeature sf) throws FDResourceException {
		Recommendations results;
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		String overriddenVariantID = request.getParameter("SmartStore.VariantID");
		if (overriddenVariantID != null)
		    session.setAttribute("SmartStore.VariantID", overriddenVariantID);

                FDUserI user = (FDUserI) session.getAttribute("fd.user");

                SessionInput input = new SessionInput(user);
		initFromSession(input);
		input.setMaxRecommendations(itemCount);
		results = recommender.getRecommendations(sf, user, input, overriddenVariantID);
		persistToSession(results);
		return results;
	}

    public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
