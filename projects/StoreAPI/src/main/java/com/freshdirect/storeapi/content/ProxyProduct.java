package com.freshdirect.storeapi.content;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DayOfWeekSet;

public abstract class ProxyProduct extends AbstractProductModelImpl {

    public ProxyProduct(ContentKey key) {
        super(key);
    }

	/**
	 * @return the ProductModel associated with this configuration
	 */
	public abstract ProductModel getProduct();

	@Override
	public Object getCmsAttributeValue(String name) {
		Object a = super.getCmsAttributeValue(name);
		if (a != null) {
			return a;
		}
		ProductModel p = getProduct();
		if (p != null) {
			return p.getCmsAttributeValue(name);
		}

		return null;
	}

	//
	// proxy pass-thru methods
	//

	@Override
    public String getAka() {
		return getProduct().getAka();
	}

	@Override
    public Html getEditorial() {
		return getProduct().getEditorial();
	}

	@Override
    public float getQuantityMinimum() {
		return getProduct().getQuantityMinimum();
	}

	@Override
    public float getQuantityMaximum() {
		return getProduct().getQuantityMaximum();
	}

	@Override
    public float getQuantityIncrement() {
		return getProduct().getQuantityIncrement();
	}

	@Override
    public boolean enforceQuantityMax() {
		return getProduct().enforceQuantityMax();
	}

	@Override
    public boolean isInvisible() {
		return getAttribute("INVISIBLE", false);
	}

	@Override
    public List<ProductModel> getAlsoSoldAsRefs() {
		return getProduct().getAlsoSoldAs();
	}

	@Override
    public SkuModel getSku(int idx) {
		return getProduct().getSku(idx);
	}

	@Override
    public SkuModel getSku(String skuCode) {
		return getProduct().getSku(skuCode);
	}

	@Override
    public List<SkuModel> getSkus() {
		return getProduct().getSkus();
	}

	@Override
    public List getSkuCodes() {
		return getProduct().getSkuCodes();
	}

	@Override
    public List getBrands() {
		return getProduct().getBrands();
	}

	@Override
    public boolean isPerishable() {
		return getProduct().isPerishable();
	}

	@Override
    public boolean isFrozen() {
		return getProduct().isFrozen();
	}

	@Override
    public boolean isGrocery() {
		return getProduct().isGrocery();
	}

	@Override
    public boolean isSoldBySalesUnits() {
		return getProduct().isSoldBySalesUnits();
	}

	@Override
    public boolean isQualifiedForPromotions() throws FDResourceException {
		return getProduct().isQualifiedForPromotions();
	}

	@Override
    public boolean isPlatter() {
		return getProduct().isPlatter();
	}

	@Override
    public DayOfWeekSet getBlockedDays() {
		return getProduct().getBlockedDays();
	}

	@Override
    public SkuModel getDefaultSku() {
		return getProduct().getDefaultSku();
	}

	@Override
	public SkuModel getDefaultSku(PricingContext ctx) {
		return getProduct().getDefaultSku(ctx);
	}

	@Override
    public Set getCommonNutritionInfo(ErpNutritionInfoType type)
			throws FDResourceException {
		return getProduct().getCommonNutritionInfo(type);
	}

	@Override
    public String getPrimaryBrandName() {
		return getProduct().getPrimaryBrandName();
	}

	@Override
    public String getPrimaryBrandName(String productName) {
		return getProduct().getPrimaryBrandName(productName);
	}

	@Override
    public boolean hasComponentGroups() {
		return getProduct().hasComponentGroups();
	}

	@Override
    public boolean isUnavailable() {
		if (getProduct().isUnavailable()) {
			return true;
		}

		return false;
	}

