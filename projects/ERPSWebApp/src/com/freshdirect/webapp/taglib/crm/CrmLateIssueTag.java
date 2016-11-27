
package com.freshdirect.webapp.taglib.crm;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class CrmLateIssueTag extends AbstractControllerTag {

	/* (non-Javadoc)
	 * @see com.freshdirect.webapp.taglib.AbstractControllerTag#performAction(javax.servlet.http.HttpServletRequest, com.freshdirect.framework.webapp.ActionResult)
	 */
	private static final int MAX_NOTE_LENGTH=100;
	private String id;
	private String route;
	private Date dlvDate;
	
	private int formsOnPage=1;
	private List lateIssues = null;
	
	private static Category LOGGER = LoggerFactory.getInstance(CrmLateIssueTag.class);

	public void setId(String id) {
		this.id = id;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getRoute() {
		return this.route;
	}
	
	public void setDeliveryDate(Date date) {
		this.dlvDate = date;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			lateIssues = new ArrayList();
			CrmManager cm = CrmManager.getInstance();
			if (route!=null && !"".equals(route) && dlvDate!=null ) {
				lateIssues = (List)cm.getLateIssuesByRouteAndDate(route,dlvDate);
			} else if (dlvDate!=null) {
				lateIssues = (List)cm.getLateIssuesByDate(dlvDate);
			}
		   if (lateIssues.size() < 1) {
			actionResult.addError(new ActionError("not_found", "No late issues found for specified date"));
		   }
		} catch (FDResourceException fde) {
			throw new JspException(fde);
		}
		pageContext.setAttribute(this.id, lateIssues);
		return true;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			lateIssues = new ArrayList();
			if (request.getMethod().equalsIgnoreCase("post")) {
				String sFormsOnPage = request.getParameter("formsOnPage");
				if (sFormsOnPage!=null) {
					try {
						formsOnPage = Integer.parseInt(sFormsOnPage);
					} catch (NumberFormatException nfe){
						//eating this exception....	
					}
				}
			}
			processForms(request, actionResult);
			if(actionResult.isSuccess() && lateIssues.size()==0) {
				actionResult.addError(new ActionError("blank", "You must fill out at least one form."));
			}
			if (actionResult.isSuccess()) {
				storeLateIssues(actionResult);
			}

		} catch (FDResourceException fde) {
			throw new JspException(fde);
		}
		pageContext.setAttribute(this.id, lateIssues);
		return true;
	}

	private void processForms(HttpServletRequest request, ActionResult result) {
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
		
		for (int i=0; i<formsOnPage; i++) {
			CrmLateIssueModel lateIssue = new CrmLateIssueModel();
			String suffix = formsOnPage>0 ? "_"+i : "";
			//fields : frmRoute, frmStops, frmDelay, frmReportedBy, frmReportedAt, frmNotes, frmDlvWindow, frmDlvDate
			if ( formsOnPage > 1 
				 && "".equals(NVL.apply(request.getParameter("frmRoute"+suffix),"")) 
				 && "".equals(NVL.apply(request.getParameter("frmStops"+suffix),""))
				 && "".equals(NVL.apply(request.getParameter("frmActualStops"+suffix),""))
				 && "".equals(NVL.apply(request.getParameter("frmNotes"+suffix),""))
				 && "".equals(NVL.apply(request.getParameter("frmDlvWindow"+suffix),""))
				 && "".equals(NVL.apply(request.getParameter("frmReportedAt"+suffix),""))
				 && "".equals(NVL.apply(request.getParameter("frmReportedBy"+suffix),""))
			     ) {
				continue; 
			}
			try {
				lateIssue.setRoute(request.getParameter("frmRoute"+suffix));
				lateIssue.setDelayMinutes(Integer.parseInt(request.getParameter("frmDelay"+suffix))); 
			} catch (NumberFormatException nfe) {
				result.addError(new ActionError("frmDelay"+suffix, "delay, in minutes, not specified"));
			} catch (IllegalArgumentException iae) {
				result.addError(new ActionError("frmRoute"+suffix, "Invalid or missing route"));
			}
			String frmPK = request.getParameter("frmPK"+suffix);
			if (frmPK!=null) {
				lateIssue.setPK(new PrimaryKey(frmPK));
			}
			String notes=NVL.apply(request.getParameter("frmNotes"+suffix),"");
			if (!"".equals(notes)) {
				lateIssue.setComments(notes.substring(0,Math.min(notes.length(),MAX_NOTE_LENGTH)));
			}
			lateIssue.setReportedBy(request.getParameter("frmReportedBy"+suffix));
			lateIssue.setAgentUserId(request.getParameter("frmAgentUID"+suffix));
			lateIssue.setDelivery_window(request.getParameter("frmDlvWindow"+suffix));
			lateIssue.setReportedStopsText(request.getParameter("frmStops"+suffix));
			lateIssue.setActualStopsText(request.getParameter("frmActualStops"+suffix));

			String frmReportedAt = 	NVL.apply(request.getParameter("frmReportedAt"+suffix),"");
			String frmDlvDate = NVL.apply(request.getParameter("dlvDate"),"");
			try {
				lateIssue.setReportedAt(df.parse(frmReportedAt));
			} catch (ParseException pe) {
				result.addError(new ActionError("frmReportedAt"+suffix,"missing or invalid report date"));
			}
			try {
				lateIssue.setDeliveryDate(df.parse(frmDlvDate+ " 12:00 am") );
			} catch (ParseException pe) {
				result.addError(new ActionError("frmDlvDate","missing or invalid delivery date"));
			}
			result.addError("".equals(NVL.apply(lateIssue.getRoute(),"").trim()) ,"frmRoute"+suffix,"missing truck route");
			result.addError("".equals(NVL.apply(lateIssue.getReportedBy(),"").trim()),"frmReportedBy"+suffix,"specify who reported issue");
			result.addError("".equals(NVL.apply(lateIssue.getComments(),"").trim()),"frmNotes"+suffix,"specify notes about this issue");
			result.addError("".equals(NVL.apply(lateIssue.getDelivery_window(),"").trim()),"frmDlvWindow"+suffix,"missing delivery window");
			result.addError("".equals(NVL.apply(lateIssue.getReportedStopsText(),"").trim()) ,"frmStops"+suffix,"missing stop numbers");
			result.addError(!lateIssue.expandReportedStops(true) ,"frmStops"+suffix,"Reported stops contain invalid information");
			result.addError(!lateIssue.expandActualStops(true) ,"frmActualStops"+suffix,"Actual stops contain invalid information");
			result.addError("".equals(NVL.apply(lateIssue.getAgentUserId(),"")) ,"frmAgentUID"+suffix,"missing agent's user id");

			if (result.isSuccess()) {
				lateIssues.add(lateIssue);
			}
		}
		
	}
	
	
	private void storeLateIssues(ActionResult result) throws FDResourceException{
		PrimaryKey pk=null;
		CrmManager cm= CrmManager.getInstance();
		LOGGER.debug("In storeLate Issues");
		try {
			for (int listIdx = 0; listIdx < lateIssues.size(); listIdx++) {
				CrmLateIssueModel lateIssue = (CrmLateIssueModel) lateIssues.get(listIdx);
				pk=lateIssue.getPK();
				if (pk==null ) {
					LOGGER.debug("about to write issue");
					pk = cm.createLateIssue(lateIssue);
				} else { //must be an update
					LOGGER.debug("about to update issue");
					cm.updateLateIssue(lateIssue);
				}
				//get it back
				LOGGER.debug("about to re-read issue");
				lateIssue = cm.getLateIssueById(pk.getId());
				lateIssues.remove(listIdx);
				lateIssues.add(listIdx,lateIssue);
			}
			LOGGER.debug("Leaving storeLate Issues");		} catch (RemoteException rme) {
			result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR));
		} catch(FinderException fe) {
			result.addError(new ActionError("technical_error","Unable to find Late Issue for id:"+pk!=null ?pk.getId() :"n/a"));
		}
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
	        return new VariableInfo[] {
	            new VariableInfo(data.getAttributeString("id"),
	                            "java.util.List",
	                            true,
	                            VariableInfo.NESTED),
					            new VariableInfo(data.getAttributeString("result"),
		                            "com.freshdirect.framework.webapp.ActionResult",
		                            true,
		                            VariableInfo.NESTED)             
	        };

	    }
	}		

}
