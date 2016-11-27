package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetWSPromotionTag extends AbstractGetterTag {

	private WSPromoFilterCriteria filter;
	

	public WSPromoFilterCriteria getFilter() {
		return filter;
	}

	public void setFilter(WSPromoFilterCriteria filter) {
		this.filter = filter;
	}
	
	@Override
	protected Object getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		List<WSPromotionInfo> wsPromos = new ArrayList<WSPromotionInfo>();
		
		if (filter == null || filter.isEmpty()) {
			if(null == request.getParameter("ws_filter_submit"))
				filter = (WSPromoFilterCriteria) request.getSession().getAttribute("wsFilter");
		}
		request.getSession().setAttribute("wsFilter", filter);
				
		if(filter == null || (filter != null && (null == filter.getFromDate() && filter.getToDate() != null)
				&& (null != filter.getFromDate() && filter.getToDate() == null)))
			pageContext.setAttribute("dateErr", "Please specify the date range or delivery date for filtering.");
		if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
			pageContext.setAttribute("dateErr", " 'To' date should not be before 'From' date for filtering.");
		}else if(filter != null && ((filter.getFromDate()!=null && filter.getToDate()!=null)
				|| filter.getDlvDate() != null || filter.getZone() != null || filter.getStatus() != null)){
			 wsPromos = FDPromotionNewManager.getWSPromotionInfos(filter.getFromDate(), filter.getToDate(), filter.getDlvDate(), filter.getZone(), filter.getStatus());
		}
		
		if(wsPromos.size() > 0) {
			for(WSPromotionInfo p : wsPromos) {
				// check if the promotion is reccuring 
				if(p.getDayOfWeek() != null && p.getDayOfWeek().length > 0) {
					setRedemtionLimit(p);
				}
			}
		}
		
		if(null !=request.getParameter("actionName")){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=WSPromotionsList_Export.xls");
		    response.setCharacterEncoding("utf-8");
		}
				
		return wsPromos;
	}
	
	
	public void setRedemtionLimit(WSPromotionInfo p) {
		
		int redeemCnt = 0;
		Calendar c1 = Calendar.getInstance();  
        c1.setTime(p.getStartDate());  
   
        Calendar c2 = Calendar.getInstance();  
        c2.setTime(p.getEndDate());  
   
        int sundays = 0, mondays = 0, tuesdays = 0, wednesdays = 0, thursdays = 0, fridays = 0, saturdays = 0;  
   
        while(c1.before(c2)) {  
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)  
                sundays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)  
            	mondays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY)  
            	tuesdays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY)  
            	wednesdays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY)  
            	thursdays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)  
            	fridays++; 
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)  
            	saturdays++;
            
            c1.add(Calendar.DATE,1);  
        }  
        
		if (p.getDayOfWeek() != null && p.getDayOfWeek().length > 0) {
			for (int i = 0; i < p.getDayOfWeek().length; i++) {
				String day = p.getDayOfWeek()[i];
				if("SUN".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += sundays * p.getRedeemCount();
				if("MON".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += mondays * p.getRedeemCount();
				if("TUE".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += tuesdays * p.getRedeemCount();
				if("WED".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += wednesdays * p.getRedeemCount();
				if("THU".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += thursdays * p.getRedeemCount();
				if("FRI".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += fridays * p.getRedeemCount();
				if("SAT".equalsIgnoreCase(p.getDayOfWeek()[i])) 
					redeemCnt += saturdays * p.getRedeemCount();
			}
		}
		p.setRedeemCount(redeemCnt);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
}
