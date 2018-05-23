package com.freshdirect.fdstore.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;
import com.freshdirect.storeapi.content.ProductModel;

public class DCPDLineItemStrategy implements LineItemStrategyI {
	protected Set<ContentKey> rawContentKeys = new HashSet<ContentKey>();
	private Set<String> skus = new HashSet<String>();
	private Set<String> brands = new HashSet<String>();
	private Boolean excludeSkus = Boolean.FALSE;
	private Boolean excludeBrands = Boolean.FALSE;
	private Boolean loopEnabled = Boolean.FALSE;
	private boolean recCategory = false;
	private Set<String> departments = new HashSet<String>();
	private Set<String> categories = new HashSet<String>();
	private Set<String> recipes = new HashSet<String>();
	
	private Set<ContentKey> __contentKeys = null;
	
	@Override
	public int getPrecedence() {
		return 200;
	}

	public DCPDLineItemStrategy(){
		
	}
	
	public void addContent(String type, String id){
		rawContentKeys.add(ContentNodeModelUtil.getContentKey(type, id));
		if(EnumDCPDContentType.DEPARTMENT.getName().equalsIgnoreCase(type)){
			addDepartment(id);
		} else if(EnumDCPDContentType.CATEGORY.getName().equalsIgnoreCase(type)){
			addCategory(id);
		} else if(EnumDCPDContentType.RECIPE.getName().equalsIgnoreCase(type)){
			addRecipe(id);
		}
	}

	/**
	 * Returns set of content keys after resolving category aliases.
	 * 
	 * @return
	 */
	public Set<ContentKey> getContentKeys() {
		if (__contentKeys == null) {
			__contentKeys = new HashSet<ContentKey>(rawContentKeys.size());
			
			final boolean aliasEnabled = FDStoreProperties.isDCPDAliasHandlingEnabled();
			
			for (final ContentKey _key : rawContentKeys) {
				ContentKey refKey = ContentNodeModelUtil.getAliasCategoryRef(_key.getType().toString(), _key.getId());
				if(aliasEnabled && refKey != null){
					/*
					 * refKey is not null when content id is pointing to a ALIAS category.
					 * So instead of adding the alias category id add the referencing category
					 * id which is refKey.
					 */
					__contentKeys.add(refKey);
				} else {
					//Regular category or department or recipe id or virtual group.
					__contentKeys.add(_key);	
				}
			}
			
		}
		
		return __contentKeys;
	}
	
	
	public void addSku(String skuCode){
			skus.add(skuCode);
	}
	
	public void addBrand(String brandId){
			brands.add(brandId);
	}
	
	public void addDepartment(String departmentId){
		departments.add(departmentId);
	}

	public void addCategory(String categoryId){
		categories.add(categoryId);
	}
	
	public void addRecipe(String recipeId){
		recipes.add(recipeId);
	}
	
	@Override
	public int evaluate(FDCartLineI lineItem, String promotionCode, PromotionContextI context) {
		Set<ContentKey> contentKeys = getContentKeys();
		
		boolean eligible = contentKeys.isEmpty();
		String recipeSourceId = lineItem.getRecipeSourceId();
		if(recipeSourceId != null && recipeSourceId.length() > 0){
			////Check if the line item is eligible for a recipe discount.
			eligible = OrderPromotionHelper.isRecipeEligible(recipeSourceId, contentKeys);
		}
		if(!eligible){
			ProductModel model = lineItem.getProductRef().lookupProductModel();
			String productId = null !=model ?model.getContentKey().getId():"";
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
		if(eligible && skus.size() != 0 && !excludeSkus){
			eligible = skus.contains(lineItem.getSkuCode());
		}
		
		if(eligible && brands.size() != 0 && !excludeBrands){			
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
				+ rawContentKeys + ", excludeBrands=" + excludeBrands
				+ ", excludeSkus=" + excludeSkus + ", loopEnebled="
				+ loopEnabled + ", skus=" + skus + "]";
	}
	
	@Override
	public boolean isStoreRequired() {
		return true;
	}

	
	public Boolean getExcludeSkus() {
		return excludeSkus;
	}

	public void setExcludeSkus(Boolean excludeSkus) {
		this.excludeSkus = excludeSkus;
	}

	public Boolean getExcludeBrands() {
		return excludeBrands;
	}

	public void setExcludeBrands(Boolean excludeBrands) {
		this.excludeBrands = excludeBrands;
	}

	public Boolean getLoopEnabled() {
		return loopEnabled;
	}

	public void setLoopEnabled(Boolean loopEnabled) {
		this.loopEnabled = loopEnabled;
	}

	public void setSkus(Set<String> skus) {
		this.skus = skus;
	}

	public void setBrands(Set<String> brands) {
		this.brands = brands;
	}

	public Set<String> getDepartments() {
		return departments;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public Set<String> getRecipes() {
		return recipes;
	}

	public void setDepartments(Set<String> departments) {
		this.departments = departments;
		setRawContentKeys(EnumDCPDContentType.DEPARTMENT.getName(),departments);
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
		setRawContentKeys(EnumDCPDContentType.CATEGORY.getName(),categories);
	}

	public void setRecipes(Set<String> recipes) {
		this.recipes = recipes;
		setRawContentKeys(EnumDCPDContentType.RECIPE.getName(),recipes);
	}
	
	public void setRawContentKeys(String type, Set<String> ids){
		for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			rawContentKeys.add(ContentNodeModelUtil.getContentKey(type, id));
		}		
	}
}



