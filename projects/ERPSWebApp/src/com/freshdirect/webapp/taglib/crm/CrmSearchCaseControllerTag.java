package com.freshdirect.webapp.taglib.crm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;

public class CrmSearchCaseControllerTag extends TagSupport {

	public int doStartTag() throws JspException {

		ServletRequest request = pageContext.getRequest();
		String action = request.getParameter("action");

		if ("searchCase".equalsIgnoreCase(action)) { 
			boolean isCaseHistoryPage = false;
			
			String id = NVL.apply(request.getParameter("pk"), "");
			PrimaryKey pk = "".equals(id) ? null : new PrimaryKey(id);
			
			String[] stateCodes = request.getParameterValues("state");
			Set states = new HashSet();
			if (stateCodes != null) {
				for (int i = 0; i < stateCodes.length; i++) {
					states.add(CrmCaseState.getEnum(stateCodes[i]));
				}
			}

			id = NVL.apply(request.getParameter("assignedAgent"), "");
			PrimaryKey assignedAgentPK = "".equals(id) ? null : new PrimaryKey(id);

			// bind
			CrmCaseTemplate ct = CrmSession.getSearchTemplate(pageContext.getSession());

			ct.setPK(pk);
			ct.setStates(states);
			ct.setOrigin(CrmCaseOrigin.getEnum(request.getParameter("origin")));
			ct.setQueue(CrmCaseQueue.getEnum(request.getParameter("queue")));
			ct.setSubject(CrmCaseSubject.getEnum(request.getParameter("subject")));
			ct.setPriority(CrmCasePriority.getEnum(request.getParameter("priority")));
			ct.setAssignedAgentPK(assignedAgentPK);
			ct.setStartDate(getDate(NVL.apply(request.getParameter("startDate"), ""), false));
			ct.setEndDate(getDate(NVL.apply(request.getParameter("endDate"), ""), true));
			ct.setStartRecord(Integer.parseInt(NVL.apply(request.getParameter("startRecord"),"0")));
			ct.setEndRecord(Integer.parseInt(NVL.apply(request.getParameter("endRecord"), FDStoreProperties.getCaseListLength(isCaseHistoryPage))));
			ct.setSortBy(NVL.apply(request.getParameter("sortBy"),"CREATE_DATE"));
			ct.setSortOrder(NVL.apply(request.getParameter("sortOrder"), "DESC"));
			CrmSession.setSearchTemplate(pageContext.getSession(), ct);
		}

		return EVAL_PAGE;
	}
	
	private Date getDate(String date, boolean endDate) throws JspException {
		if(date == null || "".equals(date)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = sdf.parse(date);
			Calendar cal = DateUtil.toCalendar(curDate);
			if(endDate){
				cal.set(Calendar.HOUR, 11);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.AM_PM, Calendar.PM);
			}
			return cal.getTime();
		} catch (ParseException e) { 
			throw new JspException(e);
		}
	}
}
