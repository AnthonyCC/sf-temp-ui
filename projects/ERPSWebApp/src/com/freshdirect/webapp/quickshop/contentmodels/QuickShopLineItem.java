package com.freshdirect.webapp.quickshop.contentmodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.webapp.taglib.cart.AddToCartItem;
import com.freshdirect.webapp.taglib.cart.CartData.Quantity;
import com.freshdirect.webapp.taglib.cart.CartData.SalesUnit;

/**
 * @author szabi
 *
 */
public class QuickShopLineItem implements Serializable {

	private static final long serialVersionUID = -5433625138270100825L;
	
	
	/**
	 * The quickshop specific id (composite id) of the item
	 */
	private String itemId;
	
	/**
	 * The id of the list this item belongs to 
	 */
	private String listId;
	
	/**
	 * The original line id (e.g shoppingListLineId) of the item 
	 */
	private String originalLineId;
	
	private String catId;
	
	private String productName;
	private String productImage;
	
	/**
	 * Amount for numeric quantity type, containing min/max/step, and the actual value.
	 */
	private Quantity quantity;
	
	/**
	 * Amount for sales-unit enum type, containing the full enum, plus the selected value.
	 */
	private List<SalesUnit> salesUnit;

	private double price;
	private double wasPrice;
	private double configuredPrice;
	private String scaleUnit;
	private String taxAndDeposit;
	private String savingString;
	private boolean mixNMatch;
	private String aboutPriceText;
	
	private String skuCode;
	private Map<String,String> configuration;
	private String configDescr;
	private String description;
	private String productId;
	
	private String productPageUrl;

	
	/**
	 * burst image type: fav, new, back
	 */
	private String badge;
	private int deal;
	
	private double inCartAmount;
	private String dealInfo; //??
	
	// E-coupons
	private FDCustomerCoupon coupon; 
	private boolean couponDisplay;
	private boolean couponClipped;
	private String couponStatusText;
	
	private boolean available;
	
	private QuickShopLineItem replacement;
	private boolean useReplacement = false;
	private QuickShopLineItem tempConfig;
	
	private boolean isConfigInvalid;
	
	private String departmentDesc;
	private double frequency=0;
	private double recency=0;
	
	private int wineRating;
	private double customerRating;
	private int customerRatingReviewCount;
	private int expertRating;
	private String sustainabilityRating;
	
	//for subtotal calculations
	private MaterialPrice[] availMatPrices;
	private CharacteristicValuePrice[] cvPrices;
	private SalesUnitRatio[] suRatios;
	private MaterialPrice[] grpPrices;
	private boolean soldBySalesUnit;
	
	// It needs a customize button with a product config popup
	private boolean customizePopup = false; 

	// Alcoholic content
	private boolean alcoholic = false;
	
	// USQ alcoholic
	private boolean usq = false;
	
	public QuickShopLineItem() {
	}
	
