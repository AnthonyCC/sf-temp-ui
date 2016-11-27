package com.freshdirect.webapp.taglib.smartstore;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.YmalSource;
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
public class ProductGroupRecommenderTag extends RecommendationsTag {

    private static final long serialVersionUID = 1L;
    
    ContentNodeModel nodeModel;
    private boolean noShuffle = false;
    private Set<ContentKey> shoppingCart = null;
    String siteFeature=null;
    
    // [APPDEV-2241] remove wines from recommended items
    private boolean excludeAlcoholicContent = false;

    /**
     * YMAL Source parameter [optional]. It can be obtained from YmalContextTag
     * This is required only if recommender utilizes SmartYMAL.
     */
    private YmalSource ymalSource = null;
    
    private boolean hide;

    public void setCurrentNode(ContentNodeModel cnm) {
        this.nodeModel = cnm;
    }
    
    public void setSiteFeature(String sf) {
    	this.siteFeature=sf;
    }
    
    public void setExcludeAlcoholicContent(boolean excludeAlcoholicContent) {
		this.excludeAlcoholicContent = excludeAlcoholicContent;
	}
    
    
    public void setYmalSource(YmalSource source) {
		this.ymalSource = source;
	}
    
    public void setHide(boolean hide) {
		this.hide = hide;
	}
    
    public boolean isHide() {
		return hide;
	}

    /* (non-Javadoc)
     * @see com.freshdirect.webapp.taglib.smartstore.RecommendationsTag#getRecommendations()
     */
    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
    	if (hide)
    		return null;

        /* if (nodeModel==null) {
            throw new RuntimeException("CurrentNode not set!");
        } */
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);

        FDStoreRecommender recommender = FDStoreRecommender.getInstance();
        
        SessionInput si = new SessionInput(user);
        initFromSession(si);
        if (nodeModel != null)
        	si.setCurrentNode(nodeModel);
        si.setNoShuffle(noShuffle);
        si.setMaxRecommendations(itemCount);
        if (windowSize > 0)
        	si.setWindowSize(windowSize);
        si.setExcludeAlcoholicContent(excludeAlcoholicContent);
        if (this.ymalSource != null) {
        	si.setYmalSource(ymalSource);
        }
        
        if (shoppingCart != null) {
        	si.setCartContents(shoppingCart);
        }
        
        EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(siteFeature);
        /*if(siteFeat==null) {
        	throw new IllegalArgumentException("illegal argument: siteFeature");
        }*/
		Recommendations results = recommender.getRecommendations(siteFeat, user, si);

        collectRequestId(request, results, user);

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
    public void setShoppingCart(Set<ContentKey> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

}
