package com.freshdirect.webapp.taglib.smartstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDEventUtil;

public abstract class RecommendationsTag extends AbstractGetterTag<Recommendations> {
	private static final Logger LOGGER = LoggerFactory.getInstance( RecommendationsTag.class );

	private static final long serialVersionUID = -7592561069328056899L;

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
    
    protected String parentVariantId;
    
    protected String facility;
    
    protected abstract Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException;

    public void setItemCount(int cnt) {
        this.itemCount = cnt;
    }
    
    public void setParentFeatureImpressionId(String id) {
        this.parentFeatureImpressionId = id;
    }

    public void setParentVariantId(String parentVariantId) {
        this.parentVariantId = parentVariantId;
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
    
    public void setFacility(String facility) {
		this.facility = facility;
	}
    
    protected void persistToSession(Recommendations r) {
        Map<String, List<ContentKey>> previousRecommendations = r.getPreviousRecommendations();
        if (previousRecommendations!=null) {
            pageContext.getSession().setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, previousRecommendations);
        }
    }
    
    protected void initFromSession(SessionInput input) {
        HttpSession session = pageContext.getSession();
        input.setPreviousRecommendations((Map<String, List<ContentKey>>) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
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

    	for (ProductModel p : r.getProducts()) {
            FDEventUtil.logRecommendationImpression(r.getVariant().getId(), p.getContentKey());
        }


    	if (ImpressionLogger.isGlobalEnabled()) {
            Impression imp = Impression.get(user, (HttpServletRequest) pageContext.getRequest(), facility);
            
            int rank = 1;
            Map<ContentKey,String> map = new HashMap<ContentKey,String>();
            String featureImpId = imp.logFeatureImpression(parentFeatureImpressionId, parentVariantId, r);

            for (ProductModel p : r.getProducts()) {
                String imp_id = imp.logProduct(featureImpId, rank, p.getContentKey(),
                		r.getRecommenderIdForProduct(p.getContentName()),
                		r.getRecommenderStrategyIdForProduct(p.getContentName()));
                map.put(p.getContentKey(), imp_id);
                rank++;
            }
            r.addImpressionIds(map);
        }
    }

    /**
     * Heart of tag can be found here.
     * 
     * @return List of <{@link Recommendation}>
     */
    protected Recommendations getResult() throws Exception {
        Recommendations results = getRecommendations();
        
        final List<ProductModel> products = results.getProducts();
		if (results != null && products.size() == 0) {
        	LOGGER.debug("Return empty result");
        	results = null;
        }

        if (results != null && shouldLogImpressions()) {
            // do impression logging
            logImpressions(results);
            
            // DEBUG --> print out recommended products
            //
            StringBuilder buf = new StringBuilder();
            buf.append("Result: [");
            int k = products.size();
            for (ProductModel obj : products) {
            	buf.append(obj.getContentName());
            	if (--k > 0)
            		buf.append(", ");
            }
            buf.append("]");
            LOGGER.debug(buf.toString());
        }
        return results;
    }

    protected boolean shouldLogImpressions() {
    	return true;
    }

    protected void collectRequestId(HttpServletRequest request, Recommendations recommendations, FDUserI user) {
        if (recommendations.getAllProducts().size() > 0) {
            Impression imp = Impression.get(user, request, facility);
            recommendations.setRequestId(imp.getRequestId());
        }
    }



    public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
