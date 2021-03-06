package com.freshdirect.fdstore.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;



import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.EnumComparisionType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.util.StringUtil;

public class CustomerStrategy implements PromotionStrategyI {
	private Set<String> cohorts;
	private Set<String> dpTypes;
	private int orderRangeStart;
	private int orderRangeEnd;
	private EnumDlvPassStatus dpStatus;
	private Date dpStartDate;
	private Date dpEndDate;
	private Set<EnumCardType> paymentTypes;
	private int priorEcheckUse;
	private Set<EnumOrderType> allowedOrderTypes;
	private EnumComparisionType eCheckMatchType;
	private List<EnumDeliveryType> orderRangeDeliveryTypes;
	
	public CustomerStrategy() {
		
	}
	
	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {
		
		boolean isEligibleByDPFreeTrialOptIn = 
				FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled() && 
				context.getUser().getDpFreeTrialOptin() && (context.getUser().getDlvPassInfo()==null || EnumDlvPassStatus.NONE.equals(context.getUser().getDlvPassInfo().getStatus()))
				&& (dpTypes == null || dpTypes.isEmpty() || dpTypes.contains(FDStoreProperties.getTwoMonthTrailDPSku()));
		
		//Evaluate Cohorts
		if(cohorts != null && cohorts.size() > 0 && !cohorts.contains(context.getUser().getCohortName())) return DENY;
		
		
		
		if(  dpTypes != null && dpTypes.size() > 0) {
			if(!isEligibleByDPFreeTrialOptIn){
				if(context.getUser()==null || context.getUser().getDlvPassInfo()==null)return DENY;
				else if( context.getUser().getDlvPassInfo().getTypePurchased()==null)return DENY;
				else if( !dpTypes.contains(context.getUser().getDlvPassInfo().getTypePurchased().getCode()))
					return DENY;
			}
		}
		
		
		//Evaluate Order Range. range is not defined properly. DENY
		if((orderRangeStart > 0 && orderRangeEnd <= 0) || (orderRangeStart <= 0 && orderRangeEnd > 0)) return DENY;
		if(orderRangeStart > 0 && orderRangeEnd > 0){
			int currentOrder = 1;//current order
			if(null ==orderRangeDeliveryTypes || orderRangeDeliveryTypes.isEmpty()){
				currentOrder = currentOrder + context.getAdjustedValidOrderCount();				
			}else{				
				for (Iterator<EnumDeliveryType> iterator = orderRangeDeliveryTypes.iterator(); iterator.hasNext();) {
					EnumDeliveryType deliveryType = iterator.next();
					currentOrder = currentOrder + context.getValidOrderCount(deliveryType);
				}
				if(context.getShoppingCart() instanceof FDModifyCartModel){
					currentOrder--;
				}
			}
			if(currentOrder  < orderRangeStart || currentOrder > orderRangeEnd) return DENY;
		}
		
		//Evaluate Delivery Pass Status
		if(dpStatus != null && !isEligibleByDPFreeTrialOptIn){
			if(!context.getUser().getDeliveryPassStatus().equals(dpStatus)) return DENY;
			if(dpStartDate != null && dpEndDate != null) {
				Date dpExpDate = context.getUser().getDlvPassInfo().getExpDate();
				if(dpExpDate.before(dpStartDate) || dpExpDate.after(dpEndDate)) return DENY;
			}
		}
		
		//Evaluate Order Types
		if(!evaluateOrderType(context.getOrderType())) {
			context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_ADDRESS_SELECTED.getErrorCode());
			return DENY;
		}
		
		//Evaluate Payment Types
		FDCartModel cart = context.getShoppingCart();
		if(paymentTypes != null && paymentTypes.size() > 0 ) {
			if(cart.getPaymentMethod() == null){
				context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_PAYMENT_METHOD_SELECTED.getErrorCode());
				return DENY;
			}
			EnumCardType currentCardType = cart.getPaymentMethod().getCardType();
			/**
			 * Check debit card and add.
			 * 
			 */
			
