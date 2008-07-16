package com.freshdirect.fdstore.customer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDConfiguredPrice;
import com.freshdirect.fdstore.FDPricingEngine;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.framework.util.MathUtil;

public class FDProductSelection implements FDProductSelectionI {

	protected final static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);
	protected final static DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");

	private final ProductRef productRef;
	protected final ErpOrderLineModel orderLine;

	protected FDConfiguredPrice price;
	protected boolean dirty = true;
	
	private SaleStatisticsI statistics;
	private String customerListLineId;
	private boolean invalidConfig = false;

	public FDProductSelection(FDSku sku, ProductRef productRef, FDConfigurableI configuration) {
		this(sku, productRef, configuration, null);
	}

	public FDProductSelection(FDSku sku, ProductRef productRef, FDConfigurableI configuration, String variantId) {
		this.orderLine = new ErpOrderLineModel();

		this.orderLine.setSku(sku);
		this.productRef = productRef;
		this.orderLine.setConfiguration( new FDConfiguration(configuration) );
		
		this.orderLine.setVariantId(variantId);
	}

	protected FDProductSelection(ErpOrderLineModel orderLine) {
		this.orderLine = orderLine;
		
		ProductModel pm;
		try {
			pm = ContentFactory.getInstance().getProduct(this.getSkuCode());
		} catch (FDSkuNotFoundException e) {
			pm = null;
		}

		this.productRef = pm == null ? new ProductRef("unknown", "unknown") : pm.getProductRef();
	}

	//
	// CONFIGURATION
	//

	public FDSku getSku() {
		return this.orderLine.getSku();
	}

	public void setSku(FDSku sku) {
		this.orderLine.setSku(sku);
		this.fireConfigurationChange();
	}

	public ProductRef getProductRef() {
		return this.productRef;
	}

	public FDConfigurableI getConfiguration() {
		return this.orderLine.getConfiguration();
	}

	public void setConfiguration(FDConfigurableI configuration) {
		this.orderLine.setConfiguration(new FDConfiguration(configuration));
		this.fireConfigurationChange();
	}

	//
	// CONFIGURATION CONVENIENCE METHODS
	//

	public String getSkuCode() {
		return this.getSku().getSkuCode();
	}

	public int getVersion() {
		return this.getSku().getVersion();
	}

	public String getCategoryName() {
		return this.getProductRef().getCategoryName();
	}

	public String getProductName() {
		return this.getProductRef().getProductName();
	}

	public double getQuantity() {
		return this.orderLine.getQuantity();
	}

	public final void setQuantity(double quantity) {
		this.setConfiguration(
			new FDConfiguration(quantity, this.getConfiguration().getSalesUnit(), this.getConfiguration().getOptions()));
	}

	public String getSalesUnit() {
		return this.orderLine.getSalesUnit();
	}

	public final void setSalesUnit(String salesUnit) {
		this.setConfiguration(
			new FDConfiguration(this.getConfiguration().getQuantity(), salesUnit, this.getConfiguration().getOptions()));
	}

	public Map getOptions() {
		return this.orderLine.getOptions();
	}

	public final void setOptions(Map options) {
		this.setConfiguration(
			new FDConfiguration(this.getConfiguration().getQuantity(), this.getConfiguration().getSalesUnit(), options));
	}

	//
	// DESCRIPTIONS
	//

	public String getDescription() {
		return this.orderLine.getDescription();
	}

	public void setDescription(String desc) {
		this.orderLine.setDescription(desc);
	}

	public String getDepartmentDesc() {
		return this.orderLine.getDepartmentDesc();
	}

	public void setDepartmentDesc(String deptDesc) {
		this.orderLine.setDepartmentDesc(deptDesc);
	}

	public String getConfigurationDesc() {
		return this.orderLine.getConfigurationDesc();
	}

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

	public void refreshConfiguration() throws FDResourceException, FDInvalidConfigurationException {
		if (this.dirty) {

			FDProduct fdProduct = this.lookupFDProduct();
			if (fdProduct != null) {
				this.orderLine.setMaterialNumber(fdProduct.getMaterial().getMaterialNumber());
				this.orderLine.setAlcohol(fdProduct.isAlcohol());
				this.orderLine.setAffiliate(fdProduct.getAffiliate());
				this.orderLine.setDeliveryPass(fdProduct.isDeliveryPass());
			}

			this.performPricing();

			ProductModel pm = this.lookupProduct();
			if (pm != null) {
				this.orderLine.setPerishable(pm.isPerishable());
			}

			OrderLineUtil.describe(this);

			this.dirty = false;
		}
	}

	//
	// CONVENIENCE
	//

	public ProductModel lookupProduct() {
		return this.productRef.lookupProduct();
	}

	public FDProduct lookupFDProduct() {
		try {
			return FDCachedFactory.getProduct(this.orderLine.getSku());
		} catch (FDSkuNotFoundException e) {
			return null;
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

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

	public double getTaxRate() {
		return this.orderLine.getTaxRate();
	}
	
	public void setTaxRate(double taxRate){
		if(!this.lookupFDProduct().isTaxable()){
			taxRate = 0.0;
		}
		this.orderLine.setTaxRate(taxRate);
	}

	public double getTaxValue() {
		return MathUtil.roundDecimal((getConfiguredPrice() - getPromotionValue()) * getTaxRate());
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

	public String getUnitPrice() {
		return CURRENCY_FORMATTER.format(this.price.getBasePrice()) + "/" + this.price.getBasePriceUnit().toLowerCase();
	}

	protected double getConfiguredPrice() {
		return this.price.getConfiguredPrice();
	}

	//
	// CONVENIENCE
	//

	public boolean isAlcohol() {
		return this.orderLine.isAlcohol();
	}

	public boolean isPerishable() {
		return this.orderLine.isPerishable();
	}

	public boolean isKosher() {
		try {
			ProductModel pm = this.lookupProduct();
			return pm == null ? false : pm.isKosherProductionItem();
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	public boolean isPlatter() {
		FDProduct fdProduct = this.lookupFDProduct();
		return fdProduct.isPlatter();
	}

	public boolean isSoldBySalesUnits() {
		ProductModel productNode = this.lookupProduct();
		return productNode == null ? false : productNode.isSoldBySalesUnits();
	}

	public boolean isPricedByLb() {
		FDProduct fdProduct = this.lookupFDProduct();
		return fdProduct.isPricedByLb();
	}

	public boolean isSoldByLb() {
		FDProduct fdProduct = this.lookupFDProduct();
		return fdProduct.isSoldByLb();
	}

	public boolean hasScaledPricing() {
		FDProduct fdProduct = this.lookupFDProduct();
		Pricing price = fdProduct.getPricing();
		MaterialPrice[] materialPrices = price.getMaterialPrices();
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

	public String getLabel() {
		ProductModel prod = this.lookupProduct();
		String quantText = prod == null ? null : prod.getAttribute("QUANTITY_TEXT_SECONDARY", null);
		if (quantText != null) {
			if ("lb".equalsIgnoreCase(quantText) || "oz".equalsIgnoreCase(quantText)) {
				return quantText;
			}
			return this.getSalesUnitDescription();
		}
		return this.isSoldByLb() ? "lb" : "";
	}

	public ErpAffiliate getAffiliate() {
		return this.lookupFDProduct().getAffiliate();
	}

	protected void performPricing() {
		try {
			this.price = FDPricingEngine.doPricing(this.lookupFDProduct(), this, this.getDiscount());

			this.orderLine.setPrice(price.getConfiguredPrice() - price.getPromotionValue());
			//this.orderLine.setTaxRate(price.getTaxRate());
			//this.orderLine.setDepositValue(price.getDepositValue());

		} catch (PricingException e) {
			throw new FDRuntimeException(e, "PricingException occured on "+getSkuCode() + " - " + getConfiguration());
		}
	}

	/** template method */
	protected Discount getDiscount() {
		return null;
	}

	public SaleStatisticsI getStatistics() {
		return this.statistics;
	}
	
	public void setStatistics(SaleStatisticsI stats){
		this.statistics = stats;
	}

	public String getCustomerListLineId() {
		return customerListLineId;
	}

	public void setCustomerListLineId(String customerListLineId) {
		this.customerListLineId = customerListLineId;
	}
	
	public void setInvalidConfig(boolean invalidConfig){
		this.invalidConfig = invalidConfig;
	}
	
	public boolean isInvalidConfig(){
		return invalidConfig;
	}
	
	public String getRecipeSourceId() {
		return this.orderLine.getRecipeSourceId();
	}
	
	public void setRecipeSourceId(String recipeSourceId) {
		this.orderLine.setRecipeSourceId(recipeSourceId);
	}
	
	public String getYmalCategoryId() {
		return this.orderLine.getYmalCategoryId();
	}
	
	public void setYmalCategoryId(String ymalCategoryId) {
		this.orderLine.setYmalCategoryId(ymalCategoryId);
	}
	
	public String getYmalSetId() {
		return this.orderLine.getYmalSetId();
	}
	
	public void setYmalSetId(String ymalSetId) {
		this.orderLine.setYmalSetId(ymalSetId);
	}
	
	public String getOriginatingProductId() {
		return this.orderLine.getOriginatingProductId();
	}
	
	public void setOriginatingProductId(String originatingProductId) {
		this.orderLine.setOriginatingProductId(originatingProductId);
	}
	
	public boolean isRequestNotification() {
		return orderLine.isRequestNotification();
	}
	
	public void setRequestNotification(boolean requestNotification) {
		this.orderLine.setRequestNotification(requestNotification);
	}


}