	public QuickShopLineItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		populateFromAddToCartItem(source);
	}
	
	
	private void populateFromAddToCartItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		
		ProductModel pm = null;
		if(source.getSkuCode()!=null){
			pm = ContentFactory.getInstance().getProduct(source.getSkuCode());
		}else{
			throw new FDResourceException("Cannot create item");
		}
		
		itemId = source.getAtcItemId();
		productName = pm.getFullName();
		productImage = pm.getProdImage().getPathWithPublishId();
		catId=source.getCategoryId();
		configuration=source.getConfiguration();
		listId=source.getListId();
		productId=source.getProductId();
		
		//quantity
		quantity = new Quantity();
		quantity.setQuantity(Double.parseDouble(source.getQuantity()));
		
		//salesUnit
		salesUnit = new ArrayList<SalesUnit>();
		SalesUnit saleUnit = new SalesUnit();
		saleUnit.setId(source.getSalesUnit());
		salesUnit.add(saleUnit);

		skuCode=source.getSkuCode();
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Map<String,String> getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Map<String,String> configuration) {
		this.configuration = configuration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public double getInCartAmount() {
		return inCartAmount;
	}
	public void setInCartAmount(double inCartAmount) {
		this.inCartAmount = inCartAmount;
	}
	public String getDealInfo() {
		return dealInfo;
	}
	public void setDealInfo(String dealInfo) {
		this.dealInfo = dealInfo;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public QuickShopLineItem getReplacement() {
		return replacement;
	}
	public void setReplacement(QuickShopLineItem replacement) {
		this.replacement = replacement;
	}	
	public boolean isUseReplacement() {
		return useReplacement;
	}	
	public void setUseReplacement( boolean useReplacement ) {
		this.useReplacement = useReplacement;
	}
	public boolean isConfigInvalid() {
		return isConfigInvalid;
	}
	public void setConfigInvalid(boolean isConfigInvalid) {
		this.isConfigInvalid = isConfigInvalid;
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
	public Quantity getQuantity() {
		return quantity;
	}	
	public void setQuantity( Quantity quantity ) {
		this.quantity = quantity;
	}	
	public List<SalesUnit> getSalesUnit() {
		return salesUnit;
	}	
	public void setSalesUnit( List<SalesUnit> salesUnit ) {
		this.salesUnit = salesUnit;
	}
	public String getConfigDescr() {
		return configDescr;
	}
	public void setConfigDescr(String configDescr) {
		this.configDescr = configDescr;
	}
	public int getWineRating() {
		return wineRating;
	}
	public void setWineRating(int wineRating) {
		this.wineRating = wineRating;
	}
	public double getCustomerRating() {
		return customerRating;
	}
	public void setCustomerRating(double customerRating) {
		this.customerRating = customerRating;
	}
	public int getExpertRating() {
		return expertRating;
	}
	public void setExpertRating(int expertRating) {
		this.expertRating = expertRating;
	}
	public String getSustainabilityRating() {
		return sustainabilityRating;
	}
	public void setSustainabilityRating(String sustainabilityRating) {
		this.sustainabilityRating = sustainabilityRating;
	}
	public String getOriginalLineId() {
		return originalLineId;
	}
	public void setOriginalLineId(String originalLineId) {
		this.originalLineId = originalLineId;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public int getDeal() {
		return deal;
	}
	public void setDeal(int deal) {
		this.deal = deal;
	}
	public String getScaleUnit() {
		return scaleUnit;
	}
	public void setScaleUnit(String scaleUnit) {
		this.scaleUnit = scaleUnit;
	}
	public String getTaxAndDeposit() {
		return taxAndDeposit;
	}
	public void setTaxAndDeposit(String taxAndDeposit) {
		this.taxAndDeposit = taxAndDeposit;
	}
	public MaterialPrice[] getAvailMatPrices() {
		return availMatPrices;
	}
	public void setAvailMatPrices(MaterialPrice[] availMatPrices) {
		this.availMatPrices = availMatPrices;
	}
	public CharacteristicValuePrice[] getCvPrices() {
		return cvPrices;
	}
	public void setCvPrices(CharacteristicValuePrice[] cvPrices) {
		this.cvPrices = cvPrices;
	}
	public SalesUnitRatio[] getSuRatios() {
		return suRatios;
	}
	public void setSuRatios(SalesUnitRatio[] suRatios) {
		this.suRatios = suRatios;
	}
	public MaterialPrice[] getGrpPrices() {
		return grpPrices;
	}
	public void setGrpPrices(MaterialPrice[] grpPrices) {
		this.grpPrices = grpPrices;
	}
	public boolean isSoldBySalesUnit() {
		return soldBySalesUnit;
	}
	public void setSoldBySalesUnit(boolean soldBySalesUnit) {
		this.soldBySalesUnit = soldBySalesUnit;
	}
	public String getSavingString() {
		return savingString;
	}
	public void setSavingString(String savingString) {
		this.savingString = savingString;
	}
	public double getWasPrice() {
		return wasPrice;
	}
	public void setWasPrice(double wasPrice) {
		this.wasPrice = wasPrice;
	}
	public boolean isCustomizePopup() {
		return customizePopup;
	}
	public void setCustomizePopup( boolean customizePopup ) {
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
	public FDCustomerCoupon getCoupon() {
		return coupon;
	}
	public void setCoupon(FDCustomerCoupon coupon) {
		this.coupon = coupon;
	}
	public boolean isCouponDisplay() {
		return couponDisplay;
	}	
	public void setCouponDisplay( boolean couponDisplay ) {
		this.couponDisplay = couponDisplay;
	}	
	public boolean isCouponClipped() {
		return couponClipped;
	}	
	public void setCouponClipped( boolean couponClipped ) {
		this.couponClipped = couponClipped;
	}	
	public String getCouponStatusText() {
		return couponStatusText;
	}	
	public void setCouponStatusText( String couponStatusText ) {
		this.couponStatusText = couponStatusText;
	}
	public boolean isMixNMatch() {
		return mixNMatch;
	}
	public void setMixNMatch(boolean mixNMatch) {
		this.mixNMatch = mixNMatch;
	}	
	public boolean isAlcoholic() {
		return alcoholic;
	}	
	public void setAlcoholic( boolean alcoholic ) {
		this.alcoholic = alcoholic;
	}	
	public boolean isUsq() {
		return usq;
	}	
	public void setUsq( boolean usq ) {
		this.usq = usq;
	}	
	public String getAboutPriceText() {
		return aboutPriceText;
	}	
	public void setAboutPriceText( String aboutPriceText ) {
		this.aboutPriceText = aboutPriceText;
	}
	public String getProductPageUrl() {
		return productPageUrl;
	}	
	public void setProductPageUrl( String productPageUrl ) {
		this.productPageUrl = productPageUrl;
	}

	public QuickShopLineItem getTempConfig() {
		return tempConfig;
	}

	public void setTempConfig(QuickShopLineItem tempConfig) {
		this.tempConfig = tempConfig;
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
}
