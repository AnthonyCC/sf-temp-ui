/**
 * 
 */
package com.freshdirect.cms.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.fdstore.content.ComponentGroupModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.EnumTemplateType;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.fdstore.content.YmalSet;
import com.freshdirect.fdstore.content.YmalSetSource;
import com.freshdirect.framework.util.DayOfWeekSet;

public class MockProductModel extends MockContentNodeModel implements
		ProductModel, Cloneable {

    private static final long serialVersionUID = 8059062882580615207L;

    private List<SkuModel> skuModels;
	private List<BrandModel> brands = new ArrayList<BrandModel>();

	public MockProductModel() {
	}

	/**
	 * @param key
	 */
	public MockProductModel(ContentKey key) {
		super(key);
	}

	public MockProductModel(String catId, String productId) {
		super(ContentKey.getContentKey(FDContentTypes.PRODUCT, productId));
		setParentKeys(Collections.singleton(ContentKey.getContentKey(FDContentTypes.CATEGORY,
				catId)));
	}

	/**
	 * @param type
	 * @param id
	 * @throws InvalidContentKeyException
	 */
	public MockProductModel(ContentType type, String id)
			throws InvalidContentKeyException {
		super(type, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#enforceQuantityMax()
	 */
	@Override
	public boolean enforceQuantityMax() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getAboutPriceFormatted(double)
	 */
	@Override
	public String getAboutPriceFormatted(double savingsPercentage) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getActiveYmalSet()
	 */
	@Override
	public YmalSet getActiveYmalSet() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAka()
	 */
	@Override
	public String getAka() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAlsoSoldAs(int)
	 */
	@Override
	public ProductModel getAlsoSoldAs(int idx) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAlsoSoldAs()
	 */
	@Override
	public List<ProductModel> getAlsoSoldAs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAlsoSoldAsName()
	 */
	@Override
	public String getAlsoSoldAsName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAlsoSoldAsRefs()
	 */
	@Override
	public List getAlsoSoldAsRefs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAlternateImage()
	 */
	@Override
	public Image getAlternateImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getAutoconfiguration()
	 */
	@Override
	public FDConfigurableI getAutoconfiguration() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getBlockedDays()
	 */
	@Override
	public DayOfWeekSet getBlockedDays() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getBrands()
	 */
	@Override
	public List<BrandModel> getBrands() {
		return brands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getCategoryImage()
	 */
	@Override
	public Image getCategoryImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getCommonNutritionInfo(com
	 * .freshdirect.content.nutrition.ErpNutritionInfoType)
	 */
	@Override
	public Set getCommonNutritionInfo(ErpNutritionInfoType type)
			throws FDResourceException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getComponentGroups()
	 */
	@Override
	public List<ComponentGroupModel> getComponentGroups() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getConfirmImage()
	 */
	@Override
	public Image getConfirmImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getContainerWeightHalfPint()
	 */
	@Override
	public Double getContainerWeightHalfPint() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getContainerWeightPint()
	 */
	@Override
	public Double getContainerWeightPint() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getContainerWeightQuart()
	 */
	@Override
	public Double getContainerWeightQuart() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getCountryOfOrigin()
	 */
	@Override
	public List<String> getCountryOfOrigin() throws FDResourceException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDealPercentage()
	 */
	@Override
	public int getDealPercentage() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getDealPercentage(java.lang
	 * .String)
	 */
	@Override
	public int getDealPercentage(String skuCode) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDefaultPrice()
	 */
	@Override
	public String getDefaultPrice() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDefaultSku()
	 */
	@Override
	public SkuModel getDefaultSku() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDepartment()
	 */
	@Override
	public DepartmentModel getDepartment() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDescriptiveImage()
	 */
	@Override
	public Image getDescriptiveImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDetailImage()
	 */
	@Override
	public Image getDetailImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDisplayableBrands()
	 */
	@Override
	public List<BrandModel> getDisplayableBrands() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDisplayableBrands(int)
	 */
	@Override
	public List<BrandModel> getDisplayableBrands(int numberOfBrands) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getDonenessGuide()
	 */
	@Override
	public List<Html> getDonenessGuide() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getExpertWeight()
	 */
	@Override
	public int getExpertWeight() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getFddefFrenching()
	 */
	@Override
	public Html getFddefFrenching() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getFddefGrade()
	 */
	@Override
	public Html getFddefGrade() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getFddefRipeness()
	 */
	@Override
	public Html getFddefRipeness() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getFeatureImage()
	 */
	@Override
	public Image getFeatureImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getFreshTips()
	 */
	@Override
	public Html getFreshTips() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getHighestDealPercentage()
	 */
	@Override
	public int getHighestDealPercentage() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getHighestDealPercentage(java
	 * .lang.String)
	 */
	@Override
	public int getHighestDealPercentage(String skuCode) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getHowtoCookitFolders()
	 */
	@Override
	public List<CategoryModel> getHowtoCookitFolders() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getLayout()
	 */
	@Override
	public EnumLayoutType getLayout() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getNewWineRegion()
	 */
	@Override
	public List<DomainValue> getNewWineRegion() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getNewWineType()
	 */
	@Override
	public List<DomainValue> getNewWineType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPackageDescription()
	 */
	@Override
	public String getPackageDescription() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPartallyFrozen()
	 */
	@Override
	public Html getPartallyFrozen() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPerfectPair()
	 */
	@Override
	public CategoryModel getPerfectPair() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPreferredSku()
	 */
	@Override
	public SkuModel getPreferredSku() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPrice(double)
	 */
	@Override
	public double getPrice(double savingsPercentage) {
		return 0.;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPriceFormatted(double,
	 * String)
	 */
	@Override
	public String getPriceFormatted(double savingsPercentage, String skuCode) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPrimaryBrandName()
	 */
	@Override
	public String getPrimaryBrandName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getPrimaryBrandName(java.lang
	 * .String)
	 */
	@Override
	public String getPrimaryBrandName(String productName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getPrimaryHome()
	 */
	@Override
	public CategoryModel getPrimaryHome() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProdImage()
	 */
	@Override
	public Image getProdImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProdPageRatings()
	 */
	@Override
	public String getProdPageRatings() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProdPageTextRatings()
	 */
	@Override
	public String getProdPageTextRatings() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductAbout()
	 */
	@Override
	public Html getProductAbout() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductBottomMedia()
	 */
	@Override
	public Html getProductBottomMedia() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductBundle()
	 */
	@Override
	public List<ProductModel> getProductBundle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductDescription()
	 */
	@Override
	public Html getProductDescription() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getProductDescriptionNote()
	 */
	@Override
	public Html getProductDescriptionNote() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductLayout()
	 */
	@Override
	public EnumProductLayout getProductLayout() {
		return null;
	}

	@Override
	public EnumProductLayout getProductLayout(EnumProductLayout defValue) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductQualityNote()
	 */
	@Override
	public Html getProductQualityNote() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductRating()
	 */
	@Override
	public String getProductRating() throws FDResourceException {
		return null;
	}

	@Override
	public EnumOrderLineRating getProductRatingEnum() throws FDResourceException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductTerms()
	 */
	@Override
	public Html getProductTerms() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getProductTermsMedia()
	 */
	@Override
	public Html getProductTermsMedia() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getQuantityIncrement()
	 */
	@Override
	public float getQuantityIncrement() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getQuantityMaximum()
	 */
	@Override
	public float getQuantityMaximum() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getQuantityMinimum()
	 */
	@Override
	public float getQuantityMinimum() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getQuantityText()
	 */
	@Override
	public String getQuantityText() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getQuantityTextSecondary()
	 */
	@Override
	public String getQuantityTextSecondary() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRating()
	 */
	@Override
	public List<DomainValue> getRating() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRatingProdName()
	 */
	@Override
	public String getRatingProdName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRatingRelatedImage()
	 */
	@Override
	public Image getRatingRelatedImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRecommendTable()
	 */
	@Override
	public Html getRecommendTable() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getRecommendedAlternatives()
	 */
	@Override
	public List<ContentNodeModel> getRecommendedAlternatives() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRedirectUrl()
	 */
	@Override
	public String getRedirectUrl() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRelatedRecipes()
	 */
	@Override
	public List<Recipe> getRelatedRecipes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getRolloverImage()
	 */
	@Override
	public Image getRolloverImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSalesUnitDescription()
	 */
	@Override
	public Html getSalesUnitDescription() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSalesUnitLabel()
	 */
	@Override
	public String getSalesUnitLabel() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSeafoodOrigin()
	 */
	@Override
	public String getSeafoodOrigin() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSeasonText()
	 */
	@Override
	public String getSeasonText() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSellBySalesunit()
	 */
	@Override
	public String getSellBySalesunit() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getServingSuggestion()
	 */
	@Override
	public String getServingSuggestion() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSku(int)
	 */
	@Override
	public SkuModel getSku(int idx) {
		return getSkus().get(idx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSku(java.lang.String)
	 */
	@Override
	public SkuModel getSku(String skuCode) {
		for (SkuModel s : getSkus()) {
			if (skuCode.equals(s.getSkuCode())) {
				return s;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSkuCodes()
	 */
	@Override
	public List<String> getSkuCodes() {
		List<String> result = new ArrayList<String>();
		for (SkuModel s : getSkus()) {
			result.add(s.getSkuCode());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSkus()
	 */
	@Override
	public List<SkuModel> getSkus() {
		return skuModels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSourceProduct()
	 */
	@Override
	public ProductModel getSourceProduct() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getSubtitle()
	 */
	@Override
	public String getSubtitle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getTemplateType()
	 */
	@Override
	public EnumTemplateType getTemplateType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getThumbnailImage()
	 */
	@Override
	public Image getThumbnailImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getTieredDealPercentage()
	 */
	@Override
	public int getTieredDealPercentage() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getTieredDealPercentage(java
	 * .lang.String)
	 */
	@Override
	public int getTieredDealPercentage(String skuCode) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getTieredPrice(double)
	 */
	@Override
	public String getTieredPrice(double savingsPercentage) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getUnitOfMeasure()
	 */
	@Override
	public DomainValue getUnitOfMeasure() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getUsageList()
	 */
	@Override
	public List<Domain> getUsageList() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getVariationMatrix()
	 */
	@Override
	public List<Domain> getVariationMatrix() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getVariationOptions()
	 */
	@Override
	public List<Domain> getVariationOptions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getWasPriceFormatted(double)
	 */
	@Override
	public String getWasPriceFormatted(double savingsPercentage) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWeRecommendImage()
	 */
	@Override
	public List<ProductModel> getWeRecommendImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWeRecommendText()
	 */
	@Override
	public List<ProductModel> getWeRecommendText() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineAging()
	 */
	@Override
	public String getWineAging() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineAlchoholContent()
	 */
	@Override
	public String getWineAlchoholContent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineCity()
	 */
	@Override
	public String getWineCity() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineClassification()
	 */
	@Override
	public String getWineClassification() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineClassifications()
	 */
	@Override
	public List<DomainValue> getWineClassifications() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineCountry()
	 */
	@Override
	public DomainValue getWineCountry() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineFyi()
	 */
	@Override
	public String getWineFyi() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineImporter()
	 */
	@Override
	public String getWineImporter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineRating1()
	 */
	@Override
	public List<DomainValue> getWineRating1() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineRating2()
	 */
	@Override
	public List<DomainValue> getWineRating2() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineRating3()
	 */
	@Override
	public List<DomainValue> getWineRating3() {
		return null;
	}

	@Override
	public DomainValue getWineRatingValue1() {
		return null;
	}

	@Override
	public DomainValue getWineRatingValue2() {
		return null;
	}

	@Override
	public DomainValue getWineRatingValue3() {
		return null;
	}

	@Override
	public boolean hasWineOtherRatings() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineRegion()
	 */
	@Override
	public String getWineRegion() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineReview1()
	 */
	@Override
	public Html getWineReview1() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineReview2()
	 */
	@Override
	public Html getWineReview2() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineReview3()
	 */
	@Override
	public Html getWineReview3() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineType()
	 */
	@Override
	public String getWineType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineVarietal()
	 */
	@Override
	public List<DomainValue> getWineVarietal() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getWineVintage()
	 */
	@Override
	public List<DomainValue> getWineVintage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getYmalCategories()
	 */
	@Override
	public List<CategoryModel> getYmalCategories() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getYmalProducts()
	 */
	@Override
	public List<ProductModel> getYmalProducts() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#getYmalProducts(java.util.Set)
	 */
	@Override
	public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getYmalRecipes()
	 */
	@Override
	public List<Recipe> getYmalRecipes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getYmals()
	 */
	@Override
	public List<ContentNodeModel> getYmals() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getZoomImage()
	 */
	@Override
	public Image getZoomImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#hasComponentGroups()
	 */
	@Override
	public boolean hasComponentGroups() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#hasTerms()
	 */
	@Override
	public boolean hasTerms() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isAutoconfigurable()
	 */
	@Override
	public boolean isAutoconfigurable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.freshdirect.storeapi.content.ProductModel#
	 * isCharacteristicsComponentsAvailable
	 * (com.freshdirect.fdstore.FDConfigurableI)
	 */
	@Override
	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isDisplayableBasedOnCms()
	 */
	@Override
	public boolean isDisplayableBasedOnCms() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#isExcludedRecommendation()
	 */
	@Override
	public boolean isExcludedRecommendation() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isFrozen()
	 */
	@Override
	public boolean isFrozen() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isGrocery()
	 */
	@Override
	public boolean isGrocery() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isIncrementMaxEnforce()
	 */
	@Override
	public boolean isIncrementMaxEnforce() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isInvisible()
	 */
	@Override
	public boolean isInvisible() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isNew()
	 */
	@Override
	public boolean isNew() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getBackInStockAge()
	 */
	@Override
	public double getBackInStockAge() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getBackInStockDate()
	 */
	@Override
	public Date getBackInStockDate() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getNewAge()
	 */
	@Override
	public double getNewAge() {
		return 0;
	}

	@Override
	public double getAge() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#getNewDate()
	 */
	@Override
	public Date getNewDate() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isBackInStock()
	 */
	@Override
	public boolean isBackInStock() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isNotSearchable()
	 */
	@Override
	public boolean isNotSearchable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isNutritionMultiple()
	 */
	@Override
	public boolean isNutritionMultiple() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isPerishable()
	 */
	@Override
	public boolean isPerishable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isPlatter()
	 */
	@Override
	public boolean isPlatter() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isPreconfigured()
	 */
	@Override
	public boolean isPreconfigured() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.storeapi.content.ProductModel#isQualifiedForPromotions()
	 */
	@Override
	public boolean isQualifiedForPromotions() throws FDResourceException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isShowSalesUnitImage()
	 */
	@Override
	public boolean isShowSalesUnitImage() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isShowTopTenImage()
	 */
	@Override
	public boolean isShowTopTenImage() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.ProductModel#isSoldBySalesUnits()
	 */
	@Override
	public boolean isSoldBySalesUnits() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdstore.content.AvailabilityI#getEarliestAvailability()
	 */
	@Override
	public Date getEarliestAvailability() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.fdstore.content.AvailabilityI#isAvailableWithin(int)
	 */
	@Override
	public boolean isAvailableWithin(int days) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.fdstore.content.AvailabilityI#isDiscontinued()
	 */
	@Override
	public boolean isDiscontinued() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.fdstore.content.AvailabilityI#isOutOfSeason()
	 */
	@Override
	public boolean isOutOfSeason() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.fdstore.content.AvailabilityI#isTempUnavailable()
	 */
	@Override
	public boolean isTempUnavailable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.fdstore.content.AvailabilityI#isUnavailable()
	 */
	@Override
	public boolean isUnavailable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.YmalSource#getRelatedProducts()
	 */
	@Override
	public List<ProductModel> getRelatedProducts() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.YmalSource#getYmalHeader()
	 */
	@Override
	public String getYmalHeader() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.freshdirect.storeapi.content.YmalSource#resetActiveYmalSetSession()
	 */
	@Override
	public void resetActiveYmalSetSession() {

	}

	@Override
	public ContentKey getWineCountryKey() {
		return null;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException cne) {
			throw new RuntimeException("Cloning attempted:" + cne.getMessage(), cne);
		}
	}

	public void setSkuModels(List<SkuModel> skuModels) {
		this.skuModels = skuModels;
	}

	public MockProductModel addSku(SkuModel sku) {
		if (skuModels == null) {
			skuModels = new ArrayList<SkuModel>();
		}
		skuModels.add(sku);
		return this;
	}

	public MockProductModel addBrand(BrandModel brand) {
		if (brands == null) {
			brands = new ArrayList<BrandModel>();
		}
		brands.add(brand);
		return this;
	}

	@Override
	public List getGiftcardType() {
		return null;
	}

	@Override
	public int getTemplateType(int defaultValue) {
		return defaultValue;
	}

	@Override
	public MediaI getMedia(String name) {
		return null;
	}

	@Override
	public boolean isHasPartiallyFrozen() {
		return false;
	}

	@Override
	public boolean isHasSalesUnitDescription() {
		return false;
	}

	@Override
	public Html getFddefSource() {
		return null;
	}

	@Override
	public String getDefaultSkuCode() {
		return getDefaultSku() != null ? getDefaultSku().getSkuCode() : null;
	}

	@Override
	public ProductModel getPrimaryProductModel() {
		return this;
	}

	@Override
	public List<SkuModel> getPrimarySkus() {
		return getSkus();
	}

	@Override
	public boolean isInPrimaryHome() {
		return true;
	}

	/*
	 * iPhone related
	 */
	@Override
	public Image getAlternateProductImage() {
		return null;
	}

	@Override
	public boolean isHideIphone() {
		return false;
	}

	@Override
	public String getFreshnessGuaranteed() throws FDResourceException {
		return null;
	}

	@Override
    public String getPriceFormatted(double savingsPercentage) {
		return null;
	}

	@Override
    public String getProductRating(String skuCode) throws FDResourceException {
		return null;
	}

	public PricingContext getPricingContext() {
		return null;
	}

	@Override
	public SkuModel getDefaultSku(PricingContext ctx) {
		return null;
	}

	@Override
	public SkuModel getValidSkuCode(PricingContext ctx, String skuCode) {
		if (skuCode != null && skuModels != null) {
			for (SkuModel s : skuModels) {
				if (skuCode.equals(s.getSkuCode())) {
					return s;
				}
			}
		}
		return getDefaultSku(ctx);
	}

	@Override
	public PriceCalculator getPriceCalculator() {
		return new PriceCalculator(getPricingContext(), this, getDefaultSku());
	}

	@Override
	public PriceCalculator getPriceCalculator(String skuCode) {
		return new PriceCalculator(getPricingContext(), this, getValidSkuCode(
				getPricingContext(), skuCode));
	}
	
	@Override
	public PriceCalculator getPriceCalculator(SkuModel sku) {
	    return new PriceCalculator(getPricingContext(), this, sku);
	}

	@Override
	public PriceCalculator getPriceCalculator(PricingContext pricingContext) {
	    return new PriceCalculator(pricingContext, this, this.getDefaultSku(pricingContext));
	}

	@Override
	public PriceCalculator getPriceCalculator(String skuCode, PricingContext pricingContext) {
        return new PriceCalculator(pricingContext, this, this.getValidSkuCode(pricingContext, skuCode));
	}

	@Override
	public PriceCalculator getPriceCalculator(SkuModel sku, PricingContext pricingContext) {
        return new PriceCalculator(pricingContext, this, sku);
	}
	
	@Override
	public boolean isFullyAvailable() {
		return isDisplayable();
	}

	@Override
	public boolean isTemporaryUnavailableOrAvailable() {
		return isFullyAvailable();
	}

	@Override
	public Set<DomainValue> getWineDomainValues() {
		return Collections.emptySet();
	}

	@Override
	public boolean isHideWineRatingPricing() {
		return false;
	}

	@Override
	public CategoryModel getCategory() {
		return null;
	}

	@Override
	public YmalSetSource getParentYmalSetSource() {
		return null;
	}

	@Override
	public List<YmalSet> getYmalSets() {
		return null;
	}

	@Override
	public boolean hasActiveYmalSets() {
		return false;
	}

	@Override
	public boolean isShowWineRatings() {
		return false;
	}

	@Override
	public String getSustainabilityRating() throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSustainabilityRating(String skuCode)
			throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumSustainabilityRating getSustainabilityRatingEnum()
			throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDGroup getFDGroup() throws FDResourceException {
		SkuModel sku = getDefaultSku();
		if(sku != null){
			try {
				FDProductInfo pInfo = sku.getProductInfo();
				return pInfo.getGroup(getPricingContext().getZoneInfo().getSalesOrg(),getPricingContext().getZoneInfo().getDistributionChanel());
			} catch (FDSkuNotFoundException e) {				
			}			
		}
		return null;
	}

	@Override
	public boolean isExcludedForEBTPayment() {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public boolean isRetainOriginalSkuOrder() {
		return false;
	}
	
	@Override
	public 	boolean showDefaultSustainabilityRating() {
		return false;
	}

	@Override
	public boolean isDisabledRecommendations() {
		return false;
	}

	@Override
	public SkuModel getDefaultTemporaryUnavSku() {
		return null;
	}

	@Override
	public EnumOrderLineRating getProductRatingEnum(String skuCode) throws FDResourceException {
		return null;
	}

	@Override
	public Image getPackageImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TagModel> getTags() {
		return null;
	}

	@Override
	public Set<TagModel> getAllTags() {
		return null;
	}

	@Override
	public Set<DomainValue> getAllDomainValues() {
		return null;
	}


	@Override
	public List<ProductModel> getUpSellProducts() {
		return null;
	}

	@Override
	public List<ProductModel> getCrossSellProducts() {
		return null;
	}
	
	@Override
	public String getBrowseRecommenderType(){
		return null;
	}

	@Override
	public int getHeatRating() {
		return 0;
	}

	@Override
	public Image getJumboImage() {
		return null;
	}

	@Override
	public Image getItemImage() {
		return null;
	}

	@Override
	public Image getExtraImage() {
		return null;
	}
	
	@Override
	public boolean isDisableAtpFailureRecommendation(){
		return false;
	}


	@Override
	public EnumProductLayout getSpecialLayout() {
		return null;
	}
	
	@Override
	public List<ProductModel> getCompleteTheMeal() {
		return null;
	}

    @Override
    public List<ProductModel> getIncludeProducts() {
        return null;
    }

	@Override
	public UserContext getUserContext() {
		return UserContext.createUserContext(EnumEStoreId.FD);
	}

	@Override
	public String getPageTitle() {
		return null;
	}
	
    @Override
    public String getFdxPageTitle() {
        return null;
    }

	@Override
	public String getSEOMetaDescription() {
		return null;
	}
	
	@Override
	public String getFdxSEOMetaDescription() {
	    return null;
	}

	@Override
	public String getPairItHeading() {
		return null;
	}

	@Override
	public String getPairItText() {
		return null;
	}

	@Override
	public int getTimeToComplete() {
	    return 0;
	}
	
	@Override
	public void setParentNode(ContentNodeModel parentNode) {
	}
}
