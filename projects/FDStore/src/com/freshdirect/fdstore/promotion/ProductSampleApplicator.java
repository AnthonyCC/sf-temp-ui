package com.freshdirect.fdstore.promotion;

import java.util.List;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;

public class ProductSampleApplicator implements PromotionApplicatorI {
	
    private static final long serialVersionUID = -4228761928725008569L;

	private final ProductReference sampleProduct;
	private final double minSubtotal;
	private DlvZoneStrategy zoneStrategy;
	private CartStrategy cartStrategy;
	
	public ProductSampleApplicator(ProductReference sampleProduct, double minSubtotal){
		this.sampleProduct = sampleProduct;
		this.minSubtotal = minSubtotal;
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
				List<FDCartLineI> orderLines=cart.getOrderLines();
				if(null !=orderLines && !orderLines.isEmpty()){
                    int eligibleQuantity = FDStoreProperties.getProductSamplesMaxQuantityLimit();
                    if (!cart.isMaxSampleReached()) {
                        int quantity = 0;
						for (FDCartLineI orderLine : orderLines) {
                            if (orderLine.getProductRef().getContentKey().equals(sampleProduct.getContentKey()) 
                            		&& quantity < eligibleQuantity
                                    && orderLine.getQuantity() <= eligibleQuantity) {
                                orderLine.setDiscount(new Discount(promotionCode, EnumDiscountType.FREE, orderLine.getQuantity()));
                                quantity += orderLine.getQuantity();
                                isApplied = true;
								try {
									orderLine.refreshConfiguration();
								} catch (FDInvalidConfigurationException ex) {
									throw new FDResourceException(ex);
								}
							}
						}
					}
				}
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return isApplied;
	}

	
	@Override
	public void setZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;		
	}

	@Override
	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
	
	public ProductModel getSampleProduct() {
		return this.sampleProduct.lookupProductModel();
	}
	
	public ProductReference getProductReference() {
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

}
