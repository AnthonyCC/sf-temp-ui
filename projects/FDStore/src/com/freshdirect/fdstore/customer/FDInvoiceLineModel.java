package com.freshdirect.fdstore.customer;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;
import com.freshdirect.storeapi.content.SkuReference;

public class FDInvoiceLineModel extends ModelSupport implements FDInvoiceLineI {

	private static final long serialVersionUID = -4864691971873241984L;

	private ErpInvoiceLineI invoiceLine;

	private UserContext userContext;

	private String substituteProductName;
	
	private String substituteProductDefaultPrice;
	
	private String substituteProductId;

	public FDInvoiceLineModel(ErpInvoiceLineI invoiceLine) {
		this.invoiceLine = invoiceLine;
	}

	public FDInvoiceLineModel(ErpInvoiceLineI invoiceLine,
			UserContext userContext) {
		this.invoiceLine = invoiceLine;
		this.userContext = userContext;
		ProductModel substituteProduct = lookUpSubstitueProduct();
		this.substituteProductName = (null!=substituteProduct?substituteProduct.getFullName():getSubstitutedSkuCode());
		this.substituteProductDefaultPrice =(null!=substituteProduct?substituteProduct.getDefaultPrice():"");
		this.substituteProductId = (null !=substituteProduct ? substituteProduct.getContentName():"");
	}

	public UserContext getUserContext() {
		return userContext;
	}

	/* 
	 * The method returns the product model based upon
	 * the substituted skucode we receive from SAP 
	 */
	/*public ProductModel getSubstituteProduct() {
		return this.lookUpSubstitueProduct();
	}*/

	
	/**
	 * @return the ProductModel of the sku if available, else return null
	 */
	private ProductModel lookUpSubstitueProduct() {

		if (this.getSubstitutedSkuCode() != null) {
			ProductReference productRef = new SkuReference(
					this.getSubstitutedSkuCode());
			if (productRef == null
					|| ProductReferenceImpl.NULL_REF.equals(productRef)) {
				return null;
			}

			return ProductPricingFactory
					.getInstance()
					.getPricingAdapter(
							productRef.lookupProductModel(),
							getUserContext().getPricingContext() != null ? getUserContext()
									.getPricingContext()
									: PricingContext.DEFAULT);
		}
		return null;
	}

	public String getSubstituteProductId() {
		return substituteProductId;
	}

	public double getPrice() {
		return invoiceLine.getPrice();
	}

	public double getActualPrice() {
		return invoiceLine.getActualPrice();
	}

	public double getCustomizationPrice() {
		return invoiceLine.getCustomizationPrice();
	}

	public double getQuantity() {
		return invoiceLine.getQuantity();
	}

	public double getWeight() {
		return invoiceLine.getWeight();
	}

	public double getTaxValue() {
		return invoiceLine.getTaxValue();
	}

	public double getDepositValue() {
		return invoiceLine.getDepositValue();
	}

	public String getOrderLineNumber() {
		return invoiceLine.getOrderLineNumber();
	}

	public String getMaterialNumber() {
		return invoiceLine.getMaterialNumber();
	}

	public double getActualCost() {
		return invoiceLine.getActualCost();
	}

	public double getActualDiscountAmount() {
		return invoiceLine.getActualDiscountAmount();
	}

	public double getCouponDiscountAmount() {
		return invoiceLine.getCouponDiscountAmount();
	}

	public String getSubstitutedSkuCode() {
		return invoiceLine.getSubstitutedSkuCode();
	}

	public String getSubSkuStatus() {
		return invoiceLine.getSubSkuStatus();
	}

	@Override
	public String getSubstituteProductName() {
		return substituteProductName;
	}

	public String getSubstituteProductDefaultPrice() {
		return substituteProductDefaultPrice;
	}

	public void setSubstituteProductDefaultPrice(
			String substituteProductDefaultPrice) {
		this.substituteProductDefaultPrice = substituteProductDefaultPrice;
	}


}