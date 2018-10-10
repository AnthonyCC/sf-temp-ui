package com.freshdirect.fdstore.promotion;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;

public class ProductSampleApplicator implements PromotionApplicatorI {
	
    private static final long serialVersionUID = -4228761928725008569L;

	private ProductReference sampleProduct;
	private String categoryId;
	private String productId;
	private double minSubtotal;
	private DlvZoneStrategy zoneStrategy;
	private CartStrategy cartStrategy;
	
	public ProductSampleApplicator(ProductReference sampleProduct, double minSubtotal){
		this.sampleProduct = sampleProduct;
		this.minSubtotal = minSubtotal;
		this.categoryId = null !=sampleProduct?sampleProduct.getCategoryId():null;
		this.productId = null !=sampleProduct?sampleProduct.getProductId():null;
	}

	public ProductSampleApplicator() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean apply(String promotionCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		boolean isApplied = false;
		try {
			int e = zoneStrategy != null ? zoneStrategy.evaluate(promotionCode, context) : PromotionStrategyI.ALLOW;
			if(e == PromotionStrategyI.DENY) return false;
			
			e = cartStrategy != null ? cartStrategy.evaluate(promotionCode, context, true) : PromotionStrategyI.ALLOW;
			if(e == PromotionStrategyI.DENY) return false;
			
			PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
			if (context.getSubTotal(promo.getExcludeSkusFromSubTotal()) < this.minSubtotal) {
				return isApplied;
			}else{
				FDCartModel cart= context.getShoppingCart();
                isApplied = cart.updateProductSampleDiscount(getProductReference().getContentKey(), promotionCode);
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return isApplied;
	}

	@Override
	public void setDlvZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;		
	}

	@Override
	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
	
	public ProductModel getSampleProduct() {
		if(null ==sampleProduct){
			sampleProduct = new ProductReferenceImpl(categoryId,productId);
		}
		return this.sampleProduct.lookupProductModel();
	}
	
	public ProductReference getProductReference() {
		if(null ==sampleProduct){
			sampleProduct = new ProductReferenceImpl(categoryId,productId);
		}
	    return this.sampleProduct;
	}

	@Override
	public void setCartStrategy(CartStrategy cartStrategy) {
		this.cartStrategy = cartStrategy;
	}

	@Override
	public CartStrategy getCartStrategy() {
		return this.cartStrategy;
	}

	public double getMinSubtotal() {
		return minSubtotal;
	}

	public void setMinSubtotal(double minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

	public DlvZoneStrategy getZoneStrategy() {
		return zoneStrategy;
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
