package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;

public class FDCouponActivityContext implements Serializable{

	
	private static final long serialVersionUID = 1700639973886511631L;
	
	private EnumTransactionSource src;
	private String initiator="SYSTEM";//default
	private CrmAgentModel agent;
	
	@JsonCreator
	public FDCouponActivityContext(@JsonProperty("src") EnumTransactionSource src,
			@JsonProperty("initiator") String initiator, @JsonProperty("agent") CrmAgentModel agent) {
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

	@Override
	public String toString() {
		return "src=" + this.src  + ", initiator=" + this.initiator +", agent=" + this.agent;
	}
}
