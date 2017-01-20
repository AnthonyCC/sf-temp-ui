package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;

/**
 *	Simple java bean for product config response. 
 *	Class structure is representing the JSON structure. 	
 * 
 * @author treer
 */
public class ProductConfigResponseData extends BasicProductData {
	
	private static final long	serialVersionUID	= 8413382103584983050L;
	
	/**
	 * list of skus 
	 */
	private List<Sku> skus;
	
	
	public List<Sku> getSkus() {
		return skus;
	}	
	public void setSkus( List<Sku> skus ) {
		this.skus = skus;
	}

	/**
	 * 	available: is available?
	 * 	selected: is currently selected?
	 * 	defaultSku:	is the default sku?
	 *	variations: list of variation enums 
	 */
	public static class Sku implements Serializable, SkuData {
		
		private static final long	serialVersionUID	= 8484303804488950735L;
		
		// Core product config data 
		private String skuCode;
		private String label;
		private boolean available;
		private boolean selected;
		private boolean defaultSku;
		private List<Variation> variations;		
		private boolean variationDisplay;

		// Pricing data - price basic
		private double price;
		private double wasPrice;
		private String scaleUnit;
		private String taxAndDeposit;
		private String savingString;
		private String aboutPriceText;
		
		// Pricing data - subtotal calculation
		private MaterialPrice[] availMatPrices;
		private CharacteristicValuePrice[] cvPrices;
		private SalesUnitRatio[] suRatios;
		private MaterialPrice[] grpPrices;

		// Pricing data - deals
		private int deal;
		private String dealInfo;
		
		// Descriptive data - ratings
		private int wineRating;
		private double customerRating;
		private int expertRating;
		private String sustainabilityRating;

		// Descriptive data - other
		private String badge;

		// Amount data (with defaults selected)
		private Quantity quantity;		
		private List<SalesUnit> salesUnit;
		private String salesUnitLabel;
		private boolean hasSalesUnitDescription;

