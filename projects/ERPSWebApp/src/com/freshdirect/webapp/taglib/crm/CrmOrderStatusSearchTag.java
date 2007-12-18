package com.freshdirect.webapp.taglib.crm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmOrderStatusSearchTag extends AbstractControllerTag {
	
	private String reportName;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String[] statusCodes = request.getParameterValues("status");
		List report = null;
		try {
			 report = CallCenterServices.getOrderStatusReport(statusCodes);
		} catch (FDResourceException e) {
			e.printStackTrace();
			actionResult.addError(new ActionError(SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		pageContext.setAttribute(this.reportName, report);
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
					data.getAttributeString("reportName"),
					"java.util.List",
					true,
					VariableInfo.NESTED)};
		}
	}
}


