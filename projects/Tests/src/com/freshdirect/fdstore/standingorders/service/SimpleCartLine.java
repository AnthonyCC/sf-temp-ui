package com.freshdirect.fdstore.standingorders.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineI;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.SaleStatisticsI;
import com.freshdirect.framework.event.EnumEventSource;

public class SimpleCartLine implements FDCartLineI {
	private static final long serialVersionUID = 1L;

	private static Random RND = new Random();

	
	private int randomId = RND.nextInt(1000);
	private String cartLineId = Integer.toString(RND.nextInt(16));
	private FDConfigurableI configuration = new FDConfiguration(1.0, "ea");
	private FDSku sku = new FDSku("SKU"+RND.nextInt(1000), 1);
	private String productName;

	// restrictions
	
	private boolean alcohol;
	private boolean kosher;

	private double price = 10.0;


	@Override
	public ErpOrderLineModel buildErpOrderLines(int baseLineNumber)
			throws FDResourceException, FDInvalidConfigurationException {
		return null;
	}

	@Override
	public FDCartLineI createCopy() {
		return null;
	}

	@Override
	public double getActualPrice() {

		return 0;
	}

	@Override
	public Set<EnumDlvRestrictionReason> getApplicableRestrictions() {

		return null;
	}

	@Override
	public String getCartlineId() {
		return cartLineId;
	}

	public void setCartLineId(String cartLineId) {
		this.cartLineId = cartLineId;
	}

	@Override
	public String getCartonNumber() {
		return null;
	}

	@Override
	public String getDeliveredQuantity() {
		return null;
	}

	@Override
	public double getDepositValue() {
		return 0;
	}

	@Override
	public Discount getDiscount() {
		return null;
	}

	@Override
	public double getDiscountAmount() {
		return 0;
	}

	@Override
	public int getErpOrderLineSize() {
		return 0;
	}

	@Override
	public ErpInvoiceLineI getInvoiceLine() {
		return null;
	}

	@Override
	public String getMaterialNumber() {
		return null;
	}

	@Override
	public String getOrderLineId() {
		return null;
	}

	@Override
	public String getOrderLineNumber() {
		return null;
	}

