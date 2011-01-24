package com.freshdirect.webapp.taglib.crm;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCurrentAgent;
import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
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
				
				String agentId = CrmSecurityManager.getUserName(request);
				String userRole = CrmSecurityManager.getUserRole(request);
				CrmAgentRole role = CrmAgentRole.getEnumByLDAPRole(userRole);
				CrmCurrentAgent agent = new CrmCurrentAgent(agentId,role);
				
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
//				PrimaryKey assignedAgentPK = "".equals(id) ? null : new PrimaryKey(id);
				String assignedAgentId = id;

				String custId = NVL.apply(request.getParameter("customerPK"), "");
				PrimaryKey customerPK = "".equals(custId) ? null : new PrimaryKey(custId);

				String saleId = NVL.apply(request.getParameter("salePK"), "");
				PrimaryKey salePK = "".equals(saleId) ? null : new PrimaryKey(saleId);

				String autoCase = NVL.apply(request.getParameter("isAutoCase"), "false");
				boolean isAutoCase = Boolean.parseBoolean(autoCase);
				
				boolean isPrivateCase = "on".equalsIgnoreCase(NVL.apply(request.getParameter("privateCase"), "off"));
				
				String summary = NVL.apply(request.getParameter("summary"), "").trim();

				String[] deptCodes = request.getParameterValues("departments");
				Set departments = new HashSet();
				if (deptCodes != null) {
					for (int i = 0; i < deptCodes.length; i++) {
						departments.add(CrmDepartment.getEnum(deptCodes[i]));
					}
				}

				String note = NVL.apply(request.getParameter("note"), "").trim();
				System.out.println("actionTypeName "+request.getParameter("actionTypeName"));
				CrmCaseActionType actionType = this.getActionTypeByName(request.getParameter("actionTypeName"));
				
				String media = NVL.apply(request.getParameter("media"), "").trim();
				
				//System.out.println("media:"+media);
				
				String morethenOne = NVL.apply(request.getParameter("morethenone"), "").trim();
				
			//	System.out.println("morethenOne:"+morethenOne);
				
                String firstContact = NVL.apply(request.getParameter("firstContact"), "").trim();                
                
             //   System.out.println("firstContact:"+firstContact);
                
                String resolvedStr = NVL.apply(request.getParameter("Resolved"), "").trim();                

                
                String notResolved = NVL.apply(request.getParameter("NotResolved"), "").trim();
                
                

                String satisfactoryReasonStr = NVL.apply(request.getParameter("satisfactoryReason"), "").trim();                

                String customerTone = NVL.apply(request.getParameter("customerTone"), "").trim();
                
                String isCaseClosed=NVL.apply(request.getParameter("isCaseClosed"), "").trim();
                
               // System.out.println("isCaseClosed:"+isCaseClosed);
                
                if("Yes".equalsIgnoreCase(resolvedStr))
                {                	
                	notResolved="";
                }
                if("Other".equalsIgnoreCase(media))
                {
                	morethenOne="";
                	firstContact="";
                	resolvedStr="";
                	notResolved="";
                	satisfactoryReasonStr="";
                	customerTone="";
                }
                
                /* [APPREQ-478] assign cartons - if case has order */
                List cartons = new ArrayList();
                if (salePK != null) {
                	FDOrderI order = CrmSession.getOrder(request.getSession(), salePK.getId());

                	final List orderCartons = new ArrayList(order.getCartonContents());
                	for (Iterator it=orderCartons.iterator(); it.hasNext();) {
                		FDCartonInfo ct = (FDCartonInfo) it.next();
                		String cn = ct.getCartonInfo().getCartonNumber();
						if (request.getParameter("cid_"+cn) != null) {
                			// put selected carton number to bag
                			cartons.add(cn);
                		}
                	}
                }



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

                // [APPREQ-478] Special case - if subject is ??? then case must have at least one carton assigned
				// [APPREQ-675] no check for auto cases
				actionResult.addError( !isAutoCase && subject.isCartonsRequired() && cartons.size() == 0, "carton", "One or more cartons required!");

				// execute
				if (actionResult.isSuccess()) {
					PrimaryKey pk = null;
					if (cm.isAnonymous()) {

						caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_PHONE));
						caseInfo.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
						caseInfo.setSubject(subject);
						caseInfo.setPriority(priority);
						caseInfo.setSummary(summary);
