package com.freshdirect.webapp.taglib.crm;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmAuthSearchTag extends AbstractControllerTag{
	
	private static final String AUTH_SEARCH_CRITERIA="authSearchCriteria";
	private CrmAuthSearchCriteria filter;
	

	public CrmAuthSearchCriteria getFilter() {
		return filter;
	}

	public void setFilter(CrmAuthSearchCriteria filter) {
		this.filter = filter;
	}

	
	protected  boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		
		List<CrmAuthInfo> list = Collections.EMPTY_LIST;
		CrmAgentModel currAgent = CrmSession.getCurrentAgent(pageContext.getSession());
		if(currAgent==null ) {
			throw new JspException("You must be signed in to access this resource.");
		}
		else if (currAgent!=null && currAgent.getRole()==null) {
			throw new JspException("No role defined for agent :"+currAgent.getUserId());
		}
		String actionName = request.getParameter("actionName");
		if("export".equalsIgnoreCase(actionName)){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=Auths_Export.xls");
		    response.setCharacterEncoding("utf-8");
		}
		try {
			if(null == filter || filter.isEmpty()){
				if(null == request.getParameter("auth_filter_submit"))
					filter = (CrmAuthSearchCriteria)request.getSession().getAttribute(AUTH_SEARCH_CRITERIA);
			}
//			request.getSession().setAttribute("filter",filter);
			
			if(StringUtil.isEmpty(filter.getFromDateStr())||StringUtil.isEmpty(filter.getToDateStr())) {
				pageContext.setAttribute("authSearchErr", " 'From' date and 'To' date must be selected.");	
			}
			else if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
				pageContext.setAttribute("authSearchErr", " 'To' date should not be before 'From' date for filtering.");
			}else{
				Calendar today = DateUtil.truncate(Calendar.getInstance());
				if(DateUtil.diffInDays(today.getTime(), filter.getFromDate())>60) {
					pageContext.setAttribute("authSearchErr", "Authorization lookup is available for the last 60 days only.");	
				} else {
					list =CrmManager.getInstance().getAuthorizations(currAgent.getRole(),filter);
				}
			}
			request.getSession().setAttribute(AUTH_SEARCH_CRITERIA, filter);
		} catch (FDResourceException e) {
			throw new JspException("Failed to get the authorizations: ",e);
		}	
		pageContext.setAttribute(this.id, list);
		return true;
	}
	
	

	public static class TagEI extends AbstractControllerTag.TagEI {
		/*protected String getResultType() {
			return "com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList";
		}*/
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
