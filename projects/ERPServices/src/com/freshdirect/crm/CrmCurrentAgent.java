package com.freshdirect.crm;

public class CrmCurrentAgent {
	
	private String agentUserId;
	private CrmAgentRole role;
	
	public CrmCurrentAgent(String agentUserId, CrmAgentRole role) {
		super();
		this.agentUserId = agentUserId;
		this.role = role;
	}
	public String getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}
	public CrmAgentRole getRole() {
		return role;
	}
	public void setRole(CrmAgentRole role) {
		this.role = role;
	}
	
	

}
