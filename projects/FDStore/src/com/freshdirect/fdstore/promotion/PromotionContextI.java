package com.freshdirect.fdstore.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;

public interface PromotionContextI {
		
	public boolean isFraudulent();

	public boolean isAddressMismatch();

	public double getSubTotal();
	
	public double getPreDeductionTotal();

	public double getApplicableSignupAmount(double amount, double maxAmountPerSku);

	public PromotionI getRedeemedPromotion();

	public FDCartModel getShoppingCart();

	public FDUserI getUser();

	/** @return ID of currently modified sale, or null */
	public String getModifiedSaleId();

	//
	// order history
	//

	public String getSubscribedSignupPromotionCode();

	public int getAdjustedValidOrderCount();

	public int getPromotionUsageCount(String promotionCode);

	//
	// zone/delivery information
	//

	public EnumOrderType getOrderType();

	public String getZipCode();

	public String getDepotCode();

	//
	// promo application
	//

	public void addSampleLine(FDCartLineI cartLine);

	public void setPromotionAddressMismatch(boolean b);

	public void setSignupDiscountRule(SignupDiscountRule discountRule);

	//
	// Customer/ customer profile
	//
	public boolean hasProfileAttribute(String attributeName, String desiredValue);

	public FDIdentity getIdentity();
	
	public void setRulePromoCode(List rulePromoCodes);
	
	public boolean hasRulePromoCode (String promoCode);

	public void addDiscount(Discount discount);
	
	public Date getCurrentDate();
	
	public AssignedCustomerParam getAssignedCustomerParam(String promoId); 
	
	public List getEligibleLinesForDCPDiscount(String promoId, Set contentKeys);
	
	public boolean applyHeaderDiscount(String promoCode, double promotionAmt, EnumPromotionType type);
	
	public Discount getHeaderDiscount();
	
	public boolean isPostPromoConflictEnabled();
	
	public void clearLineItemDiscounts();
	
	public double getTotalLineItemDiscount();
	
	public void clearHeaderDiscounts();
		
}
