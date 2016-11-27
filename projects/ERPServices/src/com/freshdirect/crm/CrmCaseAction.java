package com.freshdirect.crm;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseAction extends ModelSupport {

	private String caseActionCode;
	private String note;
	private Date timestamp;
	private PrimaryKey agentPK;

	public void setPK(PrimaryKey pk) {
		super.setPK(pk);
	}

	public CrmCaseActionType getType() {
		return caseActionCode == null ? null : CrmCaseActionType.getEnum(this.caseActionCode);
	}

	public void setType(CrmCaseActionType type) {
		this.caseActionCode = type == null ? null : type.getCode();
	}

	public String getNote() {
		return note;
	}

	public void setNote(String string) {
		note = string;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date date) {
		timestamp = date;
	}
	
	public PrimaryKey getAgentPK() {
		return agentPK;
	}

	public void setAgentPK(PrimaryKey key) {
		agentPK = key;
	}

}
