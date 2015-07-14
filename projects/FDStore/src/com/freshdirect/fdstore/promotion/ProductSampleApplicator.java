package com.freshdirect.fdstore.promotion;

import java.util.List;

import org.apache.log4j.Category;

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
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductSampleApplicator implements PromotionApplicatorI {
	
	private final static Category LOGGER = LoggerFactory.getInstance(ProductSampleApplicator.class);
	
	private final ProductReference sampleProduct;
	private final double minSubtotal;
	private DlvZoneStrategy zoneStrategy;
	
	public ProductSampleApplicator(ProductReference sampleProduct, double minSubtotal){
		this.sampleProduct = sampleProduct;
		this.minSubtotal = minSubtotal;
	}

	@Override
	public boolean apply(String promotionCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		try {
			int e = zoneStrategy != null ? zoneStrategy.evaluate(promotionCode, context) : PromotionStrategyI.ALLOW;
			if(e == PromotionStrategyI.DENY) return false;
			
			PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
			if (context.getSubTotal(promo.getExcludeSkusFromSubTotal()) < this.minSubtotal) {
				return false;
			}else{
				FDCartModel cart= context.getShoppingCart();
				List<FDCartLineI> orderLines=cart.getOrderLines();
				if(null !=orderLines && !orderLines.isEmpty()){
					int eligibleQuantity = FDStoreProperties.getProductSamplesMaxQuantityLimit();
					int eligibleProducts = FDStoreProperties.getProductSamplesMaxBuyProductsLimit();
					if(!isMaxSampleReached(orderLines, eligibleProducts)){
						int quantity = 0;
						for (FDCartLineI orderLine : orderLines) {
							if(orderLine.getProductRef().equals(sampleProduct) && quantity < eligibleQuantity){
								orderLine.setDiscount(new Discount(promotionCode, EnumDiscountType.FREE, 1.0));
								orderLine.setDepartmentDesc("FREE SAMPLE(S)");
								quantity += orderLine.getQuantity();
	
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
		return true;
				
	}

	
	private boolean isMaxSampleReached(List<FDCartLineI> orderLines, int eligibleProducts) {
		int numberOfFreeSampleProducts = 0;
		for(FDCartLineI orderLine: orderLines){
			if(orderLine.getDiscount().getDiscountType().equals(EnumDiscountType.FREE)){
				numberOfFreeSampleProducts++;
			}
		}
		if(numberOfFreeSampleProducts >= eligibleProducts){
			return true;
		}
		return false;
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
	

}
