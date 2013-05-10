package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;

public class FDCouponActivityContext implements Serializable{

	
	private static final long serialVersionUID = 1700639973886511631L;
	
	private EnumTransactionSource src;
	private String initiator="SYSTEM";//default
	private CrmAgentModel agent;
	
	public FDCouponActivityContext(EnumTransactionSource src, String initiator,
			CrmAgentModel agent) {
		super();
		this.src = src;
		this.initiator = initiator;
		this.agent = agent;
	}
	/**
	 * @return the src
	 */
	public EnumTransactionSource getSrc() {
		return src;
	}
	/**
	 * @return the initiator
	 */
	public String getInitiator() {
		return initiator;
	}
	/**
	 * @return the agent
	 */
	public CrmAgentModel getAgent() {
		return agent;
	}

}
