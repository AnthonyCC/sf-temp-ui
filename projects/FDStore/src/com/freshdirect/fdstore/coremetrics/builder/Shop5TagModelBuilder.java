package com.freshdirect.fdstore.coremetrics.builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class Shop5TagModelBuilder extends AbstractShopTagModelBuilder{
	private static final Logger LOGGER = LoggerFactory.getInstance(Shop5TagModelBuilder.class);
	
	public List<ShopTagModel> buildTagModels() throws SkipTagException {
		
		if (cart == null) {
			LOGGER.error("cart is null");
			throw new SkipTagException("cart is null");

		} else {
			createTagModels(collectRecentKeys());
		}
		return tagModels;
	}
	
	private Set<ContentKey> collectRecentKeys(){
		Set<ContentKey> recentKeys = new HashSet<ContentKey>();
		
		for (FDCartLineI recentCartLine : cart.getRecentOrderLines()) {

			ProductReference recentProductRef = recentCartLine.getProductRef();
			if (recentProductRef != null) {
				ContentKey recentKey = recentProductRef.getContentKey();
				recentKeys.add(recentKey);
			}
		}
		return recentKeys;
	}

	private void createTagModels(Set<ContentKey> recentKeys) throws SkipTagException{
		
		for (FDCartLineI cartLine : cart.getOrderLines()){
			ProductReference productRef = cartLine.getProductRef();
			
			if (productRef != null && recentKeys.contains(productRef.getContentKey())) {
				tagModels.add(createTagModel(cartLine, productRef));
			}
		}
	}
	
}