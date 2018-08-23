package com.freshdirect.fdstore.ecomm.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.customer.EnumATCContext;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineI;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDInvoiceLineI;
import com.freshdirect.fdstore.customer.SaleStatisticsI;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;

public class ModifiedCartlineData implements FDCartLineI{


	private static final long serialVersionUID = -2058182744456578847L;

	private FDSku sku;
	
	private String cartlineId;

	private double quantity;

	private Map<String, String> options;

	private String recipeSourceId;

	private boolean requestNotification;

	private String variantId;

	private boolean discountFlag;

	private String savingsId;

	private boolean addedFromSearch;

	private EnumATCContext atcContext;

	private String externalSource;

	private ExternalAgency externalAgency;

	private String externalGroup;

	private EnumEStoreId eStore;

	private String salesUnit;
	@Override
	public FDSku getSku() {
		return sku;
	}

	@Override
	public void setSku(FDSku sku) {
		this.sku = sku;
	}

	@Override
	public ProductReference getProductRef() {
		return null;
	}

	@Override
	public String getSkuCode() {
		return null;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public String getCategoryName() {
		return null;
	}

	@Override
	public String getProductName() {
		return null;
	}

	@Override
	public FDConfigurableI getConfiguration() {
		return null;
	}

	@Override
	public void setConfiguration(FDConfigurableI configuration) {
		
	}

	@Override
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Override
	public void setFixedPrice(double price) {
		
	}

	@Override
	public double getFixedPrice() {
		return 0;
	}

	@Override
	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	@Override
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	@Override
	public boolean isAlcohol() {
		return false;
	}

	@Override
	public boolean isPerishable() {
		return false;
	}

	@Override
	public boolean isKosher() {
		return false;
	}

	@Override
	public boolean isPlatter() {
		return false;
	}

	@Override
	public void setDepartmentDesc(String deptDesc) {
		
	}

	@Override
	public String getDepartmentDesc() {
		return null;
	}

	@Override
	public void setDescription(String desc) {
		
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setConfigurationDesc(String configDesc) {
		
	}

	@Override
	public String getConfigurationDesc() {
		return null;
	}

	@Override
	public String getSalesUnitDescription() {
		return null;
	}

	@Override
	public String getUnitPrice() {
		return null;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public boolean isPricedByLb() {
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
	public ProductModel lookupProduct() {
		return null;
	}

	@Override
	public FDProductInfo lookupFDProductInfo() {
		return null;
	}

	@Override
	public FDProduct lookupFDProduct() {
		return null;
	}

	@Override
	public String getDisplayQuantity() {
		return null;
	}

	@Override
	public ErpAffiliate getAffiliate() {
		return null;
	}

	@Override
	public SaleStatisticsI getStatistics() {
		return null;
	}

	@Override
	public void setStatistics(SaleStatisticsI statistics) {
		
	}

	@Override
	public String getCustomerListLineId() {
		return null;
	}

	@Override
	public void setCustomerListLineId(String id) {
		
	}

	@Override
	public EnumOrderLineRating getProduceRating() {
		return null;
	}

	@Override
	public String getRecipeSourceId() {
		return this.recipeSourceId;
	}

	@Override
	public void setRecipeSourceId(String recipeSourceId) {
		this.recipeSourceId = recipeSourceId;
	}

	@Override
	public void setYmalCategoryId(String ymalCategoryId) {
		
	}

	@Override
	public String getYmalCategoryId() {
		return null;
	}

	@Override
	public void setYmalSetId(String ymalSetId) {
		
	}

	@Override
	public String getYmalSetId() {
		return null;
	}

	@Override
	public void setOriginatingProductId(String originatingProductId) {
		
	}

	@Override
	public void setUserContext(UserContext uCtx) {
		
	}

	@Override
	public void setFDGroup(FDGroup group) {
		
	}

	@Override
	public FDGroup getFDGroup() {
		return null;
	}

	@Override
	public String getOriginatingProductId() {
		return null;
	}

	@Override
	public boolean isRequestNotification() {
		return this.requestNotification;
	}

	@Override
	public void setRequestNotification(boolean requestNotification) {
		this.requestNotification = requestNotification;
	}

	@Override
	public boolean isInvalidConfig() {
		return false;
	}

	@Override
	public void refreshConfiguration() throws FDResourceException, FDInvalidConfigurationException {
		
	}

	@Override
	public boolean hasBrandName(Set<String> brandNames) {
		return false;
	}

	@Override
	public List<ErpClientCode> getClientCodes() {
		return null;
	}

	@Override
	public void setGroupQuantity(double quantity) {
		
	}

	@Override
	public double getGroupQuantity() {
		return 0;
	}

	@Override
	public double getGroupScaleSavings() {
		return 0;
	}

	@Override
	public EnumSustainabilityRating getSustainabilityRating() {
		return null;
	}

	@Override
	public double getBasePrice() {
		return 0;
	}

	@Override
	public String getUpc() {
		return null;
	}

	@Override
	public Date getDeliveryStartDate() {
		return null;
	}

	@Override
	public void setDeliveryStartDate(Date deliveryStartDate) {
		
	}

	@Override
	public String getOrderId() {
		return null;
	}

	@Override
	public void setOrderId(String orderId) {
		
	}

	@Override
	public EnumSaleStatus getSaleStatus() {
		return null;
	}

	@Override
	public void setSaleStatus(EnumSaleStatus saleStatus) {
		
	}

	@Override
	public double getConfiguredPrice() {
		return 0;
	}

	@Override
	public String getExternalGroup() {
		return this.externalGroup;
	}

	@Override
	public void setExternalGroup(String externalGroup) {
		this.externalGroup = externalGroup;
	}

	@Override
	public ExternalAgency getExternalAgency() {
		return this.externalAgency;
	}

	@Override
	public void setExternalAgency(ExternalAgency externalAgency) {
		this.externalAgency = externalAgency;
	}

	@Override
	public String getExternalSource() {
		return this.externalSource;
	}

	@Override
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}

	@Override
	public boolean isWine() {
		return false;
	}

	@Override
	public boolean isBeer() {
		return false;
	}

	@Override
	public Double getScaleQuantity() {
		return null;
	}

	@Override
	public void setScaleQuantity(Double scaleQuantity) {
		
	}

	@Override
	public double getQuantity() {
		return this.quantity;
	}

	@Override
	public String getSalesUnit() {
		return this.salesUnit;
	}

	@Override
	public Map<String, String> getOptions() {
		return this.options;
	}

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
	public int getErpOrderLineSize() {
		return 0;
	}

	@Override
	public int getRandomId() {
		return 0;
	}

	@Override
	public Discount getDiscount() {
		return null;
	}

	@Override
	public void setTaxCode(String taxCode) {
		
	}

	@Override
	public String getTaxCode() {
		return null;
	}

	@Override
	public double getPrice() {
		return 0;
	}

	@Override
	public double getSaveAmount() {
		return 0;
	}

	@Override
	public double getPromotionValue() {
		return 0;
	}

	@Override
	public double getTaxRate() {
		return 0;
	}

	@Override
	public void setTaxRate(double taxRate) {
		
	}

	@Override
	public double getTaxValue() {
		return 0;
	}

	@Override
	public boolean isSample() {
		return false;
	}

	@Override
	public boolean isEstimatedPrice() {
		return false;
	}

	@Override
	public boolean hasTax() {
		return false;
	}

	@Override
	public boolean hasScaledPricing() {
		return false;
	}

	@Override
	public boolean hasDepositValue() {
		return false;
	}

	@Override
	public double getDepositValue() {
		return 0;
	}

	@Override
	public void setDepositValue(double depositRate) {
		
	}

	@Override
	public boolean hasInvoiceLine() {
		return false;
	}

	@Override
	public FDInvoiceLineI getInvoiceLine() {
		return null;
	}

	@Override
	public boolean hasReturnLine() {
		return false;
	}

	@Override
	public ErpReturnLineI getReturnLine() {
		return null;
	}

	@Override
	public String getOrderedQuantity() {
		return null;
	}

	@Override
	public String getDeliveredQuantity() {
		return null;
	}

	

	@Override
	public String getReturnedQuantity() {
		return null;
	}

	@Override
	public String getUnitsOfMeasure() {
		return null;
	}

	@Override
	public String getReturnDisplayQuantity() {
		return null;
	}

	@Override
	public boolean hasRestockingFee() {
		return false;
	}

	@Override
	public Set<EnumDlvRestrictionReason> getApplicableRestrictions() {
		return null;
	}

	@Override
	public String getOrderLineId() {
		return null;
	}

	@Override
	public void setOrderLineId(String orderLineId) {
		
	}

	@Override
	public String getOrderLineNumber() {
		return null;
	}

	@Override
	public String getMaterialNumber() {
		return null;
	}

	@Override
	public String getMaterialGroup() {
		return null;
	}

	@Override
	public String getCartlineId() {
		return cartlineId;
	}

	public void setCartlineId(String id) {
		this.cartlineId = id;
	}
	@Override
	public void setSource(EnumEventSource source) {
		
	}

	@Override
	public EnumEventSource getSource() {
		return null;
	}

	@Override
	public boolean hasAdvanceOrderFlag() {
		return false;
	}

	@Override
	public String getVariantId() {
		return this.variantId;
	}
	
	public void setVariantId(String id) {
		this.variantId = id;
	}
	@Override
	public double getDiscountAmount() {
		return 0;
	}

	@Override
	public boolean isDiscountFlag() {
		return this.discountFlag;
	}

	@Override
	public boolean isDiscountApplied() {
		return false;
	}

	@Override
	public void setDiscountFlag(boolean b) {
		this.discountFlag = b;
		
	}

	@Override
	public void setDiscount(Discount d) {
		
	}

	@Override
	public boolean hasDiscount(String promoCode) {
		return false;
	}

	@Override
	public void setSavingsId(String savingsId) {
		this.savingsId = savingsId;
	}

	@Override
	public String getSavingsId() {
		return this.savingsId;
	}

	@Override
	public void removeLineItemDiscount() {
		
	}

	@Override
	public double getActualPrice() {
		return 0;
	}

	@Override
	public UserContext getUserContext() {
		return null;
	}

	@Override
	public void setCartonNumber(String no) {
		
	}

	@Override
	public String getCartonNumber() {
		return null;
	}

	@Override
	public FDGroup getOriginalGroup() {
		return null;
	}

	@Override
	public boolean isAddedFromSearch() {
		return this.addedFromSearch;
	}

	@Override
	public void setAddedFromSearch(boolean addedFromSearch) {
		this.addedFromSearch = addedFromSearch;
		
	}

	@Override
	public ErpCouponDiscountLineModel getCouponDiscount() {
		return null;
	}

	@Override
	public void setCouponDiscount(ErpCouponDiscountLineModel discount) {
		
	}

	@Override
	public EnumCouponStatus getCouponStatus() {
		return null;
	}

	@Override
	public void setCouponStatus(EnumCouponStatus couponStatus) {
		
	}

	@Override
	public void clearCouponDiscount() {
		
	}

	@Override
	public EnumTaxationType getTaxationType() {
		return null;
	}

	@Override
	public void setTaxationType(EnumTaxationType taxationType) {
		
	}

	@Override
	public void setCouponApplied(boolean applied) {
		
	}

	@Override
	public boolean hasCouponApplied() {
		return false;
	}

	@Override
	public void setAddedFrom(EnumATCContext atcContext) {
		this.atcContext = atcContext;
	}

	@Override
	public EnumATCContext getAddedFrom() {
		return this.atcContext;
	}

	@Override
	public String getAtcItemId() {
		return null;
	}

	@Override
	public void setAtcItemId(String atcItemId) {
		
	}

	@Override
	public void setEStoreId(EnumEStoreId eStore) {
		this.eStore = eStore;
		
	}

	@Override
	public EnumEStoreId getEStoreId() {
		return this.eStore;
	}

	@Override
	public void setPlantId(String plantId) {
		
	}

	@Override
	public String getPlantId() {
		return null;
	}

	@Override
	public void setErpOrderLineSource(EnumEventSource source) {
	}

	@Override
	public EnumEventSource getErpOrderLineSource() {
		return null;
	}

	@Override
	public String getSubstitutedQuantity() {
		return null;
	}

	@JsonIgnore
	public void setDataFromCartline(FDCartLineI cartline) {
		setCartlineId(cartline.getCartlineId());
		setSku(cartline.getSku());
		setQuantity(cartline.getQuantity());
		setSalesUnit(cartline.getSalesUnit());
		setOptions(cartline.getOptions());
		setRecipeSourceId(cartline.getRecipeSourceId());
		setRequestNotification(cartline.isRequestNotification());
		setVariantId(cartline.getVariantId());
		setDiscountFlag(cartline.isDiscountFlag());
		setSavingsId(cartline.getSavingsId());
		setAddedFromSearch(cartline.isAddedFromSearch());
		setAddedFrom(cartline.getAddedFrom());
		setExternalAgency(cartline.getExternalAgency());
		setExternalGroup(cartline.getExternalGroup());
		setExternalSource(cartline.getExternalSource());
		setEStoreId(cartline.getEStoreId());
	}
}
