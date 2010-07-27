package com.freshdirect.fdstore.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.customer.FDCartModel;

public class CustomerStrategy implements PromotionStrategyI {
	private Set<String> cohorts;
	private int orderRangeStart;
	private int orderRangeEnd;
	private EnumDlvPassStatus dpStatus;
	private Date dpStartDate;
	private Date dpEndDate;
	private Set<EnumCardType> paymentTypes;
	private int priorEcheckUse;
	private Set<EnumOrderType> allowedOrderTypes;
	
	public CustomerStrategy() {
		
	}
	
	public int evaluate(String promotionCode, PromotionContextI context) {
		//Evaluate Cohorts
		if(cohorts != null && cohorts.size() > 0 && !cohorts.contains(context.getUser().getCohortName())) return DENY;
		
		//Evaluate Order Range. range is not defined properly. DENY
		if((orderRangeStart > 0 && orderRangeEnd <= 0) || (orderRangeStart <= 0 && orderRangeEnd > 0)) return DENY;
		if(orderRangeStart > 0 && orderRangeEnd > 0){
			int currentOrder = context.getAdjustedValidOrderCount() + 1;
			if(currentOrder  < orderRangeStart || currentOrder > orderRangeEnd) return DENY;
		}
		
		//Evaluate Delivery Pass Status
		if(dpStatus != null){
			if(!context.getUser().getDeliveryPassStatus().equals(dpStatus)) return DENY;
			if(dpStatus.equals(EnumDlvPassStatus.EXPIRED) && dpStartDate != null && dpEndDate != null) {
				Date dpExpDate = context.getUser().getDlvPassInfo().getExpDate();
				if(dpExpDate.before(dpStartDate) || dpExpDate.after(dpEndDate)) return DENY;
			}
		}
		
		//Evaluate Payment Types
		FDCartModel cart = context.getShoppingCart();
		if(paymentTypes != null && paymentTypes.size() > 0 && cart.getPaymentMethod() != null) {
			EnumCardType currentCardType = cart.getPaymentMethod().getCardType();
			if(!paymentTypes.contains(currentCardType)) return DENY;
			if(priorEcheckUse > 0 && currentCardType.equals(EnumCardType.ECP)){
				int validEcheckOrderCount = context.getSettledECheckOrderCount();
				if(validEcheckOrderCount  < priorEcheckUse) return DENY;
			}
		}
		
		//Evaluate Order Types
		if(!evaluateOrderType(context.getOrderType())) return DENY;
		
		return ALLOW;
	}

	public boolean evaluateOrderType(EnumOrderType orderType){
		if(allowedOrderTypes != null && allowedOrderTypes.size() > 0 && !allowedOrderTypes.contains(orderType)) return false;
		return true;
	}
	
	public int getPrecedence() {
		return 2000;
	}

	public String toString() {
		return "CustomerStrategy[...]";
	}


	public String getCohortNames() {
		return convertToCohortNames(this.cohorts);
	}

	public Set getCohorts() {
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

	public Set getPaymentTypes() {
		return this.paymentTypes;
	}

	
	public int getPriorEcheckUse() {
		return priorEcheckUse;
	}


	public void setCohorts(String cohortNames) {
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
	
	public void setOrderStartRange(int orderRangeStart) {
		this.orderRangeStart = orderRangeStart;
	}


	public void setOrderEndRange(int orderRangeEnd) {
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


	public void setPaymentTypes(String paymentTypes) {
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
	
	public boolean evaluateByPaymentCardType(EnumCardType cardType, PromotionContextI context){
		boolean isEligible = true;
		if(paymentTypes != null && paymentTypes.size() > 0 ) {			
			if(!paymentTypes.contains(cardType)) {
				isEligible = false;
			}
			if(priorEcheckUse > 0 && cardType.equals(EnumCardType.ECP)){
				int validEcheckOrderCount = context.getSettledECheckOrderCount();
				if(validEcheckOrderCount  < priorEcheckUse){
					isEligible = false;
				}
			}
		}
		return isEligible;
	}
}
