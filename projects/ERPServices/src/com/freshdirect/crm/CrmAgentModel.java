package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 */

public class CrmAgentModel extends ModelSupport {

	private static final long serialVersionUID = 5436939163075682988L;
	
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private boolean active;
	private String roleCode;
	private Date createDate;
	private List<String> agentCaseQueues;
	private boolean masqueradeAllowed = false;
	private String ldapId;
	private String curStoreContext = null;
	private String curFacilityContext = null;

	public CrmAgentModel() {
		super();
		this.agentCaseQueues = Collections.<String>emptyList();
	}

	public CrmAgentModel(PrimaryKey pk) {
		this();
		this.setPK(pk);
	}

	public CrmAgentModel(String userId, String password, String firstName, String lastName, boolean active, CrmAgentRole role, boolean masqueradeAlowed) {
		this();
		this.userId = userId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.masqueradeAllowed = masqueradeAlowed;
		this.setRole(role);
	}

	public String getUserId() {
		//return this.userId;
		return this.ldapId;
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

	public List<CrmCaseQueue> getAgentQueues() {
		List<CrmCaseQueue> queues = new ArrayList<CrmCaseQueue>();
		for ( String code : this.agentCaseQueues ) {
			queues.add(CrmCaseQueue.getEnum(code));
		}
		return queues;
	}

	public boolean hasCaseQueue(CrmCaseQueue caseQueue) {
		for ( String code : this.agentCaseQueues ) {
			CrmCaseQueue queue = CrmCaseQueue.getEnum(code);
			if (queue.equals(caseQueue)) {
				return true;
			}
		}
		return false;
	}

	public void setAgentQueues(List<CrmCaseQueue> agentQueues) {
		List<String> qs = new ArrayList<String>();
		for ( CrmCaseQueue q : agentQueues ) {
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
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.DEV_CODE))
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.QA_CODE))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOpsUser() {
		if (CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.OPS_CODE))
			|| CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE))) {
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
	
	public boolean isFDX() {
		if ( CrmAgentRole.getEnum(this.roleCode).equals(CrmAgentRole.getEnum(CrmAgentRole.FDX_CODE)) ) {
			return true;
		} else {
			return false;
		}
	}

	
	public void setMasqueradeAllowed( boolean flag ) {
		masqueradeAllowed = flag;
	}
	
	public boolean isMasqueradeAllowed() {
		return masqueradeAllowed;
	}

	public String getLdapId() {
		return ldapId;
	}

	public void setLdapId(String ldapId) {
		this.ldapId = ldapId;
	}


	//TODO : faking facility
	public String getCurFacilityContext() {
		if (this.curFacilityContext == null) {
			this.setCurFacilityContext("All");
		}
		return curFacilityContext;
	}

	public void setCurFacilityContext(String curFacilityContext) {
		this.curFacilityContext = curFacilityContext;
	}

	
	//Introduced for Storefront 2.0
	public String getRoleCode() {
		return roleCode;
	}
	
	
}
