/*
 * $Workfile:IssueCreditControllerTag.java$
 *
 * $Date:8/5/2003 12:29:45 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

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
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.CallcenterUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;

/**
 *
 * @version $Revision:17$
 * @author $Author:Mike Rose$
 */
public class IssueCreditControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(IssueCreditControllerTag.class);

	private final String ACTION_SUBMIT = "submit";
	private final String ACTION_APPROVE = "approve";
	private final String ACTION_REJECT = "reject";
	private static final int MAX_DESCRIPTION_SIZE=1024;

	private String orderId;
	private FDOrderI order;

	// Tag attribute variables
	private String action = ACTION_SUBMIT;
	private String result;
	private String successPage;
	private ErpComplaintModel complaintModel;

	public void setAction(String s) {
		this.action = s;
	}

	public void setResult(String s) {
		this.result = s;
	}

	public void setSuccessPage(String sp) {
		this.successPage = sp;
	}

	public void setComplaintModel(ErpComplaintModel cm) {
		this.complaintModel = cm;
	}

	public int doStartTag() throws JspException {

		ActionResult actionResult = new ActionResult();
		ActionResult createResult = (ActionResult) pageContext.getAttribute("createComplaintResult");
		if (createResult != null && createResult.isSuccess()) {

			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpSession session = (HttpSession) request.getSession();
			this.orderId = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
			// this is the situation when this is called from pending_credit_list.jsp
			if (orderId == null || "".equals(orderId)) {
				orderId = (String) request.getParameter("orderId");
			}
			try {
				this.order = FDCustomerManager.getOrder(orderId);
			} catch (FDResourceException fdre) {
				LOGGER.error("Couldn't get order to issue credit on", fdre);
				throw new JspException(fdre.getMessage());
			}

			if ("POST".equalsIgnoreCase(request.getMethod())) {

				if (ACTION_APPROVE.equals(this.action)) {
					//
					// This is a SUPERVISOR approval request
					//
					try {
						doApproval(request, actionResult, true);
						if (actionResult.isSuccess()) {
							CrmSession.invalidateCachedOrder(session);
							
							// Also invalidate assigned user's order history
							FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
							if (user != null) {
								user.invalidateOrderHistoryCache();
							}
							
							LOGGER.debug("Success, redirecting to: " + successPage);
							HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
							try {
								response.sendRedirect(response.encodeRedirectURL(successPage));
								JspWriter writer = pageContext.getOut();
								writer.close();
							} catch (IOException ioe) {
								throw new JspException(ioe.getMessage());
							}
						}
					} catch (ErpComplaintException ex) {
						LOGGER.warn("ErpComplaintException: could not approve complaint.", ex);
						actionResult.addError(new ActionError("approval_error", ex.getMessage()));
					} catch (FDResourceException ex) {
						LOGGER.warn("FDResourceException: ", ex);
						actionResult.addError(
							new ActionError("technical_difficulty", "Action not processed due to technical difficulty."));
					} catch (CrmAuthorizationException ex) {
						LOGGER.warn("CrmAuthorizationException: could not approve complaint.", ex);
						actionResult.addError(new ActionError("technical_difficulty", ex.getMessage()));
					}
				} else if (ACTION_REJECT.equals(this.action)) {
					//
					// This is a SUPERVISOR rejection request
					//
					try {
						doApproval(request, actionResult, false);
						if (actionResult.isSuccess()) {
							CrmSession.invalidateCachedOrder(session);

							// Also invalidate assigned user's order history
							FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
							if (user != null) {
								user.invalidateOrderHistoryCache();
							}
							
							LOGGER.debug("Success, redirecting to: " + successPage);
							HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
							try {
								response.sendRedirect(response.encodeRedirectURL(successPage));
								JspWriter writer = pageContext.getOut();
								writer.close();
							} catch (IOException ioe) {
								throw new JspException(ioe.getMessage());
							}
						}
					} catch (ErpComplaintException ex) {
						LOGGER.warn("ErpComplaintException: could not reject complaint.", ex);
						actionResult.addError(new ActionError("approval_error", ex.getMessage()));
					} catch (FDResourceException ex) {
						LOGGER.warn("FDResourceException: ", ex);
						actionResult.addError(
							new ActionError("technical_difficulty", "Action not processed due to technical difficulty."));
					} catch (CrmAuthorizationException ex) {
						LOGGER.warn("CrmAuthorizationException: could not approve complaint.", ex);
						actionResult.addError(new ActionError("technical_difficulty", ex.getMessage()));
					}
				} else if (ACTION_SUBMIT.equals(this.action)) {
					//
					// This is a standard CSR complaint submission
					//
					validateComplaint(actionResult, complaintModel);
					if (actionResult.isSuccess() && "true".equalsIgnoreCase(request.getParameter("do_issue_credit"))) {
						try {
							// Create complaint first
							addComplaint(actionResult, complaintModel);
							
							// Generate an auto case
							CrmCaseModel autoCase = AutoCaseGenerator.createAutoCase(CrmSession.getCurrentAgent(session), order, complaintModel, pageContext.getRequest());
							// store case
							PrimaryKey autoCasePK = CrmManager.getInstance().createCase(autoCase);

							FDCustomerManager.assignAutoCaseToComplaint(complaintModel, autoCasePK);


							LOGGER.debug("Success, redirecting to: " + successPage);
							CrmSession.invalidateCachedOrder(session);
							HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
							try {
								response.sendRedirect(response.encodeRedirectURL(successPage));
								JspWriter writer = pageContext.getOut();
								writer.close();
							} catch (IOException ioe) {
								throw new JspException(ioe.getMessage());
							}
						} catch (ErpComplaintException e) {
							LOGGER.warn("ErpComplaintException: order status is not COMPLETE", e);
							actionResult.addError(true, "invalid_complaint", e.getMessage());
						} catch (FDResourceException ex) {
							LOGGER.warn("FDResourceException while trying to add complaint", ex);
							actionResult.addError(true, "technical_difficulty", "Complaint not logged due to technical difficulty.");
						}
					} // end if result.isSuccess()
				}
			} // end if POST
		}
		pageContext.setAttribute(this.result, actionResult);
		return EVAL_BODY_BUFFERED;

	}

	/**
	 * Validates complaint value against previous credit amounts
	 *
	 * @param ActionResult to capture success or failure of validation
	 * @param ErpComplaintModel containing info for this complaint
	 */
	private void validateComplaint(ActionResult result, ErpComplaintModel complaintModel) {

		if (complaintModel.getComplaintLines().isEmpty()) {
			result.addError(new ActionError("invalid_complaint", "Form incomplete: no complaint lines specified"));
			return;
		}
		if(complaintModel.getDescription().length()>MAX_DESCRIPTION_SIZE) {
			result.addError(new ActionError("invalid_complaint", "Credit notes can only have a maximum of 1024 characters"));
			return;
		}

		int method = complaintModel.getComplaintMethod();
		if(0.0 == MathUtil.roundDecimal(complaintModel.getAmount()) && (ErpComplaintModel.CASH_BACK == method || ErpComplaintModel.MIXED == method)){
			result.addError(new ActionError("invalid_complaint", "Cannont issue cashback for $0"));
			return;
		}
		
		for(Iterator i = complaintModel.getComplaintLines().iterator(); i.hasNext(); ){
			ErpComplaintLineModel complaintLine = (ErpComplaintLineModel) i.next();
			if(EnumComplaintLineMethod.CASH_BACK.equals(complaintLine.getMethod()) && MathUtil.roundDecimal(complaintLine.getAmount()) == 0.0){
				result.addError(new ActionError("invalid_complaint", "Cannont issue cashback for $0"));
				return;
			}
		}
		
		ComplaintUtil complaintUtil = new ComplaintUtil();

		//
		// Compare the total value of the complaint against the amount already approved as Customer Credit
		//
		double previousComplaintTotal = 0;
		Collection previousComplaints = this.order.getComplaints();

		HashMap allDeptsMap = complaintUtil.getOrderInfo(this.order);
		//
		// Get total for previous APPROVED complaints
		//
		for (Iterator it = previousComplaints.iterator(); it.hasNext();) {
			ErpComplaintModel complaint = (ErpComplaintModel) it.next();
			if (complaint.getStatus().equals(EnumComplaintStatus.APPROVED))
				previousComplaintTotal += complaint.getAmount();
		}
		LOGGER.debug("previousComplaintTotal is: " + previousComplaintTotal);
		LOGGER.debug("complaintModel amount is : " + complaintModel.getAmount());
		LOGGER.debug("order subtotal is        : " + order.getSubTotal());

		if (order.getSubTotal() - previousComplaintTotal <= 1.0) {
			result.addError(true, "no_more_refunds", "Order already fully refunded.");
		}
		
		//
		// Check whether new complaint amount is too much
		//
		if (complaintModel.getAmount() + previousComplaintTotal > order.getSubTotal()) {
			//
			// Get the amount to be deducted from the complaint overall
			//
			double diff = (complaintModel.getAmount() + previousComplaintTotal) - order.getSubTotal();
			LOGGER.debug("diff: " + diff);

			//
			// Deduct amount from each line according to its proportion of the order subtotal
			//
			LOGGER.debug("AMOUNT BEFORE ADJUSTMENT: " + complaintModel.getAmount());
			for (Iterator it = complaintModel.getComplaintLines().iterator(); it.hasNext();) {
				ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
				ComplaintDeptInfo deptInfo = (ComplaintDeptInfo) allDeptsMap.get(line.getDepartmentCode());
				if (deptInfo != null) {
					double pctOfSubtotal = deptInfo.getTotal().doubleValue() / order.getSubTotal();
					LOGGER.debug(
						"deducting $"
							+ (diff * pctOfSubtotal)
							+ " because "
							+ line.getDepartmentCode()
							+ " dept was "
							+ pctOfSubtotal
							+ " of subtotal");
					if (EnumComplaintLineType.DEPARTMENT.equals(line.getType())
						|| EnumComplaintLineType.SEVENTY_FIVE_PCT.equals(line.getType())
						|| EnumComplaintLineType.FULL_REFUND.equals(line.getType()))
						line.setAmount(line.getAmount() - (diff * pctOfSubtotal));
				} else {
					LOGGER.debug("NO ComplaintDeptInfo FOR " + line.getDepartmentCode());
				}
			}
			LOGGER.debug("AMOUNT AFTER ADJUSTMENT: " + complaintModel.getAmount());
			LOGGER.debug(
				"BALANCE AFTER COMPLAINT APPROVAL: "
					+ (order.getSubTotal() - (complaintModel.getAmount() + previousComplaintTotal)));
		}
	}

	/**
	 * Adds a complaint to the user's list of complaints and begins the associated credit issuing process
	 *
	 * @param ErpComplaintModel represents the complaint
	 * @param ActionResult result object for error checking/logging
	 * @return String the PK of the ErpComplaintModel just created
	 * @throws ErpComplaintException if order was not in proper state to accept complaints
	 */
	private void addComplaint(ActionResult result, ErpComplaintModel complaintModel)
		throws FDResourceException, ErpComplaintException {

		LOGGER.debug(complaintModel.describe() );
		
		HttpSession session = pageContext.getSession();
		FDIdentity identity = ((FDUserI)session.getAttribute(SessionName.USER)).getIdentity();
		FDCustomerManager.addComplaint(complaintModel, orderId,identity);
	}

	private void doApproval(HttpServletRequest request, ActionResult result, boolean isApproved)
		throws FDResourceException, ErpComplaintException, CrmAuthorizationException {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());

		
		// Close auto case first
		if (agent != null && this.complaintModel.getAutoCaseId() != null) {
			CrmCaseModel aCase = CrmManager.getInstance().getCaseByPk(this.complaintModel.getAutoCaseId());
			
			// try to lock case
			PrimaryKey ownerPK = aCase.getLockedAgentPK();

			boolean caseIsMine = false;
			if (ownerPK != null) {
				if (!agent.getPK().equals(ownerPK)) {
					// gosh .. someone already owning the case
					result.addError(true, "approval_error", "Requested action cannot be performed. Associated case is locked by another user.");
					return;
				}
				caseIsMine = true;
			}

			if (!caseIsMine) {
				// lock the case
				CrmSession.setLockedCase(session, aCase);
			}


			// now do the work - close case physically
			{
				/*
				aCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_CLOSED));
	
				CrmCaseAction caseAction = new CrmCaseAction();
				caseAction.setType(CrmCaseActionType.getEnum( CrmCaseActionType.CODE_CLOSE ) );
				caseAction.setTimestamp(new Date());
				caseAction.setAgentPK(agent.getPK());
				caseAction.setNote((isApproved ? "Approved" : "Rejected")+" and closed");
				*/

				CrmManager.getInstance().closeAutoCase(aCase.getPK());
			}
		}

		
		
		boolean sendMail = true;
		if(request.getParameter("sendMail") != null) sendMail = false;
		
		if (agent != null) {
			FDCustomerManager.approveComplaint(request.getParameter("complaintId"), isApproved, agent.getUserId(), sendMail);
		} else {
			CallcenterUser ccUser = (CallcenterUser) session.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
			FDCustomerManager.approveComplaint(request.getParameter("complaintId"), isApproved, ccUser.getId(), sendMail);
		}
	}
}
