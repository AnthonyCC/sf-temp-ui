package com.freshdirect.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.EnumKosherTypeValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.erp.EnumAlcoholicContent;

/**
 * Product class - provides all details about a SKU that is necessary for displaying it on a product page.
 * It is a rough equivalent of an ErpProduct.
 *
 */
public class FDProduct extends FDSku {

	private static final long serialVersionUID = -6190779000162681376L;

	private final Date pricingDate;

	/**
	 * Array of variations.
	 * 
	 * @link aggregationByValue
	 */
	private final FDVariation[] variations;

	/**
	 * Array of sales units.
	 * 
	 * @link aggregationByValue
	 */
	private final FDSalesUnit[] salesUnits;

	/** Pricing info for this product */
	private final Pricing pricing;

	private final FDMaterial material;

	/** nutrition. List<FDNutrition> */
	private List<FDNutrition> nutrition;

	private FDSalesUnit[] displaySalesUnits;
	
	private transient ErpNutritionModel nutritionModel;

	public FDProduct(
		String skuCode,
		int version,
		Date pricingDate,
		FDMaterial material,
		FDVariation[] variations,
		FDSalesUnit[] salesUnits,
		Pricing pricing,
		List<FDNutrition> nutrition) {
		super(skuCode, version);
		this.pricingDate = pricingDate;
		this.material = material;
		this.variations = variations;
		this.salesUnits = salesUnits;
		this.pricing = pricing;
		
		this.nutrition = nutrition;
		
	}
	
	public FDProduct(
			String skuCode,
			int version,
			Date pricingDate,
			FDMaterial material,
			FDVariation[] variations,
			FDSalesUnit[] salesUnits,
			Pricing pricing,
			FDSalesUnit[] displaySalesUnits,
			ErpNutritionModel nutritionModel) {
			super(skuCode, version);
			this.pricingDate = pricingDate;
			this.material = material;
			this.variations = variations;
			this.salesUnits = salesUnits;
			this.pricing = pricing;
			
			this.displaySalesUnits = displaySalesUnits;
			this.setNutritionModel(nutritionModel);
			
		}
	/**
	 * Get effective pricing date.
	 */
	public Date getPricingDate() {
		return this.pricingDate;
	}

	public FDMaterial getMaterial() {
		return this.material;
	}

	/**
	 *  Tell if this product can be autoconfigured.
	 *  This is equivalent to saying it has only one possible configuration,
	 *  with regards to variation options and sales units.
	 *  
	 *  @return true if the product can be auto-configured,
	 *          false otherwise.
	 */
	public boolean isAutoconfigurable(boolean isSoldBySalesUnit) {
		if (!isSoldBySalesUnit && getDefaultSalesUnit() == null) {
			return false;
		}

		for (int i = 0; i < variations.length; i++) {
			// FDVariation 		variation = variations[i];
			// FDVariationOption	options[] = variation.getVariationOptions();
			
			if (variations[i].getVariationOptions().length > 1) {
				return false;
			}
		}

		return true;
	}

	/**
	 *  Return the auto-configuration for the product, if applicable.
	 *  
	 *  @param quanitity the quantity to use in the configuration
	 *  @return the configuration describing the auto-configuration of the
	 *          product, or null if the product can not be auto-configured.
	 */
	public FDConfigurableI getAutoconfiguration(boolean isSoldBySalesUnit, double quantity) {
		if (!isAutoconfigurable(isSoldBySalesUnit)) {
			return null;
		}

		FDConfiguration ret = null;
		
		if (isSoldBySalesUnit) {
			ret = new FDConfiguration(quantity, salesUnits[0].getName(), Collections.<String,String>emptyMap());
		} else {
			ret = new FDConfiguration(quantity,
				getDefaultSalesUnit().getName() /* salesUnits[0].getName() */,
				getOptions()
			);
		}
		return ret;
	}

	/**
	 *  Return an options map, with the first variation option selected
	 *  for each variation possibility.
	 *  Use for auto-configuration.
	 *  
	 *  @return an options map reflecting the first variation options.
	 *  @see #getAutoconfiguration
	 */
	private Map<String,String> getOptions() {
		Map<String,String> options = new HashMap<String,String>();
		
		for (int i = 0; i < variations.length; i++) {
			FDVariation         variation = variations[i];
			FDVariationOption	option    = variation.getVariationOptions()[0];
			                 	           
			options.put(variation.getName(), option.getName());
		}
		
		return options;
	}
	
	/**
	 * Get the variations for this product.
	 *
	 * @return array of FDVariation objects
	 */
	public FDVariation[] getVariations() {
		return this.variations;
	}
	
