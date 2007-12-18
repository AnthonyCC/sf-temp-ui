package com.freshdirect.fdstore.promotion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SignupDiscountApplicator implements PromotionApplicatorI {

	private final static Category LOGGER = LoggerFactory.getInstance(SignupDiscountApplicator.class);

	/** List of SignupDiscountRule */
	private final List discountRules;

	public SignupDiscountApplicator(SignupDiscountRule[] discountRules) {
		this.discountRules = Arrays.asList(discountRules);
	}

	public boolean apply(String promotionCode, PromotionContextI context) {

		// select appropriate discount rule for this order
		int orderNum = context.getPromotionUsageCount(promotionCode);
		if (orderNum >= discountRules.size()) {
			LOGGER.warn("No discount rule for " + orderNum + ". order.");
			return false;
		}

		SignupDiscountRule discountRule = (SignupDiscountRule) discountRules.get(orderNum);

		context.setSignupDiscountRule(discountRule);

		if (context.isAddressMismatch()) {
			context.setPromotionAddressMismatch(true);
			return false;
		}

		LOGGER.debug(promotionCode + " applying " + discountRule);

		if (context.getPreDeductionTotal() < discountRule.getMinSubtotal()) {
			return false;
		}

		double applicableAmount = context.getApplicableSignupAmount(discountRule.getMaxAmount(), discountRule.getMaxAmountPerSku());

		Discount discount = new Discount(promotionCode, EnumDiscountType.DOLLAR_OFF, applicableAmount);
		context.addDiscount(discount);

		return true;
	}

	/** @return List of SignupDiscountRule */
	public List getDiscountRules() {
		return Collections.unmodifiableList(this.discountRules);
	}

	public String toString() {
		return "SignupDiscountApplicator[" + this.discountRules + "]";
	}

}