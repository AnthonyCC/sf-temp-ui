package com.freshdirect.webapp.taglib.callcenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.webapp.util.JspMethods;


/**
 * 
 * @author segabor
 *
 */
public class AutoCaseGenerator {
	public static CrmCaseModel createAutoCase(CrmAgentModel agent, FDOrderI order, ErpComplaintModel complaint, ServletRequest request) throws FDResourceException {
		/* create case info */
		String ciMethod = "";
		double ciAmount = 0;
		
		switch(complaint.getComplaintMethod()) {
		case ErpComplaintModel.CASH_BACK:
			ciMethod = "cash-back";
			ciAmount = complaint.getCashBackAmount();
			break;
		case ErpComplaintModel.STORE_CREDIT:
			ciMethod = "store credit";
			ciAmount = complaint.getStoreCreditAmount();
			break;
		case ErpComplaintModel.MIXED:
		default:
			ciMethod = "mixed";
			ciAmount = complaint.getAmount();
			break;
		}
		
		CrmCaseInfo caseInfo = new CrmCaseInfo();

		// TODO: review case attributes
		caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS)); // FIXME: ...
		
		ErpComplaintReason topReason = complaint.getTopReason();
		CrmCaseSubject subject = CrmCaseSubject.getEnum(topReason.getSubjectCode());
		
		// FIXME: subject will be automatically assigned from reason to subject reference
		caseInfo.setSubject(subject);
		caseInfo.setPriority(CrmCasePriority.getEnum(CrmCasePriority.CODE_LOW));
		// caseInfo.setSummary(complaint.getDescription());

		// collect departments from credited items
		Set<String> depts = new HashSet<String>();
		for (Iterator<ErpComplaintLineModel> it=complaint.getComplaintLines().iterator(); it.hasNext();) {
			ErpComplaintLineModel line = it.next();
			depts.add(line.getDepartmentName());
		}
		StringBuffer deptsString = new StringBuffer();
		for (Iterator<String> it=depts.iterator(); it.hasNext();) {
			String deptName = it.next();
			deptsString.append(deptName);
			if (it.hasNext())
				deptsString.append("; ");
		}
		caseInfo.setSummary("Credit issued for items in " + deptsString.toString());
		
		
		caseInfo.setAssignedAgentPK(agent.getPK());
		caseInfo.setCustomerPK( new PrimaryKey(order.getCustomerId()) );
		caseInfo.setSalePK(new PrimaryKey(order.getErpSalesId()) );
		

		caseInfo.setActualQuantity(0);
		caseInfo.setProjectedQuantity(0);
		

		caseInfo.setCartonNumbers( new ArrayList(complaint.collectCartonNumbers()) );


		/* create new case */
		CrmCaseModel newCase = new CrmCaseModel(caseInfo);

		final boolean autoApproved = EnumComplaintStatus.APPROVED.equals(complaint.getStatus()) && "AUTO_APPROVED".equalsIgnoreCase(complaint.getApprovedBy());
		if (autoApproved) {
			// complaint is auto-approved so case can be closed now
			newCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_CLOSED));
		} else {
			// case needs to be reviewed / escalated to supervisor
			newCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_REVIEW));
		}
		

		// fill in additional fields
		String value;
		value = request.getParameter("ci_media");
		if (value != null)
			newCase.setCrmCaseMedia(value);

		value = request.getParameter("ci_moreissue");
		if (value != null)
			newCase.setMoreThenOneIssue(value);

		value = request.getParameter("ci_1st_ctct");
		if (value != null)
			newCase.setFirstContactForIssue(value);

		value = request.getParameter("ci_isres");
		if (value != null)
			newCase.setFirstContactResolved(value);

		value = request.getParameter("ci_reason_nr");
		if (value != null)
			newCase.setResonForNotResolve(value);

		value = request.getParameter("ci_satisf");
		if (value != null)
			newCase.setSatisfiedWithResolution(value);

		value = request.getParameter("ci_ctone");
		if (value != null)
			newCase.setCustomerTone(value);
		
		
		// Add note action about details
		{
			CrmCaseAction caseAction = new CrmCaseAction();
			caseAction.setType(CrmCaseActionType.getEnum( CrmCaseActionType.CODE_NOTE ) );
			caseAction.setTimestamp(new Date());
			caseAction.setAgentPK(agent.getPK());

			caseAction.setNote( complaint.getDescription() );
			newCase.addAction(caseAction);																		
		}
		
		
		{
			CrmCaseAction caseAction = new CrmCaseAction();
			caseAction.setType(CrmCaseActionType.getEnum( CrmCaseActionType.CODE_CLOSE ) );
			caseAction.setTimestamp(new Date());
			caseAction.setAgentPK(agent.getPK());

			caseAction.setNote( "Case Closed" );
			newCase.addAction(caseAction);																		
		}


		/* add action */
		if (autoApproved) {
			CrmCaseAction caseAction = new CrmCaseAction();
			caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
			caseAction.setTimestamp(new Date());
			caseAction.setAgentPK(agent.getPK());
			
			caseAction.setNote("Auto-approved credits ("+ JspMethods.currencyFormatter.format(ciAmount) +", "+ciMethod+")");
			newCase.addAction(caseAction);																		
		}

		return newCase;
	}
}
