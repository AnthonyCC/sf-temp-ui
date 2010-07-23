package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;

public class FakePromotionContext implements PromotionContextI {

	private EnumOrderType orderType;
	private double subTotal;
	private List<Discount> discounts = new ArrayList<Discount>();
	private final Date now = new Date();

	public FakePromotionContext() {
		this(EnumOrderType.HOME);
	}

	public FakePromotionContext(EnumOrderType orderType) {
		this.orderType = orderType;
	}

	@Override
	public boolean isFraudulent() {
		return false;
	}

	@Override
	public boolean isAddressMismatch() {
		return false;
	}

	void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	@Override
	public double getSubTotal(Set<String> excludeSkus) {
		return this.subTotal;
	}

	@Override
	public double getApplicableSignupAmount(double amount, double maxAmountPerSku) {
		return 0;
	}

	@Override
	public PromotionI getRedeemedPromotion() {
		return null;
	}

	@Override
	public FDCartModel getShoppingCart() {
		return null;
	}

	@Override
	public String getModifiedSaleId() {
		return null;
	}

	@Override
	public String getSubscribedSignupPromotionCode() {
		return null;
	}

	@Override
	public int getAdjustedValidOrderCount() {
		return 0;
	}

	@Override
	public int getPromotionUsageCount(String promotionCode) {
		return 0;
	}

	@Override
	public String getZipCode() {
		return null;
	}

	@Override
	public String getDepotCode() {
		return null;
	}

	@Override
	public void addSampleLine(FDCartLineI cartLine) {
	}

	@Override
	public void setPromotionAddressMismatch(boolean b) {
	}

	@Override
	public void setSignupDiscountRule(SignupDiscountRule discountRule) {
	}

	@Override
	public boolean hasProfileAttribute(String attributeName, String desiredValue) {
		return false;
	}

	@Override
	public FDIdentity getIdentity() {
		return null;
	}

	@Override
	public EnumOrderType getOrderType() {
		return this.orderType;
	}

	@Override
	public boolean hasRulePromoCode(String promoCode) {
		return false;
	}

	@Override
	public FDUserI getUser() {
		return null;
	}

	public void addDiscount(Discount discount) {
		this.discounts.add(discount);
	}

	public List<Discount> getDiscounts() {
		return this.discounts;
	}

	@Override
	public Date getCurrentDate() {
		return now;
	}
	
	@Override
	public double getPreDeductionTotal(Set<String> excludeSkus) {
		return getSubTotal(excludeSkus);
	}
	
	@Override
	public AssignedCustomerParam getAssignedCustomerParam(String promoId){
		return null;
	}
	
	@Override
	public Discount getHeaderDiscount(){
		return null;
	}

	@Override
	public boolean isPostPromoConflictEnabled() {
		return false;
	}

	@Override
	public void clearLineItemDiscounts() {
	}

	@Override
	public double getTotalLineItemDiscount() {
		return 0;
	}

	@Override
	public void clearHeaderDiscounts() {
	}
	
	@Override
	public PricingContext getPricingContext() {
		return null;
	}

	@Override
	public FDReservation getDeliveryReservation() {
		return null;
	}

	@Override
	public String getDeliveryZone() {
		return "";
	}

	@Override
	public List<FDCartLineI> getEligibleLinesForDCPDiscount(String promoId, Set<ContentKey> contentKeys) {
		return null;
	}

	@Override
	public void setRulePromoCode(List<String> rulePromoCodes) {
	}
	
	@Override
	public int getSettledECheckOrderCount() {
		return 0;
	}
	
	@Override
	public boolean applyLineItemDiscount(PromotionI promo, FDCartLineI lineItem, double percentOff){
		return false;
	}
	
	@Override
	public boolean applyHeaderDiscount(PromotionI promo, double promotionAmt){
 		Discount discount = new Discount(promo.getPromotionCode(), EnumDiscountType.DOLLAR_OFF, promotionAmt);
 		this.addDiscount(discount);	
 		return true;
	}
	
	@Override
	public PromotionI getNonCombinableHeaderPromotion(){
		return null;
	}
	
	public boolean applyZoneDiscount(PromotionI promo, double promotionAmt){
		return false;
	}

	@Override
	public String getDepotCode(AddressModel addr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumOrderType getOrderType(AddressModel address) {
		// TODO Auto-generated method stub
		return null;
	}
}