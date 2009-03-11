package com.freshdirect.fdstore.content;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.DayOfWeekSet;

public abstract class ProxyProduct extends AbstractProductModelImpl {

	public ProxyProduct(ContentKey key) {
		super(key);
	}

	/**
	 * @return the ProductModel associated with this configuration
	 */
	public abstract ProductModel getProduct();

	public AttributeI getCmsAttribute(String name) {
		AttributeI   a = super.getCmsAttribute(name);
		if (a != null) {
			return a;
		}
		ProductModel p = getProduct();
		if (p != null) {
			return p.getCmsAttribute(name);
		}
		
		return null;
	}

	//
	// proxy pass-thru methods
	// 

	public String getAka() {
		return getProduct().getAka();
	}

	public Html getEditorial() {
		return getProduct().getEditorial();
	}

	public float getQuantityMinimum() {
		return getProduct().getQuantityMinimum();
	}

	public float getQuantityMaximum() {
		return getProduct().getQuantityMaximum();
	}

	public float getQuantityIncrement() {
		return getProduct().getQuantityIncrement();
	}

	public boolean enforceQuantityMax() {
		return getProduct().enforceQuantityMax();
	}

	public boolean isInvisible() {
		return getAttribute("INVISIBLE", false);
	}

	public List getAlsoSoldAsRefs() {
		return getProduct().getAlsoSoldAs();
	}

	public SkuModel getSku(int idx) {
		return getProduct().getSku(idx);
	}

	public SkuModel getSku(String skuCode) {
		return getProduct().getSku(skuCode);
	}

	public List getSkus() {
		return getProduct().getSkus();
	}

	public List getSkuCodes() {
		return getProduct().getSkuCodes();
	}

	public List getBrands() {
		return getProduct().getBrands();
	}

	public boolean isPerishable() {
		return getProduct().isPerishable();
	}

	public boolean isFrozen() {
		return getProduct().isFrozen();
	}

	public boolean isGrocery() {
		return getProduct().isGrocery();
	}
	
	public boolean isSoldBySalesUnits() {
		return getProduct().isSoldBySalesUnits();
	}

	public boolean isQualifiedForPromotions() throws FDResourceException {
		return getProduct().isQualifiedForPromotions();
	}

	public boolean isPlatter() {
		return getProduct().isPlatter();
	}

	public DayOfWeekSet getBlockedDays() {
		return getProduct().getBlockedDays();
	}

	public SkuModel getDefaultSku() {
		return getProduct().getDefaultSku();
	}

	public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException {
		return getProduct().getCommonNutritionInfo(type);
	}

	public String getPrimaryBrandName() {
		return getProduct().getPrimaryBrandName();
	}

	public String getPrimaryBrandName(String productName) {
		return getProduct().getPrimaryBrandName(productName);
	}

	public String getSizeDescription() throws FDResourceException {
		return getProduct().getSizeDescription();
	}

	public String getKosherSymbol() throws FDResourceException {
		return getProduct().getKosherSymbol();
	}

	public String getKosherType() throws FDResourceException {
		return getProduct().getKosherType();
	}

	public boolean isKosherProductionItem() throws FDResourceException {
		return getProduct().isKosherProductionItem();
	}

	public int getKosherPriority() throws FDResourceException {
		return getProduct().getKosherPriority();
	}

	public boolean hasComponentGroups() {
		return getProduct().hasComponentGroups();
	}

