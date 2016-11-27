package com.freshdirect.payment.ejb;

import java.io.Serializable;

import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;

public class PaymentGatewayContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4356597751482322400L;
	
	/*public PaymentGatewayContext(String piProfile, String pmProfile) {
		super();
		this.piProfile = piProfile;
		this.pmProfile = pmProfile;
	}
	
	
	private String piProfile;
	private String pmProfile;
	public String getPiProfile() {
		return piProfile;
	}
	public void setPiProfile(String piProfile) {
		this.piProfile = piProfile;
	}
	public String getPmProfile() {
		return pmProfile;
	}
	public void setPmProfile(String pmProfile) {
		this.pmProfile = pmProfile;
	}*/
	
	public PaymentGatewayContext(GatewayType preferred, GatewayType alternate) {
		super();
		this.preferred=preferred;
		this.alternate=alternate;
		
	}
	
	private GatewayType preferred;
	private GatewayType alternate;
	
	public GatewayType getPreferred() {
		return preferred;
	}
	public GatewayType getAlternate() {
		return alternate;
	}
	
	
}

