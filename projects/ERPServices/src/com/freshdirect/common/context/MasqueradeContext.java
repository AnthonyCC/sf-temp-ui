package com.freshdirect.common.context;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class MasqueradeContext implements Serializable {

	private static final long serialVersionUID = 2846585496074385119L;

	private String agentId;
	private boolean hasCustomerCase;
	private boolean forceOrderAvailable;
	private boolean forceOrderEnabled;
	private String makeGoodFromOrderId;
	private Set<String> makeGoodAllowedOrderLineIds;
	// map of orderline -> carton number
	private Map<String, String> cartonInfo;
	private boolean autoApproveAuthorized;
	private Double autoApprovalLimit;
	private String parentOrderId;
	private boolean csrWaivedDeliveryCharge = false;
	private boolean csrWaivedDeliveryPremium = false;
	private boolean silentMode = false;

	public boolean isAddOnOrderEnabled(){
	    return parentOrderId != null;
	}
	
	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}


	public String validateAddToCart(String orderLineId){
		if (makeGoodAllowedOrderLineIds!=null && (orderLineId==null || !makeGoodAllowedOrderLineIds.contains(orderLineId))){
			return "MakeGood - Product not allowed";
		} else {
			return null; // no error message
		}
	}

	public void clearMakeGoodContext() {
		makeGoodAllowedOrderLineIds = null;
		makeGoodFromOrderId = null;
		cartonInfo = null;
	}

	public boolean isMakeGood() {
		return makeGoodAllowedOrderLineIds != null
				|| makeGoodFromOrderId != null /* && cartonInfo != null */;
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

	public boolean isForceOrderEnabled() {
		return forceOrderEnabled;
	}

	public void setForceOrderEnabled(boolean forceOrderEnabled) {
		this.forceOrderEnabled = forceOrderEnabled;
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

	public void setCartonInfo(Map<String, String> cartonInfo) {
		this.cartonInfo = cartonInfo;
	}

	public Map<String, String> getCartonInfo() {
		return cartonInfo;
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
		if (autoApprovalLimit != null) {
			this.autoApprovalLimit = Double.parseDouble(autoApprovalLimit);
		}
	}

	public boolean isCsrWaivedDeliveryCharge() {
		return csrWaivedDeliveryCharge;
	}

	public void setCsrWaivedDeliveryCharge(boolean csrWaivedDeliveryCharge) {
		this.csrWaivedDeliveryCharge = csrWaivedDeliveryCharge;
	}

	public boolean isCsrWaivedDeliveryPremium() {
		return csrWaivedDeliveryPremium;
	}

	public void setCsrWaivedDeliveryPremium(boolean csrWaivedDeliveryPremium) {
		this.csrWaivedDeliveryPremium = csrWaivedDeliveryPremium;
	}

	public boolean isSilentMode() {
		return silentMode;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode = silentMode;
	}
}
