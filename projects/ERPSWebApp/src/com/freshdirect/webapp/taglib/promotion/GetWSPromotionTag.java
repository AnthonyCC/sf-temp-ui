package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
		if(filter == null || (filter != null && (null == filter.getFromDate() && filter.getToDate() != null)
				&& (null != filter.getFromDate() && filter.getToDate() == null)))
			pageContext.setAttribute("dateErr", "Please specify the date range for filtering.");
		if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
			pageContext.setAttribute("dateErr", " 'To' date should not be before 'From' date for filtering.");
		}else if(filter != null && ((filter.getFromDate()!=null && filter.getToDate()!=null)
				|| filter.getDlvDate() != null || filter.getZone() != null || filter.getStatus() != null)){
			 infos = FDPromotionNewManager.getWSPromotionInfos(filter.getFromDate(), filter.getToDate(), filter.getDlvDate(), filter.getZone(), filter.getStatus());
		}
		
		request.getSession().setAttribute("wsFilter", filter);
		return infos;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
}
