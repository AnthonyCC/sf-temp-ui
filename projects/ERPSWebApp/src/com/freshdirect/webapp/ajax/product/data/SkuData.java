package com.freshdirect.webapp.ajax.product.data;

import java.util.List;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Variation;


public interface SkuData {

	public void setAboutPriceText( String aboutPriceText );

	public String getAboutPriceText();

	public void setLabel( String label );

	public String getLabel();

	public void setHasSalesUnitDescription( boolean hasSalesUnitDescription );

	public boolean isHasSalesUnitDescription();

	public void setSalesUnitLabel( String salesUnitLabel );

	public String getSalesUnitLabel();

	public void setVariations( List<Variation> variations );

	public List<Variation> getVariations();

	public boolean isVariationDisplay();

	public void setVariationDisplay(boolean variationDisplay);

	public void setAvailable( boolean available );

	public boolean isAvailable();

	public void setSkuCode( String skuCode );

	public String getSkuCode();

	public void setBadge( String badge );

	public String getBadge();

	public void setSustainabilityRating( String sustainabilityRating );

	public String getSustainabilityRating();

	public void setExpertRating( int expertRating );

	public int getExpertRating();

	public void setCustomerRating( double customerRating );

	public double getCustomerRating();

	public void setWineRating( int wineRating );

	public int getWineRating();

	public void setDealInfo( String dealInfo );

	public String getDealInfo();

	public void setDeal( int deal );

	public int getDeal();

	public void setGrpPrices( MaterialPrice[] grpPrices );

	public MaterialPrice[] getGrpPrices();

	public void setSuRatios( SalesUnitRatio[] suRatios );

	public SalesUnitRatio[] getSuRatios();

	public void setCvPrices( CharacteristicValuePrice[] cvPrices );

	public CharacteristicValuePrice[] getCvPrices();

	public void setAvailMatPrices( MaterialPrice[] availMatPrices );

	public MaterialPrice[] getAvailMatPrices();

	public void setSavingString( String savingString );

	public String getSavingString();

	public void setTaxAndDeposit( String taxAndDeposit );

	public String getTaxAndDeposit();

	public void setScaleUnit( String scaleUnit );

	public String getScaleUnit();

	public void setWasPrice( double wasPrice );

	public double getWasPrice();

	public void setPrice( double price );

	public double getPrice();

	// APPDEV-3438
	public void setUtPrice( String price );

	// APPDEV-3438
	public String getUtPrice();

	public void setSalesUnit( List<SalesUnit> salesUnit );

	public List<SalesUnit> getSalesUnit();

	// APPDEV-3438
	public void setUtSalesUnit( String salesUnit );

	// APPDEV-3438
	public String getUtSalesUnit();

	public void setQuantity( Quantity quantity );

	public Quantity getQuantity();

}
