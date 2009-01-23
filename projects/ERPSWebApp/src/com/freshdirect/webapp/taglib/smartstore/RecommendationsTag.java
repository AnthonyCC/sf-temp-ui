package com.freshdirect.webapp.taglib.smartstore;

import java.util.Iterator;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.FDEventUtil;

public abstract class RecommendationsTag extends AbstractGetterTag {
    // maximum number of recommended items
    protected int     itemCount     = 5;

    // skip checking user eligibility
    protected boolean skipCheck     = false;

    // if this set true tag should not recommend new. Instead, return the
    // previous if any
    protected boolean errorOccurred = false;
    
    
    protected abstract Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException;

    public void setItemCount(int cnt) {
        this.itemCount = cnt;
    }

    public void setSkipCheck(boolean flag) {
        this.skipCheck = flag;
    }

    public void setErrorOccurred(boolean flag) {
        this.errorOccurred = flag;
    }

    /**
     * Generate event for impression logging
     * 
     * @param r
     *            Recommendation to log
     * @author segabor
     */
    protected void logImpressions(Recommendations r) {
        Iterator it = r.getContentNodes().iterator();
        while (it.hasNext()) {
            ProductModel p = (ProductModel) it.next();
            FDEventUtil.logRecommendationImpression(r.getVariant().getId(), p.getContentKey());
        }
    }

    /**
     * Heart of tag can be found here.
     * 
     * @return List of <{@link Recommendation}>
     */
    protected Object getResult() throws Exception {
        Recommendations results = getRecommendations();

        if (results != null && results.getContentNodes().size() == 0) {
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