			if(!paymentTypes.contains(currentCardType) ) {
				
				if(paymentTypes.contains(EnumCardType.DEBIT)) {
					
					if(currentCardType.equals(EnumCardType.VISA)||currentCardType.equals(EnumCardType.MC)) {
						String profileId=cart.getPaymentMethod().getProfileID();
						if(!StringUtil.isEmpty(profileId) /*&& StringUtil.isEmpty(cart.getPaymentMethod().getAccountNumber())*/) {
							if(!cart.getPaymentMethod().isDebitCard()) {
								context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
								return DENY;
							}
						} else {
							context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
							return DENY;
						}
						
					} else {
						context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
						return DENY;
					}
					
				} else if(paymentTypes.contains(EnumCardType.MASTERPASS)) {
					if(null !=cart.getPaymentMethod().geteWalletID()){
						Integer eWalletId = null;
						try {
							eWalletId = Integer.parseInt(cart.getPaymentMethod().geteWalletID());
						} catch (NumberFormatException e) {
							//Ignore
						}
						 EnumEwalletType eWalletType = (null != eWalletId? EnumEwalletType.getEnum(eWalletId): null);
						 
						 
						 if(null ==eWalletType || !eWalletType.getName().equals(EnumCardType.MASTERPASS.getFdName())){
							 context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
							 return DENY;
						 }
					}else{
						context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
						return DENY;
					}
					
				} else if(paymentTypes.contains(EnumCardType.PAYPAL)) {
					if(null !=cart.getPaymentMethod().geteWalletID()){
						Integer eWalletId = null;
						try {
							eWalletId = Integer.parseInt(cart.getPaymentMethod().geteWalletID());
						} catch (NumberFormatException e) {
							//Ignore
						}
						 EnumEwalletType eWalletType = (null != eWalletId? EnumEwalletType.getEnum(eWalletId): null);
						 
						/* if(null ==eWalletType || !eWalletType.getName().equals(EnumCardType.PAYPAL.getFdName())){
							 context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
							 return DENY;
						 }*/
						 if(null ==eWalletType || !(cart.getPaymentMethod()!= null && cart.getPaymentMethod().getCardType() != null && 
								 EnumCardType.PAYPAL.equals(cart.getPaymentMethod().getCardType()))){
							 context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
							 return DENY;
						 }
						 
						 
					}else{
						context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
						return DENY;
					}
					
				} else {
					context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode());
					return DENY;
				}
			}
