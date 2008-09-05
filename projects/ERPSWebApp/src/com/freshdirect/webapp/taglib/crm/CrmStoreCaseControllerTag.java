package com.freshdirect.webapp.taglib.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmStoreCaseControllerTag extends AbstractControllerTag {

	private final static Category LOGGER = LoggerFactory.getInstance(CrmStoreCaseControllerTag.class);

	private final static int MAX_SUMMARY = 256;
	private final static int MAX_NOTE = 4000;

	private CrmCaseModel cm;

	public void setCase(CrmCaseModel caseModel) {
		this.cm = caseModel;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		try {
			if ("storeCase".equalsIgnoreCase(this.getActionName())) {

				CrmCaseInfo caseInfo = cm.isAnonymous() ? new CrmCaseInfo() : new CrmCaseInfo(cm);

				// parse
				CrmCaseSubject subject = CrmCaseSubject.getEnum(request.getParameter("subject"));

				CrmCasePriority priority = CrmCasePriority.getEnum(request.getParameter("priority"));
				int reportedQuantity=0;
				int actualQuantity=0;
				try {
					reportedQuantity = Integer.parseInt(NVL.apply(request.getParameter("reported"),"0"));
				} catch (NumberFormatException nfe) {
					// eating exception don't care;
				}

				try {
					actualQuantity = Integer.parseInt(NVL.apply(request.getParameter("actual"),"0"));
				} catch (NumberFormatException nfe) {
					// eating exception don't care;
				}
				
				String id = NVL.apply(request.getParameter("assignedAgent"), "");
				PrimaryKey assignedAgentPK = "".equals(id) ? null : new PrimaryKey(id);

				String custId = NVL.apply(request.getParameter("customerPK"), "");
				PrimaryKey customerPK = "".equals(custId) ? null : new PrimaryKey(custId);

				String saleId = NVL.apply(request.getParameter("salePK"), "");
				PrimaryKey salePK = "".equals(saleId) ? null : new PrimaryKey(saleId);

				String summary = NVL.apply(request.getParameter("summary"), "").trim();

				String[] deptCodes = request.getParameterValues("departments");
				Set departments = new HashSet();
				if (deptCodes != null) {
					for (int i = 0; i < deptCodes.length; i++) {
						departments.add(CrmDepartment.getEnum(deptCodes[i]));
					}
				}

				String note = NVL.apply(request.getParameter("note"), "").trim();

				CrmCaseActionType actionType = this.getActionTypeByName(request.getParameter("actionTypeName"));
				
				String media = NVL.apply(request.getParameter("media"), "").trim();
				
				String morethenoneStr = NVL.apply(request.getParameter("morethenone"), "").trim();
                boolean isMoreThenOne=morethenoneStr.equalsIgnoreCase("Yes")?true:false;
				
                String firstContactStr = NVL.apply(request.getParameter("firstContact"), "").trim();
                boolean isFirstContact=firstContactStr.equalsIgnoreCase("Yes")?true:false;
                
                String resolvedStr = NVL.apply(request.getParameter("Resolved"), "").trim();
                boolean isresolved=resolvedStr.equalsIgnoreCase("Yes")?true:false;

                String notResolved = NVL.apply(request.getParameter("NotResolved"), "").trim();
                                

                String satisfactoryReasonStr = NVL.apply(request.getParameter("satisfactoryReason"), "").trim();
                boolean issatisfactoryReason=satisfactoryReasonStr.equalsIgnoreCase("Yes")?true:false;

                String customerTone = NVL.apply(request.getParameter("customerTone"), "").trim();
                
                
                // validate
				actionResult.addError(subject == null, "subject", "required");
				if (priority == null && subject != null) {
					priority = subject.getPriority();
				}
				actionResult.addError("".equals(summary), "summary", "required");
				actionResult.addError(summary.length() > MAX_SUMMARY, "summary", "too long (max " + MAX_SUMMARY + ")");

				if ((actionType == null) && "".equals(note)) {
					actionResult.addError(new ActionError("note", "required"));
					return true;
				} else if ((actionType != null) && !actionType.getCode().equals(CrmCaseActionType.CODE_EDIT) && "".equals(note)) {
					actionResult.addError(new ActionError("note", "required"));
					return true;
				}
				actionResult.addError(note.length() > MAX_NOTE, "note", "too long (max " + MAX_NOTE + ")");

				// execute
				if (actionResult.isSuccess()) {
					PrimaryKey pk = null;
					if (cm.isAnonymous()) {

						caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_PHONE));
						caseInfo.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
						caseInfo.setSubject(subject);
						caseInfo.setPriority(priority);
						caseInfo.setSummary(summary);
						caseInfo.setAssignedAgentPK(this.getCurrentAgent().getPK());
						caseInfo.setCustomerPK(customerPK);
						caseInfo.setSalePK(salePK);
						caseInfo.setDepartments(departments);
						caseInfo.setActualQuantity(actualQuantity);
						caseInfo.setProjectedQuantity(reportedQuantity);
						
						caseInfo.setCrmCaseMedia(media);
						caseInfo.setMoreThenOneIssue(isMoreThenOne);
						caseInfo.setFirstContactForIssue(isFirstContact);
						caseInfo.setFirstContactResolved(isresolved);
						caseInfo.setResonForNotResolve(notResolved);
						caseInfo.setSatisfiedWithResolution(issatisfactoryReason);
						caseInfo.setCustomerTone(customerTone);
						
						CrmCaseModel newCase = new CrmCaseModel(caseInfo);
						CrmCaseAction caseAction = new CrmCaseAction();
						caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
						caseAction.setTimestamp(new Date());
						caseAction.setAgentPK(this.getCurrentAgent().getPK());
						caseAction.setNote(note);
						newCase.addAction(caseAction);																		
						pk = CrmManager.getInstance().createCase(newCase);

					} else {

						caseInfo.setSubject(subject);
						caseInfo.setPriority(priority);
						caseInfo.setSummary(summary);
						caseInfo.setAssignedAgentPK(assignedAgentPK);
						caseInfo.setCustomerPK(customerPK);
						caseInfo.setSalePK(salePK);
						caseInfo.setDepartments(departments);
						caseInfo.setActualQuantity(actualQuantity);
						caseInfo.setProjectedQuantity(reportedQuantity);
						
						caseInfo.setCrmCaseMedia(media);
						caseInfo.setMoreThenOneIssue(isMoreThenOne);
						caseInfo.setFirstContactForIssue(isFirstContact);
						caseInfo.setFirstContactResolved(isresolved);
						caseInfo.setResonForNotResolve(notResolved);
						caseInfo.setSatisfiedWithResolution(issatisfactoryReason);
						caseInfo.setCustomerTone(customerTone);


						CrmCaseAction caseAction = new CrmCaseAction();
						if (actionType == null) {
							caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
						} else {
							caseAction.setType(actionType);
						}
						caseAction.setTimestamp(new Date());
						caseAction.setAgentPK(this.getCurrentAgent().getPK());
						caseAction.setNote(note);

						CrmManager.getInstance().updateCase(caseInfo, caseAction, this.getCurrentAgent().getPK());
						pk = cm.getPK();
					}
					this.setSuccessPage(this.getSuccessPage() + "?case=" + pk.getId());
				}
			}

		} catch (CrmAuthorizationException e) {
			actionResult.setError(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.warn("FDResourceException occured", e);
			throw new JspException(e);
		}

		return true;

	}

	private CrmCaseActionType getActionTypeByName(String name) {
		for (Iterator i = CrmCaseActionType.getEnumList().iterator(); i.hasNext();) {
			CrmCaseActionType ca = (CrmCaseActionType) i.next();
			if (ca.getName().equals(name)) {
				return ca;
			}
		}
		return null;
	}

	private CrmAgentModel getCurrentAgent() {
		return CrmSession.getCurrentAgent(pageContext.getSession());
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
	}

}
