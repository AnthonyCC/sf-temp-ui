package com.freshdirect.crm;

import java.io.Serializable;

public class CrmCaseOperation implements Serializable {

	private final String roleCode;
	private final String subjectCode;
	private final String startStateCode;
	private final String endStateCode;
	private final String actionTypeCode;

	public CrmCaseOperation(
		CrmAgentRole role,
		CrmCaseSubject subject,
		CrmCaseState startState,
		CrmCaseState endState,
		CrmCaseActionType actionType) {
		this(
			role == null ? null : role.getCode(),
			subject == null ? null : subject.getCode(),
			startState == null ? null : startState.getCode(),
			endState == null ? null : endState.getCode(),
			actionType == null ? null : actionType.getCode());
	}

	public CrmCaseOperation(
		String roleCode,
		String subjectCode,
		String startStateCode,
		String endStateCode,
		String actionTypeCode) {
		this.roleCode = roleCode;
		this.subjectCode = subjectCode;
		this.startStateCode = startStateCode;
		this.endStateCode = endStateCode;
		this.actionTypeCode = actionTypeCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public CrmAgentRole getRole() {
		return CrmAgentRole.getEnum(roleCode);
	}

	public CrmCaseSubject getSubject() {
		return CrmCaseSubject.getEnum(subjectCode);
	}

	public CrmCaseState getStartState() {
		return CrmCaseState.getEnum(startStateCode);
	}

	public CrmCaseState getEndState() {
		return CrmCaseState.getEnum(endStateCode);
	}

	public CrmCaseActionType getActionType() {
		return CrmCaseActionType.getEnum(actionTypeCode);
	}

	public boolean isMatching(CrmCaseOperation template) {
		boolean match = true;

		match &= fieldMatch(template.roleCode, roleCode);
		match &= fieldMatch(template.subjectCode, subjectCode);
		match &= fieldMatch(template.startStateCode, startStateCode);
		match &= fieldMatch(template.endStateCode, endStateCode);
		match &= fieldMatch(template.actionTypeCode, actionTypeCode);

		return match;
	}

	private boolean fieldMatch(String template, String actual) {
		return template == null || actual.equals(template);
	}

}
