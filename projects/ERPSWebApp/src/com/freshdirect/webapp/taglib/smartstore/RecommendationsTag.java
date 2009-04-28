package com.freshdirect.webapp.taglib.smartstore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDEventUtil;

public abstract class RecommendationsTag extends AbstractGetterTag {
    // maximum number of recommended items
    protected int     itemCount     = 5;

    // skip checking user eligibility
    /**
     * @deprecated
     * */
    protected boolean skipCheck     = false;

    // if this set true tag should not recommend new. Instead, return the
    // previous if any
    protected boolean errorOccurred = false;
    
    protected String parentFeatureImpressionId;
    
    
    protected abstract Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException;

    public void setItemCount(int cnt) {
        this.itemCount = cnt;
    }
    
    public void setParentFeatureImpressionId(String id) {
        this.parentFeatureImpressionId = id;
    }

    /**
     * @deprecated
     * */
    public void setSkipCheck(boolean flag) {
        this.skipCheck = flag;
    }

    public void setErrorOccurred(boolean flag) {
        this.errorOccurred = flag;
    }
    
    protected void persistToSession(Recommendations r) {
        Map previousRecommendations = r.getSessionInput().getPreviousRecommendations();
        if (previousRecommendations!=null) {
            pageContext.getSession().setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, previousRecommendations);
        }
    }
    
    protected void initFromSession(SessionInput input) {
        HttpSession session = pageContext.getSession();
        input.setPreviousRecommendations((Map) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
    }

    /**
     * Generate event for impression logging
     * 
     * @param r
     *            Recommendation to log
     * @author segabor
     */
    protected void logImpressions(Recommendations r) {
        FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
    	if (r.getProducts().size() > 0) {
    		if (user != null && user instanceof FDSessionUser) {
    			FDSessionUser sessionUser = (FDSessionUser) user;
    			sessionUser.logImpression(r.getVariant().getId(), r.getProducts().size());
    		}
    	}
        for (Iterator it = r.getProducts().iterator();it.hasNext();) {
            ProductModel p = (ProductModel) it.next();
            FDEventUtil.logRecommendationImpression(r.getVariant().getId(), p.getContentKey());
        }
        if (ImpressionLogger.isGlobalEnabled()) {
            
            Impression imp = Impression.get(user, (HttpServletRequest) pageContext.getRequest());
            
            int rank = 1;
            Map map = new HashMap();
            String featureImpId = imp.logFeatureImpression(parentFeatureImpressionId, r);
            for (Iterator it = r.getProducts().iterator();it.hasNext();) {
                ProductModel p = (ProductModel) it.next();

                String imp_id = imp.logProduct(featureImpId, rank, p.getContentKey());
                map.put(p.getContentKey(), imp_id);
                rank++;
            }
            r.setImpressionIds(map);
        }
        AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.set(null);
    }

    /**
     * Heart of tag can be found here.
     * 
     * @return List of <{@link Recommendation}>
     */
    protected Object getResult() throws Exception {
        Recommendations results = getRecommendations();

        if (results != null && results.getProducts().size() == 0) {
            results = null;
        } else {
            // do impression logging
            logImpressions(results);
        }
        return results;
    }

    
    public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
