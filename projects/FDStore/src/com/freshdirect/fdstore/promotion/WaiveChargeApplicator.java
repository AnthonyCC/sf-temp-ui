package com.freshdirect.fdstore.promotion;

import com.freshdirect.customer.EnumChargeType;

/**
 * Waives charge of the specified type.
 */
public class WaiveChargeApplicator implements PromotionApplicatorI {

	private EnumChargeType chargeType;
	private double minSubtotal;
	private DlvZoneStrategy zoneStrategy;
	private boolean fuelSurcharge;
	private CartStrategy cartStrategy;
	
	public double getMinSubtotal() {
		return minSubtotal;
	}
	public EnumChargeType getChargeType() {
		return chargeType;
	}

	public WaiveChargeApplicator(double minSubtotal, EnumChargeType chargeType, boolean fuelSurcharge) {
		if (chargeType == null) {
			throw new IllegalArgumentException("ChargeType cannot be null");
		}
		this.chargeType = chargeType;
		this.minSubtotal = minSubtotal;
		this.fuelSurcharge = fuelSurcharge;
	}

	public WaiveChargeApplicator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean apply(String promotionCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		int e = zoneStrategy != null ? zoneStrategy.evaluate(promotionCode, context) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		e = cartStrategy != null ? cartStrategy.evaluate(promotionCode, context, true) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
		if (context.getSubTotal(promo.getExcludeSkusFromSubTotal()) < this.getMinSubtotal()) {
			return false;
		}
		if(fuelSurcharge) {
			context.getShoppingCart().setChargeWaived(chargeType, true, promotionCode);
		} else {
			context.getShoppingCart().setChargeWaived(chargeType, true, promotionCode, fuelSurcharge);
		}
		if(chargeType == EnumChargeType.DELIVERY){
			//If it is a Delivery Promotion
			context.getShoppingCart().setDlvPromotionApplied(true);
			context.getShoppingCart().setDlvPassApplied(false);
		}
		return true;
	}

	public void setDlvZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}

	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
	@Override
	public void setCartStrategy(CartStrategy cartStrategy) {
		this.cartStrategy = cartStrategy;
	}
	@Override
	public CartStrategy getCartStrategy() {
		return this.cartStrategy;
	}
	public boolean isFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(boolean fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public DlvZoneStrategy getZoneStrategy() {
		return zoneStrategy;
	}
	public void setChargeType(EnumChargeType chargeType) {
		this.chargeType = chargeType;
	}
	public void setMinSubtotal(double minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

}
