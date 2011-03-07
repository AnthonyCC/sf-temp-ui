package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpOderLine interface
 *
 * @version    $Revision:15$
 * @author     $Author:Kashif Nadeem$
 * @stereotype fd-model
 */
public class ErpOrderLineModel extends ModelSupport implements FDConfigurableI {

	private static final long	serialVersionUID	= 6088652627874909097L;
	
	private String orderLineNumber;
    private String orderLineId;
    
    private String affiliateCode;

    private FDSku sku;
    private FDConfiguration configuration;
    
    private Discount discount;
    private String materialNumber;
	private String description;
	private String configurationDesc;
	private String departmentDesc;
    private double price;
	private boolean perishable;
    private double taxRate;
    private double depositValue;
    private boolean alcohol;
    
    private String recipeSourceId;
    private String cartLineId;
    private boolean requestNotification;
    private boolean deliveryPass;
    private double discountAmount=0.0;
    // SORI
    private boolean isDiscountApplied=false;
    
    // produce rating
    
    private EnumOrderLineRating produceRating=null;
    
	private FDGroup group;
    
	private double grpQuantity;
	
// sustainability rating
    
    private EnumSustainabilityRating sustainabilityRating=null;
	
    public FDGroup getFDGroup() {
		return group;
	}

	public void setFDGroup(FDGroup group) {
		this.group = group;
	}

	public void setGroupQuantity(double quantity){
		this.grpQuantity = quantity;
	}
	
	public double getGroupQuantity(){
		return this.grpQuantity;
	}
	

	/**
     *  The ID of the category shown when this product was displayed as a YMAL
     *  when added to the cart. Optional property.
     */
    private String ymalCategoryId;
    
    /**
     *  The ID of the YMAL set this product was displayed in when added
     *  to the cart. Optional property.
     */
    private String ymalSetId;
    
    /**
     *  The ID of the product this product was displayed as a YMAL for when added
     *  to the cart. Optional property.
     */
    private String originatingProductId;
   
    
    /**
     * SmartStore DYF
     * Variant ID of the recommended product
     * Optional property.
     */
    private String variantId;
    
    /*
     * Deals 
     */
    private double basePrice;
    private String basePriceUnit;
    
    /** 
     * Savings ID which is nothing variant id used only for tracking smart savings
     * @return
     */ 
    
    private String savingsId;
    
    //Added for Zone Pricing
    private PricingContext pricingCtx;
    private String pricingZoneId;
    
    private List<ErpClientCode> clientCodes = new ArrayList<ErpClientCode>();
    
    public String getSavingsId() {
		return savingsId;
	}
    
	public void setSavingsId(String savingsId) {
		this.savingsId = savingsId;
	}
	
	public boolean isDeliveryPass() {
		return deliveryPass;
	}
	public void setDeliveryPass(boolean deliveryPass) {
		this.deliveryPass = deliveryPass;
	}
	public String getOrderLineNumber(){ return orderLineNumber; }
    public void setOrderLineNumber(String orderLineNumber){ this.orderLineNumber = orderLineNumber; }
	
