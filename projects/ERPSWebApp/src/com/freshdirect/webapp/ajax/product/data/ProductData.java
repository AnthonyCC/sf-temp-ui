package com.freshdirect.webapp.ajax.product.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.holidaymealbundle.data.HolidayMealBundleContainer;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Variation;

public class ProductData extends BasicProductData implements SkuData {

    private static final long serialVersionUID = 4609027472414542229L;

    /**
     * Sku code
     */
    protected String skuCode;

    /**
     * Category content-ID
     */
    protected String catId;
    
    /**
     * Department content-ID
     */
    protected String departmentId;

    /**
     * Does it need a customize button with a product config popup?
     */
    protected boolean customizePopup = false;

    /**
     * Is product available for sale?
     */
    protected boolean available;

    /**
     * Is product discontinued for sale?
     */
    protected boolean isDiscontinued;
    
    private boolean isOutOfSeason;

    /**
     * This flag signals that product data is rather incomplete Expect that most of them are not populated It might only be turned on when populating products / skus without
     * backing data in ERPS
     * 
     * @ticket APPDEV-3918
     */
    protected boolean incomplete;

    /**
     * Amount for numeric quantity type, containing min/max/step, and the actual value.
     */
    protected Quantity quantity;

    protected boolean maxProductSampleQuantityReached;
    /**
     * Amount for sales-unit enum type, containing the full enum, plus the selected value.
     */
    protected List<SalesUnit> salesUnit;

    // APPDEV-4123
    /**
     * showMsg will display the error message if the configuration does not match.
     */
    protected boolean showMsg;

    public boolean isShowMsg() {
        return showMsg;
    }

    public void setShowMsg(boolean showMsg) {
        this.showMsg = showMsg;
    }

    /**
     * deal
     */
    protected int deal;

    /**
     * burst image type: fav, new, back, free
     */
    protected String badge;

    protected boolean newProduct;

    // Ratings
    protected int wineRating;
    protected double customerRating;
    protected int customerRatingReviewCount;
    protected int expertRating;
    protected String sustainabilityRating;
    protected boolean showRatings;
    /**
     * Heat rating value between 0 and 5. -1 means invalid value.
     */
    protected int heatRating = -1;

    /**
     * Heat Scale Legend media include: "/media/editorial/brands/fd_heatscale/fd_heatscale.html"
     */
    protected String heatRatingScale = "";

    // scores
    protected double frequency = 0;
    protected double recency = 0;

    // PRICING

    // Pricing data
    protected double price;
    protected String scaleUnit;
    protected String taxAndDeposit;
    protected String aboutPriceText;

    // APPDEV-3438
    private String utPrice;
    private String utSalesUnit;

    // APPDEV-4357
    private String pricePerDefaultSalesUnit;
    private String dispDefaultSalesUnit;

    // TODO ???
    protected double wasPrice;

    // for subtotal calculations
    protected MaterialPrice[] availMatPrices;
    protected CharacteristicValuePrice[] cvPrices;
    protected SalesUnitRatio[] suRatios;
    protected MaterialPrice[] grpPrices;

    // OTHER ???

    // Product configuration (productselection/orderline/cartline)
    protected boolean isConfigInvalid;
    protected Map<String, String> configuration;
    protected String configDescr;
    protected String salesUnitDescrPDP;
    protected double configuredPrice;
    protected String description;
    protected String departmentDesc;

    // from Sku
    protected String label;
    protected String salesUnitLabel;
    protected boolean hasSalesUnitDescription = false;
    protected String salesUnitDescrPopup;
    protected List<Variation> variations;
    protected boolean variationDisplay;

    protected ProductData browseRecommandation;

    /**
     * Messaging - various messages
     */
    protected String msgCancellation;
    protected String msgCutoffHeader;
    protected String msgCutoffNotice;
    protected String msgDayOfWeek;
    protected String msgDayOfWeekHeader;
    protected String msgDeliveryNote;
    protected String msgLeadTime;
    protected String msgLeadTimeHeader;
    protected String msgKosherRestriction;

    // Earliest availability message (optional)
    // it contains a date in short format Fri, 02/14
    protected String msgEarliestAvailability;

    // CM related
    private String variantId;

    private String nutritionSortTitle;
    private String nutritionSortValue;
    private String nutritionServingSizeValue;

    private boolean isFeatured;
    private String featuredHeader;

    private String pageType; // FilteringFlowType

    protected boolean hasTerms;

