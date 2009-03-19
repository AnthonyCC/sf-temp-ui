package com.freshdirect.webapp.taglib.smartstore;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
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
        if (ImpressionLogger.isEnabled()) {
            ContentNodeModel node = r.getSessionInput().getCurrentNode();
            FDIdentity identity = user.getIdentity();
            
            HttpServletRequest httpServletRequest = (HttpServletRequest) this.pageContext.getRequest();
            String messagePrefix = "" + user.getUserId() + ',' + pageContext.getSession().getId() + ',' + (identity != null ? identity.getErpCustomerPK() : "")
                    + ',' + (identity != null ? identity.getFDCustomerPK() : "") + ',' + r.getVariant().getId() + ',' + httpServletRequest.getRequestURI()
                    + ',' + (node != null ? node.getContentKey().getId() : "") + ',';
            int rank = 1;
            for (Iterator it = r.getProducts().iterator();it.hasNext();) {
                ProductModel p = (ProductModel) it.next();

                ImpressionLogger.logEvent(messagePrefix + rank + ','+p.getContentKey().getId());
                rank++;
            }
        }
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