    public String getOrderLineId() {
		return this.orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	public ErpAffiliate getAffiliate() {
		ErpAffiliate affiliate = ErpAffiliate.getEnum(this.affiliateCode);
		return affiliate == null ? ErpAffiliate.getEnum(ErpAffiliate.CODE_FD) : affiliate;
	}

	public void setAffiliate(ErpAffiliate affiliate) {
		this.affiliateCode = affiliate == null ? null : affiliate.getCode();
	}

	public FDSku getSku() {
		return this.sku;
	}

	public void setSku(FDSku sku) {
		this.sku = sku;
	}

	public String getSalesUnit() {
		return this.configuration.getSalesUnit();
	}

	public double getQuantity() {
		return this.configuration.getQuantity();
	}

	public Map<String,String> getOptions() {
		return this.configuration.getOptions();
	}

	public FDConfiguration getConfiguration() {
		return this.configuration;
	}
	public void setConfiguration(FDConfiguration configuration) {
		this.configuration = configuration;
	}

    public Discount getDiscount(){ return discount; }
    public void setDiscount(Discount discount){ this.discount = discount; }

	public String getMaterialNumber( ){ return this.materialNumber; }
	public void setMaterialNumber(String materialNumber){ this.materialNumber = materialNumber; }

	public String getDescription() { return description; }
	public void setDescription(String description){ this.description = description; }

	public String getConfigurationDesc() { return configurationDesc; }
	public void setConfigurationDesc(String configurationDesc) { this.configurationDesc = configurationDesc; }

	public String getDepartmentDesc() { return departmentDesc; }
	public void setDepartmentDesc(String departmentDesc){ this.departmentDesc = departmentDesc; }

    public double getPrice(){ return price; }
    public void setPrice(double price){ 
    	this.price = price; 
    }

	public boolean isPerishable(){ return this.perishable; }
	public void setPerishable(boolean perishable) { this.perishable = perishable; }
	
	public boolean isAlcohol(){ return this.alcohol; }
	public void setAlcohol(boolean alcohol){ this.alcohol = alcohol; }
				
    public double getTaxRate(){ return this.taxRate; }
    public void setTaxRate(double taxRate){ this.taxRate = taxRate; }

	public double getDepositValue() {	return this.depositValue;	}
	public void setDepositValue(double depositValue) { this.depositValue = depositValue; }
    
    public String getCartlineId() {
    	return this.cartLineId;
    }
    
    public void setCartlineId(String cartlineId) {
    	this.cartLineId = cartlineId;
    }
    
    public String getRecipeSourceId() {
		return recipeSourceId;
	}
    
    public void setRecipeSourceId(String recipeSourceId) {
		this.recipeSourceId = recipeSourceId;
	}

    public String getYmalCategoryId() {
		return ymalCategoryId;
	}
    
    public void setYmalCategoryId(String ymalCategoryId) {
		this.ymalCategoryId = ymalCategoryId;
	}

    public String getYmalSetId() {
		return ymalSetId;
	}
    
    public void setYmalSetId(String ymalSetId) {
		this.ymalSetId = ymalSetId;
	}

    public String getOriginatingProductId() {
		return originatingProductId;
	}
    
    public void setOriginatingProductId(String originatingProductId) {
		this.originatingProductId = originatingProductId;
	}

    public boolean isRequestNotification() {
		return requestNotification;
	}
    
    public void setRequestNotification(boolean requestNotification) {
		this.requestNotification = requestNotification;
	}

    
    public String getVariantId() {
    	return this.variantId;
    }
    
    public void setVariantId(String variantId) {
    	this.variantId = variantId;
    }
	public EnumOrderLineRating getProduceRating() {
		return produceRating;
	}
	public void setProduceRating(EnumOrderLineRating produceRating) {
		this.produceRating = produceRating;
	}
    public double getBasePrice(){ return basePrice; }
    public void setBasePrice(double price){ this.basePrice = price; }
    
    public String getBasePriceUnit(){ return basePriceUnit; }
    public void setBasePriceUnit(String unit){ this.basePriceUnit = unit; }
    
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public boolean isDiscountFlag() {
		return isDiscountApplied;
	}
	public void setDiscountFlag(boolean isDiscountApplied) {
		this.isDiscountApplied = isDiscountApplied;
	}
    public double getActualPrice() {
    	Discount d = this.getDiscount();
    	if(d == null || d.getDiscountType().isSample()) 
    			return this.getPrice();
    	return this.getPrice() + this.getDiscountAmount();
    }

	public PricingContext getPricingContext() {
		return pricingCtx;
	}

	public void setPricingContext(PricingContext pricingCtx) {
		this.pricingCtx = pricingCtx;
	}
	
	public String getPricingZoneId() {
	      return this.pricingCtx!=null? this.pricingCtx.getZoneId():"";
	     //return pricingZoneId;
	}


	public void setPricingZoneId(String pricingZoneId) {
		this.pricingZoneId = pricingZoneId;
	}

	public List<ErpClientCode> getClientCodes() {
		return clientCodes;
	}

	public EnumSustainabilityRating getSustainabilityRating() {
		return sustainabilityRating;
	}

	public void setSustainabilityRating(EnumSustainabilityRating sustainabilityRating) {
		this.sustainabilityRating = sustainabilityRating;
	}
}


