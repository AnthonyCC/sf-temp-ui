/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FavoriteList;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;

/**
 * TODO : think about, that the current RecommendationService contract states, that the recommend method should return a list of ContentKey-s. 
 *  
 * 
 * @author csongor
 *
 */
public class FavoritesRecommendationService extends AbstractRecommendationService {
	private String favoriteListId;
	
	/**
     * @param variant
     */
    public FavoritesRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems, String favoriteListId) {
        super(variant, sampler, catAggr, includeCartItems);
        this.favoriteListId = favoriteListId;
    }

    /**
     * 
     * @param max
     * @param input
     * @return a List<{@link ContentNodeModel}> of recommendations
     *         
     */
    public List recommendNodes(SessionInput input) {
        List favoriteNodes = Collections.EMPTY_LIST;
        
    	ContentFactory cf = ContentFactory.getInstance();
    	if (favoriteListId == null)
    		return favoriteNodes;
    	
    	FavoriteList fl = (FavoriteList) cf.getContentNodeByKey(new ContentKey(FDContentTypes.FAVORITE_LIST, favoriteListId));
    	if (fl != null) {
    	    favoriteNodes = fl.getFavoriteItems();
    	    
    	    List keys = new ArrayList(favoriteNodes.size());
    	    for (int i=0;i<favoriteNodes.size();i++){ 
    	        ContentNodeModel contentNodeModel = (ContentNodeModel)favoriteNodes.get(i);
                keys.add(new RankedContent.Single((favoriteNodes.size() - i) * 5.0, contentNodeModel));
    	    }
    	    List sample = RankedContent.getContentNodeModel(getSampler(input).sample(keys, input.getCartContents(), keys.size()));
    	    SmartStoreUtil.clearConfiguredProductCache();
    	    favoriteNodes = SmartStoreUtil.addConfiguredProductToCache(sample);
    	}

        return favoriteNodes;
    }
}