		// APPDEV-3438
		private String utPrice;
		private String utSalesUnit;

		
		
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
		@Override
		public double getPrice() {
			return price;
		}		
		@Override
		public void setPrice( double price ) {
			this.price = price;
		}		
		@Override
		public double getWasPrice() {
			return wasPrice;
		}		
		@Override
		public void setWasPrice( double wasPrice ) {
			this.wasPrice = wasPrice;
		}		
		@Override
		public String getScaleUnit() {
			return scaleUnit;
		}		
		@Override
		public void setScaleUnit( String scaleUnit ) {
			this.scaleUnit = scaleUnit;
		}		
		@Override
		public String getTaxAndDeposit() {
			return taxAndDeposit;
		}		
		@Override
		public void setTaxAndDeposit( String taxAndDeposit ) {
			this.taxAndDeposit = taxAndDeposit;
		}		
		@Override
		public String getSavingString() {
			return savingString;
		}		
		@Override
		public void setSavingString( String savingString ) {
			this.savingString = savingString;
		}
		@Override
		public MaterialPrice[] getAvailMatPrices() {
			return availMatPrices;
		}		
		@Override
		public void setAvailMatPrices( MaterialPrice[] availMatPrices ) {
			this.availMatPrices = availMatPrices;
		}		
		@Override
		public CharacteristicValuePrice[] getCvPrices() {
			return cvPrices;
		}		
		@Override
		public void setCvPrices( CharacteristicValuePrice[] cvPrices ) {
			this.cvPrices = cvPrices;
		}		
		@Override
		public SalesUnitRatio[] getSuRatios() {
			return suRatios;
		}		
		@Override
		public void setSuRatios( SalesUnitRatio[] suRatios ) {
			this.suRatios = suRatios;
		}		
		@Override
		public MaterialPrice[] getGrpPrices() {
			return grpPrices;
		}		
		@Override
		public void setGrpPrices( MaterialPrice[] grpPrices ) {
			this.grpPrices = grpPrices;
		}		
		@Override
		public int getDeal() {
			return deal;
		}		
		@Override
		public void setDeal( int deal ) {
			this.deal = deal;
		}		
		@Override
		public String getDealInfo() {
			return dealInfo;
		}		
		@Override
		public void setDealInfo( String dealInfo ) {
			this.dealInfo = dealInfo;
		}		
		@Override
		public int getWineRating() {
			return wineRating;
		}		
		@Override
		public void setWineRating( int wineRating ) {
			this.wineRating = wineRating;
		}		
		@Override
		public double getCustomerRating() {
			return customerRating;
		}		
		@Override
		public void setCustomerRating( double customerRating ) {
			this.customerRating = customerRating;
		}		
		@Override
		public int getExpertRating() {
			return expertRating;
		}		
		@Override
		public void setExpertRating( int expertRating ) {
			this.expertRating = expertRating;
		}		
		@Override
		public String getSustainabilityRating() {
			return sustainabilityRating;
		}		
		@Override
		public void setSustainabilityRating( String sustainabilityRating ) {
			this.sustainabilityRating = sustainabilityRating;
		}		
		@Override
		public String getBadge() {
			return badge;
		}		
		@Override
		public void setBadge( String badge ) {
			this.badge = badge;
		}
		@Override
		public String getSkuCode() {
			return skuCode;
		}		
		@Override
		public void setSkuCode( String skuCode ) {
			this.skuCode = skuCode;
		}
		@Override
		public boolean isAvailable() {
			return available;
		}		
		@Override
		public void setAvailable( boolean available ) {
			this.available = available;
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
		public void setLabel( String label ) {
			this.label = label;
		}
		@Override
		public String getAboutPriceText() {
			return aboutPriceText;
		}	
		@Override
		public void setAboutPriceText( String aboutPriceText ) {
			this.aboutPriceText = aboutPriceText;
		}
		
		public boolean isSelected() {
			return selected;
		}		
		public void setSelected( boolean selected ) {
			this.selected = selected;
		}		
		public boolean isDefaultSku() {
			return defaultSku;
		}		
		public void setDefaultSku( boolean defaultSku ) {
			this.defaultSku = defaultSku;
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
	}
	
	/**
	 * name:		enum name
	 * label: 		string description
	 * optional: 	is optional?
	 * display: 	display format (checkbox/dropdown)
	 * variations: 	[variation enum: {value,label} ]
	 */
	public static class Variation implements Serializable {
		
		private static final long	serialVersionUID	= -982722495940470686L;
		
		private String name;
		private String label;
		private boolean optional;
		private String display;		
		private List<VarItem> values;
		private String underLabel;
		private String descrPopup;
		
		public String getName() {
			return name;
		}		
		public void setName( String name ) {
			this.name = name;
		}		
		public String getLabel() {
			return label;
		}		
		public void setLabel( String label ) {
			this.label = label;
		}		
		public boolean isOptional() {
			return optional;
		}		
		public void setOptional( boolean optional ) {
			this.optional = optional;
		}		
		public String getDisplay() {
			return display;
		}		
		public void setDisplay( String display ) {
			this.display = display;
		}		
		public List<VarItem> getValues() {
			return values;
		}		
		public void setValues( List<VarItem> values ) {
			this.values = values;
		}
		public String getUnderLabel() {
			return underLabel;
		}
		public void setUnderLabel(String underLabel) {
			this.underLabel = underLabel;
		}		
		public String getDescrPopup() {
			return descrPopup;
		}		
		public void setDescrPopup( String descrPopup ) {
			this.descrPopup = descrPopup;
		}
	}
	
	/**
	 *	value:		enum value name
	 *	label: 		string description 
	 *	selected:	is selected in current config?
	 */
	public static class VarItem implements Serializable {
		
		private static final long	serialVersionUID	= -2219315493994618243L;
		
		private String name;
		private String label;
		private boolean isLabelValue;
		private boolean selected;
		private String cvp;
	    private String imagePath;
		
		public String getName() {
			return name;
		}		
		public void setName( String name ) {
			this.name = name;
		}		
		public String getLabel() {
			return label;
		}		
		public void setLabel( String label ) {
			this.label = label;
		}		
		public boolean isSelected() {
			return selected;
		}		
		public void setSelected( boolean selected ) {
			this.selected = selected;
		}
		public String getCvp() {
			return cvp;
		}
		public void setCvp(String cvp) {
			this.cvp = cvp;
		}
		public boolean isLabelValue() {
			return isLabelValue;
		}
		public void setLabelValue(boolean isLabelValue) {
			this.isLabelValue = isLabelValue;
		}

		public String getImagePath() {
		    return imagePath;
		}
		
		public void setImagePath(String imagePath) {
		    this.imagePath = imagePath;
		}
	}
}
