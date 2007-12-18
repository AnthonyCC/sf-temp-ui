package com.freshdirect.webapp.taglib.crm;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmCaseDownloadControllerTag extends AbstractControllerTag {
	
	private static final int MAX_ALLOWED_DOWNLOAD = 10;
	private PrimaryKey agentPK;
	private String selectedQueue;
	private String subject;
	private int numberToDownload = 0;
	
	public void setAgentPk(PrimaryKey agentPK){
		this.agentPK = agentPK;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			if("downloadCases".equalsIgnoreCase(this.getActionName())){
				this.processRequest(request);
				this.validateRequest(actionResult);
				if(actionResult.isSuccess()){
					CrmManager.getInstance().downloadCases(this.agentPK, this.selectedQueue, this.subject, this.numberToDownload); 
				}
			}
			return true;
		}catch(FDResourceException e){
			throw new JspException(e);
		}
	}

	private void processRequest(HttpServletRequest request) {
		this.selectedQueue = NVL.apply(request.getParameter("selectedQueue"), "").trim();
		this.subject = NVL.apply(request.getParameter("subject"), "").trim();
		
		try{
			this.numberToDownload = Integer.parseInt(NVL.apply(request.getParameter("numberToDownload"), "").trim());
		}catch(NumberFormatException e){
			this.numberToDownload = 0;
		}
	}
	
	private void validateRequest(ActionResult actionResult) throws FDResourceException {
		int openCasesInQueue = 0;
		actionResult.addError("".equals(this.selectedQueue), "selectedQueue", "Queue is required");
		if(actionResult.isSuccess()){
			CrmCaseQueue queue = CrmCaseQueue.getEnum(this.selectedQueue);
			List queueOverview = CrmManager.getInstance().getQueueOverview();
			for(Iterator i = queueOverview.iterator(); i.hasNext(); ){
				CrmQueueInfo qInfo = (CrmQueueInfo) i.next();
				if(qInfo.getQueue().equals(queue)){
					openCasesInQueue = qInfo.getUnassigned();
				}
			}
		}
		actionResult.addError(this.numberToDownload == 0 || this.numberToDownload > MAX_ALLOWED_DOWNLOAD, "numberToDownload", "The number must be between 1 and "+MAX_ALLOWED_DOWNLOAD);
		if(actionResult.isSuccess()){
			actionResult.addError(this.numberToDownload > openCasesInQueue, "numberToDownload", "The number must be less than or equal to: "+openCasesInQueue);
		}
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}

}
