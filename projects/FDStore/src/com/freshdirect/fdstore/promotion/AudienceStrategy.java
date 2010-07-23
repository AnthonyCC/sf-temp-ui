package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.framework.util.DateUtil;

public class AudienceStrategy implements PromotionStrategyI {

	private boolean maxUsagePerCust;
	private int rollingExpirationDays;
	
	public AudienceStrategy(boolean maxUsagePerCust, int rollingExpirationDays) {
		this.maxUsagePerCust = maxUsagePerCust;
		this.rollingExpirationDays = rollingExpirationDays;
	}
	
	public boolean isMaxUsagePerCustomer() { return this.maxUsagePerCust; } 
	public int getRollingExpirationDays() { return this.rollingExpirationDays; } 
	
	public int evaluate(String promotionCode, PromotionContextI context) {
		AssignedCustomerParam param = context.getAssignedCustomerParam(promotionCode);
		if (param != null && isValidUsageCount(promotionCode, context, param) && isBeforeExpirationDate(promotionCode, context, param)) {
					return ALLOW;
		}
		return DENY;		
	}

	private boolean isValidUsageCount(String promotionCode, PromotionContextI context, AssignedCustomerParam param) {
		int promoUsageCnt = context.getPromotionUsageCount(promotionCode);		
		Integer usageCount = param.getUsageCount();
		return (!this.maxUsagePerCust || (usageCount != null && usageCount.intValue() > promoUsageCnt)); 
	}

	private boolean isBeforeExpirationDate(String promotionCode, PromotionContextI context, AssignedCustomerParam param) {
		Date today = DateUtil.truncate(new Date());
		Date expirationDate = param.getExpirationDate();
		return (this.rollingExpirationDays == 0 || (expirationDate != null && !today.after(DateUtil.truncate(expirationDate))));					
	}

	public int getPrecedence() {
		return 800;
	}

	public String toString() {
		return "AudienceCustomerStrategy [ UsageCount = "+ maxUsagePerCust+", ExpirationDate "+rollingExpirationDays+"]";
	}
	
}
