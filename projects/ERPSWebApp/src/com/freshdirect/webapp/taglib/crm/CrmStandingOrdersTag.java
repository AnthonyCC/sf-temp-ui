package com.freshdirect.webapp.taglib.crm;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
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
					filter = (FDStandingOrderFilterCriteria)request.getSession().getAttribute("sofilter");
			}
//			request.getSession().setAttribute("filter",filter);
			if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
				pageContext.setAttribute("endDateBeforeErr", " 'To' date should not be before 'From' date for filtering.");
			}else{
				list =(FDStandingOrderInfoList)FDStandingOrdersManager.getInstance().getActiveStandingOrdersCustInfo(filter);
				if(null != filter && null != filter.getFromDate()){
					performDateFilter(filter.getFromDate(),filter.getToDate(),list);
				}
			}
			request.getSession().setAttribute("sofilter", filter);
		} catch (FDResourceException e) {
			throw new JspException("Failed to get the standing orders: ",e);
		}	
		pageContext.setAttribute(this.id, list);
		return true;
	}
	
	private void performDateFilter(Date fromDate, Date toDate, FDStandingOrderInfoList list){
		
		Calendar now = Calendar.getInstance();
		
		long base; //next delivery
		int freq; //delivery frequency
		long intervalStart; //filter interval start
		long intervalEnd; //filter interval end	
		
		long startM; //interval start modulo
		long endM; //interval end modulo
		long baseM; //next delivery modulo
		
		Iterator<FDStandingOrderInfo> soIterator=list.getStandingOrdersInfo().listIterator();
		
		if(toDate==null){
			toDate=fromDate;
		}
		
		while(soIterator.hasNext()) {
			
			
			FDStandingOrderInfo so=soIterator.next();
			
			Calendar actual=Calendar.getInstance();
			actual.setTime(so.getNextDate());
			if(now.after(actual)){
				soIterator.remove();
				continue;
			}
				
				
			base=calculateDayNumber(so.getNextDate());
			freq=so.getFrequency()*7;
			intervalStart=calculateDayNumber(fromDate);
			intervalEnd=calculateDayNumber(toDate);		
			
			if(intervalEnd-intervalStart>=freq) { //if the interval wider than the frequency then we can be sure the SO will be delivered in that range
				continue;
			} else {
				
				startM=intervalStart%freq;
				endM=intervalEnd%freq;
				baseM=base%freq;
				
				if(intervalStart==intervalEnd && baseM==startM){ //if one exact day given
					continue;
				}else if(endM>startM && baseM>=startM && baseM<=endM){ //if the modulos in range doesnt containes the 0 value
					continue;
				}else if(endM<startM && (baseM>=startM || baseM<=endM)){ //if the modulos in range containes 0
					continue;
				}else {
					soIterator.remove();
				}
			}		
		}		
	}
	
	private long calculateDayNumber(Date date){
			
		Calendar local=Calendar.getInstance();
		local.setTime(date);
		
		Calendar epoch = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		epoch.set(1970, 0, 5, 1, 0);
		
		Calendar actual = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		actual.set(local.get(Calendar.YEAR),local.get(Calendar.MONTH),local.get(Calendar.DATE));// I want exactly this time to be set (setTime would calculate with offset)
				
		return (actual.getTimeInMillis()-epoch.getTimeInMillis())/86400000;
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
