package com.freshdirect.fdstore.promotion;

import org.apache.log4j.Category;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;
import com.freshdirect.storeapi.content.SkuModel;

public class SampleLineApplicator implements PromotionApplicatorI {

	private final static Category LOGGER = LoggerFactory.getInstance(SampleStrategy.class);

	private ProductReference sampleProduct;
	private String categoryId;
	private String productId;
	private double minSubtotal;
	private DlvZoneStrategy zoneStrategy;

	private CartStrategy cartStrategy;
	
	public SampleLineApplicator(ProductReference sampleProduct, double minSubtotal) {
		this.sampleProduct = sampleProduct;
		this.minSubtotal = minSubtotal;
		this.categoryId = null !=sampleProduct?sampleProduct.getCategoryId():null;
		this.productId = null !=sampleProduct?sampleProduct.getProductId():null;
	}
	
	public SampleLineApplicator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductModel getSampleProduct() {
		if(null ==sampleProduct){
			sampleProduct = new ProductReferenceImpl(categoryId,productId);
		}
		return this.sampleProduct.lookupProductModel();
	}
	
	@Override
    public boolean apply(String promotionCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		int e = zoneStrategy != null ? zoneStrategy.evaluate(promotionCode, context) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		e = cartStrategy != null ? cartStrategy.evaluate(promotionCode, context, true) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
		if (context.getSubTotal(promo.getExcludeSkusFromSubTotal()) < this.minSubtotal) {
			return false;
		}
		try {
			FDCartLineI cartLine = this.createSampleLine(promotionCode, context.getUserContext());
			if (cartLine != null) {
				context.addSampleLine(cartLine);
				return true;
			}
			return false;
		} catch (FDResourceException fe) {
			throw new FDRuntimeException(fe);
		}
	}

	/** @return null if product is not found */
	private FDCartLineI createSampleLine(String promotionCode, UserContext userCtx) throws FDResourceException {
		ProductModel product = null;	
		try{
            product = ProductPricingFactory.getInstance().getPricingAdapter(this.sampleProduct.lookupProductModel());

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
				product,
				new FDConfiguration(product.getQuantityMinimum(), su.getName()), null, userCtx);

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

	@Override
    public void setDlvZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}

	@Override
    public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
	
	@Override
    public String toString() {
		return "SampleLineApplicator[" + this.sampleProduct + " min $" + this.minSubtotal + "]";
	}

	@Override
	public void setCartStrategy(CartStrategy cartStrategy) {
		this.cartStrategy = cartStrategy;
	}

	@Override
	public CartStrategy getCartStrategy() {
		return this.cartStrategy;
	}

	public DlvZoneStrategy getZoneStrategy() {
		return zoneStrategy;
	}

	public void setMinSubtotal(double minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getProductId() {
		return productId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
