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
public class ProductConfigResponseData implements Serializable {
	
	private static final long	serialVersionUID	= 8413382103584983050L;
	
	/**
	 * list of skus 
	 */
	private List<Sku> skus;
	
	private boolean soldBySalesUnit;

	// Product level extra data
	private String productId;
	private String productName;
	private String productImage;
	private String akaName;
	private String quantityText;
	private String packageDescription;
	private double inCartAmount;

	private String productPageUrl;

	// Alcoholic content
	private boolean alcoholic = false;
	
	// USQ alcoholic
	private boolean usq = false; 
	
	
	public List<Sku> getSkus() {
		return skus;
	}	
	public void setSkus( List<Sku> skus ) {
		this.skus = skus;
	}

	public boolean isSoldBySalesUnit() {
		return soldBySalesUnit;
	}
	public void setSoldBySalesUnit(boolean soldBySalesUnit) {
		this.soldBySalesUnit = soldBySalesUnit;
	}
	
	public String getProductId() {
		return productId;
	}	
	public void setProductId( String productId ) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}	
	public void setProductName( String productName ) {
		this.productName = productName;
	}
	
	public String getProductImage() {
		return productImage;
	}	
	public void setProductImage( String productImage ) {
		this.productImage = productImage;
	}

	public double getInCartAmount() {
		return inCartAmount;
	}	
	public void setInCartAmount( double inCartAmount ) {
		this.inCartAmount = inCartAmount;
	}
	


	public String getAkaName() {
		return akaName;
	}
	public void setAkaName(String akaName) {
		this.akaName = akaName;
	}



	public String getPackageDescription() {
		return packageDescription;
	}
	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}



	public String getQuantityText() {
		return quantityText;
	}
	public void setQuantityText(String quantityText) {
		this.quantityText = quantityText;
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

	public String getProductPageUrl() {
		return productPageUrl;
	}
	
	public void setProductPageUrl( String productPageUrl ) {
		this.productPageUrl = productPageUrl;
	}

	
	
	/**
	 * 	available: is available?
	 * 	selected: is currently selected?
	 * 	defaultSku:	is the default sku?
	 *	variations: list of variation enums 
	 */
	public static class Sku implements Serializable {
		
		private static final long	serialVersionUID	= 8484303804488950735L;
		
		// Core product config data 
		private String skuCode;
		private String label;
		private boolean available;
		private boolean selected;
		private boolean defaultSku;
		List<Variation> variations;		

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
		public double getPrice() {
			return price;
		}		
		public void setPrice( double price ) {
			this.price = price;
		}		
		public double getWasPrice() {
			return wasPrice;
		}		
		public void setWasPrice( double wasPrice ) {
			this.wasPrice = wasPrice;
		}		
		public String getScaleUnit() {
			return scaleUnit;
		}		
		public void setScaleUnit( String scaleUnit ) {
			this.scaleUnit = scaleUnit;
		}		
		public String getTaxAndDeposit() {
			return taxAndDeposit;
		}		
		public void setTaxAndDeposit( String taxAndDeposit ) {
			this.taxAndDeposit = taxAndDeposit;
		}		
		public String getSavingString() {
			return savingString;
		}		
		public void setSavingString( String savingString ) {
			this.savingString = savingString;
		}
		public MaterialPrice[] getAvailMatPrices() {
			return availMatPrices;
		}		
		public void setAvailMatPrices( MaterialPrice[] availMatPrices ) {
			this.availMatPrices = availMatPrices;
		}		
		public CharacteristicValuePrice[] getCvPrices() {
			return cvPrices;
		}		
		public void setCvPrices( CharacteristicValuePrice[] cvPrices ) {
			this.cvPrices = cvPrices;
		}		
		public SalesUnitRatio[] getSuRatios() {
			return suRatios;
		}		
		public void setSuRatios( SalesUnitRatio[] suRatios ) {
			this.suRatios = suRatios;
		}		
		public MaterialPrice[] getGrpPrices() {
			return grpPrices;
		}		
		public void setGrpPrices( MaterialPrice[] grpPrices ) {
			this.grpPrices = grpPrices;
		}		
		public int getDeal() {
			return deal;
		}		
		public void setDeal( int deal ) {
			this.deal = deal;
		}		
		public String getDealInfo() {
			return dealInfo;
		}		
		public void setDealInfo( String dealInfo ) {
			this.dealInfo = dealInfo;
		}		
		public int getWineRating() {
			return wineRating;
		}		
		public void setWineRating( int wineRating ) {
			this.wineRating = wineRating;
		}		
		public double getCustomerRating() {
			return customerRating;
		}		
		public void setCustomerRating( double customerRating ) {
			this.customerRating = customerRating;
		}		
		public int getExpertRating() {
			return expertRating;
		}		
		public void setExpertRating( int expertRating ) {
			this.expertRating = expertRating;
		}		
		public String getSustainabilityRating() {
			return sustainabilityRating;
		}		
		public void setSustainabilityRating( String sustainabilityRating ) {
			this.sustainabilityRating = sustainabilityRating;
		}		
		public String getBadge() {
			return badge;
		}		
		public void setBadge( String badge ) {
			this.badge = badge;
		}
		public String getSkuCode() {
			return skuCode;
		}		
		public void setSkuCode( String skuCode ) {
			this.skuCode = skuCode;
		}
		public boolean isAvailable() {
			return available;
		}		
		public void setAvailable( boolean available ) {
			this.available = available;
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
		public List<Variation> getVariations() {
			return variations;
		}
		public void setVariations( List<Variation> variations ) {
			this.variations = variations;
		}
		public String getSalesUnitLabel() {
			return salesUnitLabel;
		}
		public void setSalesUnitLabel(String salesUnitLabel) {
			this.salesUnitLabel = salesUnitLabel;
		}
		public boolean isHasSalesUnitDescription() {
			return hasSalesUnitDescription;
		}
		public void setHasSalesUnitDescription(boolean hasSalesUnitDescription) {
			this.hasSalesUnitDescription = hasSalesUnitDescription;
		}		
		public String getLabel() {
			return label;
		}		
		public void setLabel( String label ) {
			this.label = label;
		}
		public String getAboutPriceText() {
			return aboutPriceText;
		}	
		public void setAboutPriceText( String aboutPriceText ) {
			this.aboutPriceText = aboutPriceText;
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
	}
}
