package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 */

public class CrmAgentModel extends ModelSupport {

	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private boolean active;
	private String roleCode;
	private Date createDate;
	private List agentCaseQueues;

	public CrmAgentModel() {
		super();
		this.agentCaseQueues = Collections.EMPTY_LIST;
	}

	public CrmAgentModel(PrimaryKey pk) {
		this();
		this.setPK(pk);
	}

	public CrmAgentModel(String userId, String password, String firstName, String lastName, boolean active, CrmAgentRole role) {
		this();
		this.userId = userId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.setRole(role);
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CrmAgentRole getRole() {
		return roleCode == null ? null : CrmAgentRole.getEnum(roleCode);
	}

	public void setRole(CrmAgentRole role) {
		this.roleCode = role == null ? null : role.getCode();
	}

	public List getAgentQueues() {
		List queues = new ArrayList();
		for (Iterator i = this.agentCaseQueues.iterator(); i.hasNext();) {
			String code = (String) i.next();
			queues.add(CrmCaseQueue.getEnum(code));
		}
		return queues;
	}

	public boolean hasCaseQueue(CrmCaseQueue caseQueue) {
		for (Iterator i = this.agentCaseQueues.iterator(); i.hasNext();) {
			String code = (String) i.next();
			CrmCaseQueue queue = CrmCaseQueue.getEnum(code);
			if (queue.equals(caseQueue)) {
				return true;
			}
		}
		return false;
	}

	public void setAgentQueues(List agentQueues) {
		List qs = new ArrayList();
		for (Iterator i = agentQueues.iterator(); i.hasNext();) {
			CrmCaseQueue q = (CrmCaseQueue) i.next();
			qs.add(q.getCode());
		}
		this.agentCaseQueues = qs;
	}

	public void addAgentQueue(CrmCaseQueue queue) {
		this.agentCaseQueues.add(queue.getCode());
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isMonitor() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE))
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSupervisor() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE))
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAdmin() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
			return true;
		}
		return false;
	}
	
	public boolean isExec() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.EXC_CODE))) {
			return true;
		}
		return false;
	}
	
	public boolean isGuest() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE))) {
			return true;
		}
		return false;
	}
	
	public boolean isAuthorizedToSeeAuthAndCCInfo() {
		return CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE).equals(this.getRole());
	}
	
	public boolean isAuthorizedToLookupAcctInfo() {
		return isSupervisor();
	}
	
	public boolean isCSR() {
		if(CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE)) ||
				isCSRHybrid()) {
			return true;
		}
		return false;
	}

	public boolean isCSRHybrid() {
		if(CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE)) || isSupervisor()) {
			return true;
		}
		return false;
	}
}