	@Override
	public String getOrderedQuantity() {
		return null;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public PricingContext getPricingContext() {
		return null;
	}

	@Override
	public double getPromotionValue() {
		return 0;
	}

	
	@Override
	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(int randomId) {
		this.randomId = randomId;
	}
	
	@Override
	public String getReturnDisplayQuantity() {
		return null;
	}

	@Override
	public ErpReturnLineI getReturnLine() {
		return null;
	}

	@Override
	public String getReturnedQuantity() {
		return null;
	}

	@Override
	public String getSavingsId() {
		return null;
	}

	@Override
	public EnumEventSource getSource() {
		return null;
	}

	@Override
	public double getTaxRate() {
		return 0;
	}

	@Override
	public double getTaxValue() {
		return 0;
	}

	@Override
	public String getUnitsOfMeasure() {
		return null;
	}

	@Override
	public String getVariantId() {
		return null;
	}

	@Override
	public boolean hasAdvanceOrderFlag() {
		return false;
	}

	@Override
	public boolean hasDepositValue() {
		return false;
	}

	@Override
	public boolean hasDiscount(String promoCode) {
		return false;
	}

	@Override
	public boolean hasInvoiceLine() {
		return false;
	}

	@Override
	public boolean hasRestockingFee() {
		return false;
	}

	@Override
	public boolean hasReturnLine() {
		return false;
	}

	@Override
	public boolean hasScaledPricing() {
		return false;
	}

	@Override
	public boolean hasTax() {
		return false;
	}

	@Override
	public boolean isDiscountApplied() {
		return false;
	}

	@Override
	public boolean isDiscountFlag() {
		return false;
	}

	@Override
	public boolean isEstimatedPrice() {
		return false;
	}

	@Override
	public boolean isSample() {
		return false;
	}

	@Override
	public void removeLineItemDiscount() {
	}

	@Override
	public void setCartonNumber(String no) {
	}

	@Override
	public void setDepositValue(double depositRate) {
	}

	@Override
	public void setDiscount(Discount d) {
	}

	@Override
	public void setDiscountFlag(boolean b) {
	}

	@Override
	public void setOrderLineId(String orderLineId) {
	}

	@Override
	public void setSavingsId(String savingsId) {
	}

	@Override
	public void setSource(EnumEventSource source) {
	}

	@Override
	public void setTaxRate(double taxRate) {
	}

	@Override
	public ErpAffiliate getAffiliate() {
		return null;
	}

	@Override
	public String getCategoryName() {
		return null;
	}

	@Override
	public List<ErpClientCode> getClientCodes() {
		return null;
	}

	@Override
	public FDConfigurableI getConfiguration() {
		return configuration;
	}

	@Override
	public String getConfigurationDesc() {
		return null;
	}

	@Override
	public String getCustomerListLineId() {
		return null;
	}

	@Override
	public String getDepartmentDesc() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayQuantity() {
		return null;
	}

	@Override
	public double getFixedPrice() {
		return 0;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public String getOriginatingProductId() {
		return null;
	}

	@Override
	public EnumOrderLineRating getProduceRating() {
		return null;
	}

	@Override
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public ProductReference getProductRef() {
		return null;
	}

	@Override
	public String getRecipeSourceId() {
		return null;
	}

	@Override
	public String getSalesUnitDescription() {
		return null;
	}

	@Override
	public FDSku getSku() {
		return this.sku;
	}

	@Override
	public String getSkuCode() {
		return this.sku.getSkuCode();
	}

	@Override
	public SaleStatisticsI getStatistics() {
		return null;
	}

	@Override
	public String getUnitPrice() {
		return null;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public String getYmalCategoryId() {
		return null;
	}

	@Override
	public String getYmalSetId() {
		return null;
	}

	@Override
	public boolean hasBrandName(Set<String> brandNames) {
		return false;
	}

	@Override
	public boolean isAlcohol() {
		return this.alcohol;
	}

	public void setAlcohol(boolean alcohol) {
		this.alcohol = alcohol;
	}
	
	@Override
	public boolean isInvalidConfig() {
		return false;
	}

	@Override
	public boolean isKosher() {
		return this.kosher;
	}

	public void setKosher(boolean kosher) {
		this.kosher = kosher;
	}
	
	@Override
	public boolean isPerishable() {
		return false;
	}

	@Override
	public boolean isPlatter() {
		return false;
	}

	@Override
	public boolean isPricedByLb() {
		return false;
	}

	@Override
	public boolean isRequestNotification() {
		return false;
	}

	@Override
	public boolean isSoldByLb() {
		return false;
	}

	@Override
	public boolean isSoldBySalesUnits() {
		return false;
	}

	@Override
	public FDProduct lookupFDProduct() {
		return null;
	}

	@Override
	public FDProductInfo lookupFDProductInfo() {
		return null;
	}

	@Override
	public ProductModel lookupProduct() {
		return null;
	}

	@Override
	public void refreshConfiguration() throws FDResourceException,
			FDInvalidConfigurationException {
	}

	@Override
	public void setConfiguration(FDConfigurableI configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setConfigurationDesc(String configDesc) {
	}

	@Override
	public void setCustomerListLineId(String id) {
	}

	@Override
	public void setDepartmentDesc(String deptDesc) {
	}

	@Override
	public void setDescription(String desc) {
	}

	@Override
	public void setFixedPrice(double price) {
	}

	@Override
	public void setOptions(Map<String, String> options) {
	}

	@Override
	public void setOriginatingProductId(String originatingProductId) {
	}

	@Override
	public void setPricingContext(PricingContext pCtx) {
	}

	@Override
	public void setQuantity(double quantity) {
		this.configuration = new FDConfiguration(quantity, this.configuration.getSalesUnit());
	}

	@Override
	public void setRecipeSourceId(String recipeSourceId) {
	}

	@Override
	public void setRequestNotification(boolean requestNotification) {
	}

	@Override
	public void setSalesUnit(String salesUnit) {
	}

	@Override
	public void setSku(FDSku sku) {
		this.sku  = sku;
	}

	@Override
	public void setStatistics(SaleStatisticsI statistics) {
	}

	@Override
	public void setYmalCategoryId(String ymalCategoryId) {
	}

	@Override
	public void setYmalSetId(String ymalSetId) {
	}

	@Override
	public Map<String, String> getOptions() {
		return null;
	}

	@Override
	public double getQuantity() {
		return configuration.getQuantity();
	}

	@Override
	public String getSalesUnit() {
		return configuration.getSalesUnit();
	}
	
	public double getGroupScaleSavings(){
		return 0.0;
	}
	
	public void setGroupQuantity(double quantity){
		
	}
	
	public double getGroupQuantity(){
		return 0.0;
	}
	public void setFDGroup(FDGroup group){
		
	}

	public FDGroup getFDGroup(){
		return null;
	}

	
	public EnumSustainabilityRating getSustainabilityRating() {
		
		return null;
	}
	

}
