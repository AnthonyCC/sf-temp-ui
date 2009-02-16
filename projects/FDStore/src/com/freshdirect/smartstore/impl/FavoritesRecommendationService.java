/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FavoriteList;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.RankedContent;

/**
 * TODO : think about, that the current RecommendationService contract states, that the recommend method should return a list of ContentKey-s. 
 *  
 * 
 * @author csongor
 *
 */
public class FavoritesRecommendationService extends AbstractRecommendationService {
	
	public static final String CKEY_FAVORITE_LIST_ID = "favorite_list_id";

	public static final String FAVORITES_NODE_NAME = "fd_favorites";
	
	public static ThreadLocal CFG_PRODS = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	}; 

    /**
     * @param variant
     */
    public FavoritesRecommendationService(Variant variant) {
        super(variant);
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
    	String listId = getListId();
    	FavoriteList fl = (FavoriteList) cf.getContentNodeByKey(new ContentKey(FDContentTypes.FAVORITE_LIST, listId));
    	if (fl != null) {
    	    favoriteNodes = fl.getFavoriteItems();
    	    
    	    List keys = new ArrayList(favoriteNodes.size());
    	    for (int i=0;i<favoriteNodes.size();i++){ 
    	        ContentNodeModel contentNodeModel = (ContentNodeModel)favoriteNodes.get(i);
                keys.add(new RankedContent.Single((favoriteNodes.size() - i) * 5.0, contentNodeModel));
    	    }
    	    List sample = RankedContent.getContentNodeModel(getSampler(input).sample(keys, input.getCartContents(), keys.size()));
    	    favoriteNodes = new ArrayList(sample.size());    	    
    	    Map cfgProds = (Map) CFG_PRODS.get();
    	    cfgProds.clear();
    	    for (int i=0;i<sample.size();i++) {
    	        final ContentNodeModel cn = (ContentNodeModel) sample.get(i);
    	        if (cn instanceof ConfiguredProduct) {
                    final ConfiguredProduct cfgProd = (ConfiguredProduct) cn;
                    favoriteNodes.add(cfgProd.getProduct());
                    cfgProds.put(cfgProd.getProduct().getContentKey().getId(), cfgProd);
                } else {
                    favoriteNodes.add(cn);
                }
    	    }    	
    	}

        return favoriteNodes;
    }


	protected String getListId() {
		String listId = this.getVariant().getServiceConfig().get(CKEY_FAVORITE_LIST_ID);
    	if (listId == null) {
    		listId = FAVORITES_NODE_NAME;
    	}
		return listId;
	}

	protected Map appendConfiguration(Map configMap) {
		// append FD List Id
		configMap.put(CKEY_FAVORITE_LIST_ID, getListId());
		
		return super.appendConfiguration(configMap);
	}

}
