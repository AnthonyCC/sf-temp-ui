package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper;

public class DCPDLineItemStrategy implements LineItemStrategyI {
	protected Set<ContentKey> contentKeys = new HashSet<ContentKey>();
	private Set<String> skus = new HashSet<String>();
	private Set<String> brands = new HashSet<String>();
	private Boolean excludeSkus = Boolean.FALSE;
	private Boolean excludeBrands = Boolean.FALSE;
	private Boolean loopEnabled = Boolean.FALSE;
	private boolean recCategory = false;
	
	public int getPrecedence() {
		return 200;
	}

	public DCPDLineItemStrategy(){
		
	}
	
	public void addContent(String type, String id){
		ContentKey refKey = ContentNodeModelUtil.getAliasCategoryRef(type, id);
		if(FDStoreProperties.isDCPDAliasHandlingEnabled() && refKey != null){
			/*
			 * refKey is not null when content id is pointing to a ALIAS category.
			 * So instead of adding the alias category id add the referencing category
			 * id which is refKey.
			 */
			contentKeys.add(refKey);
		} else {
			//Regular category or department or recipe id or virtual group.
			contentKeys.add(ContentNodeModelUtil.getContentKey(type, id));	
		}
	}
	
	public void addSku(String skuCode){
			skus.add(skuCode);
	}
	
	public void addBrand(String brandId){
			brands.add(brandId);
	}
	
	public int evaluate(FDCartLineI lineItem, String promotionCode, PromotionContextI context) {
		boolean eligible = contentKeys.isEmpty();
		String recipeSourceId = lineItem.getRecipeSourceId();
		if(recipeSourceId != null && recipeSourceId.length() > 0){
			////Check if the line item is eligible for a recipe discount.
			eligible = OrderPromotionHelper.isRecipeEligible(recipeSourceId, contentKeys);
		}
		if(!eligible){
			ProductModel model = lineItem.getProductRef().lookupProductModel();
			String productId = model.getContentKey().getId();
			DCPDPromoProductCache dcpdCache = context.getUser().getDCPDPromoProductCache();
			//Check if the line item product is already evaluated.
			if(dcpdCache.isEvaluated(productId, promotionCode)){
				eligible = dcpdCache.isEligible(productId, promotionCode);
			}else{
				//Check if the line item is eligible for a category or department discount.
				if(this.isRecCategory() && this.isLoopEnabled())
					eligible = OrderPromotionHelper.evaluateProductForDCPDPromoWithRecCategory(model, contentKeys, true);				
				else
					eligible = OrderPromotionHelper.evaluateProductForDCPDPromo(model, contentKeys);
				//Set Eligiblity info to user session.
				dcpdCache.setPromoProductInfo(productId, promotionCode, eligible);
			}
		}		
		
		//Additionally check for exclude SKUS and exclude Brands.
		if(eligible && excludeSkus){
			eligible = !skus.contains(lineItem.getSkuCode());
		}
		
		if(eligible && excludeBrands){
			eligible = !lineItem.hasBrandName(brands);
		}
		
		//Additionally check for include SKUS and include Brands.
		if(eligible && !excludeSkus && !isRecCategory()){
			eligible = skus.contains(lineItem.getSkuCode());
		}
		
		if(eligible && !excludeBrands && !isRecCategory()){
			eligible = lineItem.hasBrandName(brands);
		}
		

		if(eligible) return ALLOW;
		context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_CART_LINES.getErrorCode());
		return DENY;
	}

	public boolean isExcludeSkus() {
		return excludeSkus;
	}

	public void setExcludeSkus(boolean excludeSkus) {
		this.excludeSkus = excludeSkus;
	}

	public boolean isExcludeBrands() {
		return excludeBrands;
	}

	public void setExcludeBrands(boolean excludeBrands) {
		this.excludeBrands = excludeBrands;
	}

	public Set<String> getSkus() {
		return skus;
	}

	public Set<String> getBrands() {
		return brands;
	}
	
	public void setLoopEnabled(boolean loopEnabled) {
		this.loopEnabled = loopEnabled;
	}
	
	public boolean isLoopEnabled() {
		return loopEnabled;
	}
	
	public boolean isRecCategory() {
		return recCategory;
	}
	
	public void setRecCategory(boolean recCategory) {
		this.recCategory = recCategory;
	}
	@Override
	public String toString() {
		return "DCPDLineItemStrategy [brands=" + brands + ", contentKeys="
				+ contentKeys + ", excludeBrands=" + excludeBrands
				+ ", excludeSkus=" + excludeSkus + ", loopEnebled="
				+ loopEnabled + ", skus=" + skus + "]";
	}
	
}



