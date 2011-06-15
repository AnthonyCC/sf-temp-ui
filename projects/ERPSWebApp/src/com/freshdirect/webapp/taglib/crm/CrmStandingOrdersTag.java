package com.freshdirect.webapp.taglib.crm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmStandingOrdersTag extends AbstractControllerTag{
	
	private FDStandingOrderFilterCriteria filter;
	

	public FDStandingOrderFilterCriteria getFilter() {
		return filter;
	}

	public void setFilter(FDStandingOrderFilterCriteria filter) {
		this.filter = filter;
	}

	/*@Override
	protected Object getResult() throws Exception {
		// TODO Auto-generated method stub
		return FDStandingOrdersManager.getInstance().getActiveStandingOrdersCustInfo(filter);		
	}*/
	
	protected  boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = (HttpSession) request.getSession();
		CrmAgentModel agent =CrmSession.getCurrentAgent(session);
		String clearErrors =request.getParameter("clear_errors");
		
		try {
			if(null != clearErrors){
				//TODO: Get the selected order ids and clear the errors for them.
				String[] soIDs =request.getParameterValues("clearErrorSOId");
				if(null != soIDs && soIDs.length > 0){
					List listIDs = Arrays.asList(soIDs);
					FDStandingOrdersManager.getInstance().clearStandingOrderErrors(soIDs, agent.getUserId());
					StringBuffer idBuffer = new StringBuffer();
					for (int i = 0; i < soIDs.length-1; i++) {
						idBuffer.append(soIDs[i].split(",")[0]).append(", ");
					}
					idBuffer.append(soIDs[soIDs.length-1].split(",")[0]);
					pageContext.setAttribute("successMsg", "Successfully cleared the errors for order(s):"+idBuffer.toString());
				}
			}
		} catch (FDResourceException e) {
			actionResult.addError(new ActionError("so_error", "Failed to clear the errors: "+e.getMessage()));
		}
		return performGetAction(request, actionResult);
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		FDStandingOrderInfoList list = new FDStandingOrderInfoList(Collections.EMPTY_LIST);
		String actionName = request.getParameter("actionName");
		if("export".equalsIgnoreCase(actionName)){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=SO_Orders_Export.xls");
		    response.setCharacterEncoding("utf-8");
		}
		try {
			if(null == filter || filter.isEmpty()){
				if(null == request.getParameter("so_filter_submit"))
					filter = (FDStandingOrderFilterCriteria)request.getSession().getAttribute("filter");
			}
			request.getSession().setAttribute("filter",filter);
			if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
				pageContext.setAttribute("endDateBeforeErr", " 'To' date should not be before 'From' date for filtering.");
			}else{
				list =(FDStandingOrderInfoList)FDStandingOrdersManager.getInstance().getActiveStandingOrdersCustInfo(filter);
			}
//			request.getSession().setAttribute("sofilter", filter);
		} catch (FDResourceException e) {
			throw new JspException("Failed to get the standing orders: ",e);
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
		                            "com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList",
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
