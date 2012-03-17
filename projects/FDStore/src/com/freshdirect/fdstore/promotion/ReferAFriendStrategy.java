package com.freshdirect.fdstore.promotion;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ReferAFriendStrategy implements PromotionStrategyI {
	
	private static final Category LOGGER = LoggerFactory.getInstance(ReferAFriendStrategy.class);

	private static final long serialVersionUID = 1L;	
	
	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {		
		if(context.getShoppingCart() != null) {
			if (context.getShoppingCart().getDeliveryAddress() != null) {
				String zipCode = context.getShoppingCart().getDeliveryAddress().getZipCode();
				try {
					FDUserI user = context.getUser();
					String firstName = user.getFirstName();				
					String lastName = user.getLastName();
					LOGGER.debug("Checking for FN+LN+ZipCode Fraud Rule:" + firstName + "+" + lastName + "+" + zipCode);
					if(!FDReferralManager.isUniqueFNLNZipCombo(firstName, lastName, zipCode)) {
						//Referral promotion cannot be applied
						context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.ERROR_DUPE_FN_LN_ZIP.getErrorCode());
						user.setReferralPromotionFraud(true);
						return DENY;
					}
				} catch (FDResourceException e) {
					LOGGER.error("Error applying referral promotion", e);
				}
			}
		}
		context.getUser().setReferralPromotionFraud(false);
		return ALLOW;
	}

	@Override
	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 0;
	}

}
