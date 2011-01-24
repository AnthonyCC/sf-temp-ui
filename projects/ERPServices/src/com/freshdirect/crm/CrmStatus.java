/**
 * @author ekracoff
 * Created on Mar 29, 2005
 * 
 * provides indicator as to where an agent was when his session expired*/

package com.freshdirect.crm;

import com.freshdirect.framework.core.PrimaryKey;



public class CrmStatus{
	private final PrimaryKey agentPK;
	private String saleId;
	private String erpCustomerId;
	private String caseId;
	private String agentId;
	
	public CrmStatus(final PrimaryKey agentPK) {
		super();
		this.agentPK = agentPK;
	}
	
	public CrmStatus(final String agentId) {
		super();
		this.agentPK = null;
		this.agentId = agentId;
	}
	
	public PrimaryKey getAgentPK() {
		return agentPK;
	}
	
	public String getCaseId() {
		return caseId;
	}
	
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	public String getSaleId() {
		return saleId;
	}
	
	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	
	public String getErpCustomerId() {
		return erpCustomerId;
	}
	
	public void setErpCustomerId(String customerPK) {
		this.erpCustomerId = customerPK;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
}
