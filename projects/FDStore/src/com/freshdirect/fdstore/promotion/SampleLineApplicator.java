package com.freshdirect.fdstore.promotion;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SampleLineApplicator implements PromotionApplicatorI {

	private final static Category LOGGER = LoggerFactory.getInstance(SampleStrategy.class);

	private final ProductRef sampleProduct;
	private final double minSubtotal;

	public SampleLineApplicator(ProductRef sampleProduct, double minSubtotal) {
		this.sampleProduct = sampleProduct;
		this.minSubtotal = minSubtotal;
	}

	public ProductRef getSampleProduct() {
		return this.sampleProduct;
	}

	public boolean apply(String promotionCode, PromotionContextI context) {
		if (context.getPreDeductionTotal() < this.minSubtotal) {
			return false;
		}
		try {
			FDCartLineI cartLine = this.createSampleLine(promotionCode);
			if (cartLine != null) {
				context.addSampleLine(cartLine);
				return true;
			}
			return false;
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	/** @return null if product is not found */
	private FDCartLineI createSampleLine(String promotionCode) throws FDResourceException {
		ProductModel product = null;	
		try{
			product = this.sampleProduct.lookupProduct();	
		}catch(Exception ex){
			// This is to handle when a invalid category id or product id is set to the sampe promo. 
			LOGGER.error("The category id or product id for the sample promo "+promotionCode+" is not invalid.");
		}
		
		if (product == null) {
			LOGGER.info("Sample product " + this.sampleProduct + " not in store");
			return null;
		}

		SkuModel sku = product.getDefaultSku();
		if (sku == null) {
			LOGGER.info("Default SKU not found for " + this.sampleProduct);
			return null;
		}

		FDProduct fdp;
		try {
			fdp = sku.getProduct();
		} catch (FDSkuNotFoundException e) {
			LOGGER.info("FDProduct not found for " + sku);
			return null;
		}

		FDSalesUnit su = fdp.getSalesUnits()[0];

		FDCartLineModel cartLine =
			new FDCartLineModel(
				new FDSku(fdp),
				product.getProductRef(),
				new FDConfiguration(product.getQuantityMinimum(), su.getName()));

		cartLine.setDiscount(new Discount(promotionCode, EnumDiscountType.SAMPLE, 1.0));

		try {
			cartLine.refreshConfiguration();
		} catch (FDInvalidConfigurationException e) {
			throw new FDResourceException(e);
		}

		return cartLine;
	}

	public double getMinSubtotal() {
		return this.minSubtotal;
	}

	public String toString() {
		return "SampleLineApplicator[" + this.sampleProduct + " min $" + this.minSubtotal + "]";
	}

}