	@Override
    public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
		return getProduct().isCharacteristicsComponentsAvailable(config);
	}

	@Override
    public String getAltText() {
		return getProduct().getAltText();
	}

	@Override
    public String getEditorialTitle() {
		return getProduct().getEditorialTitle();
	}

	@Override
    public String getKeywords() {
		return getProduct().getKeywords();
	}

	@Override
    public boolean isSearchable() {
		return getProduct().isSearchable();
	}

	@Override
    public boolean isHidden() {
		return getProduct().isHidden();
	}

	@Override
    public String getHideUrl() {
		return getProduct().getHideUrl();
	}

	@Override
    public String getPath() {
		return getProduct().getPath();
	}

	//
	// availability
	//

	@Override
    public boolean isDiscontinued() {
		return getProduct().isDiscontinued();
	}

	@Override
    public boolean isTempUnavailable() {
		return getProduct().isTempUnavailable();
	}

	@Override
    public boolean isOutOfSeason() {
		return getProduct().isOutOfSeason();
	}

	/**
	 * Tell if the product is available within the specified number of days.
	 *
	 * @param days
	 *          the number of days to look at, '1' means today, '2' means by
	 *          tomorrow, etc.
	 * @return if the product is available within the specified number of days.
	 */
	@Override
    public boolean isAvailableWithin(int days) {
		return getProduct().isAvailableWithin(days);
	}

	@Override
    public Date getEarliestAvailability() {
		return getProduct().getEarliestAvailability();
	}

	@Override
    public String getSubtitle() {
		return getAttribute("SUBTITLE", "");
	}

	@Override
    public List getDisplayableBrands() {
		return getProduct().getDisplayableBrands(1);
	}

	@Override
    public List getDisplayableBrands(int numOfBrands) {
		return getProduct().getDisplayableBrands(numOfBrands);
	}

	@Override
    public EnumLayoutType getLayout() {
		return getProduct().getLayout();
	}

	@Override
    public EnumTemplateType getTemplateType() {
		return getProduct().getTemplateType();
	}

	@Override
    public String getRedirectUrl() {
		return getProduct().getRedirectUrl();
	}

	@Override
    public String getSellBySalesunit() {
		return getProduct().getSellBySalesunit();
	}

	@Override
    public String getSalesUnitLabel() {
		return getProduct().getSalesUnitLabel();
	}

	@Override
    public String getQuantityText() {
		return getProduct().getQuantityText();
	}

	@Override
    public String getQuantityTextSecondary() {
		return getProduct().getQuantityTextSecondary();
	}

	@Override
    public String getServingSuggestion() {
		return getProduct().getServingSuggestion();
	}

	@Override
    public String getSeasonText() {
		return getProduct().getSeasonText();
	}

	@Override
    public String getWineFyi() {
		return getProduct().getWineFyi();
	}

	@Override
    public String getWineRegion() {
		return getProduct().getWineRegion();
	}

	@Override
    public String getSeafoodOrigin() {
		return getProduct().getSeafoodOrigin();
	}

	@Override
    public boolean isShowSalesUnitImage() {
		return getProduct().isShowSalesUnitImage();
	}

	@Override
    public boolean isNutritionMultiple() {
		return getProduct().isNutritionMultiple();
	}

	@Override
    public boolean isNotSearchable() {
		return getProduct().isNotSearchable();
	}

	@Override
    public Double getContainerWeightHalfPint() {
		return getProduct().getContainerWeightHalfPint();
	}

	@Override
    public Double getContainerWeightPint() {
		return getProduct().getContainerWeightPint();
	}

	@Override
    public Double getContainerWeightQuart() {
		return getProduct().getContainerWeightQuart();
	}

	@Override
    public boolean isIncrementMaxEnforce() {
		return getProduct().isIncrementMaxEnforce();
	}

	@Override
    public String getProdPageRatings() {
		return getProduct().getProdPageRatings();
	}

	@Override
    public String getProdPageTextRatings() {
		return getProduct().getProdPageTextRatings();
	}

	@Override
    public String getRatingProdName() {
		return getProduct().getRatingProdName();
	}

	@Override
    public List getYmals() {
		return getProduct().getYmals();
	}

	@Override
    public YmalSet getActiveYmalSet() {
		return getProduct().getActiveYmalSet();
	}

	@Override
    public void resetActiveYmalSetSession() {
		getProduct().resetActiveYmalSetSession();
	}

	/**
	 * Return a list of YMAL products.
	 *
	 * @return a list of ProductModel objects, which are contained in the YMALs
	 *         for this product.
	 * @see #getYmals()
	 * @see #getYmalProducts(Set)
	 */
	@Override
    public List getYmalProducts() {
		return getProduct().getYmalProducts();
	}

	/**
	 * Return a list of YMAL products.
	 *
	 * @param removeSkus
	 *          a set of FDSku objects, for which correspoding products need to be
	 *          removed from the final list of YMALs. this might be null, in which
	 *          case it has no effect.
	 * @return a list of ProductModel objects, which are contained in the YMALs
	 *         for this product.
	 * @see #getYmals()
	 */
	@Override
    public List getYmalProducts(Set removeSkus) {
		return getProduct().getYmalProducts(removeSkus);
	}

	/**
	 * Return a list of YMAL categories.
	 *
	 * @return a list of CategoryModel objects, which are contained in the YMALs
	 *         for this product.
	 * @see #getYmals()
	 */
	@Override
    public List getYmalCategories() {
		return getProduct().getYmalCategories();
	}

	/**
	 * Return a list of YMAL recipes.
	 *
	 * @return a list of Recipe objects, which are contained in the YMALs for this
	 *         product.
	 * @see #getYmals()
	 */
	@Override
    public List getYmalRecipes() {
		return getProduct().getYmalRecipes();
	}

	@Override
    public List getWeRecommendText() {
		return getProduct().getWeRecommendText();
	}

	@Override
    public List getWeRecommendImage() {
		return getProduct().getWeRecommendImage();
	}

	@Override
    public List getRelatedProducts() {
		return getProduct().getYmals();
	}

	@Override
    public List getRelatedRecipes() {
		return getProduct().getRelatedRecipes();
	}

	@Override
    public List getProductBundle() {
		return getProduct().getProductBundle();
	}

	@Override
    public List getHowtoCookitFolders() {
		return getProduct().getHowtoCookitFolders();
	}

	@Override
    public CategoryModel getPrimaryHome() {
		return getProduct().getPrimaryHome();
	}

	@Override
    public SkuModel getPreferredSku() {
		return getProduct().getPreferredSku();
	}

	@Override
    public List getRating() {
		return getProduct().getRating();
	}

	@Override
    public List getUsageList() {
		return getProduct().getUsageList();
	}

	@Override
    public DomainValue getUnitOfMeasure() {
		return getProduct().getUnitOfMeasure();
	}

	@Override
    public List<Domain> getVariationMatrix() {
		return getProduct().getVariationMatrix();
	}

	@Override
    public List<Domain> getVariationOptions() {
		return getProduct().getVariationOptions();
	}

	@Override
    public DomainValue getWineCountry() {
		return getProduct().getWineCountry();
	}

	@Override
	public ContentKey getWineCountryKey() {
		return getProduct().getWineCountryKey();
	}

	@Override
    public Image getProdImage() {
		return getProduct().getProdImage();
	}

	@Override
    public Image getFeatureImage() {
		return getProduct().getFeatureImage();
	}

	@Override
    public Image getRatingRelatedImage() {
		return getProduct().getRatingRelatedImage();
	}

	@Override
    public Image getAlternateImage() {
		return getProduct().getAlternateImage();
	}

	@Override
    public Image getDescriptiveImage() {
		return getProduct().getDescriptiveImage();
	}

	@Override
    public Image getRolloverImage() {
		return getProduct().getRolloverImage();
	}

	@Override
    public Html getProductAbout() {
		return getProduct().getProductAbout();
	}

	@Override
    public Html getRecommendTable() {
		return getProduct().getRecommendTable();
	}

	@Override
    public Html getProductQualityNote() {
		return getProduct().getProductQualityNote();
	}

	@Override
    public Html getProductDescriptionNote() {
		return getProduct().getProductDescriptionNote();
	}

	@Override
    public Html getFreshTips() {
		return getProduct().getFreshTips();
	}

	@Override
    public List<Html> getDonenessGuide() {
		return getProduct().getDonenessGuide();
	}

	@Override
    public Html getFddefFrenching() {
		return getProduct().getFddefFrenching();
	}

	@Override
    public Html getFddefGrade() {
		return getProduct().getFddefGrade();
	}

	@Override
    public Html getFddefRipeness() {
		return getProduct().getFddefRipeness();
	}

	@Override
    public Html getSalesUnitDescription() {
		return getProduct().getSalesUnitDescription();
	}

	@Override
    public Html getPartallyFrozen() {
		return getProduct().getPartallyFrozen();
	}

	@Override
	public boolean isHasPartiallyFrozen() {
		return getProduct().isHasPartiallyFrozen();
	}

	@Override
    public List getComponentGroups() {
		return getProduct().getComponentGroups();
	}

	@Override
    public Html getProductTermsMedia() {
		return getProduct().getProductTermsMedia();
	}

	// new Wine Store changes

	@Override
    public String getWineClassification() {
		return getProduct().getWineClassification();
	}

	@Override
    public String getWineImporter() {
		return getProduct().getWineImporter();
	}

	@Override
    public String getWineAlchoholContent() {
		return getProduct().getWineAlchoholContent();
	}

	@Override
    public String getWineAging() {
		return getProduct().getWineAging();
	}

	@Override
    public String getWineCity() {
		// TODO Auto-generated method stub
		return getProduct().getWineCity();
	}

	@Override
    public String getWineType() {
		return getProduct().getWineType();
	}

	@Override
    public List getNewWineType() {
		return getProduct().getNewWineType();
	}

	@Override
    public List<DomainValue> getWineVintage() {
		return getProduct().getWineVintage();
	}

	@Override
    public List getNewWineRegion() {
		return getProduct().getNewWineRegion();
	}

	@Override
    public List getWineVarietal() {
		return getProduct().getWineVarietal();
	}

	@Override
    public List<DomainValue> getWineRating1() {
		return getProduct().getWineRating1();
	}

	@Override
    public List<DomainValue> getWineRating2() {
		return getProduct().getWineRating2();
	}

	@Override
    public List<DomainValue> getWineRating3() {
		return getProduct().getWineRating3();
	}

	@Override
	public DomainValue getWineRatingValue1() {
		return getProduct().getWineRatingValue1();
	}

	@Override
	public DomainValue getWineRatingValue2() {
		return getProduct().getWineRatingValue2();
	}

	@Override
	public DomainValue getWineRatingValue3() {
		return getProduct().getWineRatingValue3();
	}

	@Override
	public boolean hasWineOtherRatings() {
		return getProduct().hasWineOtherRatings();
	}

	@Override
    public Html getWineReview1() {
		return getProduct().getWineReview1();
	}

	@Override
    public Html getWineReview2() {
		return getProduct().getWineReview2();
	}

	@Override
    public Html getWineReview3() {
		return getProduct().getWineReview3();
	}

	@Override
    public Html getProductBottomMedia() {
		return getProduct().getProductBottomMedia();
	}

	@Override
    public CategoryModel getPerfectPair() {
		return getProduct().getPerfectPair();
	}

	@Override
    public List getWineClassifications() {
		return getProduct().getWineClassifications();
	}

	@Override
    public String getProductRating() throws FDResourceException {
		return getProduct().getProductRating();
	}

	@Override
    public String getProductRating(String skuCode) throws FDResourceException {
		return getProduct().getProductRating(skuCode);
	}

	@Override
    public EnumOrderLineRating getProductRatingEnum() throws FDResourceException {
		return getProduct().getProductRatingEnum();
	}

	@Override
    public EnumOrderLineRating getProductRatingEnum(String skuCode) throws FDResourceException {
		return getProduct().getProductRatingEnum(skuCode);
	}

	@Override
    public String getFreshnessGuaranteed() throws FDResourceException {
		if (FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
			return getProduct().getFreshnessGuaranteed();
		}
		return null;
	}

	@Override
    public List getCountryOfOrigin() throws FDResourceException {
		return getProduct().getCountryOfOrigin();
	}

	// Gift Card changes
	@Override
    public List getGiftcardType() {
		return getProduct().getGiftcardType();
	}

	@Override
	public MediaI getMedia(String name) {
		return getProduct().getMedia(name);
	}

	@Override
	public boolean isHasSalesUnitDescription() {
		return getProduct().isHasSalesUnitDescription();
	}

	@Override
	public Html getFddefSource() {
		return getProduct().getFddefSource();
	}

	@Override
	public String getDefaultSkuCode() {
		return getProduct().getDefaultSkuCode();
	}

	@Override
	public String getPriceFormatted(double savingsPercentage, String skuCode) {
		return getProduct().getPriceFormatted(savingsPercentage, skuCode);
	}

	@Override
	public boolean isShowWineRatings() {
		return getProduct().isShowWineRatings();
	}
	@Override
    public String getSustainabilityRating() throws FDResourceException {
		return getProduct().getSustainabilityRating();
	}

	@Override
    public String getSustainabilityRating(String skuCode) throws FDResourceException {
		return getProduct().getSustainabilityRating(skuCode);
	}

	@Override
    public EnumSustainabilityRating getSustainabilityRatingEnum() throws FDResourceException {
		return getProduct().getSustainabilityRatingEnum();
	}

	@Override
	public boolean isRetainOriginalSkuOrder() {
		return getProduct().isRetainOriginalSkuOrder();
	}

	@Override
	public List<TagModel> getTags() {
		return getProduct().getTags();
	}

	@Override
	public Set<TagModel> getAllTags() {
		return getProduct().getAllTags();
	}


	@Override
	public Set<DomainValue> getAllDomainValues() {
		return getProduct().getAllDomainValues();
	}

}