    private boolean isFreeSamplePromoProduct;

    private HolidayMealBundleContainer holidayMealBundleContainer;

    private String productQualityNote;

    /**
     * standing order data
     */
    protected Map<String, Object> soData;

    /* staff picks */
    protected String erpCategory;
    protected int erpCatPosition;
    protected int priority;

    // Required for HookLogic
    private String clickBeacon;
    private String imageBeacon;

    /**
     * Time to Complete field for MealKit items
     * 
     * Note, only positive values are valid, 0 means no-value
     */
    private int timeToComplete = 0;
    
    public String getProductQualityNote() {
        return productQualityNote;
    }

    public void setProductQualityNote(String productQualityNote) {
        this.productQualityNote = productQualityNote;
    }

    public String getErpCategory() {
        return erpCategory;
    }

    public void setErpCategory(String erpCategory) {
        this.erpCategory = erpCategory;
    }

    public int getErpCatPosition() {
        return erpCatPosition;
    }

    public void setErpCatPosition(int erpPosition) {
        this.erpCatPosition = erpPosition;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Map<String, Object> getSoData() {
        return soData;
    }

    @Override
    public void setSoData(Map<String, Object> soData) {
        this.soData = soData;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getFeaturedHeader() {
        return featuredHeader;
    }

    public void setFeaturedHeader(String featuredHeader) {
        this.featuredHeader = featuredHeader;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getSkuCode() {
        return skuCode;
    }

    @Override
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getBadge() {
        return badge;
    }

    @Override
    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDiscontinued() {
        return isDiscontinued;
    }

    public void setDiscontinued(boolean isDiscontinued) {
        this.isDiscontinued = isDiscontinued;
    }

    public boolean isOutOfSeason() {
        return isOutOfSeason;
    }

    public void setOutOfSeason(boolean isOutOfSeason) {
        this.isOutOfSeason = isOutOfSeason;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    public double getRecency() {
        return recency;
    }

    public void setRecency(double recency) {
        this.recency = recency;
    }

    @Override
    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public boolean isMaxProductSampleQuantityReached() {
        return maxProductSampleQuantityReached;
    }

    public void setMaxProductSampleQuantityReached(boolean maxProductSampleQuantityReached) {
        this.maxProductSampleQuantityReached = maxProductSampleQuantityReached;
    }

    @Override
    public List<SalesUnit> getSalesUnit() {
        return salesUnit;
    }

    @Override
    public void setSalesUnit(List<SalesUnit> salesUnit) {
        this.salesUnit = salesUnit;
    }

    public String getConfigDescr() {
        return configDescr;
    }

    public void setConfigDescr(String configDescr) {
        this.configDescr = configDescr;
    }

    @Override
    public int getWineRating() {
        return wineRating;
    }

    @Override
    public void setWineRating(int wineRating) {
        this.wineRating = wineRating;
    }

    @Override
    public double getCustomerRating() {
        return customerRating;
    }

    @Override
    public void setCustomerRating(double customerRating) {
        this.customerRating = customerRating;
    }

    @Override
    public int getExpertRating() {
        return expertRating;
    }

    @Override
    public void setExpertRating(int expertRating) {
        this.expertRating = expertRating;
    }

    @Override
    public String getSustainabilityRating() {
        return sustainabilityRating;
    }

    @Override
    public void setSustainabilityRating(String sustainabilityRating) {
        this.sustainabilityRating = sustainabilityRating;
    }

    public int getHeatRating() {
        return heatRating;
    }

    public void setHeatRating(int heatRating) {
        this.heatRating = heatRating;
    }

    public String getHeatRatingScale() {
        return heatRatingScale;
    }

    public void setHeatRatingScale(String heatRatingScale) {
        this.heatRatingScale = heatRatingScale;
    }

    @Override
    public int getDeal() {
        return deal;
    }

    @Override
    public void setDeal(int deal) {
        this.deal = deal;
    }

    @Override
    public String getScaleUnit() {
        return scaleUnit;
    }

    @Override
    public void setScaleUnit(String scaleUnit) {
        this.scaleUnit = scaleUnit;
    }

    @Override
    public String getTaxAndDeposit() {
        return taxAndDeposit;
    }

    @Override
    public void setTaxAndDeposit(String taxAndDeposit) {
        this.taxAndDeposit = taxAndDeposit;
    }

    @Override
    public MaterialPrice[] getAvailMatPrices() {
        return availMatPrices;
    }

    @Override
    public void setAvailMatPrices(MaterialPrice[] availMatPrices) {
        this.availMatPrices = availMatPrices;
    }

    @Override
    public CharacteristicValuePrice[] getCvPrices() {
        return cvPrices;
    }

    @Override
    public void setCvPrices(CharacteristicValuePrice[] cvPrices) {
        this.cvPrices = cvPrices;
    }

    @Override
    public SalesUnitRatio[] getSuRatios() {
        return suRatios;
    }

    @Override
    public void setSuRatios(SalesUnitRatio[] suRatios) {
        this.suRatios = suRatios;
    }

    @Override
    public MaterialPrice[] getGrpPrices() {
        return grpPrices;
    }

    @Override
    public void setGrpPrices(MaterialPrice[] grpPrices) {
        this.grpPrices = grpPrices;
    }

    @Override
    public double getWasPrice() {
        return wasPrice;
    }

    @Override
    public void setWasPrice(double wasPrice) {
        this.wasPrice = wasPrice;
    }

    public boolean isCustomizePopup() {
        return customizePopup;
    }

    public void setCustomizePopup(boolean customizePopup) {
        this.customizePopup = customizePopup;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    @Override
    public String getAboutPriceText() {
        return aboutPriceText;
    }

    @Override
    public void setAboutPriceText(String aboutPriceText) {
        this.aboutPriceText = aboutPriceText;
    }

    public double getConfiguredPrice() {
        return configuredPrice;
    }

    public void setConfiguredPrice(double configuredPrice) {
        this.configuredPrice = configuredPrice;
    }

    public int getCustomerRatingReviewCount() {
        return customerRatingReviewCount;
    }

    public void setCustomerRatingReviewCount(int customerRatingReviewCount) {
        this.customerRatingReviewCount = customerRatingReviewCount;
    }

    @Override
    public List<Variation> getVariations() {
        return variations;
    }

    @Override
    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

    @Override
    public void setVariationDisplay(boolean variationDisplay) {
        this.variationDisplay = variationDisplay;
    }

    @Override
    public boolean isVariationDisplay() {
        return variationDisplay;
    }

    @Override
    public String getSalesUnitLabel() {
        return salesUnitLabel;
    }

    @Override
    public void setSalesUnitLabel(String salesUnitLabel) {
        this.salesUnitLabel = salesUnitLabel;
    }

    @Override
    public boolean isHasSalesUnitDescription() {
        return hasSalesUnitDescription;
    }

    @Override
    public void setHasSalesUnitDescription(boolean hasSalesUnitDescription) {
        this.hasSalesUnitDescription = hasSalesUnitDescription;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isConfigInvalid() {
        return isConfigInvalid;
    }

    public void setConfigInvalid(boolean isConfigInvalid) {
        this.isConfigInvalid = isConfigInvalid;
    }

    public void setSalesUnitDescrPopup(String salesUnitDescrPopup) {
        this.salesUnitDescrPopup = salesUnitDescrPopup;
    }

    public String getSalesUnitDescrPopup() {
        return salesUnitDescrPopup;
    }

    public ProductData getBrowseRecommandation() {
        return browseRecommandation;
    }

    public void setBrowseRecommandation(ProductData browseRecommandation) {
        this.browseRecommandation = browseRecommandation;
    }

    public String getMsgCancellation() {
        return msgCancellation;
    }

    public void setMsgCancellation(String msgCancellation) {
        this.msgCancellation = msgCancellation;
    }

    public String getMsgCutoffHeader() {
        return msgCutoffHeader;
    }

    public void setMsgCutoffHeader(String msgCutoffHeader) {
        this.msgCutoffHeader = msgCutoffHeader;
    }

    public String getMsgCutoffNotice() {
        return msgCutoffNotice;
    }

    public void setMsgCutoffNotice(String msgCutoffNotice) {
        this.msgCutoffNotice = msgCutoffNotice;
    }

    public String getMsgDayOfWeek() {
        return msgDayOfWeek;
    }

    public void setMsgDayOfWeek(String msgDayOfWeek) {
        this.msgDayOfWeek = msgDayOfWeek;
    }

    public String getMsgDayOfWeekHeader() {
        return msgDayOfWeekHeader;
    }

    public void setMsgDayOfWeekHeader(String msgDayOfWeekHeader) {
        this.msgDayOfWeekHeader = msgDayOfWeekHeader;
    }

    public String getMsgDeliveryNote() {
        return msgDeliveryNote;
    }

    public void setMsgDeliveryNote(String msgDeliveryNote) {
        this.msgDeliveryNote = msgDeliveryNote;
    }

    public String getMsgLeadTime() {
        return msgLeadTime;
    }

    public void setMsgLeadTime(String msgLeadTime) {
        this.msgLeadTime = msgLeadTime;
    }

    public String getMsgLeadTimeHeader() {
        return msgLeadTimeHeader;
    }

    public void setMsgLeadTimeHeader(String msgLeadTimeHeader) {
        this.msgLeadTimeHeader = msgLeadTimeHeader;
    }

    public String getMsgKosherRestriction() {
        return msgKosherRestriction;
    }

    public void setMsgKosherRestriction(String msgKosherRestriction) {
        this.msgKosherRestriction = msgKosherRestriction;
    }

    public String getMsgEarliestAvailability() {
        return msgEarliestAvailability;
    }

    public void setMsgEarliestAvailability(String msgEarliestAvailability) {
        this.msgEarliestAvailability = msgEarliestAvailability;
    }

    public String getSalesUnitDescrPDP() {
        return salesUnitDescrPDP;
    }

    public void setSalesUnitDescrPDP(String salesUnitDescrPDP) {
        this.salesUnitDescrPDP = salesUnitDescrPDP;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getNutritionSortTitle() {
        return nutritionSortTitle;
    }

    public void setNutritionSortTitle(String nutritionSortTitle) {
        this.nutritionSortTitle = nutritionSortTitle;
    }

    public String getNutritionSortValue() {
        return nutritionSortValue;
    }

    public void setNutritionSortValue(String nutritionSortValue) {
        this.nutritionSortValue = nutritionSortValue;
    }

    public String getNutritionServingSizeValue() {
        return nutritionServingSizeValue;
    }

    public void setNutritionServingSizeValue(String nutritionServingSizeValue) {
        this.nutritionServingSizeValue = nutritionServingSizeValue;
    }

    public boolean isShowRatings() {
        return showRatings;
    }

    public void setShowRatings(boolean showRatings) {
        this.showRatings = showRatings;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    @Override
    public void setUtPrice(String price) {
        this.utPrice = price;
    }

    @Override
    public String getUtPrice() {
        return utPrice;
    }

    @Override
    public void setUtSalesUnit(String salesUnit) {
        this.utSalesUnit = salesUnit;
    }

    @Override
    public String getUtSalesUnit() {
        return utSalesUnit;
    }

    @Override
    public void setHasTerms(boolean hasTerms) {
        this.hasTerms = hasTerms;

    }

    @Override
    public boolean getHasTerms() {
        return hasTerms;

    }

    public boolean isFreeSamplePromoProduct() {
        return isFreeSamplePromoProduct;
    }

    public void setFreeSamplePromoProduct(boolean isFreeSamplePromoProduct) {
        this.isFreeSamplePromoProduct = isFreeSamplePromoProduct;
    }

    public HolidayMealBundleContainer getHolidayMealBundleContainer() {
        return holidayMealBundleContainer;
    }

    public void setHolidayMealBundleContainer(HolidayMealBundleContainer holidayMealBundleContainer) {
        this.holidayMealBundleContainer = holidayMealBundleContainer;
    }

    public Double getUnitPrice() {
        if (null != utPrice && !"".equals(utPrice)) {
            return Double.parseDouble(utPrice);
        }
        return null;
    }

    public String getClickBeacon() {
        return clickBeacon;
    }

    public void setClickBeacon(String clickBeacon) {
        this.clickBeacon = clickBeacon;
    }

    public String getImageBeacon() {
        return imageBeacon;
    }

    public void setImageBeacon(String imageBeacon) {
        this.imageBeacon = imageBeacon;
    }

    public String getPricePerDefaultSalesUnit() {
        return pricePerDefaultSalesUnit;
    }

    public String getDispDefaultSalesUnit() {
        return dispDefaultSalesUnit;
    }

    public void setPricePerDefaultSalesUnit(String pricePerDefaultSalesUnit) {
        this.pricePerDefaultSalesUnit = pricePerDefaultSalesUnit;
    }

    public void setDispDefaultSalesUnit(String dispDefaultSalesUnit) {
        this.dispDefaultSalesUnit = dispDefaultSalesUnit;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }
    
    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

    public boolean isNewProduct() {
        return newProduct;
    }

    public void setNewProduct(boolean newProduct) {
        this.newProduct = newProduct;
    }
}
