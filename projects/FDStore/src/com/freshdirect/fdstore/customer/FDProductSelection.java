package com.freshdirect.fdstore.customer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Price;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.TaxCalculatorUtil;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDConfiguredPrice;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDPricingEngine;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;
import com.freshdirect.storeapi.content.ProxyProduct;
import com.freshdirect.storeapi.content.SkuReference;
import com.freshdirect.storeapi.util.ProductInfoUtil;

public class FDProductSelection implements FDProductSelectionI {

	private static final long	serialVersionUID	= 4143825923906335052L;
	
	protected final static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);
	protected final static DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");
	
	private final ProductReference productRef;
	protected final ErpOrderLineModel orderLine;

	protected FDConfiguredPrice price;
	protected boolean dirty = true;
	
	private SaleStatisticsI statistics;
	private String customerListLineId;
	private boolean invalidConfig = false;
	private double fixedPrice;
	
	//used by quickshop
	private String orderId;
	private Date deliveryStartDate;
	private EnumSaleStatus saleStatus;
	
	
	public FDProductSelection(FDSku sku, ProductModel productRef, FDConfigurableI configuration, UserContext ctx) {
		this(sku, productRef, configuration, null, ctx);
	}
	
	//Introduced for Storefront 2.0
	public FDProductSelection(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, UserContext ctx,String plantId) {
		this.orderLine = new ErpOrderLineModel();

		this.orderLine.setSku(sku);
		this.productRef = new ProductReferenceImpl((productRef instanceof ProxyProduct) ? ((ProxyProduct) productRef).getProduct() : productRef);
		this.orderLine.setConfiguration( new FDConfiguration(configuration) );
		
		this.orderLine.setVariantId(variantId);
		//For now setting the default. Need to be parameterized
		this.orderLine.setUserContext(ctx);
		this.orderLine.setEStoreId(ctx.getStoreContext().getEStoreId());
		this.orderLine.setPlantID(plantId);
	}

	public FDProductSelection(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, UserContext ctx) {
		this.orderLine = new ErpOrderLineModel();

		this.orderLine.setSku(sku);
		this.productRef = new ProductReferenceImpl((productRef instanceof ProxyProduct) ? ((ProxyProduct) productRef).getProduct() : productRef);
		this.orderLine.setConfiguration( new FDConfiguration(configuration) );
		
		this.orderLine.setVariantId(variantId);
		//For now setting the default. Need to be parameterized
		this.orderLine.setUserContext(ctx);
		this.orderLine.setEStoreId(ctx.getStoreContext().getEStoreId());
		this.orderLine.setPlantID(getPickingPlantId());
	}

	protected FDProductSelection(ErpOrderLineModel orderLine, final boolean lazy) {
		this.orderLine = orderLine;

		if (!lazy) {
			this.productRef = new SkuReference(this.getSkuCode());
		} else {
			this.productRef = ProductReferenceImpl.NULL_REF;
		}
	}

	//
	// CONFIGURATION
	//

	@Override
    public FDSku getSku() {
		return this.orderLine.getSku();
	}

	@Override
    public void setSku(FDSku sku) {
		this.orderLine.setSku(sku);
		this.fireConfigurationChange();
	}

	@Override
    public ProductReference getProductRef() {
		return this.productRef;
	}

	@Override
    public FDConfigurableI getConfiguration() {
		return this.orderLine.getConfiguration();
	}

	@Override
    public void setConfiguration(FDConfigurableI configuration) {
		this.orderLine.setConfiguration(new FDConfiguration(configuration));
		this.fireConfigurationChange();
	}

	//
	// CONFIGURATION CONVENIENCE METHODS
	//

	@Override
    public String getSkuCode() {
		return this.getSku().getSkuCode();
	}

	@Override
    public int getVersion() {
		return this.getSku().getVersion();
	}

	@Override
    public String getCategoryName() {
		return this.getProductRef().getCategoryId();
	}

	@Override
    public String getProductName() {
		return this.getProductRef().getProductId();
	}

	@Override
    public double getQuantity() {
		return this.orderLine.getQuantity();
	}

	@Override
    public final void setQuantity(double quantity) {
		this.setConfiguration(
			new FDConfiguration(quantity, this.getConfiguration().getSalesUnit(), this.getConfiguration().getOptions()));
	}

	@Override
    public String getSalesUnit() {
		return this.orderLine.getSalesUnit();
	}

	@Override
    public final void setSalesUnit(String salesUnit) {
		this.setConfiguration(
			new FDConfiguration(this.getConfiguration().getQuantity(), salesUnit, this.getConfiguration().getOptions()));
	}

	@Override
    public Map<String,String> getOptions() {
		return this.orderLine.getOptions();
	}

	@Override
    public final void setOptions(Map<String,String> options) {
		this.setConfiguration(
			new FDConfiguration(this.getConfiguration().getQuantity(), this.getConfiguration().getSalesUnit(), options));
	}

	//
	// DESCRIPTIONS
	//

	@Override
    public String getDescription() {
		return this.orderLine.getDescription();
	}

	@Override
    public void setDescription(String desc) {
		this.orderLine.setDescription(desc);
	}

	@Override
    public String getDepartmentDesc() {
		return this.orderLine.getDepartmentDesc();
	}

	@Override
    public void setDepartmentDesc(String deptDesc) {
		this.orderLine.setDepartmentDesc(deptDesc);
	}

	@Override
    public String getConfigurationDesc() {
		return this.orderLine.getConfigurationDesc();
	}

	@Override
    public void setConfigurationDesc(String configDesc) {
		this.orderLine.setConfigurationDesc(configDesc);
	}

	//
	// DIRTY CHECKING
	//

	protected void fireConfigurationChange() {
		this.dirty = true;
	}

	protected boolean isDirty() {
		return this.dirty;
	}

	@Override
    public void refreshConfiguration() throws FDResourceException, FDInvalidConfigurationException {
		if (this.dirty) {

			FDProduct fdProduct = this.lookupFDProduct();
			if (fdProduct != null) {
				this.orderLine.setMaterialNumber(fdProduct.getMaterial().getMaterialNumber());
				this.orderLine.setAlcohol(fdProduct.isAlcohol());
				this.orderLine.setWine(fdProduct.isWine());
				this.orderLine.setBeer(fdProduct.isBeer());
				this.orderLine.setAffiliate(fdProduct.getAffiliate(this.getUserContext().getStoreContext().getEStoreId()));
				this.orderLine.setDeliveryPass(fdProduct.isDeliveryPass());
				this.orderLine.setTaxCode(fdProduct.getTaxCode());
				this.orderLine.setMaterialGroup(fdProduct.getMaterialGroup());
			}
			
			this.performPricing();

			
			if (this.productRef == null || ProductReferenceImpl.NULL_REF.equals( this.productRef )) {
				// salvage original descriptions
				this.setDepartmentDesc(this.orderLine.getDepartmentDesc());
				this.setDescription(this.orderLine.getDescription());
				this.setConfigurationDesc(this.orderLine.getConfigurationDesc());				
			} else {			
				ProductModel pm = this.lookupProduct();
				if (pm != null) {
					this.orderLine.setPerishable(pm.isPerishable());
					OrderLineUtil.describe(this);				
				}
			}

			if(this.lookupProduct()!=null){
				OrderLineUtil.describe(this);
				if(null != orderLine.getDiscount() && EnumDiscountType.FREE.equals(orderLine.getDiscount().getDiscountType())){
					orderLine.setDepartmentDesc("FREE SAMPLE(S)");					
				}
			}

			this.dirty = false;
		}
	}

	//
	// CONVENIENCE
	//

	@Override
    public ProductModel lookupProduct() {
		// In CRM no product models are expected
		if (this.productRef == null || ProductReferenceImpl.NULL_REF.equals(productRef)) {
			return null;
		}
		
        return ProductPricingFactory.getInstance().getPricingAdapter(this.productRef.lookupProductModel());
	}

	@Override
    public FDProduct lookupFDProduct() {
		try {
			return FDCachedFactory.getProduct(this.orderLine.getSku());
		} catch (FDSkuNotFoundException e) {
			return null;
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	@Override
    public FDProductInfo lookupFDProductInfo() {
		try {
			return FDCachedFactory.getProductInfo(this.getSkuCode());
		} catch (FDSkuNotFoundException e) {
			return null;
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	//
	// PRICING
	//

	public double getPrice() {
		return this.orderLine.getPrice();
	}

	public double getSaveAmount() {
		// return 0 for free and sample items.
		if (this.orderLine.getDiscount() != null && (
				this.orderLine.getDiscount().getDiscountType() == EnumDiscountType.FREE ||
				this.orderLine.getDiscount().getDiscountType() == EnumDiscountType.SAMPLE)) {
			return 0;
		}
		
		double currentPrice = getPrice();
		double originalPrice = (this.orderLine.getUnscaledPrice() == 0? this.price.getOriginalPrice() * getQuantity() : this.orderLine.getUnscaledPrice());
		if (originalPrice > currentPrice) {
			return (originalPrice - currentPrice);
		}
		return 0;
	}
	
	public double getActualPrice() {
		return this.orderLine.getActualPrice();
	}
	
	public double getTaxRate() {
		return this.orderLine.getTaxRate();
	}
	
	public EnumTaxationType getTaxationType() {
		return this.orderLine.getTaxationType();
	}
	
	public void setTaxRate(double taxRate){
		if(!this.lookupFDProduct().isTaxable() && !FDStoreProperties.getAvalaraTaxEnabled()){
			taxRate = 0.0;
		}
		this.orderLine.setTaxRate(taxRate);
	}

	public double getTaxValue() {
//		return MathUtil.roundDecimal((getConfiguredPrice() - getPromotionValue() - getCouponDiscountValue()) * getTaxRate());
		return TaxCalculatorUtil.getTaxValue(getConfiguredPrice(),getPromotionValue(), getCouponDiscountValue(), getTaxRate(), getTaxationType());
	}

	public double getDepositValue() {
		return this.orderLine.getDepositValue();
	}
	
	public void setDepositValue(double depositRate){
		double val = MathUtil.roundDecimal(getQuantity() * this.lookupFDProduct().getDepositsPerEach() * depositRate);
		this.orderLine.setDepositValue(val);
	}

	public double getPromotionValue() {
		return this.price.getPromotionValue();
	}

	public double getCouponDiscountValue() {
		return this.price.getCouponDiscountValue();
	}
	
	@Override
    public String getUnitPrice() {
		// dirty requirement so got to do this
		Price discountP=null;
		double disAmount=this.price.getBasePrice();
		Price p=new Price(this.price.getBasePrice());
		if(this.getDiscount()!=null){
			if(this.getDiscount().getSkuLimit() > 0 && !this.price.getBasePriceUnit().equalsIgnoreCase("lb") && this.getDiscount().getSkuLimit() != this.getQuantity()) {
				
				disAmount=this.price.getBasePrice();
				
			//APPDEV-4148-Displaying unit price if quantity is greater than sku limit - START
				
				if(this.getDiscount().getSkuLimit() < this.getQuantity()){
					if(this.getDiscount().getDiscountType().equals(EnumDiscountType.PERCENT_OFF))
					{											
						disAmount=((this.price.getBasePrice() * this.getQuantity()) - ((this.price.getBasePrice() * this.getDiscount().getSkuLimit()) * this.getDiscount().getAmount() )) / this.getQuantity();
					}
					else
					{
						disAmount=((this.price.getBasePrice() * (this.getQuantity())) - (this.getDiscount().getAmount() * this.getDiscount().getSkuLimit()))/ this.getQuantity() ;
					}
				}
				
			//APPDEV-4148-Displaying unit price if quantity is greater than sku limit - END
				
			} else if(this.getDiscount().getSkuLimit() > 0 && this.price.getBasePriceUnit().equalsIgnoreCase("lb") && this.getDiscount().getDiscountType().equals(EnumDiscountType.DOLLAR_OFF)) {
				disAmount=this.price.getBasePrice();
			} else {
				if(this.getDiscount().getMaxPercentageDiscount() > 0) {
					disAmount = this.orderLine.getPrice()/this.orderLine.getQuantity();
				} else {
					try {
						discountP=PricingEngine.applyDiscount(p,1,this.getDiscount(),this.price.getBasePriceUnit());
						disAmount=discountP.getBasePrice();
					} catch (PricingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		//Apply the coupon discount on top of line item discount and calculate the final base price.
		if (this.getCouponDiscount() != null) {
			try {
				discountP = PricingEngine.applyCouponDiscount(null!=discountP?discountP:p, 1/this.getQuantity(), this.getCouponDiscount(), this.price.getBasePriceUnit());
				disAmount = discountP.getBasePrice();
			} catch (PricingException e) {
				disAmount = 0.0;
			}
		} 
			 
		return CURRENCY_FORMATTER.format(disAmount) + "/" + this.price.getBasePriceUnit().toLowerCase();
	}

	protected ErpCouponDiscountLineModel getCouponDiscount() {
		return this.orderLine.getCouponDiscount();
	}
	
	@Override
    public double getConfiguredPrice() {
		return this.price.getConfiguredPrice();
	}

	//
	// CONVENIENCE
	//

	@Override
    public boolean isAlcohol() {
		return this.orderLine.isAlcohol();
	}
	
	@Override
    public boolean isWine() {
		return this.orderLine.isWine();
	}

	@Override
    public boolean isBeer() {
		return this.orderLine.isBeer();
	}

	@Override
    public boolean isPerishable() {
		return this.orderLine.isPerishable();
	}

	@Override
    public boolean isKosher() {
		try {
			FDProduct pr = lookupFDProduct();
			FDProductInfo prodInfo = this.lookupFDProductInfo();
			//return pr.isKosherProduction(getUserContext().getFulfillmentContext().getPlantId());
			return pr.isKosherProduction(ProductInfoUtil.getPickingPlantId(prodInfo));
		} catch (Exception exc) {
			return false;
		}
	}

	@Override
    public boolean isPlatter() {
		try {
			FDProduct fdProduct = this.lookupFDProduct();
			FDProductInfo prodInfo = this.lookupFDProductInfo();
			//return fdProduct.isPlatter(getUserContext().getFulfillmentContext().getPlantId());
			return fdProduct.isPlatter(ProductInfoUtil.getPickingPlantId(prodInfo));
		} catch (Exception exc) {
			return false;
		}
	}

	@Override
    public boolean isSoldBySalesUnits() {
		ProductModel productNode = this.lookupProduct();
		return productNode == null ? false : productNode.isSoldBySalesUnits();
	}

	@Override
    public boolean isPricedByLb() {
		FDProduct fdProduct = this.lookupFDProduct();
		return fdProduct.isPricedByLb();
	}

	@Override
    public boolean isSoldByLb() {
		FDProduct fdProduct = this.lookupFDProduct();
		return fdProduct.isSoldByLb();
	}

	public boolean hasScaledPricing() {
		FDProduct fdProduct = this.lookupFDProduct();
		Pricing price = fdProduct.getPricing();
		ZonePriceModel zpm=price.getZonePrice(getUserContext().getPricingContext().getZoneInfo());
		MaterialPrice[] materialPrices=null;
		if(zpm!=null)
			materialPrices = zpm.getMaterialPrices();
		else return false;
		
		if(materialPrices==null)
			return false;
		return !materialPrices[0].isWithinBounds(this.getQuantity());
	}

	public boolean isEstimatedPrice() {
		FDProduct fdProduct = this.lookupFDProduct();

		boolean displaySalesUnitsOnly =
			isSoldBySalesUnits() || (!fdProduct.hasSingleSalesUnit() && fdProduct.isSoldByLb() && fdProduct.isPricedByLb());
		boolean displayEstimatedQuantity = !displaySalesUnitsOnly && fdProduct.isPricedByLb() && !fdProduct.isSoldByLb();

		return displayEstimatedQuantity || fdProduct.isPricedByLb();
	}

	//
	// FORMATTING, DISPLAY
	// 

	@Override
    public String getSalesUnitDescription() {
		return "LB".equalsIgnoreCase(this.getSalesUnit()) ? "lb" : "";
	}

	protected FDSalesUnit lookupFDSalesUnit() {
		return this.lookupFDProduct().getSalesUnit(this.getSalesUnit());
	}

	public String getOrderedQuantity() {
		if (this.isSoldBySalesUnits()) {
			return this.lookupFDSalesUnit().getDescriptionQuantity();

		} else {
			return QUANTITY_FORMATTER.format(this.getQuantity());
		}
	}

	public String getUnitsOfMeasure() {
		return this.isSoldBySalesUnits() ? this.lookupFDSalesUnit().getDescriptionUnit() : "";
	}

	@Override
    public String getDisplayQuantity() {
		StringBuffer qty = new StringBuffer();
		if (this.isSoldBySalesUnits()) {
			FDSalesUnit unit = this.lookupFDSalesUnit();
			qty.append(unit.getDescriptionQuantity());
			qty.append(" ");
			qty.append(unit.getDescriptionUnit());
		} else {
			qty.append(QUANTITY_FORMATTER.format(this.getQuantity()));
		}
		return qty.toString();
	}

	@Override
    public String getLabel() {
		ProductModel prod = this.lookupProduct();
		String quantText = prod == null ? null : prod.getQuantityTextSecondary();
		if (quantText != null) {
			if ("lb".equalsIgnoreCase(quantText) || "oz".equalsIgnoreCase(quantText)) {
				return quantText;
			}
			return this.getSalesUnitDescription();
		}
		return this.isSoldByLb() ? "lb" : "";
	}

	@Override
    public ErpAffiliate getAffiliate() {
		return this.lookupFDProduct().getAffiliate(this.orderLine.getUserContext().getStoreContext().getEStoreId());
	}

	protected void performPricing() {
		String pricingUnit = "";
		if(this.lookupFDProduct().isPricedByLb()) {
			pricingUnit = "lb";
		}
		try {
			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(this.getSkuCode()) || FDStoreProperties.getRobinHoodSkucode().equalsIgnoreCase(this.getSkuCode())){
				this.price = FDPricingEngine.doPricing(this.lookupFDProduct(), this, this.getDiscount(), this.orderLine.getUserContext().getPricingContext(), null, 0.0, pricingUnit,null,this.orderLine.getScaleQuantity());
				this.orderLine.setPrice(this.getFixedPrice());
				this.orderLine.setDiscountAmount(0);
			}else
			{
				FDGroup group = this.getFDGroup();

				this.price = FDPricingEngine.doPricing(this.lookupFDProduct(), this, this.getDiscount(), this.orderLine.getUserContext().getPricingContext(), group, this.getGroupQuantity(), pricingUnit,this.getCouponDiscount(),this.orderLine.getScaleQuantity());
				this.orderLine.setPrice(price.getConfiguredPrice() - price.getPromotionValue() - price.getCouponDiscountValue());
				this.orderLine.setDiscountAmount(price.getPromotionValue());
				this.orderLine.setPricingZoneId(price.getZoneInfo().getPricingZoneId());
				this.orderLine.setSalesOrg(price.getZoneInfo().getSalesOrg());
				this.orderLine.setDistChannel(price.getZoneInfo().getDistributionChanel());
				this.orderLine.setUnscaledPrice(price.getUnscaledPrice());
			}	
			
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$Price is set here?" + this.getDescription() + " -price:" + this.orderLine.getPrice() + " -discount:" + this.orderLine.getDiscountAmount());

		} catch (PricingException e) {
			throw new FDRuntimeException(e, "PricingException occured on "+getSkuCode() + " - " + getConfiguration());
		}
	}

	/** template method */
	protected Discount getDiscount() {
		return this.orderLine.getDiscount();
	}

	@Override
    public SaleStatisticsI getStatistics() {
		return this.statistics;
	}
	
	@Override
    public void setStatistics(SaleStatisticsI stats){
		this.statistics = stats;
	}

	@Override
    public String getCustomerListLineId() {
		return customerListLineId;
	}

	@Override
    public void setCustomerListLineId(String customerListLineId) {
		this.customerListLineId = customerListLineId;
	}
	
	public void setInvalidConfig(boolean invalidConfig){
		this.invalidConfig = invalidConfig;
	}
	
	@Override
    public boolean isInvalidConfig(){
		return invalidConfig;
	}
	
	@Override
    public String getRecipeSourceId() {
		return this.orderLine.getRecipeSourceId();
	}
	
	@Override
    public void setRecipeSourceId(String recipeSourceId) {
		this.orderLine.setRecipeSourceId(recipeSourceId);
	}
	
	@Override
    public String getYmalCategoryId() {
		return this.orderLine.getYmalCategoryId();
	}
	
	@Override
    public void setYmalCategoryId(String ymalCategoryId) {
		this.orderLine.setYmalCategoryId(ymalCategoryId);
	}
	
	@Override
    public String getYmalSetId() {
		return this.orderLine.getYmalSetId();
	}
	
	@Override
    public void setYmalSetId(String ymalSetId) {
		this.orderLine.setYmalSetId(ymalSetId);
	}
	
	@Override
    public String getOriginatingProductId() {
		return this.orderLine.getOriginatingProductId();
	}
	
	@Override
    public void setOriginatingProductId(String originatingProductId) {
		this.orderLine.setOriginatingProductId(originatingProductId);
	}
	
	@Override
    public boolean isRequestNotification() {
		return orderLine.isRequestNotification();
	}
	
	@Override
    public void setRequestNotification(boolean requestNotification) {
		this.orderLine.setRequestNotification(requestNotification);
	}

	@Override
    public EnumOrderLineRating getProduceRating() {
		return orderLine.getProduceRating();
	}


	public double getDiscountAmount() {
		return orderLine.getDiscountAmount();
	}
	public void setDiscountAmount(double discountAmount) {
		this.orderLine.setDiscountAmount(discountAmount);
	}
	
	@Override
    public void setFDGroup(FDGroup group) {
        this.orderLine.setFDGroup(group);
	}
	
	 @Override
    public FDGroup getFDGroup() {
		 FDGroup group = this.orderLine.getFDGroup();
			if(group == null) {//If not in the line item level check sku level.
				FDProductInfo _p=this.lookupFDProductInfo();
				if(_p!=null){
//					group = _p.getGroup(this.getUserContext().getPricingContext().getZoneInfo().getSalesOrg(),this.getUserContext().getPricingContext().getZoneInfo().getDistributionChanel());
					group = _p.getGroup(this != null && this.getUserContext()!=null && this.getUserContext().getPricingContext() !=null ? this.getUserContext().getPricingContext().getZoneInfo() : null);
				}
			}
		 
		 return group;
	 }
	
	public boolean isDiscountFlag(){
		return orderLine.isDiscountFlag();
	}
	
	public void setDiscountFlag(boolean discountApplied){
		this.orderLine.setDiscountFlag(discountApplied);
	}

	@Override
    public double getFixedPrice() {
		return this.fixedPrice;
	}

	@Override
    public void setFixedPrice(double price) {
		this.fixedPrice=price;
	}
	
	@Override
    public UserContext getUserContext() {
		return orderLine.getUserContext();
	}
	
	@Override
    public void setUserContext(UserContext uCtx) {
		this.orderLine.setUserContext(uCtx);
		this.orderLine.setPlantID(getPickingPlantId());
	}
	
	@Override
    public List<ErpClientCode> getClientCodes() {
		return this.orderLine.getClientCodes();
	}
	
	@Override
    public boolean hasBrandName(Set<String> brandNames) {
			ProductModel pm = ContentFactory.getInstance().getProductByName(this.getCategoryName(), this.getProductName());
			if(pm != null && pm.getBrands() != null) {
				for ( BrandModel brand : pm.getBrands() ) {
					if (brandNames.contains(brand.getContentName())) {
						return true;
					}
				}
			}
		return false;
	}
	
	@Override
    public void setGroupQuantity(double quantity){
		this.orderLine.setGroupQuantity(quantity);
		this.fireConfigurationChange();
	}
	
	@Override
    public double getGroupQuantity(){
		return this.orderLine.getGroupQuantity();
	}
	
	@Override
    public double getGroupScaleSavings() {
		double savings = 0.0;
		try {
				FDGroup group = this.getFDGroup();
				if(group != null) {
					//System.out.println("getGroupScaleSavings=>"+this.lookupFDProduct().getSkuCode());
					FDConfiguredPrice regPrice = FDPricingEngine.doPricing(this.lookupFDProduct(), this, this.getDiscount(), this.orderLine.getUserContext().getPricingContext(), group, 0.0, this.price.getBasePriceUnit(),this.getCouponDiscount(),this.orderLine.getScaleQuantity());
					savings = regPrice.getConfiguredPrice() - (regPrice.getPromotionValue() + this.orderLine.getPrice()+regPrice.getCouponDiscountValue());
				}
		} catch (PricingException e) {
			throw new FDRuntimeException(e, "PricingException occured in getGroupScaleSavings() on "+getSkuCode() + " - " + getConfiguration());
		}
		return savings;
	}
	@Override
	public EnumSustainabilityRating getSustainabilityRating() {
		return orderLine.getSustainabilityRating();
	}
	
	@Override
    public double getBasePrice() {
		if(this.price == null) {
			return 0.0;
		}
		return this.price.getBasePrice();
	}
	
	@Override
    public String getUpc(){
		FDProductInfo prodInfo =lookupFDProductInfo();
		return null !=prodInfo?prodInfo.getUpc():"";
	}

	@Override
    public Date getDeliveryStartDate() {
		return deliveryStartDate;
	}

	@Override
    public void setDeliveryStartDate(Date deliveryStartDate) {
		this.deliveryStartDate = deliveryStartDate;
	}

	@Override
    public String getOrderId() {
		return orderId;
	}

	@Override
    public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Override
    public EnumSaleStatus getSaleStatus() {
		return saleStatus;
	}

	@Override
    public void setSaleStatus(EnumSaleStatus saleStatus) {
		this.saleStatus = saleStatus;
	}
	
	public void setOrderLineId(String orderLineId){
		this.orderLine.setOrderLineId(orderLineId);
	}
	
	@Override
    public String getOrderLineId(){
		return this.orderLine.getOrderLineId();
	}

	@Override
	public String getExternalGroup() {
		return this.orderLine.getExternalGroup();
	}

	@Override
	public void setExternalGroup(String externalGroup) {
		this.orderLine.setExternalGroup(externalGroup);
	}

	@Override
	public ExternalAgency getExternalAgency() {
		return this.orderLine.getExternalAgency();
	}

	@Override
	public void setExternalAgency(ExternalAgency externalAgency) {
		this.orderLine.setExternalAgency(externalAgency);
	}

	@Override
	public String getExternalSource() {
		return this.orderLine.getExternalSource();
	}

	@Override
	public void setExternalSource(String externalSource) {
		this.orderLine.setExternalSource(externalSource);
	}

	@Override
	public Double getScaleQuantity() {
		this.orderLine.getScaleQuantity();
		return null;
	}

	@Override
	public void setScaleQuantity(Double scaleQuantity) {
		this.orderLine.setScaleQuantity(scaleQuantity);
		this.fireConfigurationChange();		
	}
	
	protected String getPickingPlantId(){
		String pickingPlantId = null;
		FDProductInfo prodInfo = this.lookupFDProductInfo();
		if(null != getUserContext()){
			if(null != prodInfo && null !=getUserContext().getPricingContext() && null!=getUserContext().getPricingContext().getZoneInfo()){
				pickingPlantId = prodInfo.getPickingPlantId(getUserContext().getPricingContext().getZoneInfo().getSalesOrg(),getUserContext().getPricingContext().getZoneInfo().getDistributionChanel());
				/*if(pickingPlantId == null){
					LOGGER.info("PickingPlantId is not found for this product: "+prodInfo.getSkuCode()+", for customer:"+getUserContext().getFdIdentity());
				}*/
			}			
			if(null == pickingPlantId && null !=getUserContext().getFulfillmentContext()){
//				LOGGER.info("PickingPlantId is not found for this product: "+prodInfo.getSkuCode());
				pickingPlantId = getUserContext().getFulfillmentContext().getPlantId();				
			}
		}
		if("1000".equalsIgnoreCase(pickingPlantId)){//TODO: Needs to be cleaned up, as part of LIC sunset proj
        	pickingPlantId ="1400";
        }
		return pickingPlantId;
	}

	//Introduced for Storefront 2.0 Only 
	/**
	 * @return the orderLine
	 */
	public ErpOrderLineModel getOrderLine() {
		return orderLine;
	}
	public boolean getDirty() {
		return this.dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void setPrice(FDConfiguredPrice price) {
		this.price = price;
	}
	
	
	
}
