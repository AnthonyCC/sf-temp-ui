package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;

public class FakePromotionContext implements PromotionContextI {

	private EnumOrderType orderType;
	private double subTotal;
	private List discounts = new ArrayList();
	private final Date now = new Date();

	public FakePromotionContext() {
		this(EnumOrderType.HOME);
	}

	public FakePromotionContext(EnumOrderType orderType) {
		this.orderType = orderType;
	}

	public boolean isFraudulent() {
		return false;
	}

	public boolean isAddressMismatch() {
		return false;
	}

	void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getSubTotal() {
		return this.subTotal;
	}

	public double getApplicableSignupAmount(double amount, double maxAmountPerSku) {
		return 0;
	}

	public PromotionI getRedeemedPromotion() {
		return null;
	}

	public FDCartModel getShoppingCart() {
		return null;
	}

	public String getModifiedSaleId() {
		return null;
	}

	public String getSubscribedSignupPromotionCode() {
		return null;
	}

	public int getAdjustedValidOrderCount() {
		return 0;
	}

	public int getPromotionUsageCount(String promotionCode) {
		return 0;
	}

	public String getZipCode() {
		return null;
	}

	public String getDepotCode() {
		return null;
	}

	public void addSampleLine(FDCartLineI cartLine) {
	}

	public void setPromotionAddressMismatch(boolean b) {
	}

	public void setSignupDiscountRule(SignupDiscountRule discountRule) {
	}

	public boolean hasProfileAttribute(String attributeName, String desiredValue) {
		return false;
	}

	public FDIdentity getIdentity() {
		return null;
	}

	public EnumOrderType getOrderType() {
		return this.orderType;
	}

	public void setRulePromoCode(List rulePromoCodes) {

	}

	public boolean hasRulePromoCode(String promoCode) {
		return false;
	}

	public FDUserI getUser() {
		return null;
	}

	public void addDiscount(Discount discount) {
		this.discounts.add(discount);
	}

	public List getDiscounts() {
		return this.discounts;
	}

	public Date getCurrentDate() {
		return now;
	}
	
		
	public double getPreDeductionTotal() {
		return getSubTotal();
	}
	
	public AssignedCustomerParam getAssignedCustomerParam(String promoId){
		return null;
	}
	
	public List getEligibleLinesForDCPDiscount(String promoId, Set contentKeys){
		return null;
	}
	
	public boolean applyHeaderDiscount(String promoCode, double promotionAmt, EnumPromotionType type){
		Discount discount = new Discount(promoCode, EnumDiscountType.DOLLAR_OFF, promotionAmt);
		this.addDiscount(discount);	
		return true;
	}

	public Map getPromoVariantMap(){
		return null;
	}
	
	public Discount getHeaderDiscount(){
		return null;
	}

	public boolean isPostPromoConflictEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void clearLineItemDiscounts() {
		// TODO Auto-generated method stub
		
	}

	public double getTotalLineItemDiscount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void clearHeaderDiscounts() {
		// TODO Auto-generated method stub
		
	}
}