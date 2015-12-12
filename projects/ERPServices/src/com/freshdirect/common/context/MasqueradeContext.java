package com.freshdirect.common.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpReturnOrderModel;

public class MasqueradeContext implements Serializable {

	private static final long serialVersionUID = 2846585496074385119L;

	private String agentId;
	private boolean hasCustomerCase;
	private boolean forceOrderAvailable;
	private boolean forceOrderEnabled;
	private String makeGoodFromOrderId;
	private Map<String,Double> makeGoodOrderLineIdQuantities;
	private Map<String, String> makeGoodOrderLineIdCartonNumbers;
	private boolean autoApproveAuthorized;
	private Double autoApprovalLimit;
	private String parentOrderId;
	private boolean csrWaivedDeliveryCharge;
	private boolean csrWaivedDeliveryPremium;
	private boolean silentMode;

	public MasqueradeContext(){
	    makeGoodOrderLineIdQuantities = new HashMap<String,Double>();
	    makeGoodOrderLineIdCartonNumbers = new HashMap<String,String>();
	}
	
	public boolean isAddOnOrderEnabled(){
	    return parentOrderId != null;
	}
	
	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

    public String validateAddToCart(String orderLineId) {
        String errorMessage = null;
        if (!isEmptyMakeGoodOrderLineIdQuantities() && (orderLineId == null || !containsMakeGoodOrderLineIdQuantity(orderLineId))) {
            errorMessage = "MakeGood - Product not allowed";
        }
         return errorMessage;
    }

    public void clearMakeGoodContext() {
        clearMakeGoodOrderLineIdQuantities();
        clearMakeGoodOrderLineIdCartonNumbers();
        makeGoodFromOrderId = null;
    }

	public boolean isMakeGood() {
		return !isEmptyMakeGoodOrderLineIdQuantities() || !isEmptyMakeGoodOrderLineIdCartonNumbers() || makeGoodFromOrderId != null;
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

    public void clearMakeGoodOrderLineIdQuantities() {
        makeGoodOrderLineIdQuantities.clear();
    }

	public Set<String> getMakeGoodOrderLineIdQuantities() {
		return makeGoodOrderLineIdQuantities.keySet();
	}

	public void addMakeGoodOrderLineIdQuantity(String orderLineId, Double quantity) {
		makeGoodOrderLineIdQuantities.put(orderLineId, quantity);
	}
	
    public boolean containsMakeGoodOrderLineIdQuantity(String orderLineId) {
        return makeGoodOrderLineIdQuantities.containsKey(orderLineId);
    }
    
    public boolean isEmptyMakeGoodOrderLineIdQuantities() {
        return makeGoodOrderLineIdQuantities.isEmpty();
    }
    
    public Double getMakeGoodOrderLineIdQuantity(String orderLineId) {
        return makeGoodOrderLineIdQuantities.get(orderLineId);
    }
    
    public void clearMakeGoodOrderLineIdCartonNumbers() {
        makeGoodOrderLineIdCartonNumbers.clear();
    }

    public Set<String> getMakeGoodOrderLineIdCartonNumbers() {
        return makeGoodOrderLineIdCartonNumbers.keySet();
    }

    public void addMakeGoodOrderLineIdCartonNumber(String orderLineId, String cartonNumber) {
        makeGoodOrderLineIdCartonNumbers.put(orderLineId, cartonNumber);
    }
    
    public boolean containsMakeGoodOrderLineIdCartonNumber(String orderLineId) {
        return makeGoodOrderLineIdCartonNumbers.containsKey(orderLineId);
    }
    
    public boolean isEmptyMakeGoodOrderLineIdCartonNumbers() {
        return makeGoodOrderLineIdCartonNumbers.isEmpty();
    }
    
    public String getMakeGoodOrderLineIdCartonNumbers(String orderLineId) {
        return makeGoodOrderLineIdCartonNumbers.get(orderLineId);
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
