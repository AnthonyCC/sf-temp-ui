package com.freshdirect.fdstore.coremetrics.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.coremetrics.tagmodel.Shop5TagModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.util.log.LoggerFactory;



public class Shop5TagModelBuilder {
	private static final Logger LOGGER = LoggerFactory.getInstance(Shop5TagModelBuilder.class);
	
	private FDCartModel cart;
	private String source;
	private List<Shop5TagModel> tagModels = new ArrayList<Shop5TagModel>();
	
	public List<Shop5TagModel> buildTagModels() throws SkipTagException {
		
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

	private void createTagModels(Set<ContentKey> recentKeys){
		
		for (FDCartLineI cartLine : cart.getOrderLines()){
			ProductReference productRef = cartLine.getProductRef();
			
			if (productRef != null && recentKeys.contains(productRef.getContentKey())) {
				tagModels.add(createTagModel(cartLine, productRef));
			}
		}
	}
	
	private Shop5TagModel createTagModel(FDCartLineI cartLine, ProductReference productRef) {
		
		Shop5TagModel tagModel = new Shop5TagModel();
		
		ProductModel product = productRef.lookupProductModel();
		PriceCalculator priceCalculator = product.getPriceCalculator();
		
		double quantity = cartLine.getQuantity();

		tagModel.setProductId(productRef.getProductId()); 
		tagModel.setProductName(product.getFullName()); 
		tagModel.setQuantity(Double.toString(quantity)); 
		tagModel.setUnitPrice(Double.toString((cartLine.getPrice() / quantity))); 
		tagModel.setCategoryId(getCategory(productRef.getCategoryId())); 
		
		Map<Integer, String> attributesMap = tagModel.getAttributesMaps();
		
		attributesMap.put(1, cartLine.getSkuCode());
		attributesMap.put(2, cartLine.getVariantId());
		attributesMap.put(3, Double.toString(cartLine.getPromotionValue()));
//		attributesMap.put(4, ); //Chosen deal type - how to determine?
//		attributesMap.put(5, Double.toString(priceCalculator.getDealPercentage())); //does not seem to care about group price, Chosen deal for group price: FDProductSelectionI.getGroupQuantity()
//		attributesMap.put(6, priceCalculator.getHighestDealPercentage()); //Best available deal how to determine?
		attributesMap.put(7, Double.toString(cartLine.getTaxValue()));
		
		return tagModel;
	}
	
	private String getCategory(String prodCategory){
		if (source == null){
			return prodCategory;
		} else {
			return TagModelUtil.dropExtension(source);
		}
	}

	public void setCart(FDCartModel cart) {
		this.cart = cart;
	}

	public void setSource(String source) {
		this.source = source;
	}
}