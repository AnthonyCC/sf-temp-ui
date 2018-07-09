package com.freshdirect.fdstore.customer;

import org.apache.log4j.Category;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;
import com.freshdirect.storeapi.content.SkuReference;

public class FDInvoiceLineModel extends ModelSupport implements FDInvoiceLineI {

	private static final long serialVersionUID = -4864691971873241984L;
    private static final Category LOGGER = LoggerFactory.getInstance(FDInvoiceLineModel.class);
	
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
		try {
			this.substituteProductDefaultPrice =(null!=substituteProduct?(""+substituteProduct.getPriceCalculator().getDefaultPriceValue()):"");
		} catch (Exception e) {
			// Ignore
			LOGGER.warn("Exception while fetching price for substituteProduct: ",e);
		}
		this.substituteProductId = (null !=substituteProduct ? substituteProduct.getContentName():"");
	}

	@Override
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

		try {
			if (this.getSubstitutedSkuCode() != null && !this.getSubstitutedSkuCode().trim().isEmpty()) {
				ProductReference productRef = new SkuReference(
						this.getSubstitutedSkuCode());
				if (productRef == null
						|| ProductReferenceImpl.NULL_REF.equals(productRef)) {
					return null;
				}

                return ProductPricingFactory.getInstance().getPricingAdapter(productRef.lookupProductModel());
			}
		} catch (Exception e) {
			//Ignore
			LOGGER.warn("Exception while fetching substituteProduct: ",e);
		}
		return null;
	}

	@Override
    public String getSubstituteProductId() {
		return substituteProductId;
	}

	@Override
    public double getPrice() {
		return invoiceLine.getPrice();
	}

	@Override
    public double getActualPrice() {
		return invoiceLine.getActualPrice();
	}

	@Override
    public double getCustomizationPrice() {
		return invoiceLine.getCustomizationPrice();
	}

	@Override
    public double getQuantity() {
		return invoiceLine.getQuantity();
	}

	@Override
    public double getWeight() {
		return invoiceLine.getWeight();
	}

	@Override
    public double getTaxValue() {
		return invoiceLine.getTaxValue();
	}

	@Override
    public double getDepositValue() {
		return invoiceLine.getDepositValue();
	}

	@Override
    public String getOrderLineNumber() {
		return invoiceLine.getOrderLineNumber();
	}

	@Override
    public String getMaterialNumber() {
		return invoiceLine.getMaterialNumber();
	}

	@Override
    public double getActualCost() {
		return invoiceLine.getActualCost();
	}

	@Override
    public double getActualDiscountAmount() {
		return invoiceLine.getActualDiscountAmount();
	}

	@Override
    public double getCouponDiscountAmount() {
		return invoiceLine.getCouponDiscountAmount();
	}

	@Override
    public String getSubstitutedSkuCode() {
		return invoiceLine.getSubstitutedSkuCode();
	}

	@Override
    public String getSubSkuStatus() {
		return invoiceLine.getSubSkuStatus();
	}

	@Override
	public String getSubstituteProductName() {
		return substituteProductName;
	}

	@Override
    public String getSubstituteProductDefaultPrice() {
		return substituteProductDefaultPrice;
	}

	public void setSubstituteProductDefaultPrice(
			String substituteProductDefaultPrice) {
		this.substituteProductDefaultPrice = substituteProductDefaultPrice;
	}


}