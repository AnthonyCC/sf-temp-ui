package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.framework.util.DayOfWeekSet;

public class ProductModelBrandAdsAdapter implements ProductModel, Serializable,
Cloneable, PrioritizedI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5808179393810418009L;
	private final ProductModel productModel;
	private String clickBeacon;
	private String impBeacon;	

	public ProductModelBrandAdsAdapter(ProductModel productModel,
			String clickBeacon, String impBeacon) {
		super();
		this.productModel = productModel;
		this.clickBeacon = clickBeacon;
		this.impBeacon = impBeacon;
	}

	public ProductModel getProductModel() {
		return productModel;
	}

	
		
	
	public boolean enforceQuantityMax() {
		 return productModel.enforceQuantityMax();
	}

	
	public String getAboutPriceFormatted(double savingsPercentage) {
		return productModel.getAboutPriceFormatted(savingsPercentage);
	}

	
	public YmalSet getActiveYmalSet() {
		return productModel.getActiveYmalSet();
	}

	
	public double getAge() {
		return productModel.getAge();
	}

	
	public String getAka() {		 
		return productModel.getAka();
	}

	
	public ProductModel getAlsoSoldAs(int idx) {
		return productModel.getAlsoSoldAs(idx);
	}

	
	public List<ProductModel> getAlsoSoldAs() {
		return productModel.getAlsoSoldAs();
	}

	
	public String getAlsoSoldAsName() {		 
		return productModel.getAlsoSoldAsName();
	}

	
	public List<ProductModel> getAlsoSoldAsRefs() {		 
		return productModel.getAlsoSoldAsRefs();
	}

	
	public String getAltText() {		 
		return productModel.getAltText();
	}

	
	public Image getAlternateImage() {		 
		return productModel.getAlternateImage();
	}

	
	public Image getAlternateProductImage() {		 
		return productModel.getAlternateProductImage();
	}

	
	public FDConfigurableI getAutoconfiguration() {		 
		return productModel.getAutoconfiguration();
	}

	
	public double getBackInStockAge() {		 
		return productModel.getBackInStockAge();
	}

	
	public Date getBackInStockDate() {		 
		return productModel.getBackInStockDate();
	}

	
	public DayOfWeekSet getBlockedDays() {		 
		return productModel.getBlockedDays();
	}

	
	public String getBlurb() {		 
		return productModel.getBlurb();
	}

	
	public List<BrandModel> getBrands() {		 
		return productModel.getBrands();
	}

	
	public CategoryModel getCategory() {		 
		return productModel.getCategory();
	}

	
	public Image getCategoryImage() {		 
		return productModel.getCategoryImage();
	}

	
	public Set getCommonNutritionInfo(ErpNutritionInfoType type)
			throws FDResourceException {		 
		return productModel.getCommonNutritionInfo(type);
	}

	
	public List<ComponentGroupModel> getComponentGroups() {		 
		return productModel.getComponentGroups();
	}

	
	public Image getConfirmImage() {		 
		return productModel.getConfirmImage();
	}

	
	public Double getContainerWeightHalfPint() {		 
		return productModel.getContainerWeightHalfPint();
	}

	
	public Double getContainerWeightPint() {		 
		return productModel.getContainerWeightPint();
	}

	
	public Double getContainerWeightQuart() {		 
		return productModel.getContainerWeightQuart();
	}

	
	public List<String> getCountryOfOrigin() throws FDResourceException {		 
		return productModel.getCountryOfOrigin();
	}

	
	public int getDealPercentage() {		 
		return productModel.getDealPercentage();
	}

	
	public int getDealPercentage(String skuCode) {		 
		return productModel.getDealPercentage();
	}

	
	public String getDefaultPrice() {		 
		return productModel.getDefaultPrice();
	}

	
	public SkuModel getDefaultSku() {		 
		return productModel.getDefaultSku();
	}

	
	public SkuModel getDefaultSku(PricingContext context) {		 
		return productModel.getDefaultSku(context);
	}
	
	
	public SkuModel getDefaultTemporaryUnavSku() {
		return productModel.getDefaultTemporaryUnavSku();
	}

	
	public String getDefaultSkuCode() {		 
		return productModel.getDefaultSkuCode();
	}

	
	public DepartmentModel getDepartment() {		 
		return productModel.getDepartment();
	}

	
	public Image getDescriptiveImage() {		 
		return productModel.getDescriptiveImage();
	}

	
	public Image getDetailImage() {		 
		return productModel.getDetailImage();
	}

	
	public List getDisplayableBrands() {		 
		return productModel.getDisplayableBrands();
	}

	
	public List getDisplayableBrands(int numberOfBrands) {		 
		return productModel.getDisplayableBrands(numberOfBrands);
	}

	
	public List<Html> getDonenessGuide() {		 
		return productModel.getDonenessGuide();
	}

	
	public int getExpertWeight() {		 
		return productModel.getExpertWeight();
	}

	
	public FDGroup getFDGroup() throws FDResourceException {		 
		return productModel.getFDGroup();
	}

	
	public Html getFddefFrenching() {		 
		return productModel.getFddefFrenching();
	}

	
	public Html getFddefGrade() {		 
		return productModel.getFddefGrade();
	}

	
	public Html getFddefRipeness() {		 
		return productModel.getFddefRipeness();
	}

	
	public Html getFddefSource() {		 
		return productModel.getFddefSource();
	}

	
	public Image getFeatureImage() {		 
		return productModel.getFeatureImage();
	}

	
	public Html getFreshTips() {		 
		return productModel.getFreshTips();
	}

	
	public String getFreshnessGuaranteed() throws FDResourceException {		 
		return productModel.getFreshnessGuaranteed();
	}

	
	public String getFullName() {		 
		return productModel.getFullName();
	}

	
	public List getGiftcardType() {		 
		return productModel.getGiftcardType();
	}

	
	public String getGlanceName() {		 
		return productModel.getGlanceName();
	}

	
	public String getHideUrl() {		 
		return productModel.getHideUrl();
	}

	
	public int getHighestDealPercentage() {		 
		return productModel.getHighestDealPercentage();
	}

	
	public int getHighestDealPercentage(String skuCode) {		 
		return productModel.getHighestDealPercentage(skuCode);
	}

	
	public List<CategoryModel> getHowtoCookitFolders() {		 
		return productModel.getHowtoCookitFolders();
	}

	
	public String getKeywords() {		 
		return productModel.getKeywords();
	}

	
	public EnumLayoutType getLayout() {		 
		return productModel.getLayout();
	}

	
	public MediaI getMedia(String name) {		 
		return productModel.getMedia(name);
	}

	
	public String getNavName() {		 
		return productModel.getNavName();
	}

	
	public double getNewAge() {		 
		return productModel.getNewAge();
	}

	
	public Date getNewDate() {		 
		return productModel.getNewDate();
	}

	
	public List<DomainValue> getNewWineRegion() {		 
		return productModel.getNewWineRegion();
	}

	
	public List<DomainValue> getNewWineType() {		 
		return productModel.getNewWineType();
	}

	
	public String getPackageDescription() {		 
		return productModel.getPackageDescription();
	}

	
	public Html getPartallyFrozen() {		 
		return productModel.getPartallyFrozen();
	}

	
	public CategoryModel getPerfectPair() {		 
		return productModel.getPerfectPair();
	}

	
	public SkuModel getPreferredSku() {		 
		return productModel.getPreferredSku();
	}

	
	public double getPrice(double savingsPercentage) {		 
		return productModel.getPrice(savingsPercentage);
	}

	
	public PriceCalculator getPriceCalculator() {
		return productModel.getPriceCalculator();
	}

	
	public PriceCalculator getPriceCalculator(String skuCode) {
			return productModel.getPriceCalculator(skuCode);
	}

	
	public PriceCalculator getPriceCalculator(SkuModel sku) {	
		return productModel.getPriceCalculator(sku);
	}

	
	public PriceCalculator getPriceCalculator(PricingContext pricingContext) {
		return productModel.getPriceCalculator(pricingContext);
	}

	
	public PriceCalculator getPriceCalculator(String skuCode,
			PricingContext pricingContext) {		 
		return productModel.getPriceCalculator(skuCode, pricingContext);
	}

	
	public PriceCalculator getPriceCalculator(SkuModel sku,
			PricingContext pricingContext) {		 
		return productModel.getPriceCalculator(sku, pricingContext);
	}

	
	public String getPriceFormatted(double savingsPercentage) {		 
		return productModel.getPriceFormatted(savingsPercentage);
	}

	
	public String getPriceFormatted(double savingsPercentage, String skuCode) {		 
		return productModel.getPriceFormatted(savingsPercentage, skuCode);
	}

	
	public UserContext getUserContext() {	
		
			return productModel.getUserContext();
	}

	
	public String getPrimaryBrandName() {		 
		return productModel.getPrimaryBrandName();
	}

	
	public String getPrimaryBrandName(String productName) {		 
		return productModel.getPrimaryBrandName(productName);
	}

	
	public CategoryModel getPrimaryHome() {		 
		return productModel.getPrimaryHome();
	}

	
	public ProductModel getPrimaryProductModel() {
		return productModel.getPrimaryProductModel();
	}

	
	public List<SkuModel> getPrimarySkus() {		 
		return productModel.getPrimarySkus();
	}

	
	public Image getProdImage() {		 
		return productModel.getProdImage();
	}

	
	public String getProdPageRatings() {		 
		return productModel.getProdPageRatings();
	}

	
	public String getProdPageTextRatings() {		 
		return productModel.getProdPageTextRatings();
	}

	
	public Html getProductAbout() {		 
		return productModel.getProductAbout();
	}

	
	public Html getProductBottomMedia() {		 
		return productModel.getProductBottomMedia();
	}

	
	public List<ProductModel> getProductBundle() {		 
		return productModel.getProductBundle();
	}

	
	public Html getProductDescription() {		 
		return productModel.getProductDescription();
	}

	
	public Html getProductDescriptionNote() {		 
		return productModel.getProductDescriptionNote();
	}

	
	public EnumProductLayout getProductLayout() {		 
		return productModel.getProductLayout();
	}

	
	public EnumProductLayout getProductLayout(EnumProductLayout defValue) {		 
		return productModel.getProductLayout(defValue);
	}

	
	public Html getProductQualityNote() {		 
		return productModel.getProductQualityNote();
	}

	
	public String getProductRating() throws FDResourceException {		 
		return productModel.getProductRating();
	}

	
	public String getProductRating(String skuCode) throws FDResourceException {		 
		return productModel.getProductRating(skuCode);
	}

	
	public EnumOrderLineRating getProductRatingEnum()
			throws FDResourceException {		 
		return productModel.getProductRatingEnum();
	}

	
	public Html getProductTerms() {		 
		return productModel.getProductTerms();
	}

	
	public Html getProductTermsMedia() {		 
		return productModel.getProductTermsMedia();
	}

	
	public float getQuantityIncrement() {		 
		return productModel.getQuantityIncrement();
	}

	
	public float getQuantityMaximum() {	 
		return productModel.getQuantityMaximum();
	}

	
	public float getQuantityMinimum() {		 
		return productModel.getQuantityMinimum();
	}

	
	public String getQuantityText() {		 
		return productModel.getQuantityText();
	}

	
	public String getQuantityTextSecondary() {		 
		return productModel.getQuantityTextSecondary();
	}

	
	public List<DomainValue> getRating() {		 
		return productModel.getRating();
	}

	
	public String getRatingProdName() {		 
		return productModel.getRatingProdName();
	}

	
	public Image getRatingRelatedImage() {		 
		return productModel.getRatingRelatedImage();
	}

	
	public Html getRecommendTable() {		 
		return productModel.getRecommendTable();
	}

	
	public List<ContentNodeModel> getRecommendedAlternatives() {		 
		return productModel.getRecommendedAlternatives();
	}

	
	public String getRedirectUrl() {		 
		return productModel.getRedirectUrl();
	}

	
	public List<Recipe> getRelatedRecipes() {		 
		return productModel.getRelatedRecipes();
	}

	
	public Image getRolloverImage() {		 
		return productModel.getRolloverImage();
	}

	
	public Html getSalesUnitDescription() {		 
		return productModel.getSalesUnitDescription();
	}

	
	public String getSalesUnitLabel() {		 
		return productModel.getSalesUnitLabel();
	}

	
	public String getSeafoodOrigin() {		 
		return productModel.getSeafoodOrigin();
	}

	
	public String getSeasonText() {		 
		return productModel.getSeasonText();
	}

	
	public String getSellBySalesunit() {	 
		return productModel.getSellBySalesunit();
	}

	
	public String getServingSuggestion() {		 
		return productModel.getServingSuggestion();
	}

	
	public SkuModel getSku(int idx) {		 
		return productModel.getSku(idx);
	}

	
	public SkuModel getSku(String skuCode) {		 
		return productModel.getSku(skuCode);
	}

	
	public List<String> getSkuCodes() {		 
		return productModel.getSkuCodes();
	}

	
	public List<SkuModel> getSkus() {		 
		return productModel.getSkus();
	}

	
	public ProductModel getSourceProduct() {		 
		return productModel.getSourceProduct();
	}

	
	public String getSubtitle() {		 
		return productModel.getSubtitle();
	}

	
	public String getSustainabilityRating() throws FDResourceException {		 
		return productModel.getSustainabilityRating();
	}

	
	public String getSustainabilityRating(String skuCode)
			throws FDResourceException {		 
		return productModel.getSustainabilityRating(skuCode);
	}

	
	public EnumSustainabilityRating getSustainabilityRatingEnum()
			throws FDResourceException {		 
		return productModel.getSustainabilityRatingEnum();
	}

	
	public EnumTemplateType getTemplateType() {		 
		return productModel.getTemplateType();
	}

	
	public int getTemplateType(int defaultValue) {		 
		return productModel.getTemplateType(defaultValue);
	}

	
	public Image getThumbnailImage() {		 
		return productModel.getThumbnailImage();
	}

	
	public int getTieredDealPercentage() {		 
		return productModel.getTieredDealPercentage();
	}

	
	public int getTieredDealPercentage(String skuCode) {		 
		return productModel.getTieredDealPercentage();
	}

	
	public String getTieredPrice(double savingsPercentage) {		 
		return productModel.getTieredPrice(savingsPercentage);
	}

	
	public DomainValue getUnitOfMeasure() {		 
		return productModel.getUnitOfMeasure();
	}

	
	public List<Domain> getUsageList() {		 
		return productModel.getUsageList();
	}

	
	public SkuModel getValidSkuCode(PricingContext ctx, String skuCode) {		 
		return productModel.getValidSkuCode(ctx, skuCode);
	}

	
	public List<Domain> getVariationMatrix() {		 
		return productModel.getVariationMatrix();
	}

	
	public List<Domain> getVariationOptions() {		 
		return productModel.getVariationOptions();
	}

	
	public String getWasPriceFormatted(double savingsPercentage) {		 
		return productModel.getWasPriceFormatted(savingsPercentage);
	}

	
	public List<ProductModel> getWeRecommendImage() {		 
		return productModel.getWeRecommendImage();
	}

	
	public List<ProductModel> getWeRecommendText() {		 
		return productModel.getWeRecommendText();
	}

	
	public String getWineAging() {		 
		return productModel.getWineAging();
	}

	
	public String getWineAlchoholContent() {		 
		return productModel.getWineAlchoholContent();
	}

	
	public String getWineCity() {		 
		return productModel.getWineCity();
	}

	
	public String getWineClassification() {		 
		return productModel.getWineClassification();
	}

	
	public List getWineClassifications() {		 
		return productModel.getWineClassifications();
	}

	
	public DomainValue getWineCountry() {		 
		return productModel.getWineCountry();
	}

	
	public ContentKey getWineCountryKey() {		 
		return productModel.getWineCountryKey();
	}

	
	public Set<DomainValue> getWineDomainValues() {		 
		return productModel.getWineDomainValues();
	}

	
	public String getWineFyi() {		 
		return productModel.getWineFyi();
	}

	
	public String getWineImporter() {		 
		return productModel.getWineImporter();
	}

	
	public List<DomainValue> getWineRating1() {		 
		return productModel.getWineRating1();
	}

	
	public List<DomainValue> getWineRating2() {		 
		return productModel.getWineRating2();
	}

	
	public List<DomainValue> getWineRating3() {		 
		return productModel.getWineRating3();
	}

	
	public DomainValue getWineRatingValue1() {		 
		return productModel.getWineRatingValue1();
	}

	
	public DomainValue getWineRatingValue2() {		 
		return productModel.getWineRatingValue2();
	}

	
	public DomainValue getWineRatingValue3() {		 
		return productModel.getWineRatingValue3();
	}

	
	public String getWineRegion() {		 
		return productModel.getWineRegion();
	}

	
	public Html getWineReview1() {		 
		return productModel.getWineReview1();
	}

	
	public Html getWineReview2() {		 
		return productModel.getWineReview2();
	}

	
	public Html getWineReview3() {		 
		return productModel.getWineReview3();
	}

	
	public String getWineType() {		 
		return productModel.getWineType();
	}

	
	public List<DomainValue> getWineVarietal() {		 
		return productModel.getWineVarietal();
	}

	
	public List<DomainValue> getWineVintage() {		 
		return productModel.getWineVintage();
	}

	
	public List<CategoryModel> getYmalCategories() {		 
		return productModel.getYmalCategories();
	}

	
	public List<ProductModel> getYmalProducts() {		 
		return productModel.getYmalProducts();
	}

	
	public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus) {		 
		return productModel.getYmalProducts(removeSkus);
	}

	
	public List<Recipe> getYmalRecipes() {		 
		return productModel.getYmalRecipes();
	}

	
	public List<ContentNodeModel> getYmals() {		 
		return productModel.getYmals();
	}

	
	public Image getZoomImage() {		 
		return productModel.getZoomImage();
	}

	
	public boolean hasComponentGroups() {		 
		return productModel.hasComponentGroups();
	}

	
	public boolean hasTerms() {		 
		return productModel.hasTerms();
	}

	
	public boolean hasWineOtherRatings() {		 
		return productModel.hasWineOtherRatings();
	}

	
	public boolean isAutoconfigurable() {		 
		return productModel.isAutoconfigurable();
	}

	
	public boolean isBackInStock() {		 
		return productModel.isBackInStock();
	}

	
	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {		 
		return productModel.isCharacteristicsComponentsAvailable(config);
	}

	
	public boolean isDisplayableBasedOnCms() {		 
		return productModel.isDisplayableBasedOnCms();
	}

	
	public boolean isExcludedRecommendation() {		 
		return productModel.isExcludedRecommendation();
	}

	
	public boolean isFrozen() {		 
		return productModel.isFrozen();
	}

	
	public boolean isFullyAvailable() {		 
		return productModel.isFullyAvailable();
	}

	
	public boolean isGrocery() {		 
		return productModel.isGrocery();
	}

	
	public boolean isHasPartiallyFrozen() {		 
		return productModel.isHasPartiallyFrozen();
	}

	
	public boolean isHasSalesUnitDescription() {		 
		return productModel.isHasSalesUnitDescription();
	}

	
	public boolean isHideIphone() {		 
		return productModel.isHideIphone();
	}

	
	public boolean isHideWineRatingPricing() {		 
		return productModel.isHideWineRatingPricing();
	}

	
	public boolean isInPrimaryHome() {		 
		return productModel.isInPrimaryHome();
	}

	
	public boolean isIncrementMaxEnforce() {		 
		return productModel.isIncrementMaxEnforce();
	}

	
	public boolean isInvisible() {		 
		return productModel.isInvisible();
	}

	
	public boolean isNew() {		 
		return productModel.isNew();
	}

	
	public boolean isNotSearchable() {		 
		return productModel.isNotSearchable();
	}

	
	public boolean isNutritionMultiple() {		 
		return productModel.isNutritionMultiple();
	}

	
	public boolean isPerishable() {		 
		return productModel.isPerishable();
	}

	
	public boolean isPlatter() {		 
		return productModel.isPlatter();
	}

	
	public boolean isPreconfigured() {		 
		return productModel.isPreconfigured();
	}

	
	public boolean isQualifiedForPromotions() throws FDResourceException {		 
		return productModel.isQualifiedForPromotions();
	}

	
	public boolean isShowSalesUnitImage() {		 
		return productModel.isShowSalesUnitImage();
	}

	
	public boolean isShowTopTenImage() {		 
		return productModel.isShowTopTenImage();
	}

	
	public boolean isShowWineRatings() {		 
		return productModel.isShowWineRatings();
	}

	
	public boolean isSoldBySalesUnits() {		 
		return productModel.isSoldBySalesUnits();
	}

	
	public boolean isTemporaryUnavailableOrAvailable() {		 
		return productModel.isTemporaryUnavailableOrAvailable();
	}

	
	public boolean showDefaultSustainabilityRating() {		 
		return productModel.showDefaultSustainabilityRating();
	}

	
	public Date getEarliestAvailability() {		 
		return productModel.getEarliestAvailability();
	}

	

	
	public boolean isAvailableWithin(int days) {		 
		return productModel.isAvailableWithin(days);
	}

	
	public boolean isDiscontinued() {		 
		return productModel.isDiscontinued();
	}

	
	public boolean isOutOfSeason() {		 
		return productModel.isOutOfSeason();
	}

	
	public boolean isTempUnavailable() {		 
		return productModel.isTempUnavailable();
	}

	
	public boolean isUnavailable() {		 
		return productModel.isUnavailable();
	}

	
	public List<ProductModel> getRelatedProducts() {		 
		return productModel.getRelatedProducts();
	}

	
	public String getYmalHeader() {		 
		return productModel.getYmalHeader();
	}

	
	public void resetActiveYmalSetSession() {		 
		productModel.resetActiveYmalSetSession();
	}

	
	public AttributeDefI getAttributeDef(String name) {		 
		return productModel.getAttributeDef(name);
	}

	
	public Object getCmsAttributeValue(String name) {		 
		return productModel.getCmsAttributeValue(name);
	}

	
	public ContentKey getContentKey() {		 
		return productModel.getContentKey();
	}

	
	public String getContentName() {		 
		return productModel.getContentName();
	}

	
	public String getContentType() {		 
		return productModel.getContentType();
	}

	
	public Html getEditorial() {		 
		return productModel.getEditorial();
	}

	
	public String getEditorialTitle() {		 
		return productModel.getEditorialTitle();
	}

	
	public Object getNotInheritedAttributeValue(String name) {		 
		return productModel.getNotInheritedAttributeValue(name);
	}

	
	public String getParentId() {
		return productModel.getParentId();
	}

	
	public Collection<ContentKey> getParentKeys() {		 
		return productModel.getParentKeys();
	}

	
	public ContentNodeModel getParentNode() {		 
		return productModel.getParentNode();
	}

	
	public String getPath() {		 
		return productModel.getPath();
	}

	
	public Image getSideNavImage() {		 
		return productModel.getSideNavImage();
	}

	
	public boolean hasParentWithName(String[] contentNames) {		 
		return productModel.hasParentWithName(contentNames);
	}

	
	public boolean isHidden() {		 
		return productModel.isHidden();
	}

	
	public boolean isOrphan() {
		return productModel.isOrphan();
	}

	
	public boolean isSearchable() {		 
		return productModel.isSearchable();
	}

	
	public int getPriority() {		 
		return productModel.getPriority();
	}

	
	public YmalSetSource getParentYmalSetSource() {		 
		return productModel.getParentYmalSetSource();
	}

	
	public List<YmalSet> getYmalSets() {		 
		return productModel.getYmalSets();
	}

	
	public boolean hasActiveYmalSets() {		 
		return productModel.hasActiveYmalSets();
	}

	

	
    public Object clone() {		
		try {
			ProductModelPromotionAdapter pm= (ProductModelPromotionAdapter)super.clone();
			ProductModel prodModel = pm.getProductModel();
			pm.setProductModel((ProductModel)prodModel.clone());
			return pm;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
	}

	
	public String toString() {		
		return productModel.toString();
	}
	
	
    public boolean isExcludedForEBTPayment(){
		return productModel.isExcludedForEBTPayment();
	}

	
	public boolean isDisabledRecommendations() {
		return productModel.isDisabledRecommendations();
	}

	
	public boolean isRetainOriginalSkuOrder() {
		return productModel.isRetainOriginalSkuOrder();
	}

	
	public EnumOrderLineRating getProductRatingEnum(String skuCode) throws FDResourceException {
		return productModel.getProductRatingEnum(skuCode);
	}

	
	public Image getPackageImage() {
		return productModel.getPackageImage();
	}

	
	public List<TagModel> getTags() {
		return productModel.getTags();
	}

	
	public Set<TagModel> getAllTags() {
		return productModel.getAllTags();
	}

	
	
	public Set<DomainValue> getAllDomainValues() {
		return productModel.getAllDomainValues();
	}
	/**
	 * @see {@link ProductModel#getUpSellProducts()}
	 */
	
	public List<ProductModel> getUpSellProducts() {
		return productModel.getUpSellProducts();
	}

	/**
	 * @see {@link ProductModel#getCrossSellProducts()}
	 */
	
	public List<ProductModel> getCrossSellProducts() {
		return productModel.getCrossSellProducts();
	}

	
	public String getBrowseRecommenderType(){
		return productModel.getBrowseRecommenderType();
	}

	/**
	 * @see {@link ProductModel#getHeatRating()}
	 */
	
	public int getHeatRating() {
		return productModel.getHeatRating();
	}

	
	public Image getJumboImage() {
		return productModel.getJumboImage();
	}

	
	public Image getItemImage() {
		return productModel.getItemImage();
	}

	
	public Image getExtraImage() {
		return productModel.getExtraImage();
	}
	
	
	public boolean isDisableAtpFailureRecommendation(){
		return productModel.isDisableAtpFailureRecommendation();
	}

	
	public EnumProductLayout getSpecialLayout() {
		return productModel.getSpecialLayout();
	}
	
	
	public List<ProductModel> getCompleteTheMeal() {
		return productModel.getCompleteTheMeal();
	}

    
    public List<ProductModel> getIncludeProducts() {
        return productModel.getIncludeProducts();
    }

	
	public String getPageTitle() {
		return productModel.getPageTitle();
	}

	
	public String getSEOMetaDescription() {
		return productModel.getSEOMetaDescription();
	}

	
	public String getPairItHeading() {
		return productModel.getPairItHeading();
	}

	
	public String getPairItText() {
		return productModel.getPairItText();
	}

    
    public void setParentNode(ContentNodeModel parentNode) {
        productModel.setParentNode(parentNode);
    }

	public String getClickBeacon() {
		return clickBeacon;
	}

	public void setClickBeacon(String clickBeacon) {
		this.clickBeacon = clickBeacon;
	}

	public String getImpBeacon() {
		return impBeacon;
	}

	public void setImpBeacon(String impBeacon) {
		this.impBeacon = impBeacon;
	}
}
