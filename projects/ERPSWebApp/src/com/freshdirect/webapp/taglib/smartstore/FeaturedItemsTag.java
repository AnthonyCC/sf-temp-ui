/**
 * 
 */
package com.freshdirect.webapp.taglib.smartstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author zsombor
 *
 */
public class FeaturedItemsTag extends RecommendationsTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ContentNodeModel nodeModel;
    private boolean noShuffle = false;
    
    public void setCurrentNode(ContentNodeModel cnm) {
        this.nodeModel = cnm;
    }
    
    /* (non-Javadoc)
     * @see com.freshdirect.webapp.taglib.smartstore.RecommendationsTag#getRecommendations()
     */
    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
        if (nodeModel==null) {
            throw new RuntimeException("CurrentNode not set!");
        }
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

        Trigger trigger = new Trigger(EnumSiteFeature.FEATURED_ITEMS, itemCount);
        FDStoreRecommender recommender = FDStoreRecommender.getInstance();
        
        SessionInput si = new SessionInput(user);
        si.setCurrentNode(nodeModel);
        si.setNoShuffle(noShuffle);
        
        Recommendations results = recommender.getRecommendations(trigger, user, si, (String) pageContext.getAttribute("fi_override_variant"));

        return results;
    }
    
    public void setNoShuffle(boolean flag) {
        this.noShuffle = flag;
    }

}
