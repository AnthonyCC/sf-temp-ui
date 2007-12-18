package com.freshdirect.webapp.taglib.crm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetAllowedActionsTag extends AbstractGetterTag {

	private CrmCaseModel cm;

	public void setCase(CrmCaseModel cm) {
		this.cm = cm;
	}

	private boolean isMatchingAgent(CrmAgentModel agent, String queueCode, String roleCode) {
		return cm.getSubject().getQueue().equals(CrmCaseQueue.getEnum(queueCode))
			&& agent.getRole().equals(CrmAgentRole.getEnum(roleCode));
	}

	private boolean isAllowedOperation(CrmCaseOperation op, CrmAgentModel agent) {
		boolean assignedToSelf = cm.getAssignedAgentPK().equals(agent.getPK());
		boolean isCloseOp = op.getActionType().equals(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_CLOSE));
		if (!assignedToSelf && isCloseOp) {
			return agent.isSupervisor()
				|| isMatchingAgent(agent, CrmCaseQueue.CODE_ASQ, CrmAgentRole.ASV_CODE)
				|| (CrmCaseQueue.isTrnQueue(cm.getSubject().getQueue().getCode())
						&& agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE)));
		}
		return true;

	}
	protected Object getResult() throws FDResourceException {
		if (cm.getSubject() == null) {
			return Collections.EMPTY_SET;
		}

		CrmAgentModel currAgent = CrmSession.getCurrentAgent(pageContext.getSession());

		List operations = CrmManager.getInstance().getOperations(currAgent.getRole(), cm.getSubject(), cm.getState());

		Set allowedActionTypes = new HashSet();
		for (Iterator i = operations.iterator(); i.hasNext();) {
			CrmCaseOperation op = (CrmCaseOperation) i.next();

			if (isAllowedOperation(op, currAgent)) {
				allowedActionTypes.add(op.getActionType());
			}
			
		}

		return allowedActionTypes;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.Set";
		}
	}

}
