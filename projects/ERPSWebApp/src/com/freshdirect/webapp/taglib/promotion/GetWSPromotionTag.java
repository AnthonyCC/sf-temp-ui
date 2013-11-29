package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
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
		List<WSPromotionInfo> infos = new ArrayList<WSPromotionInfo>();
		
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
			 infos = FDPromotionNewManager.getWSPromotionInfos(filter.getFromDate(), filter.getToDate(), filter.getDlvDate(), filter.getZone(), filter.getStatus());
		}
				
		if(null !=request.getParameter("actionName")){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=WSPromotionsList_Export.xls");
		    response.setCharacterEncoding("utf-8");
		}
				
		return infos;
	}
	
	private void sortRows(List<WSPromotionInfo> promoRows,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
}
