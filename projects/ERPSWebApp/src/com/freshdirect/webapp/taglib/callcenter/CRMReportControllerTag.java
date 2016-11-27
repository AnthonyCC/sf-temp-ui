package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDComplaintReportCriteria;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CRMReportControllerTag extends AbstractControllerTag {
	
	private FDComplaintReportCriteria criteria;
	private String id;
	
	public void setCriteria(FDComplaintReportCriteria criteria){
		this.criteria = criteria;
	}
	
	public void setId(String id){
		this.id = id;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			if(criteria.getStartDate() == null || criteria.getEndDate() == null){
				actionResult.addError(true, "required", SystemMessageList.MSG_REQUIRED);
				return true;
			}
			if(criteria.getEndDate().before(criteria.getStartDate()) || criteria.getEndDate().equals(criteria.getStartDate())){
				actionResult.addError(true, "dateRange", "end date must be after start date");
				return true;
			}
			pageContext.setAttribute(this.id, CallCenterServices.runComplaintReport(criteria));
		}catch (FDResourceException e){
			actionResult.addError(true, "technical difficulties", SystemMessageList.MSG_TECHNICAL_ERROR);
		}
		
		return true;
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),
				new VariableInfo(
					data.getAttributeString("id"),
					"java.util.List",
					true,
					VariableInfo.NESTED )
					
			};
		}
	}
}
