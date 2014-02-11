package com.freshdirect.webapp.ajax.product.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Variation;


public class ProductData extends BasicProductData implements SkuData {

	private static final long	serialVersionUID	= 4609027472414542229L;

	
	/**
	 * Sku code
	 */
	protected String skuCode;
	
	/**
	 * Category content-ID
	 */
	protected String catId;	

	/**
	 * Does it need a customize button with a product config popup?
	 */
	protected boolean customizePopup = false; 

	/**
	 * Is product available for sale?
	 */
	protected boolean available;


	
	
	/**
	 * Amount for numeric quantity type, containing min/max/step, and the actual value.
	 */
	protected Quantity quantity;
	
	/**
	 * Amount for sales-unit enum type, containing the full enum, plus the selected value.
	 */
	protected List<SalesUnit> salesUnit;

	
	

	/**
	 * deal
	 */
	protected int deal;	

	/**
	 * burst image type: fav, new, back
	 */
	protected String badge;
	
	
	
	
	// Ratings
	protected int wineRating;
	protected double customerRating;
	protected int customerRatingReviewCount;
	protected int expertRating;
	protected String sustainabilityRating;
	/**
	 * Heat rating value between 0 and 5.
	 * -1 means invalid value.
	 */
	protected int heatRating = -1;

	
	
	
	// scores
	protected double frequency = 0;
	protected double recency = 0;

	
	
	
	// PRICING

	// Pricing data	
	protected double price;
	protected String scaleUnit;
	protected String taxAndDeposit;
	protected String aboutPriceText;	

	// TODO ???
	protected double wasPrice;
	
	
	

	//for subtotal calculations
	protected MaterialPrice[] availMatPrices;
	protected CharacteristicValuePrice[] cvPrices;
	protected SalesUnitRatio[] suRatios;
	protected MaterialPrice[] grpPrices;
	

	
	
	// OTHER ???
	
	
	// Product configuration (productselection/orderline/cartline)
	protected boolean isConfigInvalid;
	protected Map<String,String> configuration;
	protected String configDescr;
	protected double configuredPrice;	
	protected String description;	
	protected String departmentDesc;
	
	
	// from Sku
	protected String label;
	protected String salesUnitLabel;
	protected boolean hasSalesUnitDescription = false;
	protected String salesUnitDescrPopup;		
	protected List<Variation> variations;



	
	
	
	
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
	public void setQuantity( Quantity quantity ) {
		this.quantity = quantity;
	}	
	@Override
	public List<SalesUnit> getSalesUnit() {
		return salesUnit;
	}	
	@Override
	public void setSalesUnit( List<SalesUnit> salesUnit ) {
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
	@Override
	public String getAboutPriceText() {
		return aboutPriceText;
	}	
	@Override
	public void setAboutPriceText( String aboutPriceText ) {
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
	public void setVariations( List<Variation> variations ) {
		this.variations = variations;
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
	public void setLabel( String label ) {
		this.label = label;
	}
	public boolean isConfigInvalid() {
		return isConfigInvalid;
	}
	public void setConfigInvalid(boolean isConfigInvalid) {
		this.isConfigInvalid = isConfigInvalid;
	}
	public void setSalesUnitDescrPopup( String salesUnitDescrPopup ) {
		this.salesUnitDescrPopup = salesUnitDescrPopup;
	}	
	public String getSalesUnitDescrPopup() {
		return salesUnitDescrPopup;
	}
	
}