//						caseInfo.setAssignedAgentPK(this.getCurrentAgent().getPK());
						if(!CrmManager.getInstance().isAgentValid(this.getCurrentAgentStr())){
							actionResult.setError("Assigned agent:'"+this.getCurrentAgentStr()+"' is invalid.");
						}
						caseInfo.setAssignedAgentUserId(this.getCurrentAgentStr());
						caseInfo.setCustomerPK(customerPK);
						caseInfo.setSalePK(salePK);
						caseInfo.setDepartments(departments);
						caseInfo.setActualQuantity(actualQuantity);
						caseInfo.setProjectedQuantity(reportedQuantity);
						caseInfo.setCartonNumbers(cartons);
						
						caseInfo.setCrmCaseMedia(media);
						caseInfo.setMoreThenOneIssue(morethenOne);
						caseInfo.setFirstContactForIssue(firstContact);
						caseInfo.setFirstContactResolved(resolvedStr);
						caseInfo.setResonForNotResolve(notResolved);
						caseInfo.setSatisfiedWithResolution(satisfactoryReasonStr);
						caseInfo.setCustomerTone(customerTone);
						caseInfo.setPrivateCase(isPrivateCase);
						
						CrmCaseModel newCase = new CrmCaseModel(caseInfo);
						CrmCaseAction caseAction = new CrmCaseAction();
						caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
						caseAction.setTimestamp(new Date());
//						caseAction.setAgentPK(this.getCurrentAgent().getPK());
						caseAction.setAgentId(this.getCurrentAgentStr());
						caseAction.setNote(note);
						newCase.addAction(caseAction);	
						if(actionResult.isSuccess()){
							pk = CrmManager.getInstance().createCase(newCase);
						}

					} else {

						caseInfo.setSubject(subject);
						caseInfo.setPriority(priority);
						caseInfo.setSummary(summary);
//						caseInfo.setAssignedAgentPK(assignedAgentPK);
						if(!CrmManager.getInstance().isAgentValid(assignedAgentId)){
							actionResult.setError("Assigned selected agent:'"+assignedAgentId+"' is invalid.");
						}
						caseInfo.setAssignedAgentUserId(assignedAgentId);
						caseInfo.setCustomerPK(customerPK);
						caseInfo.setSalePK(salePK);
						caseInfo.setDepartments(departments);
						caseInfo.setActualQuantity(actualQuantity);
						caseInfo.setProjectedQuantity(reportedQuantity);
						caseInfo.setCartonNumbers(isAutoCase ? cm.getCartonNumbers() : cartons);
						caseInfo.setPrivateCase(isPrivateCase);

						if(!"true".equalsIgnoreCase(isCaseClosed)){
							caseInfo.setCrmCaseMedia(media);
							caseInfo.setMoreThenOneIssue(morethenOne);
							caseInfo.setFirstContactForIssue(firstContact);
							caseInfo.setFirstContactResolved(resolvedStr);
							caseInfo.setResonForNotResolve(notResolved);
							caseInfo.setSatisfiedWithResolution(satisfactoryReasonStr);
							caseInfo.setCustomerTone(customerTone);
						}
                       
						CrmCaseAction caseAction = new CrmCaseAction();
						if (actionType == null) {
							caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
						} else {
							caseAction.setType(actionType);
						}
						System.out.println("actionType after "+actionType);
						caseAction.setTimestamp(new Date());
//						caseAction.setAgentPK(this.getCurrentAgent().getPK());
						caseAction.setAgentId(CrmSecurityManager.getUserName(request));
						caseAction.setNote(note); 
						if(actionResult.isSuccess()){
							CrmManager.getInstance().updateCase(caseInfo, caseAction, agent);
							pk = cm.getPK();
						}
					}
					if(actionResult.isSuccess()){
						this.setSuccessPage(this.getSuccessPage() + "?case=" + pk.getId());
					}
				}
			}

		} catch (CrmAuthorizationException e) {
			System.out.println("CrmAuthorizationException :"+e);
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

	private String getCurrentAgentStr() {
		return CrmSession.getCurrentAgentStr(pageContext.getSession());
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
	}

}