	public FDVariation getVariation(String variationName) {
		for (int i = 0; i < variations.length; i++) {
			if (variationName.equals(variations[i].getName())) {
				return variations[i];
			}
		}
		return null;
	}
	
	public FDVariationOption getVariationOption(String optionName) {
		for (int i = 0; i < variations.length; i++) {
			FDVariationOption o = variations[i].getVariationOption(optionName);
			if (o != null) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Get the sales units for this product.
	 *
	 * @return array of FDSalesUnit objects
	 */
	public FDSalesUnit[] getSalesUnits() {
		return this.salesUnits;
	}

	/**
	 * Get a sales unit by name.
	 *  
	 * @param name sales unit name
	 * @return FDSalesUnit or null if no such unit exists
	 */
	public FDSalesUnit getSalesUnit(String name) {
		for (int i = 0; i < this.salesUnits.length; i++) {
			FDSalesUnit unit = this.salesUnits[i];
			if (unit.getName().equals(name)) {
				return unit;
			}
		}
		return null;
	}

	/**
	 * Try to find a valid sales unit based on requested unit.
	 * 
	 * @return FDSalesUnit or null if nothing matches
	 */
	public FDSalesUnit getDefaultSalesUnit() {
		if (salesUnits.length == 1) {
			//
			// if there's only one sales unit, pick it
			//
			return this.salesUnits[0];
		}

		//
		// just pick the SELECTED sales unit
		//
		for (int j = 0; j < this.salesUnits.length; j++) {
			FDSalesUnit unit = this.salesUnits[j];
			if (unit.getAttributes().isSelected()) {
				return unit;
			}
		}

		return null;
	}

	/**
	 * Get the pricing information that contains necessary information to perform pricing.
	 *
	 * @return Pricing object
	 */
	public Pricing getPricing() {
		return this.pricing;
	}

	public boolean hasIngredients() {
		return getNutritionModel().hasIngredients();
	}

	public String getIngredients() {
		return getNutritionModel().getIngredients();
	}

	public Set getNutritionInfoNames() {
		return getNutritionModel().getNutritionInfoNames();
	}

	public String getNutritionInfoString(ErpNutritionInfoType type) {
		return getNutritionModel().getNutritionInfoString(type);
	}

	public List getNutritionInfoList(ErpNutritionInfoType type) {
		return getNutritionModel().getNutritionInfoList(type);
	}
	
	public List<EnumClaimValue> getClaims() {
		return getNutritionInfoList( ErpNutritionInfoType.CLAIM );
	}


	public boolean hasOANClaim() {
		return hasClaim(EnumClaimValue.OAN_ORGANIC);
	}
	
	public boolean hasClaim(EnumClaimValue claimValue) {
		boolean rtnValue = false;
		List ni = getNutritionInfoList(ErpNutritionInfoType.CLAIM);

		if (ni != null) {
			if (ni.contains(claimValue)) {
				rtnValue = true;
			}
		}
		return rtnValue;
	}
	
	public boolean hasNutritionInfo(ErpNutritionInfoType type) {
		return getNutritionModel().hasNutritionInfo(type);
	}

	public FDKosherInfo getKosherInfo() {
		
		final ErpNutritionModel nutritionModel = getNutritionModel();
                EnumKosherSymbolValue kSym = nutritionModel.getKosherSymbol();
		int kPri = 999;
		if(kSym != null){
			kPri = kSym.getPriority();
		}

		EnumKosherTypeValue kTyp = nutritionModel.getKosherType();

		boolean kPrd = material.isKosherProduction();

		return new FDKosherInfo(kSym, kTyp, kPrd, kPri);
	}

	
	
    /**
     * @return
     */
    ErpNutritionModel getNutritionModel() {
        if (nutritionModel == null) {
            nutritionModel = FDNutritionCache.getInstance().getNutrition(this.getSkuCode());
        }
        return nutritionModel;
    }
    
    public void setNutritionModel(ErpNutritionModel nutritionModel) {
        this.nutritionModel = nutritionModel;
        ArrayList<FDNutrition> fdNutritionList = new ArrayList<FDNutrition>();
        if (this.nutritionModel != null) {
            // Construct FDNutrition from ErpNutritionModel.value map
            for (Iterator<String> nIter = nutritionModel.getKeyIterator(); nIter.hasNext(); ) {
                String key = nIter.next();
                FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(), nutritionModel.getValueFor(key), nutritionModel
                        .getUomFor(key));
                fdNutritionList.add(fdn);
            }
        }
        this.nutrition = fdNutritionList;
    }

	public List<FDNutrition> getNutrition() {
		return this.nutrition;
	}

	public boolean hasNutritionFacts() {
		if (nutrition.size() == 0)
			return false;
		boolean result = false;
		for (FDNutrition nutr : nutrition) {
			if (nutr.getName().equals("Ignore"))
				return false;
			if (nutr.getValue() != 0)
				result |= true;
		}
		return result;
	}

	public boolean isTaxable() {
		return this.material.isTaxable();
	}

	public boolean isPlatter() {
		return this.material.isPlatter();
	}

	public int getDepositsPerEach() {
		return this.material.getAttributes().getDepositAmount();
	}

	public boolean hasDeposit() {
		return this.getDepositsPerEach() > 0;
	}

	public boolean isPricedByLb() {
		return ("LB".equalsIgnoreCase((this.pricing.getZonePriceList().getZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE).getMaterialPrices()[0]).getPricingUnit()));
	}