//			if(priorEcheckUse > 0 && currentCardType.equals(EnumCardType.ECP)){
			if(currentCardType.equals(EnumCardType.ECP)) {
				int validEcheckOrderCount = context.getSettledECheckOrderCount();
				if((EnumComparisionType.EQUAL.equals(eCheckMatchType) && validEcheckOrderCount  != priorEcheckUse)
						||(EnumComparisionType.GREATER_OR_EQUAL.equals(eCheckMatchType) &&  validEcheckOrderCount  < priorEcheckUse)
						||(EnumComparisionType.LESS_OR_EQUAL.equals(eCheckMatchType) && validEcheckOrderCount  > priorEcheckUse)){
					return DENY;
				}
//				if(validEcheckOrderCount  < priorEcheckUse) return DENY;
			}
		}
		
		// Voucher redemption promotion :  should not allow promotion for new customer who has profile as 
		// Voucher holder in or out delivery zone
		try {
			if (((context.getUser().isVHInDelivery() || context.getUser().isVHOutOfDelivery())
					&& context.getAdjustedValidOrderCount() == 0)) {
				return DENY;
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
		}

		
		return ALLOW;
	}

	
	public boolean evaluateOrderType(EnumOrderType orderType){
		if(allowedOrderTypes != null && allowedOrderTypes.size() > 0){
			for(Iterator<EnumOrderType> it = allowedOrderTypes.iterator();it.hasNext();){
				if(it.next().getName().equals(orderType.getName())) return true;
			}
		}
		return false;
	}
	
	@Override
	public int getPrecedence() {
		return 2000;
	}

	public String toString() {
		return "CustomerStrategy[...]";
	}


	public String getCohortNames() {
		return convertToCohortNames(this.cohorts);
	}

	public Set<String> getCohorts() {
		return this.cohorts;
	}
	
	public int getOrderRangeStart() {
		return orderRangeStart;
	}


	public int getOrderRangeEnd() {
		return orderRangeEnd;
	}


	public EnumDlvPassStatus getDPStatus() {
		return dpStatus;
	}


	public Date getDPStartDate() {
		return dpStartDate;
	}


	public Date getDPEndDate() {
		return dpEndDate;
	}


	public String getPaymentTypeNames() {
		return convertToPaymentTypeNames(this.paymentTypes);
	}

	public Set<EnumCardType> getPaymentTypes() {
		return this.paymentTypes;
	}

	
	public int getPriorEcheckUse() {
		return priorEcheckUse;
	}


	public void setCohortNames(String cohortNames) {
		this.cohorts = convertToCohorts(cohortNames);

	}

	/*
	 * Utility method to convert comma seperated values into Set.
	 */
	private Set<String> convertToCohorts(String value){
		StringTokenizer tokens = new StringTokenizer(value, ",");
		Set<String> returnSet = new HashSet<String>();
		
		while(tokens.hasMoreTokens()){
			returnSet.add(tokens.nextToken());
		}
		return returnSet;
	}
	
	/*
	 * Utility method to convert Set to comma seperated values.
	 */
	private String convertToCohortNames(Set<String> values){
		if(values == null) return null;
		StringBuffer buf = new StringBuffer();
		for(Iterator<String> it = values.iterator(); it.hasNext();){
			buf.append(it.next());
			if(it.hasNext())
				buf.append(",");
		}
		return buf.toString();
	}
	
	/*
	 * Utility method to convert comma seperated values into Set.
	 */
	private Set<EnumCardType> convertToPaymentTypes(String value){
		StringTokenizer tokens = new StringTokenizer(value,",");
		Set<EnumCardType> returnSet = new HashSet<EnumCardType>();
		
		while(tokens.hasMoreTokens()){
			returnSet.add(EnumCardType.getEnum(tokens.nextToken()));
		}
		return returnSet;
	}
	
	/*
	 * Utility method to convert Set to comma seperated values.
	 */
	private String convertToPaymentTypeNames(Set<EnumCardType> values){
		if(values == null) return null;
		StringBuffer buf = new StringBuffer();
		for(Iterator<EnumCardType> it = values.iterator(); it.hasNext();){
			buf.append(it.next());
			if(it.hasNext())
				buf.append(",");
		}
		return buf.toString();
	}
	
	public void setOrderRangeStart(int orderRangeStart) {
		this.orderRangeStart = orderRangeStart;
	}


	public void setOrderRangeEnd(int orderRangeEnd) {
		this.orderRangeEnd = orderRangeEnd;
	}


	public void setDPStatus(EnumDlvPassStatus dpStatus) {
		this.dpStatus = dpStatus;
	}


	public void setDPStartDate(Date dpStartDate) {
		this.dpStartDate = dpStartDate;
	}


	public void setDPEndDate(Date dpEndDate) {
		this.dpEndDate = dpEndDate;
	}


	public void setPaymentTypeNames(String paymentTypes) {
		this.paymentTypes = convertToPaymentTypes(paymentTypes);
	}


	public void setPriorEcheckUse(int priorEcheckUse) {
		this.priorEcheckUse = priorEcheckUse;
	}

	public Set<EnumOrderType> getAllowedOrderTypes() {
		return allowedOrderTypes;
	}

	public void setAllowedOrderTypes(Set<EnumOrderType> allowedOrderTypes) {
		this.allowedOrderTypes = allowedOrderTypes;
	}
	
	public boolean evaluateByPayment(ErpPaymentMethodI paymentMethod, PromotionContextI context){
		if(paymentMethod==null) return false;
		EnumCardType cardType=paymentMethod.getCardType();
		boolean isEligible = true;
		if(paymentTypes != null && paymentTypes.size() > 0 ) {			
			if(!paymentTypes.contains(cardType)) {
				
				if(paymentTypes.contains(EnumCardType.DEBIT)) {
					
					if(cardType.equals(EnumCardType.VISA)||cardType.equals(EnumCardType.MC)) {
						
						String profileId=paymentMethod.getProfileID();
								
						if(!StringUtil.isEmpty(profileId)) {							
							if(!paymentMethod.isDebitCard()) {
								isEligible = false;
						    }
						} else {
							isEligible = false;
						}
						
					} else {
						isEligible = false;
					}
					
				} else {
					isEligible = false;
				}
				
				//isEligible = false;
			}
			else if(cardType.equals(EnumCardType.ECP)) {
				int validEcheckOrderCount = context.getSettledECheckOrderCount();
				if((EnumComparisionType.EQUAL.equals(eCheckMatchType) && validEcheckOrderCount  != priorEcheckUse)
						||(EnumComparisionType.GREATER_OR_EQUAL.equals(eCheckMatchType) &&  validEcheckOrderCount  < priorEcheckUse)
						||(EnumComparisionType.LESS_OR_EQUAL.equals(eCheckMatchType) && validEcheckOrderCount  > priorEcheckUse)){
					isEligible = false;
				}
			}
		}
		return isEligible;
	}
	
	
	public void setDpTypesNames(String dpTypes) {
		this.dpTypes = convertToCohorts(dpTypes);

	}
	
	public String getDpTypesNames() {
		return convertToCohortNames(this.dpTypes);
	}

	public Set<String> getDpTypes() {
		return this.dpTypes;
	}

	public EnumComparisionType getECheckMatchType() {
		return eCheckMatchType;
	}

	public void setECheckMatchType(EnumComparisionType checkMatchType) {
		eCheckMatchType = checkMatchType;
	}

	
	@Override
	public boolean isStoreRequired() {
		return false;
	}

	public List<EnumDeliveryType> getOrderRangeDeliveryTypes() {
		return orderRangeDeliveryTypes;
	}

	public void setOrderRangeDeliveryTypes(
			List<EnumDeliveryType> orderRangeDeliveryTypes) {
		this.orderRangeDeliveryTypes = orderRangeDeliveryTypes;
	}

	public void setCohorts(Set<String> cohorts) {
		this.cohorts = cohorts;
	}

	public void setDpTypes(Set<String> dpTypes) {
		this.dpTypes = dpTypes;
	}

	public void setPaymentTypes(Set<EnumCardType> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}
}
