package com.freshdirect.webapp.taglib.callcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCutoffTimeInfo;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CutoffTimeSearchControllerTag extends AbstractControllerTag {
	
	private static final SimpleDateFormat SF = new SimpleDateFormat("h:mm a");
	
	private String cutoffReportId;
	
	public void setCutoffReportId(String cutoffReportId){
		this.cutoffReportId = cutoffReportId;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		if ("cutoffReport".equalsIgnoreCase(this.getActionName())) {
			String cutoffTime = NVL.apply(request.getParameter("cutoffTime"), "");
			this.getCutoffReport(this.getDate(request), cutoffTime);
			this.getCutoffTimes(this.getDate(request));
		}else{
			this.getCutoffTimes(new Date());
		}

		return true;
	}

	private Date getDate(HttpServletRequest request) throws JspException {
		String month = NVL.apply(request.getParameter("month"), "");
		String day = NVL.apply(request.getParameter("day"), "");
		String year = NVL.apply(request.getParameter("year"), "");
		if ("".equals(month) || "".equals(day) || "".equals(year)) {
			throw new JspException("Missing Date Parameters");
		}

		Calendar dlvCalendar = Calendar.getInstance();
		dlvCalendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		return dlvCalendar.getTime();
	}

	private void getCutoffTimes(Date date) throws JspException {
		try {
			List cTimes = FDDeliveryManager.getInstance().getCutofftimesByDate(date);
			pageContext.setAttribute("cutoffTimes", cTimes);
		} catch (FDResourceException e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	}

	private void getCutoffReport(Date date, String cutoffTime) throws JspException {
		try {
			List cReport = CallCenterServices.getCutoffTimeReport(date);
			List ret = new ArrayList();
			if(!"".equals(cutoffTime)){
				for(Iterator i = cReport.iterator(); i.hasNext(); ){
					FDCutoffTimeInfo info = (FDCutoffTimeInfo) i.next();
					if(cutoffTime.equals(SF.format(info.getCutoffTime()))){
						ret.add(info);
					}
				}
				pageContext.setAttribute(this.cutoffReportId, ret);
			}else{
				pageContext.setAttribute(this.cutoffReportId, cReport);
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	}

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		this.getCutoffTimes(new Date());
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
				new VariableInfo("cutoffTimes", "java.util.List", true, VariableInfo.NESTED),
				new VariableInfo(data.getAttributeString("cutoffReportId"), "java.util.List", true, VariableInfo.NESTED)};
		}
	}
}
