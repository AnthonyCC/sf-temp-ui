package com.freshdirect.common.context;

import java.io.Serializable;
import java.util.Set;

public class MasqueradeContext implements Serializable {
	
	private static final long serialVersionUID = 2846585496074385119L;

	private String agentId;
	private boolean hasCustomerCase;
	private boolean forceOrderAvailable;
	private String makeGoodFromOrderId;
	private Set<String> makeGoodAllowedOrderLineIds;
	private boolean autoApproveAuthorized;
	private Double autoApprovalLimit;

	public String validateAddToCart(String orderLineId){
		if (makeGoodAllowedOrderLineIds!=null && (orderLineId==null || !makeGoodAllowedOrderLineIds.contains(orderLineId))){
			return "MakeGood - Product not allowed";
		} else {
			return null; //no error message
		}
	}
	
	public void clearMakeGoodContext(){
		makeGoodAllowedOrderLineIds = null;
		makeGoodFromOrderId = null;
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	
	
	public boolean isHasCustomerCase() {
		return hasCustomerCase;
	}

	public void setHasCustomerCase(boolean hasCustomerCase) {
		this.hasCustomerCase = hasCustomerCase;
	}

	public void setAutoApprovalLimit(Double autoApprovalLimit) {
		this.autoApprovalLimit = autoApprovalLimit;
	}

	public boolean isForceOrderAvailable() {
		return forceOrderAvailable;
	}

	public void setForceOrderAvailable(boolean forceOrderAvailable) {
		this.forceOrderAvailable = forceOrderAvailable;
	}

	public String getMakeGoodFromOrderId() {
		return makeGoodFromOrderId;
	}

	public void setMakeGoodFromOrderId(String makeGoodFromOrderId) {
		this.makeGoodFromOrderId = makeGoodFromOrderId;
	}

	public Set<String> getMakeGoodAllowedOrderLineIds() {
		return makeGoodAllowedOrderLineIds;
	}

	public void setMakeGoodAllowedOrderLineIds(Set<String> makeGoodAllowedOrderLineIds) {
		this.makeGoodAllowedOrderLineIds = makeGoodAllowedOrderLineIds;
	}

	public boolean isAutoApproveAuthorized() {
		return autoApproveAuthorized;
	}

	public void setAutoApproveAuthorized(boolean autoApproveAuthorized) {
		this.autoApproveAuthorized = autoApproveAuthorized;
	}

	public Double getAutoApprovalLimit() {
		return autoApprovalLimit;
	}

	public void setAutoApprovalLimit(String autoApprovalLimit) {
		if (autoApprovalLimit!=null){
			this.autoApprovalLimit = Double.parseDouble(autoApprovalLimit);
		}
	}
	
}