	public boolean hasSingleSalesUnit() {
		return (1 == this.salesUnits.length);
	}

	public boolean isSoldByLb() {
		return (isPricedByLb() && ("LB".equalsIgnoreCase((this.salesUnits[0]).getName())));
	}

	public boolean isAlcohol() {
		return !EnumAlcoholicContent.NONE.equals(this.material.getAlcoholicContent());
	}

	public boolean isBeer() {
		return EnumAlcoholicContent.BEER.equals(this.material.getAlcoholicContent());
	}

	public boolean isWine() {
		return EnumAlcoholicContent.BC_WINE.equals(this.material.getAlcoholicContent()) || EnumAlcoholicContent.USQ_WINE.equals(this.material.getAlcoholicContent());
	}

	public boolean isDeliveryPass() {
		String value = this.getMaterial().getAttributes().getSpecialProduct();
		if (value != null) {
			return EnumSpecialProductType.DELIVERY_PASS.getName().equals(value);
		} else {
			return false;
		}
	}

	public ErpAffiliate getAffiliate() {
		if (this.isWine()) {
			if(EnumAlcoholicContent.BC_WINE.equals(this.material.getAlcoholicContent()))
				return ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
			if(EnumAlcoholicContent.USQ_WINE.equals(this.material.getAlcoholicContent()))
				return ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		}
		return ErpAffiliate.getEnum(ErpAffiliate.CODE_FD);
	}

	public boolean isQualifiedForPromotions() {
		return this.material.getAttributes().isCustPromo();
	}

	/**
	 * Check for the presence of an attribute.
	 *
	 * @param name name of the attribute
	 * @return true if the attribute was found
	 */
/*	public boolean hasAttribute(String name) {
		return this.material.hasAttribute(name);
	}
*/
	/**
	 * Get attribute as a String.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return String attribute
	 */
/*	public String getAttribute(String name, String defaultValue) {
		return this.material.getAttribute(name, defaultValue);
	}
*/
	/**
	 * Get attribute as a boolean.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return boolean attribute
	 */
/*	public boolean getAttributeBoolean(String name, boolean defaultValue) {
		return this.material.getAttributeBoolean(name, defaultValue);
	}
*/
	/**
	 * Get attribute as a integer.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return int attribute
	 * @throws ClassCastException if the specified attributeName is of different type
	 */
/*	public int getAttributeInt(EnumAttributeName attributeName) {
		return this.material.getAttributeInt(attributeName);
	}*/

	public String toString() {
		StringBuffer buf = new StringBuffer("FDProduct[");
		buf.append(this.getSkuCode()).append(" v").append(this.getVersion()).append(" pd ").append(this.getPricingDate());
		for (int i = 0; i < this.salesUnits.length; i++) {
			buf.append("\n\t").append(this.salesUnits[i].toString());
		}
		for (int i = 0; i < this.variations.length; i++) {
			buf.append("\n\t").append(this.variations[i].toString());
		}
		buf.append("\n]");
		return buf.toString();
	}

	public boolean hasAdvanceOrderFlag() {
		return this.material.getAttributes().isAdvanceOrderFlag();
	}

	public String getLabelName() {
		return this.material.getAttributes().getLabelName();
	}
        
	public FDSalesUnit[] getDisplaySalesUnits() {
		return displaySalesUnits;
	}

	public void setDisplaySalesUnits(FDSalesUnit[] displaySalesUnits) {
		this.displaySalesUnits = displaySalesUnits;
	}

	public boolean isKosherProduction() {
	    return material.isKosherProduction();
	}
}