	public boolean isUnavailable() {
		if (getProduct().isUnavailable()) {
			return true;
		}

		return false;
	}

	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
		return getProduct().isCharacteristicsComponentsAvailable(config);
	}

	public String getAltText() {
		return getProduct().getAltText();
	}

	public String getEditorialTitle() {
		return getProduct().getEditorialTitle();
	}

	public String getKeywords() {
		return getProduct().getKeywords();
	}

	public List getAssocEditorial() {
		return getProduct().getAssocEditorial();
	}

	public boolean isSearchable() {
		return getProduct().isSearchable();
	}

	public boolean isHidden() {
		return getProduct().isHidden();
	}

	public String getHideUrl() {
		return getProduct().getHideUrl();
	}

	public String getPath() {
		return getProduct().getPath();
	}

	//
	// availability
	//

	public boolean isDiscontinued() {
		return getProduct().isDiscontinued();
	}

	public boolean isTempUnavailable() {
		return getProduct().isTempUnavailable();
	}

	public boolean isOutOfSeason() {
		return getProduct().isOutOfSeason();
	}

    /**
     *  Tell if the product is available within the specified number
     *  of days.
     *  
     * @param days the number of days to look at, '1' means today,
     *        '2' means by tomorrow, etc.
     * @return if the product is available within the specified number
     *         of days.
     */    
	public boolean isAvailableWithin(int days) {
		return getProduct().isAvailableWithin(days);
	}

	public Date getEarliestAvailability() {
		return getProduct().getEarliestAvailability();
	}

	public String getSubtitle() {
		return getAttribute("SUBTITLE", "");
	}

	public List getDisplayableBrands() {
		return getProduct().getDisplayableBrands(1);
	}

	public List getDisplayableBrands(int numOfBrands) {
		return getProduct().getDisplayableBrands(numOfBrands);
	}


	public EnumLayoutType getLayout() {
		return getProduct().getLayout();
	}

	public EnumTemplateType getTemplateType() {
		return getProduct().getTemplateType();
	}

	public String getRedirectUrl() {
		return getProduct().getRedirectUrl();
	}

	public String getSellBySalesunit() {
		return getProduct().getSellBySalesunit();
	}

	public String getSalesUnitLabel() {
		return getProduct().getSalesUnitLabel();
	}

	public String getQuantityText() {
		return getProduct().getQuantityText();
	}

	public String getQuantityTextSecondary() {
		return getProduct().getQuantityTextSecondary();
	}

	public String getServingSuggestion() {
		return getProduct().getServingSuggestion();
	}

	public String getSeasonText() {
		return getProduct().getSeasonText();
	}

	public String getWineFyi() {
		return getProduct().getWineFyi();
	}

	public String getWineRegion() {
		return getProduct().getWineRegion();
	}

	public String getSeafoodOrigin() {
		return getProduct().getSeafoodOrigin();
	}

	public boolean isShowSalesUnitImage() {
		return getProduct().isShowSalesUnitImage();
	}

	public boolean isNutritionMultiple() {
		return getProduct().isNutritionMultiple();
	}

	public boolean isNotSearchable() {
		return getProduct().isNotSearchable();
	}

	public double getContainerWeightHalfPint() {
		return getProduct().getContainerWeightHalfPint();
	}

	public double getContainerWeightPint() {
		return getProduct().getContainerWeightPint();
	}

	public double getContainerWeightQuart() {
		return getProduct().getContainerWeightQuart();
	}

	public boolean isIncrementMaxEnforce() {
		return getProduct().isIncrementMaxEnforce();
	}

	public String getProdPageRatings() {
		return getProduct().getProdPageRatings();
	}

	public String getProdPageTextRatings() {
		return getProduct().getProdPageTextRatings();
	}

	public String getRatingProdName() {
		return getProduct().getRatingProdName();
	}

	public List getYouMightAlsoLike() {
		return getProduct().getYouMightAlsoLike();
	}

	public YmalSet getActiveYmalSet() {
		return getProduct().getActiveYmalSet();
	}

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 *  @see #getYmalProducts(Set)
	 */
	public List getYmalProducts() {
		return getProduct().getYmalProducts();
	}
	
	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @param removeSkus a set of FDSku objects, for which correspoding
	 *         products need to be removed from the final list of YMALs.
	 *         this might be null, in which case it has no effect. 
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 */
	public List getYmalProducts(Set removeSkus) {
		return getProduct().getYmalProducts(removeSkus);
	}
	
	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 */
	public List getYmalCategories() {
		return getProduct().getYmalCategories();
	}
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 */
	public List getYmalRecipes() {
		return getProduct().getYmalRecipes();
	}

	public List getWeRecommendText() {
		return getProduct().getWeRecommendText();
	}

	public List getWeRecommendImage() {
		return getProduct().getWeRecommendImage();
	}

	public List getRelatedProducts() {
		return getProduct().getYouMightAlsoLike();
	}

	public List getRelatedRecipes() {
		return getProduct().getRelatedRecipes();
	}
	
	public List getProductBundle() {
		return getProduct().getProductBundle();
	}

	public List getHowtoCookitFolders() {
		return getProduct().getHowtoCookitFolders();
	}

	public CategoryModel getPrimaryHome() {
		return getProduct().getPrimaryHome();
	}

	public SkuModel getPreferredSku() {
		return getProduct().getPreferredSku();
	}

	public List getRating() {
		return getProduct().getRating();
	}

	public List getUsageList() {
		return getProduct().getUsageList();
	}

	public DomainValue getUnitOfMeasure() {
		return getProduct().getUnitOfMeasure();
	}

	public List getVariationMatrix() {
		return getProduct().getVariationMatrix();
	}

	public List getVariationOptions() {
		return getProduct().getVariationOptions();
	}

	public DomainValue getWineCountry() {
		return getProduct().getWineCountry();
	}

	public Image getProdImage() {
		return getProduct().getProdImage();
	}

	public Image getFeatureImage() {
		return getProduct().getFeatureImage();
	}

	public Image getRatingRelatedImage() {
		return getProduct().getRatingRelatedImage();
	}

	public Image getAlternateImage() {
		return getProduct().getAlternateImage();
	}

	public Image getDescriptiveImage() {
		return getProduct().getDescriptiveImage();
	}

	public Image getRolloverImage() {
		return getProduct().getRolloverImage();
	}

	public Html getProductAbout() {
		return getProduct().getProductAbout();
	}

	public Html getRecommendTable() {
		return getProduct().getRecommendTable();
	}

	public Html getProductQualityNote() {
		return getProduct().getProductQualityNote();
	}

	public Html getProductDescriptionNote() {
		return getProduct().getProductDescriptionNote();
	}

	public Html getFreshTips() {
		return getProduct().getFreshTips();
	}

	public Html getDonenessGuide() {
		return getProduct().getDonenessGuide();
	}

	public Html getFddefFrenching() {
		return getProduct().getFddefFrenching();
	}

	public Html getFddefGrade() {
		return getProduct().getFddefGrade();
	}

	public Html getFddefRipeness() {
		return getProduct().getFddefRipeness();
	}

	public Html getSalesUnitDescription() {
		return getProduct().getSalesUnitDescription();
	}

	public Html getPartallyFrozen() {
		return getProduct().getPartallyFrozen();
	}

	public List getComponentGroups() {
		return getProduct().getComponentGroups();
	}

	public Html getProductTermsMedia() {
		return getProduct().getProductTermsMedia();
	}
	
	// new Wine Store changes
	
	public String getWineClassification(){
	     return getProduct().getWineClassification();
	}
	
	public String getWineImporter(){
		return getProduct().getWineImporter();
	}
	
	public String getWineAlchoholContent(){
		return getProduct().getWineAlchoholContent();
	}
	
	public String getWineAging(){
		return getProduct().getWineAging();	
	}
	
	public String getWineCity() {
		// TODO Auto-generated method stub
		return getProduct().getWineCity();
	}

	
	public String getWineType(){
		return getProduct().getWineType();
	}
	
	public List getNewWineType(){
		return getProduct().getNewWineType();
	}
	
	public List getWineVintage(){
		return getProduct().getWineVintage();
	}
	
	public List getNewWineRegion(){
		return getProduct().getNewWineRegion();
	}
	
	public List getWineVarietal() {
		return getProduct().getWineVarietal();
	}
	
	public List getWineRating1(){
		return getProduct().getWineRating1();
	}
	
	public List getWineRating2(){
		return getProduct().getWineRating2();
	}
	
	public List getWineRating3(){
		return getProduct().getWineRating3();
	}
	
	public Html getWineReview1(){
		return getProduct().getWineReview1();	
	}
	
	public Html getWineReview2(){
		return getProduct().getWineReview2();
	}
	
	
	public Html getWineReview3(){
		return getProduct().getWineReview3();	
	}
	
	public Html getProductBottomMedia(){
		return getProduct().getProductBottomMedia();
	}
	
	public CategoryModel getPerfectPair(){
		return getProduct().getPerfectPair();	
	}		
	public List getWineClassifications(){
		return getProduct().getWineClassifications();
	}
	
	public String getProductRating() throws FDResourceException{
		return getProduct().getProductRating();
	}
	
	public String getCOOLText() throws FDResourceException{
		return getProduct().getCOOLText();
	}
}
