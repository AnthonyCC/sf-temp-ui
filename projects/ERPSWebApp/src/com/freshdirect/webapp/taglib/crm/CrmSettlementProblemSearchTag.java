/*
 * Created on Apr 18, 2005
 */
package com.freshdirect.webapp.taglib.crm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.EnumMonth;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**
 * @author jng
 *
 */
public class CrmSettlementProblemSearchTag extends AbstractControllerTag {

	private String reportName;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String[] statusTransType = request.getParameterValues("status_trans_type");
		if (statusTransType == null || statusTransType.length < 1) {
			return true;
		}		
		String[] statusCodes = new String [statusTransType.length];
		String[] transactionTypes = new String [statusTransType.length];
		for (int i = 0; i < statusTransType.length; i++ ) {
			StringTokenizer strTok = new StringTokenizer(statusTransType[i]);
			if (strTok.countTokens() == 2) {
				statusCodes[i] = strTok.nextToken();
				transactionTypes[i] = strTok.nextToken();
			}
		}
		Date failureStartDate = populateDateParameter(request, "failure_start");
		Date failureEndDate = populateDateParameter(request, "failure_end");
		if (failureEndDate != null) {
			failureEndDate = DateUtil.addDays(failureEndDate, 1);  // to be inclusive of the end fail date
		}
		List report = null;
		try {
			 report = CallCenterServices.getSettlementProblemReport(statusCodes, transactionTypes, failureStartDate, failureEndDate);
			 if (report == null) {
			 	report = new ArrayList();  // to prevent null pointer exception
			 }
		} catch (FDResourceException e) {
			e.printStackTrace();
			actionResult.addError(new ActionError(SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		pageContext.setAttribute(this.reportName, report);
		return true;
	}
	
	private Date populateDateParameter(HttpServletRequest request, String dateParamPrefix) {
		String day = NVL.apply(request.getParameter(dateParamPrefix +"_day"), "");
		String month = NVL.apply(request.getParameter(dateParamPrefix+"_month"), "");
		String year = NVL.apply(request.getParameter(dateParamPrefix+"_year"), "");
		if (!"".equals(year) && !"".equals(month) && !"".equals(day)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EnumMonth enumMonth = EnumMonth.getEnum(month);
			if (enumMonth == null) { enumMonth = EnumMonth.JAN;}  
			if (day.length() < 2) {day = "0" + day;}
			try {
				return sdf.parse(day.trim() + "-" +  enumMonth.getDescription().trim() + "-" + year.trim());
			} catch (Exception e) {}
		}	
		return null;		
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
