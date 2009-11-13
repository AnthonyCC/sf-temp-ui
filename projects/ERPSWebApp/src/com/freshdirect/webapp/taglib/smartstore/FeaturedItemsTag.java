/**
 * 
 */
package com.freshdirect.webapp.taglib.smartstore;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
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
    private Set shoppingCart = null;
    
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
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);

        FDStoreRecommender recommender = FDStoreRecommender.getInstance();
        
        SessionInput si = new SessionInput(user);
        initFromSession(si);
        si.setCurrentNode(nodeModel);
        si.setNoShuffle(noShuffle);
        si.setMaxRecommendations(itemCount);
        
        Recommendations results = recommender.getRecommendations(EnumSiteFeature.FEATURED_ITEMS, user,
        		si, shoppingCart != null ? shoppingCart : FDStoreRecommender.getShoppingCartContentKeys(user));
		if (results.getAllProducts().size() > 0) {
	        Impression imp = Impression.get(user, request, facility);
	        results.setRequestId(imp.getRequestId());
		}
        persistToSession(results);
        return results;
    }
    
    public void setNoShuffle(boolean flag) {
        this.noShuffle = flag;
    }
    
    /**
     * 
     * @param shoppingCart Set<ContentKey> of product keys
     */
    public void setShoppingCart(Set shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

}
