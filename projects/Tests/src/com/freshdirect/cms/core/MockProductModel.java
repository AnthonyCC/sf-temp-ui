/**
 * 
 */
package com.freshdirect.cms.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.EnumTemplateType;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.YmalSet;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * @author zsombor
 *
 */
public class MockProductModel extends MockContentNodeModel implements ProductModel, Cloneable {

    private List<SkuModel> skuModels;

    /**
     * 
     */
    public MockProductModel() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param key
     */
    public MockProductModel(ContentKey key) {
        super(key);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param type
     * @param id
     * @throws InvalidContentKeyException
     */
    public MockProductModel(ContentType type, String id) throws InvalidContentKeyException {
        super(type, id);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#enforceQuantityMax()
     */
    @Override
    public boolean enforceQuantityMax() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAboutPriceFormatted(double)
     */
    @Override
    public String getAboutPriceFormatted(double savingsPercentage) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getActiveYmalSet()
     */
    @Override
    public YmalSet getActiveYmalSet() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAka()
     */
    @Override
    public String getAka() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAlsoSoldAs(int)
     */
    @Override
    public ProductModel getAlsoSoldAs(int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAlsoSoldAs()
     */
    @Override
    public List getAlsoSoldAs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAlsoSoldAsName()
     */
    @Override
    public String getAlsoSoldAsName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAlsoSoldAsRefs()
     */
    @Override
    public List getAlsoSoldAsRefs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAlternateImage()
     */
    @Override
    public Image getAlternateImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getAutoconfiguration()
     */
    @Override
    public FDConfigurableI getAutoconfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getBlockedDays()
     */
    @Override
    public DayOfWeekSet getBlockedDays() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getBrands()
     */
    @Override
    public List getBrands() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getCategoryImage()
     */
    @Override
    public Image getCategoryImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getCommonNutritionInfo(com.freshdirect.content.nutrition.ErpNutritionInfoType)
     */
    @Override
    public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getComponentGroups()
     */
    @Override
    public List getComponentGroups() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getConfirmImage()
     */
    @Override
    public Image getConfirmImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getContainerWeightHalfPint()
     */
    @Override
    public double getContainerWeightHalfPint() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getContainerWeightPint()
     */
    @Override
    public double getContainerWeightPint() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getContainerWeightQuart()
     */
    @Override
    public double getContainerWeightQuart() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getCountryOfOrigin()
     */
    @Override
    public List getCountryOfOrigin() throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDealPercentage()
     */
    @Override
    public int getDealPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDealPercentage(java.lang.String)
     */
    @Override
    public int getDealPercentage(String skuCode) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDefaultPrice()
     */
    @Override
    public String getDefaultPrice() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDefaultSku()
     */
    @Override
    public SkuModel getDefaultSku() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDepartment()
     */
    @Override
    public DepartmentModel getDepartment() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDescriptiveImage()
     */
    @Override
    public Image getDescriptiveImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDetailImage()
     */
    @Override
    public Image getDetailImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDisplayableBrands()
     */
    @Override
    public List getDisplayableBrands() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDisplayableBrands(int)
     */
    @Override
    public List getDisplayableBrands(int numberOfBrands) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getDonenessGuide()
     */
    @Override
    public Html getDonenessGuide() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getExpertWeight()
     */
    @Override
    public int getExpertWeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getFddefFrenching()
     */
    @Override
    public Html getFddefFrenching() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getFddefGrade()
     */
    @Override
    public Html getFddefGrade() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getFddefRipeness()
     */
    @Override
    public Html getFddefRipeness() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getFeatureImage()
     */
    @Override
    public Image getFeatureImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getFreshTips()
     */
    @Override
    public Html getFreshTips() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getHighestDealPercentage()
     */
    @Override
    public int getHighestDealPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getHighestDealPercentage(java.lang.String)
     */
    @Override
    public int getHighestDealPercentage(String skuCode) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getHowtoCookitFolders()
     */
    @Override
    public List getHowtoCookitFolders() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getKosherPriority()
     */
    @Override
    public int getKosherPriority() throws FDResourceException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getKosherSymbol()
     */
    @Override
    public String getKosherSymbol() throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getKosherType()
     */
    @Override
    public String getKosherType() throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getLayout()
     */
    @Override
    public EnumLayoutType getLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getNewWineRegion()
     */
    @Override
    public List getNewWineRegion() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getNewWineType()
     */
    @Override
    public List getNewWineType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPackageDescription()
     */
    @Override
    public String getPackageDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPartallyFrozen()
     */
    @Override
    public Html getPartallyFrozen() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPerfectPair()
     */
    @Override
    public CategoryModel getPerfectPair() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPreferredSku()
     */
    @Override
    public SkuModel getPreferredSku() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPriceFormatted(double)
     */
    @Override
    public String getPriceFormatted(double savingsPercentage) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPrimaryBrandName()
     */
    @Override
    public String getPrimaryBrandName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPrimaryBrandName(java.lang.String)
     */
    @Override
    public String getPrimaryBrandName(String productName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getPrimaryHome()
     */
    @Override
    public CategoryModel getPrimaryHome() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProdImage()
     */
    @Override
    public Image getProdImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProdPageRatings()
     */
    @Override
    public String getProdPageRatings() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProdPageTextRatings()
     */
    @Override
    public String getProdPageTextRatings() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductAbout()
     */
    @Override
    public Html getProductAbout() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductBottomMedia()
     */
    @Override
    public Html getProductBottomMedia() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductBundle()
     */
    @Override
    public List getProductBundle() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductDescription()
     */
    @Override
    public Html getProductDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductDescriptionNote()
     */
    @Override
    public Html getProductDescriptionNote() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductLayout()
     */
    @Override
    public EnumProductLayout getProductLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductQualityNote()
     */
    @Override
    public Html getProductQualityNote() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductRating()
     */
    @Override
    public String getProductRating() throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public EnumOrderLineRating getProductRatingEnum()
    		throws FDResourceException {
    	// TODO Auto-generated method stub
    	return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductRef()
     */
    @Override
    public ProductRef getProductRef() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductTerms()
     */
    @Override
    public Html getProductTerms() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getProductTermsMedia()
     */
    @Override
    public Html getProductTermsMedia() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getQuantityIncrement()
     */
    @Override
    public float getQuantityIncrement() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getQuantityMaximum()
     */
    @Override
    public float getQuantityMaximum() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getQuantityMinimum()
     */
    @Override
    public float getQuantityMinimum() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getQuantityText()
     */
    @Override
    public String getQuantityText() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getQuantityTextSecondary()
     */
    @Override
    public String getQuantityTextSecondary() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRating()
     */
    @Override
    public List getRating() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRatingProdName()
     */
    @Override
    public String getRatingProdName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRatingRelatedImage()
     */
    @Override
    public Image getRatingRelatedImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRecommendTable()
     */
    @Override
    public Html getRecommendTable() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRecommendedAlternatives()
     */
    @Override
    public List getRecommendedAlternatives() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRedirectUrl()
     */
    @Override
    public String getRedirectUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRelatedRecipes()
     */
    @Override
    public List getRelatedRecipes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getRolloverImage()
     */
    @Override
    public Image getRolloverImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSalesUnitDescription()
     */
    @Override
    public Html getSalesUnitDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSalesUnitLabel()
     */
    @Override
    public String getSalesUnitLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSeafoodOrigin()
     */
    @Override
    public String getSeafoodOrigin() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSeasonText()
     */
    @Override
    public String getSeasonText() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSellBySalesunit()
     */
    @Override
    public String getSellBySalesunit() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getServingSuggestion()
     */
    @Override
    public String getServingSuggestion() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSizeDescription()
     */
    @Override
    public String getSizeDescription() throws FDResourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSku(int)
     */
    @Override
    public SkuModel getSku(int idx) {
        return getSkus().get(idx);
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSku(java.lang.String)
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

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSkuCodes()
     */
    @Override
    public List<String> getSkuCodes() {
        List<String> result = new ArrayList<String>();
        for (SkuModel s : getSkus()) {
            result.add(s.getSkuCode());
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSkus()
     */
    @Override
    public List<SkuModel> getSkus() {
        return skuModels;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSourceProduct()
     */
    @Override
    public ProductModel getSourceProduct() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getSubtitle()
     */
    @Override
    public String getSubtitle() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getTemplateType()
     */
    @Override
    public EnumTemplateType getTemplateType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getThumbnailImage()
     */
    @Override
    public Image getThumbnailImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getTieredDealPercentage()
     */
    @Override
    public int getTieredDealPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getTieredDealPercentage(java.lang.String)
     */
    @Override
    public int getTieredDealPercentage(String skuCode) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getTieredPrice(double)
     */
    @Override
    public String getTieredPrice(double savingsPercentage) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getUnitOfMeasure()
     */
    @Override
    public DomainValue getUnitOfMeasure() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getUsageList()
     */
    @Override
    public List getUsageList() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getVariationMatrix()
     */
    @Override
    public List getVariationMatrix() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getVariationOptions()
     */
    @Override
    public List getVariationOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWasPriceFormatted(double)
     */
    @Override
    public String getWasPriceFormatted(double savingsPercentage) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWeRecommendImage()
     */
    @Override
    public List getWeRecommendImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWeRecommendText()
     */
    @Override
    public List getWeRecommendText() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineAging()
     */
    @Override
    public String getWineAging() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineAlchoholContent()
     */
    @Override
    public String getWineAlchoholContent() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineCity()
     */
    @Override
    public String getWineCity() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineClassification()
     */
    @Override
    public String getWineClassification() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineClassifications()
     */
    @Override
    public List getWineClassifications() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineCountry()
     */
    @Override
    public DomainValue getWineCountry() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineFyi()
     */
    @Override
    public String getWineFyi() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineImporter()
     */
    @Override
    public String getWineImporter() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineRating1()
     */
    @Override
    public List getWineRating1() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineRating2()
     */
    @Override
    public List getWineRating2() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineRating3()
     */
    @Override
    public List getWineRating3() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineRegion()
     */
    @Override
    public String getWineRegion() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineReview1()
     */
    @Override
    public Html getWineReview1() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineReview2()
     */
    @Override
    public Html getWineReview2() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineReview3()
     */
    @Override
    public Html getWineReview3() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineType()
     */
    @Override
    public String getWineType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineVarietal()
     */
    @Override
    public List getWineVarietal() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getWineVintage()
     */
    @Override
    public List getWineVintage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getYmalCategories()
     */
    @Override
    public List getYmalCategories() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getYmalProducts()
     */
    @Override
    public List getYmalProducts() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getYmalProducts(java.util.Set)
     */
    @Override
    public List getYmalProducts(Set removeSkus) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getYmalRecipes()
     */
    @Override
    public List getYmalRecipes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getYmals()
     */
    @Override
    public List getYmals() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#getZoomImage()
     */
    @Override
    public Image getZoomImage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#hasComponentGroups()
     */
    @Override
    public boolean hasComponentGroups() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#hasTerms()
     */
    @Override
    public boolean hasTerms() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isAutoconfigurable()
     */
    @Override
    public boolean isAutoconfigurable() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isCharacteristicsComponentsAvailable(com.freshdirect.fdstore.FDConfigurableI)
     */
    @Override
    public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isDisplayableBasedOnCms()
     */
    @Override
    public boolean isDisplayableBasedOnCms() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isExcludedRecommendation()
     */
    @Override
    public boolean isExcludedRecommendation() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isFrozen()
     */
    @Override
    public boolean isFrozen() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isGrocery()
     */
    @Override
    public boolean isGrocery() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isIncrementMaxEnforce()
     */
    @Override
    public boolean isIncrementMaxEnforce() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isInvisible()
     */
    @Override
    public boolean isInvisible() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isKosherProductionItem()
     */
    @Override
    public boolean isKosherProductionItem() throws FDResourceException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isNew()
     */
    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isNotSearchable()
     */
    @Override
    public boolean isNotSearchable() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isNutritionMultiple()
     */
    @Override
    public boolean isNutritionMultiple() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isPerishable()
     */
    @Override
    public boolean isPerishable() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isPlatter()
     */
    @Override
    public boolean isPlatter() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isPreconfigured()
     */
    @Override
    public boolean isPreconfigured() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isQualifiedForPromotions()
     */
    @Override
    public boolean isQualifiedForPromotions() throws FDResourceException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isShowSalesUnitImage()
     */
    @Override
    public boolean isShowSalesUnitImage() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isShowTopTenImage()
     */
    @Override
    public boolean isShowTopTenImage() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.ProductModel#isSoldBySalesUnits()
     */
    @Override
    public boolean isSoldBySalesUnits() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#getEarliestAvailability()
     */
    @Override
    public Date getEarliestAvailability() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#isAvailableWithin(int)
     */
    @Override
    public boolean isAvailableWithin(int days) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#isDiscontinued()
     */
    @Override
    public boolean isDiscontinued() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#isOutOfSeason()
     */
    @Override
    public boolean isOutOfSeason() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#isTempUnavailable()
     */
    @Override
    public boolean isTempUnavailable() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.AvailabilityI#isUnavailable()
     */
    @Override
    public boolean isUnavailable() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.YmalSource#getRelatedProducts()
     */
    @Override
    public List getRelatedProducts() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.YmalSource#getYmalHeader()
     */
    @Override
    public String getYmalHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.fdstore.content.YmalSource#resetActiveYmalSetSession()
     */
    @Override
    public void resetActiveYmalSetSession() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cne) {
            throw new RuntimeException("Cloning attempted:"+cne.getMessage(), cne);
        }
    }
    
    public void setSkuModels(List<SkuModel> skuModels) {
        this.skuModels = skuModels;
    }
    
    public MockProductModel addSku(SkuModel sku) {
        if (skuModels==null) {
            skuModels = new ArrayList();
        }
        skuModels.add(sku);
        return this;
    }

	@Override
	public List getGiftcardType() {
		return null;
	}
}
