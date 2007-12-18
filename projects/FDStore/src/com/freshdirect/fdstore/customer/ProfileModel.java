package com.freshdirect.fdstore.customer;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.core.ModelSupport;

public class ProfileModel extends ModelSupport {

	private final Map attributes = new HashMap();

	public String getAttribute(String key) {
		return (String) attributes.get(key);
	}

	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	public Map getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map attributes) {
		this.attributes.putAll(attributes);
	}

	public void removeAttribute(String key) {
		this.attributes.remove(key);
	}
	
	/** helper methods **/
	public boolean isVIPCustomer() {
		return "true".equalsIgnoreCase(getAttribute("VIPCustomer"));
	}

	public boolean isChefsTable() {
		return "1".equalsIgnoreCase(getAttribute("ChefsTable"));
	}

	public String getCustomerMetalType() {
		String metalValue = getAttribute("MetalCategory");
		return (metalValue == null || metalValue.length() != 1 || "0123456".indexOf(metalValue) == -1) ? "0" : metalValue;
	}

	public boolean isCOSPilot() {
		return "true".equalsIgnoreCase(getAttribute("COSPilot"));
	}

	public String getQuickShopLevel() {
		return getAttribute("QuickShopLevel");
	}

	public String getEmergencyFlag() {
		return getAttribute("EmergencyFlag");
	}

	public String getProfileGroup() {
		return getAttribute("ProfileGroup");
	}

	public boolean isPhonePrivate() {
		return "true".equalsIgnoreCase(getAttribute("PhonePrivate"));
	}
	
	public boolean isOnFDAccount(){
		return ("true".equalsIgnoreCase(getAttribute("onFDAccount")));
	}
	
	public String getEcpPromo(){
		return getAttribute("EcpPromo");
	}
	
	public String getDeliveryPass() {
		return getAttribute("DeliveryPass");
	}
	public String getHouseholdType() {
		String householdType = getAttribute("HouseholdType");
		return (householdType == null || householdType.length() != 1 || "0123456".indexOf(householdType) == -1) ? "unknown" : householdType;
	}
	
}