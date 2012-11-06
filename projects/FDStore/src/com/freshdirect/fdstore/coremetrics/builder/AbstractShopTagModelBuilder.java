package com.freshdirect.fdstore.coremetrics.builder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartLineDealHelper;
import com.freshdirect.fdstore.customer.FDCartLineDealHelper.DealType;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.smartstore.service.VariantRegistry;

public abstract class AbstractShopTagModelBuilder {
	
	protected List<ShopTagModel> tagModels = new ArrayList<ShopTagModel>();
	protected FDCartModel cart;
	protected boolean isStandingOrder = false;
	
	public abstract List<ShopTagModel> buildTagModels() throws SkipTagException;
	
	protected ShopTagModel createTagModel(FDCartLineI cartLine, ProductReference productRef) throws SkipTagException {
		
		ShopTagModel tagModel = new ShopTagModel();
		ProductModel product = productRef.lookupProductModel();
		
		double quantity = cartLine.getQuantity();
		double price = cartLine.getPrice();
		double unitPrice = price / quantity;
		String variantId = cartLine.getVariantId();
		
		tagModel.setProductId(productRef.getProductId()); 
		tagModel.setProductName(product.getFullName()); 
		tagModel.setQuantity(Double.toString(quantity)); 
		tagModel.setUnitPrice(new DecimalFormat("#.##").format((unitPrice)));
		tagModel.setOrderSubtotal(Double.toString(price)); 
		
				
		if (isStandingOrder) {
			tagModel.setCategoryId(PageViewTagModelBuilder.CustomCategory.SO_TEMPLATE.toString());
		} else 	if (cartLine.isAddedFromSearch()){
			tagModel.setCategoryId(PageViewTagModelBuilder.CustomCategory.SEARCH.toString());
			
		} else if (variantId == null){
			
			EnumEventSource source = cartLine.getSource();
			if (source == null || EnumEventSource.BROWSE.equals(source)){
				tagModel.setCategoryId(productRef.getCategoryId());

			} else {
				tagModel.setCategoryId(source.getName());
			}
		
		} else {
			tagModel.setCategoryId(VariantRegistry.getInstance().getService(variantId).getSiteFeature().getName());
		}
		
		//set attributes
		Map<Integer, String> attributesMap = tagModel.getAttributesMaps();
		
		attributesMap.put(1, cartLine.getSkuCode());
		attributesMap.put(2, variantId);
		
		double promotion = cartLine.getPromotionValue();
		if (promotion != 0) {
			attributesMap.put(3, Double.toString(promotion));
		}

		FDCartLineDealHelper dealHelper = new FDCartLineDealHelper(cartLine);
		if (dealHelper.init()){
						
			DealType usedDealType = dealHelper.getUsedDealType();
			if (usedDealType != DealType.NONE){	
				attributesMap.put(4, usedDealType.toString());
			}

			int usedDealPercentage = dealHelper.getUsedDealPercentage();
			if (usedDealPercentage > 0){
				attributesMap.put(5, Integer.toString(usedDealPercentage));
			}
	
			DealType bestDealType = dealHelper.getBestDealType();
			if (bestDealType != DealType.NONE){	
				attributesMap.put(6, bestDealType.toString());
			}
		} else{
			throw new SkipTagException("dealHelper.init() failed"); 
		}

		attributesMap.put(7, Double.toString(cartLine.getTaxValue()));
		
		return tagModel;
	}
	
	public void setCart(FDCartModel cart) {
		this.cart = cart;
	}

	public List<ShopTagModel> getTagModels() {
		return tagModels;
	}

	public void setTagModels(List<ShopTagModel> tagModels) {
		this.tagModels = tagModels;
	}

	public boolean isStandingOrder() {
		return isStandingOrder;
	}

	public void setStandingOrder(boolean isStandingOrder) {
		this.isStandingOrder = isStandingOrder;
	}
}